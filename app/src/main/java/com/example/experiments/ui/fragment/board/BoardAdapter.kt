package com.example.experiments.ui.fragment.board

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.R
import com.example.experiments.databinding.ItemOnBoardBinding

class BoardAdapter : RecyclerView.Adapter<BoardAdapter.BoardViewHolder>() {


    val titleList = listOf("Заметки" , "Контакты ", "Конец")
    val decsList = listOf("Добавляем заметки" , "Доступ ко всем контактам ", "Это все что есть")
    val imgist = listOf(R.drawable.my_gif, R.drawable.my_gif2, R.drawable.my_gif3)


    inner class BoardViewHolder(val binding: ItemOnBoardBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun onBind(position: Int) {
            binding.imgBoardItem.setBackgroundResource(imgist[position])
            binding.tvOnBoardItemTit.text = titleList[position]
            binding.tvOnBoardItemDes.text = decsList[position]
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val binding = ItemOnBoardBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return BoardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return titleList.size
    }

}