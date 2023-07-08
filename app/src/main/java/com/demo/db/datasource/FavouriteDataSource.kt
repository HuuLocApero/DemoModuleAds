package com.demo.db.datasource

import com.demo.db.entity.FavouriteEntity

interface FavouriteDataSource {
    suspend fun insert(favourite: FavouriteEntity)

    suspend fun update(favourite: FavouriteEntity)

    suspend fun delete(id: Long)

    suspend fun findByID(id: Long): FavouriteEntity

    suspend fun getList(pageSize: Int, offset: Int): List<FavouriteEntity>

    suspend fun getList(): List<FavouriteEntity>
}