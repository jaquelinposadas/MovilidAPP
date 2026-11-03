package com.example.movilidapp

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.movilidapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        window.statusBarColor = Color.TRANSPARENT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNav.setOnItemSelectedListener { item ->
            val options = androidx.navigation.NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(true)
                .setPopUpTo(
                    navController.graph.startDestinationId,
                    inclusive = false,
                    saveState = true
                )
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.fade_out)
                .build()

            try {
                navController.navigate(item.itemId, null, options)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }

        // Add a listener to sync the bottom bar and change the profile icon/logo depending on the destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Keep the selected item visually in sync when hitting back
            if (binding.bottomNav.selectedItemId != destination.id) {
                binding.bottomNav.menu.findItem(destination.id)?.isChecked = true
            }
            when (destination.id) {
                R.id.navigation_saved -> {
                    binding.profileIcon.setBackgroundResource(R.drawable.bg_profile)
                    binding.profileIcon.setImageResource(0) // Remove S logo
                }
                R.id.navigation_nearby, R.id.navigation_help -> {
                    binding.profileIcon.setBackgroundResource(0)
                    binding.profileIcon.setImageResource(R.drawable.ic_logo_s)
                }
            }
        }
    }

    fun updateLocationText(location: String?) {
        if (location.isNullOrEmpty()) {
            binding.llTopLocation.visibility = View.INVISIBLE
        } else {
            binding.llTopLocation.visibility = View.VISIBLE
            binding.tvTopLocation.text = location
        }
    }
}