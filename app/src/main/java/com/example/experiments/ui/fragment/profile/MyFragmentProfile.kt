package com.example.experiments.ui.fragment.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import com.example.experiments.base.BaseFragment
import com.example.experiments.databinding.FragmentProfileBinding


class MyFragmentProfile : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    companion object{
        const val IMAGE_PICK_CODE = 1000
        const val PERMISSION_CODE = 1001
    }

    override fun setupUI() {
    }

    override fun setupObserver() {
      binding.btnGallery.setOnClickListener{
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
              if (requireActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                  == PackageManager.PERMISSION_DENIED){

                  val permissions = Manifest.permission.READ_EXTERNAL_STORAGE
                  requireActivity().requestPermissions(arrayOf(permissions), PERMISSION_CODE)

              }
              else{
                  pickImageFromGallery()
              }
          }
          else{
              pickImageFromGallery()

          }
      }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }else{
                    Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
             binding.imgGallery.setImageURI(data?.data)

        }
    }
}