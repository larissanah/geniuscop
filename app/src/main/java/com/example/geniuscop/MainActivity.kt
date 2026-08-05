package com.example.geniuscop

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.SwitchPreferenceCompat
import com.example.geniuscop.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = findViewById(R.id.drawer_layout)

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val btnAbrirMenu = findViewById<Button>(R.id.abrirmenu)
        btnAbrirMenu.setOnClickListener {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START)
            } else {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        val iniciar = findViewById<Button>(R.id.btnStart)
        iniciar.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GameFragment())
                .addToBackStack(null)
                .commit()
            iniciar.visibility = View.GONE
        }
        val intent = Intent(this, MusicService::class.java)
        startService(intent)

        if (savedInstanceState == null){
            navigationView.setCheckedItem(R.id.nav_main)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.imageperfil -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
            }
            R.id.exit -> {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                         onBackPressedDispatcher.onBackPressed()
                }
            }
            R.id.nav_main -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)}
            R.id.nav_settings -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment())
                .commit()
            R.id.nav_progress -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ProgressFragment())
                .commit()
            R.id.nav_help -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HelpFragment())
                .commit()
            R.id.nav_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)}
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}


