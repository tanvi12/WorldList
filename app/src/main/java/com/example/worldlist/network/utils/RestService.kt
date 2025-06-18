package com.example.worldlist.network.utils

import com.example.worldlist.network.utils.interfaces.ServiceInterface
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

abstract class RestService<API : Any>(private val api: Class<API>): ServiceInterface {

    /**
     * Construct the API instance with default network call headers (including auth headers), and with
     * default retrofit converter and error handling call adapter.
     */
    val apiInstance: API by lazy {
        Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(
                HttpClient.okHttpClient
                    .newBuilder()
                    .also { okHttpBuilder ->
                        okHttpBuilder.addInterceptor(
                            HttpLoggingInterceptor().setLevel(
                                HttpLoggingInterceptor.Level.BASIC
                            )
                        )
                        getInterceptors().forEach { okHttpBuilder.addInterceptor(it) }
                    }.build()
            ).also { retrofitBuilder ->
                getAdditionalConverters().forEach { retrofitBuilder.addConverterFactory(it) }
                getRetrofitConverterFactory().forEach { retrofitBuilder.addConverterFactory(it) }
                retrofitBuilder.addCallAdapterFactory(ErrorHandlingCallAdapterFactory())
            }
            .build()
            .create(api)
    }

    /**
     * Function to be implemented by the actual service layer extended from [RestService] for base HTTP call URL
     * URL for the REST network call. The path of each request will be defined through respective retrofit
     * interface.
     */
    abstract fun getBaseUrl(): String

    /**
     * Function to be implemented by the actual service layer to provide additional
     * Retrofit converter for response data parsing
     */
    abstract fun getAdditionalConverters(): List<Converter.Factory>

    /**
     * Add additional okhttp interceptors to the retrofit call interface. Function to be implemented by the actual service layer.
     * Returns empty list if no additional interceptors are required
     */
    abstract fun getInterceptors(): List<Interceptor>

    private fun getRetrofitConverterFactory(): List<Converter.Factory> = mutableListOf(
        ScalarsConverterFactory.create(),
        GsonConverterFactory.create(GsonBuilder().setLenient().serializeNulls().create())
    )
}

