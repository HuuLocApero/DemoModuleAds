package com.demo.db.base

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T): Long

    suspend fun update(entity: T)

    suspend fun delete(id: Long)

    suspend fun deleteAll()

    suspend fun findByID(id: Long): T
}