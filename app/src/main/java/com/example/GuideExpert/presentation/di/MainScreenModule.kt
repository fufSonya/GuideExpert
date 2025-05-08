package com.example.GuideExpert.presentation.di

import com.example.GuideExpert.domain.DeleteAvatarProfileUseCase
import com.example.GuideExpert.domain.DeleteFavoriteExcursionUseCase
import com.example.GuideExpert.domain.GetAuthTokenByYandexUseCase
import com.example.GuideExpert.domain.GetConfigUseCase
import com.example.GuideExpert.domain.GetExcursionByFiltersUseCase
import com.example.GuideExpert.domain.GetExcursionByQueryUseCase
import com.example.GuideExpert.domain.GetExcursionDataUseCase
import com.example.GuideExpert.domain.GetExcursionDetailUseCase
import com.example.GuideExpert.domain.GetExcursionFavoriteUseCase
import com.example.GuideExpert.domain.GetExcursionsFavoriteIdUseCase
import com.example.GuideExpert.domain.GetFiltersBarUseCase
import com.example.GuideExpert.domain.GetFiltersCategoriesUseCase
import com.example.GuideExpert.domain.GetFiltersDurationUseCase
import com.example.GuideExpert.domain.GetFiltersGroupsUseCase
import com.example.GuideExpert.domain.GetFiltersSortUseCase
import com.example.GuideExpert.domain.GetImageExcursionUseCase
import com.example.GuideExpert.domain.GetImagesExcursionDataUseCase
import com.example.GuideExpert.domain.GetProfileUseCase
import com.example.GuideExpert.domain.GetSortDefaultUseCase
import com.example.GuideExpert.domain.LoadExcursionFavoriteUseCase
import com.example.GuideExpert.domain.LogoutProfileUseCase
import com.example.GuideExpert.domain.RestoreFavoriteExcursionUseCase
import com.example.GuideExpert.domain.SetFavoriteExcursionUseCase
import com.example.GuideExpert.domain.UpdateAvatarProfileUseCase
import com.example.GuideExpert.domain.UpdateProfileUseCase
import com.example.GuideExpert.domain.impl.DeleteAvatarProfileUseCaseImpl
import com.example.GuideExpert.domain.impl.DeleteFavoriteExcursionUseCaseImpl
import com.example.GuideExpert.domain.impl.GetAuthTokenByYandexUseCaseImpl
import com.example.GuideExpert.domain.impl.GetConfigUseCaseImpl
import com.example.GuideExpert.domain.impl.GetExcursionByFiltersUseCaseImpl
import com.example.GuideExpert.domain.impl.GetExcursionByQueryUseCaseImpl
import com.example.GuideExpert.domain.impl.GetExcursionDataUseCaseImpl
import com.example.GuideExpert.domain.impl.GetExcursionDetailUseCaseImpl
import com.example.GuideExpert.domain.impl.GetExcursionFavoriteUseCaseImpl
import com.example.GuideExpert.domain.impl.GetExcursionsFavoriteIdUseCaseImpl
import com.example.GuideExpert.domain.impl.GetFiltersBarUseCaseImpl
import com.example.GuideExpert.domain.impl.GetFiltersCategoriesUseCaseImpl
import com.example.GuideExpert.domain.impl.GetFiltersDurationUseCaseImpl
import com.example.GuideExpert.domain.impl.GetFiltersGroupsUseCaseImpl
import com.example.GuideExpert.domain.impl.GetFiltersSortUseCaseImpl
import com.example.GuideExpert.domain.impl.GetImageExcursionUseCaseImpl
import com.example.GuideExpert.domain.impl.GetImagesExcursionDataUseCaseImpl
import com.example.GuideExpert.domain.impl.GetProfileUseCaseImpl
import com.example.GuideExpert.domain.impl.GetSortDefaultUseCaseImpl
import com.example.GuideExpert.domain.impl.LoadExcursionFavoriteUseCaseImpl
import com.example.GuideExpert.domain.impl.LogoutProfileUseCaseImpl
import com.example.GuideExpert.domain.impl.RestoreFavoriteExcursionUseCaseImpl
import com.example.GuideExpert.domain.impl.SetFavoriteExcursionUseCaseImpl
import com.example.GuideExpert.domain.impl.UpdateAvatarProfileUseCaseImpl
import com.example.GuideExpert.domain.impl.UpdateProfileUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class MainScreenModule {

    @Binds
    abstract fun bindGetExcursionDetailUseCase(
        getExcursionDetailUseCaseImpl: GetExcursionDetailUseCaseImpl
    ) : GetExcursionDetailUseCase

    @Binds
    abstract fun bindGetExcursionByQueryUseCase(
        getExcursionByQueryUseCaseImpl: GetExcursionByQueryUseCaseImpl
    ) : GetExcursionByQueryUseCase

    @Binds
    abstract fun bindGetExcursionByFiltersUseCase(
        getExcursionByFiltersUseCaseImpl: GetExcursionByFiltersUseCaseImpl
    ) : GetExcursionByFiltersUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindGetFiltersBarUseCase(
        getFiltersBarUseCaseImpl: GetFiltersBarUseCaseImpl
    ) : GetFiltersBarUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindGetFiltersDurationUseCase(
        getFiltersDurationUseCaseImpl: GetFiltersDurationUseCaseImpl
    ) : GetFiltersDurationUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindGetFiltersSortUseCase(
        getFiltersSortUseCaseImpl: GetFiltersSortUseCaseImpl
    ) : GetFiltersSortUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindGetFiltersGroupsUseCase(
        getFiltersGroupsUseCaseImpl: GetFiltersGroupsUseCaseImpl
    ) : GetFiltersGroupsUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindGetFiltersCategoriesUseCase(
        getFiltersCategoriesUseCaseImpl: GetFiltersCategoriesUseCaseImpl
    ) : GetFiltersCategoriesUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindGetSortDefaultUseCase(
        getGetSortDefaultUseCaseImpl: GetSortDefaultUseCaseImpl
    ) : GetSortDefaultUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindGetConfigUseCase(
        getGetConfigUseCaseImpl: GetConfigUseCaseImpl
    ) : GetConfigUseCase

    @Binds
    abstract fun bindGetExcursionDataUseCase(
        getExcursionDataUseCaseImpl: GetExcursionDataUseCaseImpl
    ) : GetExcursionDataUseCase

    @Binds
    abstract fun bindGetImagesExcursionDataUseCase(
        getImagesExcursionDataUseCaseImpl: GetImagesExcursionDataUseCaseImpl
    ) : GetImagesExcursionDataUseCase

    @Binds
    abstract fun bindGetImageExcursionUseCase(
        getImageExcursionUseCaseImpl: GetImageExcursionUseCaseImpl
    ) : GetImageExcursionUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindGetAuthTokenByYandexUseCase(
        getAuthTokenByYandexUseCaseImpl: GetAuthTokenByYandexUseCaseImpl
    ) : GetAuthTokenByYandexUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindGetProfileUseCase(
        getProfileUseCaseImpl: GetProfileUseCaseImpl
    ) : GetProfileUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindUpdateAvatarProfileUseCase(
        updateAvatarProfileUseCaseImpl: UpdateAvatarProfileUseCaseImpl
    ) : UpdateAvatarProfileUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindLogoutProfileUseCase(
        updateLogoutProfileUseCaseImpl: LogoutProfileUseCaseImpl
    ) : LogoutProfileUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindDeleteAvatarProfileUseCase(
        deleteAvatarProfileUseCaseImpl: DeleteAvatarProfileUseCaseImpl
    ) : DeleteAvatarProfileUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindUpdateProfileUseCase(
        updateProfileUseCaseImpl: UpdateProfileUseCaseImpl
    ) : UpdateProfileUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindGetExcursionsFavoriteIdUseCase(
        getExcursionsFavoriteIdUseCaseImpl: GetExcursionsFavoriteIdUseCaseImpl
    ) : GetExcursionsFavoriteIdUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindSetFavoriteExcursionUseCase(
        setFavoriteExcursionUseCaseImpl: SetFavoriteExcursionUseCaseImpl
    ) : SetFavoriteExcursionUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindDeleteFavoriteExcursionUseCase(
        deleteFavoriteExcursionUseCaseImpl: DeleteFavoriteExcursionUseCaseImpl
    ) : DeleteFavoriteExcursionUseCase


    @ViewModelScoped
    @Binds
    abstract fun bindLoadExcursionFavoriteUseCase(
        loadExcursionFavoriteUseCaseImpl: LoadExcursionFavoriteUseCaseImpl
    ) : LoadExcursionFavoriteUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindGetExcursionFavoriteUseCase(
        getExcursionFavoriteUseCaseImpl: GetExcursionFavoriteUseCaseImpl
    ) : GetExcursionFavoriteUseCase

    @ViewModelScoped
    @Binds
    abstract fun bindRestoreFavoriteExcursionUseCase(
        restoreFavoriteExcursionUseCaseImpl: RestoreFavoriteExcursionUseCaseImpl
    ) : RestoreFavoriteExcursionUseCase
}