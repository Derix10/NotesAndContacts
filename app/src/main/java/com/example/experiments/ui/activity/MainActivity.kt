package com.example.experiments.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.experiments.R
import com.example.experiments.R.id.noteFragment
import com.example.experiments.databinding.ActivityMainBinding
import com.example.experiments.ui.App
import com.example.experiments.ui.fragment.contacts.ContactFragment
import com.example.experiments.ui.fragment.note.NoteFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller : NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                NavHostFragment

        controller = navHostFragment.navController

        if(!App.prefs.isBoardShow()){
            controller.navigate(R.id.onBoardFragment)

        }


        binding.botNavView.setOnItemSelectedListener {
            when (it.itemId) {
                noteFragment -> replaceFragment(NoteFragment())
                R.id.contactFragment -> replaceFragment(ContactFragment())
                else -> {
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment)
        fragmentTransaction.commit()
    }

}