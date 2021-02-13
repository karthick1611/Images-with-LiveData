package com.online.liveimages.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.online.liveimages.model.GetImages
import com.online.liveimages.model.ResultWrapper
import com.online.liveimages.repositories.GetImagesAPIRepository

class ImagesViewModel :ViewModel() {

    private val _isLoading=MutableLiveData<Boolean>()
    private var context: Context? = null
    private var _imageData:MutableLiveData<ResultWrapper<GetImages>>?=null
    init {
        _isLoading.value=true
        _imageData= context?.let {
            GetImagesAPIRepository.getImagesList(
                it
            )
        }

    }
    val loaderData: LiveData<Boolean> =_isLoading

    fun getImages(context: Context):LiveData<ResultWrapper<GetImages>>{
        this.context = context
        return if (_imageData!=null)
            _imageData as LiveData<ResultWrapper<GetImages>>
        else {
            _isLoading.postValue(true)
            GetImagesAPIRepository.getImagesList(context)
        }
    }

    fun updateLoader(boolean: Boolean){
        _isLoading.postValue(boolean)
    }
}