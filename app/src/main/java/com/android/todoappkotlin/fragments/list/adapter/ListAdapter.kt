package com.android.todoappkotlin.fragments.list.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.todoappkotlin.R
import com.android.todoappkotlin.data.model.Priority
import com.android.todoappkotlin.data.model.ToDoData
import com.android.todoappkotlin.databinding.RowLayoutBinding
import com.android.todoappkotlin.fragments.list.ListFragmentDirections

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList = emptyList<ToDoData>()

    class MyViewHolder(private val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root){

        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(toDoData: ToDoData){
            val tvTitle = binding.tvRowTitle
            val tvDescription = binding.tvRowDescription
            val cardPriority = binding.cardPriority
            val tvData = binding.tvRowData

            tvTitle.text = toDoData.title
            tvDescription.text = toDoData.description
            if (tvData.text.isNotEmpty()){
                tvData.text = "${toDoData.date}  ${toDoData.hour}"
            }else{
                tvData.text = ""
            }
            when(toDoData.priority){
                Priority.HIGH -> {cardPriority.setCardBackgroundColor(cardPriority.context.getColor(
                    R.color.red))}
                Priority.MEDIUM -> {cardPriority.setCardBackgroundColor(cardPriority.context.getColor(
                    R.color.yellow))}
                Priority.LOW -> {cardPriority.setCardBackgroundColor(cardPriority.context.getColor(R.color.green))}
            }
            binding.rowBackground.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(toDoData)
                itemView.findNavController().navigate(action)
            }
        }
        companion object{
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(
            parent
        )
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.bind(currentItem)
    }

    fun setData(toDoData: List<ToDoData>){
        val toDoDiffUtil = ToDoDiffUtil(dataList, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}