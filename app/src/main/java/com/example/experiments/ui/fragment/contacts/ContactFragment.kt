package com.example.experiments.ui.fragment.contacts


import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.experiments.R
import com.example.experiments.base.BaseFragment
import com.example.experiments.databinding.FragmentContactBinding
import com.example.experiments.model.ContactModel
import com.example.experiments.ui.App
import com.example.experiments.ui.fragment.addContact.AddContacts
import com.example.experiments.ui.fragment.note.SwipeToDelete

class ContactFragment : BaseFragment<FragmentContactBinding>(FragmentContactBinding::inflate), ContactAdapter.ContactListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupUI()
    }

    override fun setupUI() {

        val adapter = ContactAdapter(this)
        binding.rcViewNew.adapter = adapter
        adapter.addContact(App.dbContact.contactDao()!!.getAllContact() as ArrayList<ContactModel>)


        val swipeToDelete = object : SwipeToDelete() {
            private var alertDialog: AlertDialog? = null
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val itemPosition = viewHolder.adapterPosition

                val builder = AlertDialog.Builder(activity)
                builder.run {
                    setTitle("Delete project list")
                    setMessage("You want to delete project?")
                    setPositiveButton("Yes") { _, _ ->   adapter.removeContact(itemPosition)                }
                    setNegativeButton("Cancel") { _, _ -> adapter.notifyItemChanged(itemPosition)
                    }
                }

                alertDialog = builder.create()
                alertDialog?.show()
            }

        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(binding.rcViewNew)

    }

    override fun onClickContact(model: ContactModel) {
        val bundle = Bundle()
        bundle.putInt("position", model.id!!)
        bundle.putString("name", model.name)
        bundle.putString("contact", model.contact)
        controller.navigate(R.id.addContacts, bundle)
    }
    override fun setupObserver() {
        super.setupObserver()

        binding.btnAddContact.setOnClickListener {
            controller.navigate(R.id.addContacts)


        }
    }



}
