package com.example.GuideExpert.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.GuideExpert.data.local.db.ExcursionsRoomDatabase
import com.example.GuideExpert.data.local.models.ExcursionSearchEntity
import com.example.GuideExpert.data.local.models.ExcursionSearchWithData
import com.example.GuideExpert.data.local.models.RemoteKeyEntity
import com.example.GuideExpert.data.mappers.toExcursionSearchEntity
import com.example.GuideExpert.data.mappers.toExcursionSearchWithData
import com.example.GuideExpert.data.remote.services.ExcursionService
import com.example.GuideExpert.domain.models.FilterQuery
import com.example.GuideExpert.utils.Constant.REMOTE_KEY_ID
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ExcursionSearchRemoteMediator @Inject constructor(
    private val excursionService: ExcursionService,
    private val excursionsRoomDatabase: ExcursionsRoomDatabase,
    private val filterQuery: FilterQuery
) : RemoteMediator<Int, ExcursionSearchWithData>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ExcursionSearchWithData>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    // RETRIEVE NEXT OFFSET FROM DATABASE
                    val remoteKey = excursionsRoomDatabase.remoteKeyDao().getById(REMOTE_KEY_ID)
                    if (remoteKey == null || remoteKey.nextOffset == 0) // END OF PAGINATION REACHED
                        return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.nextOffset
                }
            }


            Log.d("TAG","${filterQuery.query}  ${filterQuery.sort}")
            // MAKE API CALL
            val apiResponse = excursionService.getExcursionsSearchPaging(
                offset = offset,
                limit = state.config.pageSize,
                query = filterQuery.query
            )

            val results = apiResponse.body()?.excursions
                ?.map { it.toExcursionSearchWithData() } ?: listOf<ExcursionSearchWithData>()
            val nextOffset = apiResponse.body()?.nextOffset ?: 0


            Log.d("TAG", "results size :: ${results.size.toString()}")
            // SAVE RESULTS AND NEXT OFFSET TO DATABASE
            if (excursionsRoomDatabase.isOpen) Log.d("TAG", "DB OPEN")
            excursionsRoomDatabase.withTransaction {
                Log.d("TAG", "withTransaction")
                if (loadType == LoadType.REFRESH) {
                    // IF REFRESHING, CLEAR DATABASE FIRST
                    excursionsRoomDatabase.excursionSearchDao().clearAll()
                    excursionsRoomDatabase.remoteKeyDao().deleteById(REMOTE_KEY_ID)
                }
                Log.d("TAG", "insert")
                excursionsRoomDatabase.excursionSearchDao().insertAll(results)
                excursionsRoomDatabase.remoteKeyDao().insert(
                    RemoteKeyEntity(
                        id = REMOTE_KEY_ID,
                        nextOffset = nextOffset,
                    )
                )
            }

            MediatorResult.Success(endOfPaginationReached = results.size < state.config.pageSize)


        }catch (e: IOException) {
            Log.d("TAG", "error :: ${e.message.toString()}")
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }

    }

}