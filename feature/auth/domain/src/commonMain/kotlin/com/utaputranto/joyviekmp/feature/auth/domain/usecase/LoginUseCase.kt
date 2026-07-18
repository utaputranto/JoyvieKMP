package com.utaputranto.joyviekmp.feature.auth.domain.usecase

import com.utaputranto.joyviekmp.core.model.User
import com.utaputranto.joyviekmp.feature.auth.domain.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
    ): User = repository.login(email = email, password = password)
}
