package com.example.worldlist.network

import com.example.worldlist.data.CountryRegion
import com.example.worldlist.data.CountryResponse
import com.example.worldlist.network.utils.RestService
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter

class CountryRestService : RestService<CountryAPI>(
    api = CountryAPI::class.java,
) {
    override fun getBaseUrl(): String = "https://restcountries.com"

    override fun getAdditionalConverters(): List<Converter.Factory> = emptyList()

    override fun getInterceptors(): List<Interceptor> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level  = HttpLoggingInterceptor.Level.BODY
        return listOf(loggingInterceptor)
    }

    override suspend fun fetchCountryList(
        filterOption: CountryRegion,
        searchQuery: String?
    ): List<CountryResponse> {
        val response =
            if (filterOption == CountryRegion.ALL) {
                apiInstance.queryCountries()
            } else {
                apiInstance.queryCountriesByRegion(region = filterOption.region)
            }

        return response.await().toList()
    }

    // Service layer, to be implemented as part of the detail page to get country info based on Country name
    override suspend fun getCountryDetail(countryName: String): CountryResponse {
        return apiInstance.queryCountryByName(countryName).await().firstOrNull()
            ?: CountryResponse()
    }
}
