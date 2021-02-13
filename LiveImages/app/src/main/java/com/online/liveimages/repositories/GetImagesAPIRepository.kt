package com.online.liveimages.repositories

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.online.liveimages.model.GetImages
import com.online.liveimages.model.ResultWrapper
import com.online.liveimages.restClient.RestClient
import com.online.liveimages.room.GetImagesDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import retrofit2.HttpException
import java.io.IOException

object GetImagesAPIRepository {
    var getImagesTypeJob:CompletableJob?=null

    var imagesDatabase: GetImagesDatabase? = null

    private fun initializeDB(context: Context) : GetImagesDatabase {
        return GetImagesDatabase.getDatabaseClient(context)
    }

    fun getImagesList(context: Context):MutableLiveData<ResultWrapper<GetImages>>{

        getImagesTypeJob= Job()
//        imagesDatabase = initializeDB(context)

        return object :MutableLiveData<ResultWrapper<GetImages>>(){
            override fun onActive() {
                super.onActive()
                getImagesTypeJob?.let {
                    CoroutineScope(IO+ it).launch {
                        try {
                            val data = RestClient.apiService.getImages()
                            withContext(Main){
                                value=ResultWrapper.Success(data)
//                                imagesDatabase?.getImagesDao()?.insertData(data)
                                it.complete()
                            }
                        }catch (throwable:Throwable){
                           when(throwable){
                               is IOException->{
                                   withContext(Main) {
                                       value = ResultWrapper.NetworkError
                                       it.complete()
                                   }
                               }
                               is HttpException->{
                                   withContext(Main) {
                                       value = ResultWrapper.Error(
                                           throwable.code(),
                                           throwable.message()
                                       )
                                       it.complete()
                                   }
                               }
                               else->{
                                   withContext(Main) {
                                       value = ResultWrapper.NetworkError
                                       it.complete()
                                   }
                               }
                           }
                        }
                    }
                }
            }
        }
    }

    fun cancelJob(){
        getImagesTypeJob?.cancel()
    }
}