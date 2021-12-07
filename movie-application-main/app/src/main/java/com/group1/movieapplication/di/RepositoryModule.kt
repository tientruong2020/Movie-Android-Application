package com.group1.movieapplication.di

import com.group1.movieapplication.data.repository.IMDBRepository
import com.group1.movieapplication.data.repository.IMDBRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindIMBDRepository(impl: IMDBRepositoryImpl): IMDBRepository
}