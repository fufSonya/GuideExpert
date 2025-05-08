package com.example.GuideExpert.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.GuideExpert.data.local.db.ExcursionsRoomDatabase
import com.example.GuideExpert.data.local.models.ExcursionFilterEntity
import com.example.GuideExpert.data.local.models.ExcursionFilterWithData
import com.example.GuideExpert.data.local.models.ExcursionSearchWithData
import com.example.GuideExpert.data.local.models.RemoteKeyEntity
import com.example.GuideExpert.data.mappers.toExcursionFilterEntity
import com.example.GuideExpert.data.mappers.toExcursionFilterWithData
import com.example.GuideExpert.data.mappers.toExcursionSearchWithData
import com.example.GuideExpert.data.remote.services.ExcursionService
import com.example.GuideExpert.domain.models.Filters
import com.example.GuideExpert.utils.Constant.REMOTE_KEY_FILTE_ID
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class ExcursionFiltersRemoteMediator @Inject constructor(
    private val excursionService: ExcursionService,
    private val excursionsRoomDatabase: ExcursionsRoomDatabase,
    private val filters: Filters
) : RemoteMediator<Int, ExcursionFilterWithData>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ExcursionFilterWithData>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    // RETRIEVE NEXT OFFSET FROM DATABASE
                    val remoteKey = excursionsRoomDatabase.remoteKeyDao().getById(REMOTE_KEY_FILTE_ID)
                    if (remoteKey == null || remoteKey.nextOffset == 0) // END OF PAGINATION REACHED
                        return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.nextOffset
                }
            }

/*
            Log.d("TAG","FILTERS SORT:  ${filters.sort} ")
            if (filters.categories.isNotEmpty()) {
                filters.categories.forEach { Log.d("TAG","FILTERS CAT ${it} ")}
            }
            if (filters.duration.isNotEmpty()) {
                filters.duration.forEach { Log.d("TAG","FILTERS DUR ${it} ")}
            }*/
            // MAKE API CALL

            val apiResponse = excursionService.getExcursionsFiltersPaging(
                offset = offset,
                limit = state.config.pageSize,
                sort = filters.sort,
                categories = filters.categories,
                duration = filters.duration,
                group = filters.group
            )

            val results = apiResponse.body()?.excursions
                ?.map { it.toExcursionFilterWithData() } ?: listOf<ExcursionFilterWithData>()
            val nextOffset = apiResponse.body()?.nextOffset ?: 0


            Log.d("TAG", "results size :: ${results.size.toString()}")
            // SAVE RESULTS AND NEXT OFFSET TO DATABASE
            if (excursionsRoomDatabase.isOpen) Log.d("TAG", "DB OPEN")
            excursionsRoomDatabase.withTransaction {
                Log.d("TAG", "withTransaction")
                if (loadType == LoadType.REFRESH) {
                    // IF REFRESHING, CLEAR DATABASE FIRST
                    excursionsRoomDatabase.excursionFilterDao().clearAll()
                    excursionsRoomDatabase.remoteKeyDao().deleteById(REMOTE_KEY_FILTE_ID)
                }
                Log.d("TAG", "insert")
                excursionsRoomDatabase.excursionFilterDao().insertAll(results)
                excursionsRoomDatabase.remoteKeyDao().insert(
                    RemoteKeyEntity(
                        id = REMOTE_KEY_FILTE_ID,
                        nextOffset = nextOffset,
                    )
                )
            }

            MediatorResult.Success(endOfPaginationReached = results.size < state.config.pageSize)


        }catch (e: IOException) {
            Log.d("TAG", "error :: ${e.message.toString()}")
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Log.d("TAG", "error http :: ${e.message.toString()}")
            MediatorResult.Error(e)
        }

    }

}