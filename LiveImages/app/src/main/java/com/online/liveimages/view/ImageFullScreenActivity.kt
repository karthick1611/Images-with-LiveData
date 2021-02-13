package com.online.liveimages.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.online.liveimages.R
import com.online.liveimages.adapter.GalleryFullScreenAdapter
import com.online.liveimages.listener.SnapOnScrollListener
import com.online.liveimages.model.Photo
import kotlinx.android.synthetic.main.activity_image_full_screen.*


class ImageFullScreenActivity : AppCompatActivity(), SnapOnScrollListener.onSnapPositionChangeListener {

    private var galleryFullScreenAdapter: GalleryFullScreenAdapter? = null
    private var imageModels: List<Photo>? = null
    private var pos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_full_screen)

        val verticalLayoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        full_recycler.layoutManager = verticalLayoutManager
        val snapHelper: SnapHelper = PagerSnapHelper()
        full_recycler.layoutManager = verticalLayoutManager
        snapHelper.attachToRecyclerView(full_recycler)

        val intent = intent
        if (intent != null) {
            imageModels = intent.getSerializableExtra("imageList") as List<Photo>?
            pos = intent.getIntExtra("pos", 0)
        }

        if (imageModels != null && imageModels!!.isNotEmpty()) {
            galleryFullScreenAdapter = GalleryFullScreenAdapter(imageModels!!)
            full_recycler.adapter = galleryFullScreenAdapter
            if (imageModels != null && pos < imageModels!!.size) {
                full_recycler.scrollToPosition(pos)
            }
            if (pos == 0) {
                prev_ic.visibility = View.GONE
                next_iv.visibility = View.VISIBLE
                if (imageModels!!.size == 1) {
                    next_iv.visibility = View.GONE
                    prev_ic.visibility = View.GONE
                }
            } else if (pos == imageModels?.size!! - 1) {
                next_iv.visibility = View.GONE
                prev_ic.visibility = View.VISIBLE
            } else {
                next_iv.visibility = View.VISIBLE
                prev_ic.visibility = View.VISIBLE
            }
        } else {
            next_iv.visibility = View.GONE
            prev_ic.visibility = View.GONE
        }

        next_iv.setOnClickListener {
            if (pos < imageModels!!.size - 1) {
                pos += 1
                scrollToPos(pos)
                prev_ic.visibility = View.VISIBLE
                if (pos == imageModels!!.size - 1) {
                    next_iv.visibility = View.GONE
                }
            } else {
                next_iv.visibility = View.GONE
            }
        }

        prev_ic.setOnClickListener {
            if (pos > 0) {
                pos -= 1
                scrollToPos(pos)
                next_iv.visibility = View.VISIBLE
                if (pos == 0) {
                    prev_ic.visibility = View.GONE
                }
            } else {
                prev_ic.visibility = View.GONE
            }
        }

        full_recycler.addOnScrollListener(
            SnapOnScrollListener(
                snapHelper,
                SnapOnScrollListener.NOTIFY_ON_SCROLL_STATE_IDLE,
                this
            )
        )

    }

    override fun onSnapChanged(postion: Int) {
        pos=postion
        if (imageModels!=null&&postion<imageModels?.size!!){
            when (pos) {
                0 -> {
                    next_iv.visibility = View.VISIBLE
                    prev_ic.visibility = View.GONE
                }
                imageModels?.size!! - 1 -> {
                    next_iv.visibility = View.GONE
                    prev_ic.visibility = View.VISIBLE
                }
                else -> {
                    next_iv.visibility = View.VISIBLE
                    prev_ic.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun scrollToPos(pos: Int) {
        if (imageModels != null) {
            if (pos > -1 && pos < imageModels!!.size) {
                full_recycler.smoothScrollToPosition(pos)
            }
        }
    }

}