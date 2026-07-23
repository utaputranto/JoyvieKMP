package com.utaputranto.joyviekmp.core.datastore

import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {
    fun isCompletedOnboarding(): Flow<Boolean>

    suspend fun completeOnboarding(completed: Boolean)
}
