package com.demo.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.demo.db.converter.Converters
import com.demo.db.dao.FavouriteDAO
import com.demo.db.entity.FavouriteEntity

@TypeConverters(Converters::class)
@Database(entities = [FavouriteEntity::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouriteDAO(): FavouriteDAO

}