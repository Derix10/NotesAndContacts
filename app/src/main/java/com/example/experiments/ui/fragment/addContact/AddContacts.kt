package com.example.experiments.ui.fragment.addContact


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Toast
import com.example.experiments.R
import com.example.experiments.base.BaseFragment
import com.example.experiments.databinding.FragmentAddContactsBinding
import com.example.experiments.model.ContactModel
import com.example.experiments.ui.App
import com.example.experiments.ui.fragment.addContact.AddContactAdapter.whatsappAndDialerListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class AddContacts : BaseFragment<FragmentAddContactsBinding>(FragmentAddContactsBinding::inflate),
    whatsappAndDialerListener {

    private var adapter: AddContactAdapter? = null
    private var cursor1: Cursor? = null

    private var contacts = arrayListOf<ContactModel>()


    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        super.setupObserver()


        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    if (p0?.permissionName.equals(Manifest.permission.READ_CONTACTS)) {
                        getContact()
                    }
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(
                        requireContext(),
                        "Permission should be granted !",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }
            }).check()

        if (arguments != null) {
            binding.edName.setText(arguments?.getString("name").toString())
            binding.edContact.setText(arguments?.getString("contact").toString())
            binding.btnSaveContact.text = "change"
        }

        binding.btnSaveContact.setOnClickListener {
            val id = arguments?.getInt("position")
            val nameContact = binding.edName.text.toString()
            val contactNumber = binding.edContact.text.toString()
            if (arguments != null) {
                App.dbContact.contactDao()!!.upDateContact(
                    ContactModel(
                        id = id,
                        name = nameContact,
                        contact = contactNumber
                    )
                )
            } else {
                App.dbContact.contactDao()!!
                    .addContact(
                        ContactModel(
                            name = nameContact,
                            contact = contactNumber
                        )
                    )
            }
            controller.navigate(R.id.contactFragment)
        }
    }


    override fun setupUI() {
        adapter = AddContactAdapter(this)
        binding.rvContact.adapter = adapter
    }

    @SuppressLint("Range", "NotifyDataSetChanged")
    private fun getContact() {
        cursor1 = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null, null, null
        )
        while (cursor1?.moveToNext()!!) {
            val contactNAme =
                cursor1?.getString(cursor1?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)!!)
            val contactContact =
                cursor1?.getString(cursor1?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)!!)
            contacts.add(
                ContactModel(
                    name = contactNAme.toString(),
                    contact = contactContact.toString()
                )
            )
            adapter?.setList(contacts)
            adapter?.notifyDataSetChanged()


        }
    }

    private fun appInstalledOrNot(): Boolean {
        val packageManager = requireActivity().packageManager

        val whatsappInstalled: Boolean? = try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return whatsappInstalled!!
    }


    override fun whatsappClick(message: String, choose: Boolean) {

        if (choose) {
            if (appInstalledOrNot()) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=${message}")
                )
                val appNameWhatsapp = "Whatsapp"
                alertDialogFun(intent, appNameWhatsapp)


            } else {
                Toast.makeText(
                    requireContext(),
                    "App is not installed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (!choose){

                val intent = Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:$message")
                )
                val appNameCall = "Dialer"
                alertDialogFun(intent, appNameCall)
        }
    }

    private fun alertDialogFun(intent: Intent, appName: String) {
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(activity)
        builder.run {
            setTitle("App $appName")
            setMessage("Вы точно хотите перейти в это приложение?")
            setPositiveButton("Да") { _, _ ->
                startActivity(intent)
            }
            setNegativeButton("Нет") { _, _ ->
            }
        }

        alertDialog = builder.create()
        alertDialog.show()
    }
}

