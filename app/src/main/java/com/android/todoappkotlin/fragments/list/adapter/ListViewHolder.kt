package com.android.todoappkotlin.fragments.list.adapter

import android.os.Build
import android.os.Build.VERSION_CODES.M
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.todoappkotlin.R
import com.android.todoappkotlin.data.model.Priority
import com.android.todoappkotlin.data.model.ToDoData
import com.android.todoappkotlin.databinding.RowLayoutBinding
import com.android.todoappkotlin.fragments.list.ListFragmentDirections

class ListViewHolder(
    private val rowLayoutBinding: RowLayoutBinding
):RecyclerView.ViewHolder(rowLayoutBinding.root) {

    private val tvTitle = rowLayoutBinding.tvRowTitle
    private val tvDescription = rowLayoutBinding.tvRowDescription
    private val cardPriority = rowLayoutBinding.cardPriority

    @RequiresApi(M)
    fun bind(toDoData: ToDoData){
        tvTitle.text = toDoData.title
        tvDescription.text = toDoData.description
        when(toDoData.priority){
            Priority.HIGH -> {cardPriority.setCardBackgroundColor(cardPriority.context.getColor(R.color.red))}
            Priority.MEDIUM -> {cardPriority.setCardBackgroundColor(cardPriority.context.getColor(R.color.yellow))}
            Priority.LOW -> {cardPriority.setCardBackgroundColor(cardPriority.context.getColor(R.color.green))}
        }
        rowLayoutBinding.rowBackground.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToUpdateFragment(toDoData)
            itemView.findNavController().navigate(action)
        }
    }

    companion object{
        fun create(parent: ViewGroup): ListViewHolder{
            val inflater = LayoutInflater.from(parent.context)
            val itemBinding = RowLayoutBinding.inflate(inflater, parent, false)
            return ListViewHolder(itemBinding)
        }
    }
}