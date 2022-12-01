package com.example.experiments.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.experiments.R
import com.example.experiments.R.id.noteFragment
import com.example.experiments.databinding.ActivityMainBinding
import com.example.experiments.ui.App
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var controller : NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navBottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navBottomNav = binding.botNavView

        initNavCotroller()
    }

    override fun onSupportNavigateUp(): Boolean {
        return controller.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
    private fun initNavCotroller(){

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as
                NavHostFragment
        controller = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                noteFragment,
                R.id.contactFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(controller, appBarConfiguration)
        navBottomNav.setupWithNavController(controller)


        if(!App.prefs.isBoardShow()){
            controller.navigate(R.id.onBoardFragment)
        }

        controller.addOnDestinationChangedListener { _, _, _ ->
            if (controller.currentDestination?.id == R.id.onBoardFragment
            ) {
                binding.botNavView.visibility = View.GONE
            } else {
                binding.botNavView.visibility = View.VISIBLE
            }

        }

    }
}