package com.idealkr.newstube.presentation

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.idealkr.newstube.R
import com.idealkr.newstube.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    companion object {
        private val mainFragment =
            setOf(R.id.fragment_home, R.id.fragment_live, R.id.fragment_bookmark)
        private const val homeMenuId = R.menu.home_app_bar_menu
        private const val defaultMenuId = R.menu.default_app_bar_menu
    }

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var menuProvider: MenuProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupNavigation()
        setupActionBar()
    }

    private fun setupActionBar() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragment_home -> setupMenuProvider(homeMenuId)
                R.id.fragment_watch -> supportActionBar?.hide()
                else -> setupMenuProvider(defaultMenuId)
            }
        }
    }

    private fun setupMenuProvider(menuId: Int) {
        if (this::menuProvider.isInitialized) removeMenuProvider(menuProvider)
        supportActionBar?.show()

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(menuId, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return true
            }
        }

        addMenuProvider(menuProvider)
    }

    private fun setupNavigation() {
        val host = supportFragmentManager
            .findFragmentById(R.id.search_nav_host_fragment) as NavHostFragment
        navController = host.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(mainFragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> navigateFragment(R.id.fragment_search)
            else -> onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun navigateFragment(fragment: Int) {
        val options = navOptions {
            anim {
                enter = androidx.navigation.ui.R.anim.nav_default_enter_anim
                exit = androidx.navigation.ui.R.anim.nav_default_exit_anim
                popEnter = androidx.navigation.ui.R.anim.nav_default_pop_enter_anim
                popExit = androidx.navigation.ui.R.anim.nav_default_pop_exit_anim
            }
        }

        navController.navigate(fragment, null, options)
    }

    fun setBottomNavigationVisibility(visibility: Int) {
        binding.bottomNavigationView.visibility = visibility
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            if (currentFocus is EditText) {
                val currentFocus = currentFocus as EditText
                currentFocus.getGlobalVisibleRect(Rect())

                if (!Rect().contains(event.rawX.toInt(), event.rawY.toInt())) {
                    currentFocus.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }
}