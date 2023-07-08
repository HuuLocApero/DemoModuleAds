package com.demo.data.repository.impl

import com.demo.data.mapping.MappingData.toDataModel
import com.demo.data.repository.FavouriteRepository
import com.demo.data.ui.Favourite
import com.demo.db.datasource.FavouriteDataSource
import com.demo.utils.PrefUtils
import javax.inject.Inject

class FavouriteRepositoryImplement
@Inject constructor(private var favouriteDataSource: FavouriteDataSource, val prefUtils: PrefUtils) : FavouriteRepository {

    override suspend fun getListFavourite(): ArrayList<Favourite> {
        return ArrayList(favouriteDataSource.getList().map { it.toDataModel() })
    }
}