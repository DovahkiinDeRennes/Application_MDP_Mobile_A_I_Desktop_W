package com.example.demo

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class AuthViewModel(
    private val repository: AuthRepository
) {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _uiState = MutableStateFlow<AuthState>(AuthState.Idle)
    val uiState: StateFlow<AuthState> = _uiState

    fun login(email: String, password: String) {
        scope.launch {
            _uiState.value = AuthState.Loading
            val result = repository.login(email, password)
            _uiState.value = result.fold(
                onSuccess = { AuthState.Success(it) },
                onFailure = { AuthState.Error(it.message ?: "Error") }
            )
        }
    }

    fun register(email: String, password: String) {
        scope.launch {
            _uiState.value = AuthState.Loading
            val result = repository.register(email, password)
            _uiState.value = result.fold(
                onSuccess = { AuthState.Success(it) },
                onFailure = { AuthState.Error(it.message ?: "Error") }
            )
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: UserModel) : AuthState()
    data class Error(val message: String) : AuthState()
}