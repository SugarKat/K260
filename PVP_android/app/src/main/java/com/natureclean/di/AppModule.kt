package com.natureclean.di

import android.content.Context
import com.natureclean.api.Backend
import com.natureclean.api.BackendInterface
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideBackend(
        api: BackendInterface
    ) = Backend(api)

    @Singleton
    @Provides
    fun provideBackendInterface(@ApplicationContext appContext: Context): BackendInterface {
        val client = OkHttpClient.Builder()

        client.addInterceptor {
            val request = it.request()
            val builder = request.newBuilder()
            val url = request.url.newBuilder()
            it.proceed(builder.addHeader("Accept", "application/json").url(url.build()).build())
        }

        client.addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })

        client.authenticator{ _, response ->

            response.request.newBuilder()
                .header("Authorization", "Bearer 1|B5ZaxixRZjsmBhjbi1ZajiHx9jJz6fDGGi8F8xDj")
                .build()

        }

        return Retrofit.Builder()
            .client(client.build())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .addLast(KotlinJsonAdapterFactory()).build()

                )
            )
            //http://10.0.2.2:8000/ //EMULATOR
            //http://192.168.0.101:8000
            .baseUrl("http://192.168.0.105:8000/")
            .build()
            .create(BackendInterface::class.java)
    }


}