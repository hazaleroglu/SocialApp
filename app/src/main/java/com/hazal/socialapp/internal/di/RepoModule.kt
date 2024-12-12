package com.hazal.socialapp.internal.di

import com.hazal.socialapp.data.repository.AuthRepositoryImpl
import com.hazal.socialapp.data.repository.FoursquareRepositoryImpl
import com.hazal.socialapp.domain.repository.AuthRepository
import com.hazal.socialapp.domain.repository.FoursquareRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepoModule {

    @Binds
    fun provideUserRepository(repositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    fun provideFoursquareRepository(repositoryImpl: FoursquareRepositoryImpl): FoursquareRepository
}