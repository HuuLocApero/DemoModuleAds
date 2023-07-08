package com.demo.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.demo.db.base.BaseDao
import com.demo.db.entity.FavouriteEntity

@Dao
interface FavouriteDAO : BaseDao<FavouriteEntity> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: FavouriteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entity: List<FavouriteEntity>)

    @Query("SELECT * FROM table_favourite WHERE table_favourite.id LIKE :id")
    override suspend fun findByID(id: Long): FavouriteEntity

    @Query("SELECT * FROM table_favourite ORDER BY table_favourite.id DESC LIMIT :pageSize OFFSET :offset")
    suspend fun getList(pageSize: Int, offset: Int): List<FavouriteEntity>

    @Query("SELECT * FROM table_favourite ORDER BY table_favourite.id DESC")
    suspend fun getList(): List<FavouriteEntity>

    @Update
    override suspend fun update(entity: FavouriteEntity)

    @Query("DELETE FROM table_favourite WHERE table_favourite.id=:id")
    override suspend fun delete(id: Long)

    @Query("DELETE FROM table_favourite")
    override suspend fun deleteAll()
}