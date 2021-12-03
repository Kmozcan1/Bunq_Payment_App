package com.kmozcan1.bunqpaymentapp.presentation.view

import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.kmozcan1.bunqpaymentapp.R
import com.kmozcan1.bunqpaymentapp.databinding.ActivityMainBinding
import com.kmozcan1.bunqpaymentapp.domain.model.Event
import com.kmozcan1.bunqpaymentapp.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val viewModel: MainViewModel by viewModels()

    private val navHostFragment : NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }

    private val navController: NavController by lazy {
        navHostFragment.navController
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
        viewModel.fragmentNavigationEvent.observe(this, observeFragmentNavigation())
    }

    /**
     * Observer method for fragmentNavigationEvent LiveData. Handles fragment navigation.
     * BaseFragment type Fragments can set the fragmentNavigationEvent from MainViewModel
     * via methods declared in BaseFragment.FragmentNavigation. fragmentNavigationEvent is
     * an Event type MutableLiveData and is consumed only once.
     * While this method may feel over-engineered for this project, it is quite useful for
     * having the navigation logic in one place as well as doing stuff between navigation
     * where Activity is used, such as asking for permissions.
     */
    private fun observeFragmentNavigation() = Observer<Event<NavDirections>> { navEvent ->
        navEvent.getContentIfNotHandled()?.let { navDirections ->
            when(navDirections.actionId) {
                R.id.action_homeFragment_to_paymentFragment,
                R.id.action_paymentFragment_to_homeFragment,
                R.id.action_homeFragment_to_paymentDetailFragment -> {
                    navController.navigate(navDirections)
                }
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

    /** Called by BaseFragment fragments to display an alert dialog when a network
     * related error occurs. Clicking the Retry button calls a method that can be
     * overridden by BaseFragments in order to make the failed api call again */
    fun createNetworkErrorDialog() {
        val currentFragment = getActiveFragment()
        if (currentFragment is BaseFragment<*, *>) {
            val alertDialog: AlertDialog = this.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setTitle(getString(R.string.network_error))
                    setMessage(getString(R.string.network_alert_dialog_message))
                    setPositiveButton(R.string.retry) { _, _ ->
                        currentFragment.onNetworkErrorDialogRetryButtonClicked()
                    }
                    setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.cancel()
                    }
                    setTheme(R.style.AlertDialogTheme)
                    setCancelable(false)
                }
                // Create the AlertDialog
                builder.create()
            }
            alertDialog.show()
        }
    }
}