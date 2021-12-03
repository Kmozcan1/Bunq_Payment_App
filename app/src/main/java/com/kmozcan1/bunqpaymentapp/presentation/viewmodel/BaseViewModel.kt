package com.kmozcan1.bunqpaymentapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bunq.sdk.exception.UncaughtExceptionError

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
abstract class BaseViewModel<ViewStateClass> : ViewModel() {

    // LiveData object with the type of ViewState class that the child ViewModel uses
    val viewState: LiveData<ViewStateClass>
        get() = _viewState
    private val _viewState = MutableLiveData<ViewStateClass>()
    internal fun setViewState(value: ViewStateClass) {
        _viewState.postValue(value!!)
    }

    /** An ugly way of checking if the BunqApi error is caused by the network connection */
    fun checkIfNetworkError(exception: Throwable): Boolean {
        return if (exception.message != null) {
            exception is UncaughtExceptionError &&
                    exception.message!!.contains(ViewModelConstants.NetworkErrorIdentifier)
        } else {
            false
        }
    }
}