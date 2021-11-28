package com.kmozcan1.bunqpaymentapp.presentation.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.bunq.sdk.context.ApiContext
import com.google.android.material.appbar.MaterialToolbar
import com.kmozcan1.bunqpaymentapp.R
import com.kmozcan1.bunqpaymentapp.databinding.ActivityMainBinding
import com.kmozcan1.bunqpaymentapp.domain.model.Event
import com.kmozcan1.bunqpaymentapp.presentation.viewmodel.MainViewModel
import com.kmozcan1.bunqpaymentapp.presentation.viewstate.MainViewState
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var isConnectedToInternet: Boolean = false
        private set

    val viewModel: MainViewModel by viewModels()

    var bunqApiContext: ApiContext? = null
        private set

    private val navHostFragment : NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }

    private val navController: NavController by lazy {
        navHostFragment.navController
    }

    private val navGraph: NavGraph by lazy {
        navController.navInflater.inflate(R.navigation.nav_graph)
    }

    val actionBar: MaterialToolbar by lazy {
        findViewById(R.id.top_app_bar)
    }

    private val appBarConfiguration: AppBarConfiguration by lazy {
        AppBarConfiguration(navController.graph)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_BunqPaymentApp)
        super.onCreate(savedInstanceState)
        setViews()
        viewModel.observeInternetConnection()
        viewModel.getBunqApiContext()
        viewModel.internetConnectionLiveData.observe(this, observeInternetConnection())
        viewModel.fragmentNavigationEvent.observe(this, observeFragmentNavigation())
        viewModel.viewState.observe(this, observeViewState())
    }

    /** Observes MainViewState */
    private fun observeViewState() = Observer<MainViewState> { viewState ->
        when (viewState) {
            is MainViewState.BunqApiContext -> {
                bunqApiContext = viewState.bunqApiContext
                binding.initializingLayout.visibility = View.GONE
            }
            is MainViewState.Error -> TODO()
            MainViewState.Loading -> TODO()
        }
    }

    /** Observer method for fragmentNavigationEvent LiveData. Handles fragment navigation.
     * While pretty straight-forward for this app, this method allows adding custom
     * stuff between navigation (like checking for permission) and keeps it all in one place
     */
    private fun observeFragmentNavigation() = Observer<Event<NavDirections>> { navEvent ->
        navEvent.getContentIfNotHandled()?.let { navDirections ->
            when(navDirections.actionId) {

            }
        }
    }

    /** Observes the internet connectivity */
    private fun observeInternetConnection() = Observer<Boolean> { connection ->
        isConnectedToInternet = connection
        val currentFragment = getActiveFragment()
        // Handles BaseFragment connection change
        if (currentFragment is BaseFragment<*, *>) {
            if (isConnectedToInternet) {
                currentFragment.onInternetConnected()
            } else {
                currentFragment.onInternetDisconnected()
            }
        }
    }


    /** View related stuff goes here */
    private fun setViews() {
        // view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionBar.setupWithNavController(navController, appBarConfiguration)
    }

    /** Returns the fragment that is currently on the screen */
    private fun getActiveFragment(): Fragment? {
        return if (supportFragmentManager.fragments.size == 0) {
            null
        } else {
            supportFragmentManager.fragments
                .first()?.childFragmentManager?.fragments?.get(0)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}