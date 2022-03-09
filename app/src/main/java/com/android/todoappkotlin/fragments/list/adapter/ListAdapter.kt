package com.android.todoappkotlin.fragments.list.adapter

import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.android.todoappkotlin.data.model.ToDoData

class ListAdapter : ListAdapter<ToDoData, ListViewHolder>(diffCallBacks) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.create(parent)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val diffCallBacks = object : DiffUtil.ItemCallback<ToDoData>() {
            override fun areItemsTheSame(oldItem: ToDoData, newItem: ToDoData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ToDoData, newItem: ToDoData): Boolean {
                return oldItem == newItem
            }

        }
    }
}