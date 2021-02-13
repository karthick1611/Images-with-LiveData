package com.online.liveimages.room

import androidx.room.*
import com.online.liveimages.model.GetImages

@Dao
interface DAOAccess {

    @Transaction
    fun deleteAndCreate(getImages: GetImages) {
        emptyTable()
        insertData(getImages)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(getImages: GetImages)

    @Query("DELETE FROM Photos")
    fun emptyTable()

    @Query("SELECT * FROM Photos")
    fun getPhotos(): GetImages

}