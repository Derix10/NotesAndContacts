package com.example.experiments.ui.fragment.addContact

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.databinding.ItemWhatsappOrDialerBinding
import com.example.experiments.model.ContactModel

class AddContactAdapter(private var listener: whatsappAndDialerListener): RecyclerView.Adapter<AddContactAdapter.AddConViewHolder>() {


    private var list = listOf<ContactModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<ContactModel>){
        this.list = list
        notifyDataSetChanged()

    }
    class AddConViewHolder(val binding: ItemWhatsappOrDialerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(contactModel: ContactModel) {

            binding.itemWhatsappName.text = contactModel.name
            binding.itemWhatsappContact.text = contactModel.contact
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddConViewHolder {
        val binding = ItemWhatsappOrDialerBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return AddConViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddConViewHolder, position: Int) {
        holder.onBind(list[position])
        holder.binding.whatsapIMG.setOnClickListener{
            listener.whatsappClick(list[position].contact, true)
        }
        holder.binding.dialerImg.setOnClickListener{
            listener.whatsappClick(list[position].contact, false)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }



    interface whatsappAndDialerListener{

        fun whatsappClick(message: String, choose: Boolean)

    }



}