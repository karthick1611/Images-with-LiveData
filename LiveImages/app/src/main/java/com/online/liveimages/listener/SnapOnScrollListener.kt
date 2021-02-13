package com.online.liveimages.listener

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper

class SnapOnScrollListener(
    private var snapHelper: SnapHelper, behavior: Int,
    onSnapPositionChangeListener: onSnapPositionChangeListener
): RecyclerView.OnScrollListener() {

    companion object {
        val NOTIFY_ON_SCROLL_STATE_IDLE = 1
    }
    var behaviour = behavior
    private var snapPosition = RecyclerView.NO_POSITION
    var snapPositionChangeListener: onSnapPositionChangeListener = onSnapPositionChangeListener

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if (behaviour == NOTIFY_ON_SCROLL_STATE_IDLE
            && newState == RecyclerView.SCROLL_STATE_IDLE
        ) {
            dispatchSnapPositionChange(recyclerView)
        }
    }

    private fun dispatchSnapPositionChange(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager != null) {
            val snapView = snapHelper.findSnapView(layoutManager)
            if (snapView != null) {
                val snapPosition = layoutManager.getPosition(snapView)
                val snapPositionChanged = this.snapPosition != snapPosition
                if (snapPositionChanged) {
                    snapPositionChangeListener.onSnapChanged(snapPosition)
                    this.snapPosition = snapPosition
                }
            }
        }
    }

    interface onSnapPositionChangeListener {
        fun onSnapChanged(postion: Int)
    }

}