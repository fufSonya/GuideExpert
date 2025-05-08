package com.example.GuideExpert.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.GuideExpert.data.ExcursionFiltersRemoteMediator
import com.example.GuideExpert.data.ExcursionSearchRemoteMediator
import com.example.GuideExpert.data.local.db.ExcursionsRoomDatabase
import com.example.GuideExpert.data.mappers.toExcursion
import com.example.GuideExpert.data.remote.services.ExcursionService
import com.example.GuideExpert.domain.models.Excursion
import com.example.GuideExpert.domain.models.FilterQuery
import com.example.GuideExpert.domain.models.Filters
import com.example.GuideExpert.domain.repository.DataPagingRepository
import com.example.GuideExpert.utils.Constant.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DataPagingRepositoryImpl @Inject constructor(
    private val excursionService: ExcursionService,
    private val excursionsRoomDatabase: ExcursionsRoomDatabase,
): DataPagingRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getExcursionByQueryFlow(filterQuery: FilterQuery): Flow<PagingData<Excursion>> {
         return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, maxSize = 30),
            remoteMediator = ExcursionSearchRemoteMediator(
                excursionsRoomDatabase = excursionsRoomDatabase,
                excursionService = excursionService,
                filterQuery = filterQuery
            ),
            pagingSourceFactory = {
                excursionsRoomDatabase.excursionSearchDao().pagingSource()
            },
        ).flow.map { pagingData -> pagingData.map { it.toExcursion() } }
            .flowOn(Dispatchers.IO)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getExcursionByFiltersFlow(filter: Filters): Flow<PagingData<Excursion>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, maxSize = 30),
            remoteMediator = ExcursionFiltersRemoteMediator(
                excursionsRoomDatabase = excursionsRoomDatabase,
                excursionService = excursionService,
                filters = filter
            ),
            pagingSourceFactory = {
                excursionsRoomDatabase.excursionFilterDao().pagingSource()
            },
        ).flow.map { pagingData -> pagingData.map { it.toExcursion() } }
            .flowOn(Dispatchers.IO)
    }

}