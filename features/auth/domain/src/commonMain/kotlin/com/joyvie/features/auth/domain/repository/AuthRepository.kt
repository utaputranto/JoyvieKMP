package com.joyvie.features.auth.domain.repository

import com.joyvie.core.model.User

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String,
    ): User
}
