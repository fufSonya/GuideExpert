package com.example.GuideExpert.domain.repository

import com.example.GuideExpert.domain.models.Filter

interface DataProviderRepository {
    fun getFiltersBar():  List<Filter>
    fun getFiltersDuration():  List<Filter>
    fun getFiltersSort():  List<Filter>
    fun getFiltersGroups():  List<Filter>
    fun getFiltersCategories():  List<Filter>
    fun getSortDefault(): Int
}