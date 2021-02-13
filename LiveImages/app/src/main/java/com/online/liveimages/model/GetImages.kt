package com.online.liveimages.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable

@Entity(tableName = "Photos")
data class GetImages(
    val next_page: String,
    val page: Int,
    val per_page: Int,
    @TypeConverters(ListPhotos::class) val photos: List<Photo>,
    val total_results: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

data class Photo(
    val avg_color: String,
    val height: Int,
    val id: Int,
    val liked: Boolean,
    val photographer: String,
    val photographer_id: Int,
    val photographer_url: String,
    @TypeConverters(ImageSrc::class)val src: Src,
    val url: String,
    val width: Int
): Serializable

data class Src(
    val landscape: String,
    val large: String,
    val large2x: String,
    val medium: String,
    val original: String,
    val portrait: String,
    val small: String,
    val tiny: String
): Serializable

class ListPhotos {
    @TypeConverter
    fun fromImagesList(value: List<Photo>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Photo>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toImagesList(value: String): List<Photo> {
        val gson = Gson()
        val type = object : TypeToken<List<Photo>>() {}.type
        return gson.fromJson(value, type)
    }
}

class ImageSrc {
    @TypeConverter
    fun fromSrcList(value: Src): String {
        val gson = Gson()
        val type = object : TypeToken<Src>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toSrcList(value: String): Src {
        val gson = Gson()
        val type = object : TypeToken<Src>() {}.type
        return gson.fromJson(value, type)
    }
}
