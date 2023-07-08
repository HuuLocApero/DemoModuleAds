package com.demo.db.datasource.impl

import com.demo.db.dao.FavouriteDAO
import com.demo.db.datasource.FavouriteDataSource
import com.demo.db.entity.FavouriteEntity
import javax.inject.Inject

class FavouriteDataSourceImplement @Inject constructor(private val favouriteDAO: FavouriteDAO) : FavouriteDataSource {
    override suspend fun insert(favourite: FavouriteEntity) {
        favouriteDAO.insert(favourite)
    }

    override suspend fun update(favourite: FavouriteEntity) {
        favouriteDAO.update(favourite)
    }

    override suspend fun delete(id: Long) {
        favouriteDAO.delete(id)
    }

    override suspend fun findByID(id: Long): FavouriteEntity {
        return favouriteDAO.findByID(id)
    }

    override suspend fun getList(pageSize: Int, offset: Int): List<FavouriteEntity> {
        return favouriteDAO.getList(pageSize, offset)
    }

    override suspend fun getList(): List<FavouriteEntity> {
        return favouriteDAO.getList()
    }

}