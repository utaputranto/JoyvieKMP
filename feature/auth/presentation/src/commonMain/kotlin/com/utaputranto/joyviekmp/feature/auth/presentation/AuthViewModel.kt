package com.utaputranto.joyviekmp.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.utaputranto.joyviekmp.core.model.User
import com.utaputranto.joyviekmp.core.platform.AppLogger
import com.utaputranto.joyviekmp.feature.auth.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
)

class AuthViewModel(
    private val login: LoginUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onLoginClicked() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = login(email = "test@joyvie.com", password = "secret")
            AppLogger.i(TAG, "Logged in as ${user.name} <${user.email}>")
            _uiState.update { it.copy(isLoading = false, user = user) }
        }
    }

    private companion object {
        const val TAG = "AuthViewModel"
    }
}
