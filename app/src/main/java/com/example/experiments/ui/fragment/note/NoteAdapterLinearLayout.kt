package com.example.experiments.ui.fragment.note

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.databinding.ItemNoteBinding
import com.example.experiments.model.NoteModel
import com.example.experiments.ui.App
import kotlin.collections.ArrayList

class NoteAdapterLinearLayout(private val listener: NoteListener) : RecyclerView.Adapter<NoteAdapterLinearLayout.NoteViewHolder>() {
    private var list: ArrayList<NoteModel> = arrayListOf()
    private var position : Int? = null

    interface NoteListener{
        fun onClickNote(model: NoteModel)
        fun onClickLinearLayout()
        fun onClickReplaceDateOrAsc()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setNote(list: ArrayList<NoteModel>){
        this.list = list
        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun removeNote(position: Int){
        App.db.noteDao()!!.deleteNote(list.removeAt(position))
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }
    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.onBind(list[position], listener)

        this.position = position
    }
    override fun getItemCount(): Int = list.size
    class NoteViewHolder(val binding: ItemNoteBinding)
        : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun onBind(model: NoteModel, listener: NoteListener) {
            itemView.setOnClickListener {
                listener.onClickNote(model )
            }
            binding.layout.setOnClickListener{
                listener.onClickLinearLayout()
            }
            binding.btnReplace.setOnClickListener{
                listener.onClickReplaceDateOrAsc()
            }
            var bundlee = Bundle()
            bundlee.putInt("keyPosition", model.id!! )
            binding.itemTvTitle.text = model.title
            binding.itemTvDescription.text = model.description
            binding.data.text = model.createdTime

        }

    }

}