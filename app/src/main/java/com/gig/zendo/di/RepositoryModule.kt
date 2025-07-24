package com.gig.zendo.di

import com.gig.zendo.data.repository.AuthRepositoryImpl
import com.gig.zendo.data.repository.HouseRepositoryImpl
import com.gig.zendo.data.repository.RoomRepositoryImpl
import com.gig.zendo.data.repository.ServiceRepositoryImpl
import com.gig.zendo.data.repository.TenantRepositoryImpl
import com.gig.zendo.domain.repository.AuthRepository
import com.gig.zendo.domain.repository.HouseRepository
import com.gig.zendo.domain.repository.RoomRepository
import com.gig.zendo.domain.repository.ServiceRepository
import com.gig.zendo.domain.repository.TenantRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth
    ): AuthRepository = AuthRepositoryImpl(auth)

    @Provides
    @Singleton
    fun provideHouseRepository(
        firebaseFirestore: FirebaseFirestore
    ): HouseRepository {
        return HouseRepositoryImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideRoomRepository(
        firebaseFirestore: FirebaseFirestore
    ): RoomRepository {
        return RoomRepositoryImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideServiceRepository(
        firebaseFirestore: FirebaseFirestore
    ): ServiceRepository {
        return ServiceRepositoryImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideTenantRepository(
        firebaseFirestore: FirebaseFirestore
    ): TenantRepository {
        return TenantRepositoryImpl(firebaseFirestore)
    }
}