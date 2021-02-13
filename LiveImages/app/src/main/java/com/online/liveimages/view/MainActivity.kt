package com.online.liveimages.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.online.liveimages.R
import com.online.liveimages.adapter.ImageListAdapter
import com.online.liveimages.adapter.SpacesItemDecoration
import com.online.liveimages.model.GetImages
import com.online.liveimages.model.ResultWrapper
import com.online.liveimages.room.GetImagesDatabase
import com.online.liveimages.viewModel.ImagesViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var imagesViewModel: ImagesViewModel

    var imagesDatabase: GetImagesDatabase? = null
    private lateinit var imageListAdapter: ImageListAdapter

    private fun initializeDB(context: Context) : GetImagesDatabase {
        return GetImagesDatabase.getDatabaseClient(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imagesDatabase = initializeDB(this)
        imagesViewModel = ViewModelProvider(this).get(ImagesViewModel::class.java)

        recyclerImages.layoutManager = GridLayoutManager(this, 3)
        recyclerImages.addItemDecoration(SpacesItemDecoration(4))

        imagesViewModel.getImages(this).observe(this, {
            imagesViewModel.updateLoader(false)
            updateView(it)
        })

        imagesViewModel.loaderData.observe(this, {
            if (it)
                loader_group.visibility = View.VISIBLE
            else
                loader_group.visibility = View.GONE
        })

    }

    private fun updateView(it: ResultWrapper<GetImages>) {
        when (it) {
            is ResultWrapper.Success -> {
                val response = it.data
                response.let {
                    imagesDatabase?.getImagesDao()?.emptyTable()
                    imagesDatabase?.getImagesDao()?.insertData(response)

                    imageListAdapter = ImageListAdapter(response.photos)
                    recyclerImages.adapter = imageListAdapter
                }
            }
            is ResultWrapper.Error -> {
                Toast.makeText(this, "code  " + it.code + "  error " + it.error, Toast.LENGTH_SHORT)
                    .show()
            }
            ResultWrapper.NetworkError -> {
                Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show()
                val getImages = imagesDatabase?.getImagesDao()?.getPhotos()
                imageListAdapter = ImageListAdapter(getImages?.photos!!)
                recyclerImages.adapter = imageListAdapter
            }
        }
    }

}