package com.online.liveimages.room

import android.content.Context
import androidx.room.*
import com.online.liveimages.model.GetImages
import com.online.liveimages.model.ImageSrc
import com.online.liveimages.model.ListPhotos

@Database(entities = [GetImages::class], version = 1, exportSchema = false)
@TypeConverters(ListPhotos::class, ImageSrc::class)
abstract class GetImagesDatabase : RoomDatabase() {

    abstract fun getImagesDao() : DAOAccess

    companion object {

        @Volatile
        private var INSTANCE: GetImagesDatabase? = null

        fun getDatabaseClient(context: Context) : GetImagesDatabase {

            if (INSTANCE != null) return INSTANCE!!

            synchronized(this) {

                INSTANCE = Room
                    .databaseBuilder(context, GetImagesDatabase::class.java, "Images.db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()

                return INSTANCE!!

            }
        }

    }

}