package com.example.experiments.ui.fragment.addContact


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.experiments.R
import com.example.experiments.base.BaseFragment
import com.example.experiments.databinding.FragmentAddContactsBinding
import com.example.experiments.model.ContactModel
import com.example.experiments.ui.App
import com.example.experiments.ui.fragment.contacts.ContactFragment


class AddContacts : BaseFragment<FragmentAddContactsBinding>(FragmentAddContactsBinding::inflate) {



    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()

        if (arguments != null) {
            binding.edName.setText(arguments?.getString("name").toString())
            binding.edContact.setText(arguments?.getString("contact").toString())
            binding.btnSaveContact.text = "change"

        }
    }
    override fun setupUI() {
        binding.btnSaveContact.setOnClickListener {
            val id = arguments?.getInt("position")
            val nameContact = binding.edName.text.toString()
            val contactNumber = binding.edContact.text.toString()
            if (arguments != null){
                App.dbContact.contactDao()!!.upDateContact(ContactModel(
                    id = id,
                    name = nameContact,
                    contact = contactNumber))
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,
                    ContactFragment()
                ).addToBackStack(null).commit()
            }else {
                App.dbContact.contactDao()!!
                    .addContact(ContactModel(
                        name = nameContact,
                        contact = contactNumber))
                requireActivity().supportFragmentManager.beginTransaction().replace(
                    R.id.nav_host_fragment,
                    ContactFragment()
                ).addToBackStack(null).commit()
            }
          }

    }
}
