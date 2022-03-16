package com.example.flyingdevicemanager.app_ui


import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.flyingdevicemanager.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    
    companion object {
        const val STORAGE_PERMISSION_CODE = 101
    }
    
    override fun onBackPressed() {
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    
        supportActionBar?.hide()
        
        val bottomNavigationView  =findViewById<BottomNavigationView>(R.id.nav)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.deviceFragment, R.id.userKycFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        
        bottomNavigationView.setupWithNavController(navController)
        
        checkPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE)
        
    }
    
    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Storage Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Storage Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}