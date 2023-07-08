package com.demo.di.module

import com.demo.data.repository.FavouriteRepository
import com.demo.data.repository.impl.FavouriteRepositoryImplement
import com.demo.db.datasource.FavouriteDataSource
import com.demo.utils.PrefUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideFavouriteRepository(
        favouriteDataSource: FavouriteDataSource, prefUtils: PrefUtils
    ): FavouriteRepository = FavouriteRepositoryImplement(favouriteDataSource, prefUtils)
}