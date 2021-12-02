package com.kmozcan1.bunqpaymentapp.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.kmozcan1.bunqpaymentapp.application.BunqPaymentApp
import com.kmozcan1.bunqpaymentapp.domain.model.Event

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 *
 * Base class for fragments. Applies operations that are common to all fragments, and
 * provides a way for fragments to access MainActivity
 */
abstract class BaseFragment<DataBindingClass : ViewDataBinding, ViewModelClass : ViewModel>
    : Fragment() {

    val mainActivity by lazy {
        activity as MainActivity
    }

    private val navController by lazy {
        findNavController()
    }

    val appBarConfiguration by lazy {
        AppBarConfiguration(navController.graph)
    }

    val appCompatActivity: AppCompatActivity by lazy {
        activity as AppCompatActivity
    }

    val bunqPaymentApp: BunqPaymentApp by lazy {
        (this.mainActivity.application as BunqPaymentApp)
    }

    /** ViewDataBinding instance with the type parameter indicated by the child class */
    lateinit var binding: DataBindingClass
        private set

    /** ViewModel instance with the type parameter indicated by the child class */
    lateinit var viewModel: ViewModelClass
        private set

    /** Layout res id for to inflate with data binding */
    abstract val layoutId: Int

    /** Must be set for providing type safe view model */
    abstract val viewModelClass: Class<ViewModelClass>

    /** Determines action bar visibility */
    abstract val isActionBarVisible: Boolean

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainActivity.actionBar.isVisible = isActionBarVisible

        // Applies type safe data binding
        binding = DataBindingUtil.inflate(
            inflater, layoutId, container, false) as DataBindingClass

        onViewBound()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Create the ViewModel
        viewModel = ViewModelProvider(this)[viewModelClass]

        // LiveData is observed by child classes in this method
        observeLiveData()
    }

    protected fun finishApplication() {
        mainActivity.finishAndRemoveTask()
    }

    /** Called just before onCreateView is finished. Default implementation is empty */
    open fun onViewBound() {}

    /** Called just before onActivityCreated is finished */
    open fun observeLiveData() {}


    /** Sets actionBar visibility */
    protected fun setActionBarVisibility(isVisible: Boolean) {
        mainActivity.actionBar.isVisible = isVisible
    }

    /**
     * Inner class to be used by fragments for navigation purposes.
     * Sets the navigationEvent LiveData of MainViewModel, which is observed from the MainActivity
     */
    protected inner class FragmentNavigation {

        fun navigateFromHomeToPaymentFragment() {
            val navAction = HomeFragmentDirections
                .actionHomeFragmentToPaymentFragment()
            mainActivity.viewModel.setFragmentNavigationEvent(Event(navAction))
        }

        fun navigateFromPaymentToHomeFragment() {
            val navAction = PaymentFragmentDirections
                .actionPaymentFragmentToHomeFragment()
            mainActivity.viewModel.setFragmentNavigationEvent(Event(navAction))
        }

        fun navigateFromHomeToPaymentDetailFragment(paymentId: Int) {
            val navAction = HomeFragmentDirections
                .actionHomeFragmentToPaymentDetailFragment(paymentId)
            mainActivity.viewModel.setFragmentNavigationEvent(Event(navAction))
        }

        fun navigateToBack() {
            navController.popBackStack()
        }
    }

    open fun onInternetConnected() {
        //previouslyDisconnected = false
    }

    open fun onInternetDisconnected() {
        //previouslyDisconnected = true
    }

    internal fun getIsConnectedToInternet(): Boolean {
        return mainActivity.isConnectedToInternet
    }
}