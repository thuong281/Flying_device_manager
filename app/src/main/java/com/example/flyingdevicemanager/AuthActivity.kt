package com.example.flyingdevicemanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController

class AuthActivity : AppCompatActivity() {
    
//    private var navController: NavController? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
    
        supportActionBar?.hide()
    
//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
//        navController = navHostFragment!!.navController
//        setupActionBarWithNavController(this, navController!!)
    }
    
//    override fun onSupportNavigateUp(): Boolean {
//        navController!!.navigateUp()
//        return super.onSupportNavigateUp()
//    }
//
}