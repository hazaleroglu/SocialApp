package com.hazal.socialapp.domain.repository

import com.hazal.socialapp.data.remote.model.AuthUser
import com.hazal.socialapp.data.remote.model.User
import com.hazal.socialapp.internal.util.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    val currentUserId: String
    val hasUser: Boolean
    val currentAuthUser: Flow<AuthUser?>

    suspend fun signIn(email: String, password: String): NetworkResult<AuthUser>
    suspend fun signOut(): NetworkResult<Unit>
    suspend fun register(email: String, password: String): NetworkResult<AuthUser>
    suspend fun saveUserInfo(userId: String, name: String, lastName: String): NetworkResult<Boolean>
    suspend fun getUserInfo(userId: String): NetworkResult<User>
    suspend fun createAnonymousUser(): NetworkResult<Unit>
}