package com.example.myfirstsubmission.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.myfirstsubmission.data.retrofit.Event

class FavoriteEventDiffCallback(private val oldFavoriteList: List<Event>, private val newFavoriteList: List<Event>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldFavoriteList.size

    override fun getNewListSize(): Int = newFavoriteList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteList[oldItemPosition].id == newFavoriteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteList[oldItemPosition] == newFavoriteList[newItemPosition]
    }
}