package com.joyvie.features.auth.data.repository

import com.joyvie.core.model.User
import com.joyvie.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl : AuthRepository {
    override suspend fun login(
        email: String,
        password: String,
    ): User {
        return User(
            id = "1",
            name = "Test User",
            email = email,
        )
    }
}
