package com.example.experiments.ui.fragment.note

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.databinding.ItemNoteBinding
import com.example.experiments.model.NoteModel
import com.example.experiments.ui.App

class NoteAdapter(private val listener: NoteListener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private var list: ArrayList<NoteModel> = arrayListOf()
    private var position : Int? = null

    interface NoteListener{
        fun onClickNote(model: NoteModel)
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
    class NoteViewHolder(private val binding: ItemNoteBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: NoteModel, listener: NoteListener) {
            itemView.setOnClickListener {
                listener.onClickNote(model )
            }
           binding.itemTvTitle.text = model.title
           binding.itemTvDescription.text = model.description
        }

    }

}