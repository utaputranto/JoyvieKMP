package com.utaputranto.joyviekmp.core.mvi

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.koin.compose.viewmodel.koinViewModel

/**
 * Retrieves a [ViewModel] (such as a [BaseStateMachine]) from Koin.
 *
 * If [viewModelStoreOwner] is provided (e.g. passed from a parent screen or navigation container),
 * the ViewModel instance will be scoped to that owner, allowing it to be shared across multiple
 * navigation routes. Otherwise, it falls back to [LocalViewModelStoreOwner.current].
 */
@Composable
inline fun <reified SM : ViewModel> rememberSharedStateMachine(viewModelStoreOwner: ViewModelStoreOwner? = null): SM {
    val owner = viewModelStoreOwner ?: LocalViewModelStoreOwner.current
    return if (owner != null) {
        koinViewModel(viewModelStoreOwner = owner)
    } else {
        koinViewModel()
    }
}
