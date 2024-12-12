package com.hazal.socialapp.domain.usecase

import com.hazal.socialapp.data.remote.model.AuthUser
import com.hazal.socialapp.domain.repository.AuthRepository
import com.hazal.socialapp.internal.util.NetworkResult
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): NetworkResult<AuthUser> {
        return repo.signIn(email, password)
    }
}