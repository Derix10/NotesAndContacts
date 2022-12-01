package com.example.experiments.ui.fragment.note

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.databinding.ItemNoteForGridBinding
import com.example.experiments.model.NoteModel
import com.example.experiments.ui.App
import kotlin.collections.ArrayList

class NoteAdapterGridLayout(private val listener: NoteListener) : RecyclerView.Adapter<NoteAdapterGridLayout.NoteGridViewHolder>() {
    private var list: ArrayList<NoteModel> = arrayListOf()
    private var position : Int? = null

    interface NoteListener{
        fun onClickNote(model: NoteModel)
        fun onClickGridLayout()
        fun onClickGridReplaceDateOrAsc()
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteGridViewHolder {
        val binding = ItemNoteForGridBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return NoteGridViewHolder(binding)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: NoteGridViewHolder, position: Int) {
        holder.onBind(list[position], listener)

        this.position = position
    }

    override fun getItemCount(): Int = list.size
    class NoteGridViewHolder(val binding: ItemNoteForGridBinding)
        : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun onBind(model: NoteModel, listener: NoteListener) {
            itemView.setOnClickListener {
                listener.onClickNote(model )
            }
            binding.layout.setOnClickListener{
                listener.onClickGridLayout()
            }
            binding.btnReplace.setOnClickListener{
                listener.onClickGridReplaceDateOrAsc()
            }
            binding.itemTvTitle.text = model.title
            binding.itemTvDescription.text = model.description
            binding.data.text = model.createdTime


        }

    }

}