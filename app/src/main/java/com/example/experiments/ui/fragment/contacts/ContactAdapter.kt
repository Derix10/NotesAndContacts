package com.example.experiments.ui.fragment.contacts

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.databinding.ItemContactBinding
import com.example.experiments.model.ContactModel
import com.example.experiments.ui.App

class ContactAdapter(private val listener: ContactListener) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {
    private var list: ArrayList<ContactModel> = arrayListOf()
    interface ContactListener{
        fun onClickContact(model: ContactModel)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun addContact(list: ArrayList<ContactModel>){
        this.list = list
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeContact(position: Int){
        App.dbContact.contactDao()?.deleteContact(list.removeAt(position))
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val binding = ItemContactBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.onBind(list[position], listener)
    }

    override fun getItemCount(): Int = list.size


    class ContactViewHolder(private val binding: ItemContactBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: ContactModel, listener: ContactListener) {
            itemView.setOnClickListener{
                listener.onClickContact(model)
            }
            binding.itemTvContact.text= model.contact
            binding.itemTvName.text = model.name
        }

    }

}