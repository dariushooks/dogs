package dariushooks.android.dogs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        val bottomNav : BottomNavigationView = findViewById(R.id.bottom_nav)
        val navHost = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupWithNavController(bottomNav, navController)

        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

            }
        })
    }
}