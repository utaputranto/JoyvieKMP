package com.utaputranto.joyviekmp.feature.auth.data.repository

import com.utaputranto.joyviekmp.core.model.User
import com.utaputranto.joyviekmp.feature.auth.domain.repository.AuthRepository

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
