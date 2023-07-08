package com.demo.di.module

import android.content.Context
import androidx.room.Room
import com.demo.db.AppDatabase
import com.demo.db.dao.FavouriteDAO
import com.demo.db.datasource.FavouriteDataSource
import com.demo.db.datasource.impl.FavouriteDataSourceImplement
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    companion object {
        const val DATABASE_NAME = "ai_generator"
    }

    @Singleton
    @Provides
    fun getDatabase(context: Context): AppDatabase {
        val database = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)

            // Todo: If want to add default value for any table
//            .createFromAsset("cities.db")
//            .addCallback(object : RoomDatabase.Callback() {
//                override fun onCreate(db: SupportSQLiteDatabase) {
//                    super.onCreate(db)
//                    db.execSQL("INSERT INTO cities_fts(cities_fts) VALUES ('rebuild')")
//                }
//            })
            .fallbackToDestructiveMigration().build()
        return database
    }


    @Singleton
    @Provides
    fun provideFavouriteDAO(db: AppDatabase): FavouriteDAO {
        return db.favouriteDAO()
    }

    @Singleton
    @Provides
    fun provideFavouriteDataSource(favouriteDAO: FavouriteDAO): FavouriteDataSource {
        return FavouriteDataSourceImplement(favouriteDAO)
    }
}