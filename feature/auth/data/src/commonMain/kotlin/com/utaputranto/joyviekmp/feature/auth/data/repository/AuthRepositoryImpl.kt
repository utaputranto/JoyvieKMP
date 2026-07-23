package com.utaputranto.joyviekmp.feature.auth.data.repository

import com.utaputranto.joyviekmp.core.model.User
import com.utaputranto.joyviekmp.feature.auth.domain.repository.AuthRepository
import org.koin.core.annotation.Single

@Single(binds = [AuthRepository::class])
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
