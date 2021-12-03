package com.kmozcan1.bunqpaymentapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.kmozcan1.bunqpaymentapp.domain.model.Event
import com.kmozcan1.bunqpaymentapp.presentation.viewstate.MainViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainViewState>() {

    // LiveData for navigation
    val fragmentNavigationEvent: MutableLiveData<Event<NavDirections>>
        get() = _fragmentNavigationEvent
    private val _fragmentNavigationEvent = MutableLiveData<Event<NavDirections>>()
    internal fun setFragmentNavigationEvent(value: Event<NavDirections>) {
        _fragmentNavigationEvent.postValue(value)
    }
}