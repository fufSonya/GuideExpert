package com.example.GuideExpert.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.GuideExpert.data.local.DBStorage
import com.example.GuideExpert.data.local.models.ExcursionFilterWithData
import com.example.GuideExpert.data.mappers.toExcursionFilterWithData
import com.example.GuideExpert.data.remote.services.ExcursionService
import com.example.GuideExpert.domain.models.Filters
import com.example.GuideExpert.utils.Constant.REMOTE_KEY_FILTER_ID
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class ExcursionFiltersRemoteMediator @Inject constructor(
    private val excursionService: ExcursionService,
    private val filters: Filters,
    private val dbStorage : DBStorage,
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
                    val remoteKey = dbStorage.getRemoteKeyById(REMOTE_KEY_FILTER_ID).first()
                    if (remoteKey == null || remoteKey.nextOffset == 0) // END OF PAGINATION REACHED
                        return MediatorResult.Success(endOfPaginationReached = true)
                    remoteKey.nextOffset
                }
            }

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

            // SAVE RESULTS AND NEXT OFFSET TO DATABASE
            dbStorage.insertExcursionsFilter(excursions= results,keyId= REMOTE_KEY_FILTER_ID,isClearDB = loadType == LoadType.REFRESH,
                nextOffset = nextOffset)

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