package com.example.experiments.ui.fragment.addContact


import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.experiments.R
import com.example.experiments.base.BaseFragment
import com.example.experiments.databinding.FragmentAddContactsBinding
import com.example.experiments.model.ContactModel
import com.example.experiments.ui.App
import com.example.experiments.ui.fragment.contacts.ContactFragment


class AddContacts : BaseFragment<FragmentAddContactsBinding>(FragmentAddContactsBinding::inflate) {

    private val CONTACT_PERMISSION_CODE = 1

    private val CONTACT_PIC_CODE = 2


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
        binding.btnContactPhone.setOnClickListener {
            if(checkCOntactPermission()){
                pickContact()

            } else{
                requestContactPermission()
            }
        }

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
    private fun checkCOntactPermission(): Boolean{

        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_CONTACTS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestContactPermission(){
        val permission = arrayOf(android.Manifest.permission.READ_CONTACTS)
        ActivityCompat.requestPermissions(requireActivity(),permission, CONTACT_PERMISSION_CODE)

    }

    private fun pickContact(){
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, CONTACT_PIC_CODE)

    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == CONTACT_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickContact()
            }else{
                Toast.makeText(requireContext(),"Permission denied...", Toast.LENGTH_SHORT).show()

            }
        }

        }



    @Deprecated("Deprecated in Java")
    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){

            if(requestCode == CONTACT_PIC_CODE){
                val cursor1: Cursor
                val cursor2: Cursor

                val uri = data!!.data
                cursor1 = requireContext().contentResolver.query(uri!!, null, null, null, null,null)!!
                if(cursor1.moveToFirst()){
                    val contactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID))
                    val contactNAme = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val contactThumbnail = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI))
                    val idResults = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                    val idResultHold = idResults.toInt()

                    binding.edName.setText(contactNAme.toString())


                    if(contactThumbnail != null){
                        binding.imgContact.setImageURI(Uri.parse(contactThumbnail))

                    }
                    if(idResultHold == 1){
                        cursor2 = requireContext().contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactID,
                            null,
                            null)!!

                        while (cursor2.moveToNext()){
                            val contactNumber = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            binding.edContact.setText(contactNumber.toString())
                        }

                        cursor2.close()
                    }

                    cursor1.close()

                }

            }
        }else{
            Toast.makeText(requireContext(),"Canceled", Toast.LENGTH_SHORT).show()
        }

    }
}
