package com.example.chat.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.chat.R
import com.example.chat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(binding.toolbar)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        binding.bottomNavBar.setupWithNavController(navHostFragment.navController)

        // to make all the fragment in the menu bar as top level fragment
        // otherwise it displays up button in other fragments
        val appBarConfiguration = AppBarConfiguration(binding.bottomNavBar.menu)

        // to display fragment name in toolbar
        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.changeTheme -> {
                changeTheme()
                true
            }
            R.id.signOut -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun changeTheme() {
        if(getDefaultNightMode() == MODE_NIGHT_NO)
            setDefaultNightMode(MODE_NIGHT_YES)
        else setDefaultNightMode(MODE_NIGHT_NO)
    }
}