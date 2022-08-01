package com.ericg.paging3xml.di

import android.app.Application
import androidx.room.Room
import com.ericg.paging3xml.data.local.database.RickMortyDatabase
import com.ericg.paging3xml.data.remote.ApiService
import com.ericg.paging3xml.data.repositoryimpl.RickMortyRepositoryImpl
import com.ericg.paging3xml.domain.repository.RickMortyRepository
import com.ericg.paging3xml.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .callTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun providesAPIService(okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesRickMortyDatabase(application: Application): RickMortyDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            RickMortyDatabase::class.java,
            "RickMortyDB"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesRickMortyRepository(
        apiService: ApiService,
        database: RickMortyDatabase
    ): RickMortyRepository {
        return RickMortyRepositoryImpl(apiService, database)
    }
}
