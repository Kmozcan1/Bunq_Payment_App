package com.kmozcan1.bunqpaymentapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.kmozcan1.bunqpaymentapp.domain.model.Event
import com.kmozcan1.bunqpaymentapp.domain.model.UseCaseResult
import com.kmozcan1.bunqpaymentapp.domain.usecase.ObserveInternetConnectivityUseCase
import com.kmozcan1.bunqpaymentapp.presentation.viewstate.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val observeInternetConnectivityUseCase: ObserveInternetConnectivityUseCase
    ) : BaseViewModel<MainViewState>() {

    // LiveData for navigation
    val fragmentNavigationEvent: MutableLiveData<Event<NavDirections>>
        get() = _fragmentNavigationEvent
    private val _fragmentNavigationEvent = MutableLiveData<Event<NavDirections>>()
    internal fun setFragmentNavigationEvent(value: Event<NavDirections>) {
        _fragmentNavigationEvent.postValue(value)
    }

    // LiveData for navigation
    val internetConnectionLiveData: MutableLiveData<Boolean>
        get() = _internetConnectionLiveData
    private val _internetConnectionLiveData = MutableLiveData<Boolean>()
    internal fun setInternetConnectionLiveData(value: Boolean) {
        _internetConnectionLiveData.postValue(value)
    }

    /** Observes internet connection changes by calling execute method of the use case class */
    fun observeInternetConnection() {
        viewModelScope.launch {
            observeInternetConnectivityUseCase(Any()).collect { result ->
                if (result is UseCaseResult.Success) {
                    setInternetConnectionLiveData(result.data)
                }
            }
        }
    }

}