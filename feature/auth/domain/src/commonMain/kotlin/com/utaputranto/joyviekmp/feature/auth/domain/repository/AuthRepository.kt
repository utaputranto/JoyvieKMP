package com.utaputranto.joyviekmp.feature.auth.domain.repository

import com.utaputranto.joyviekmp.core.model.User

interface AuthRepository {
    suspend fun login(
        email: String,
        password: String,
    ): User
}
