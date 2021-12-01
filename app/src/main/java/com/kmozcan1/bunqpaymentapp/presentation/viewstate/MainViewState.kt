package com.kmozcan1.bunqpaymentapp.presentation.viewstate

import androidx.navigation.NavDirections

/**
 * Created by Kadir Mert Özcan on 27-Nov-21.
 */
sealed class MainViewState {
    class Error(val e: Throwable) : MainViewState()
    object Loading : MainViewState()
    class FragmentNavigation (val navigationAction: NavDirections)
}
