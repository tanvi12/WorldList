package com.example.worldlist.network

import com.example.worldlist.data.CountryResponse
import com.example.worldlist.network.utils.ErrorHandlingCall
import retrofit2.http.GET
import retrofit2.http.Path

interface CountryAPI {
    @GET("/v3.1/all?fields=name,flags,capital")
    fun queryCountries(): ErrorHandlingCall<List<CountryResponse>>

    @GET("/v3.1/region/{region}")
    fun queryCountriesByRegion(
        @Path("region") region: String
    ): ErrorHandlingCall<List<CountryResponse>>

    @GET("/v3.1/name/{countryName}")
    fun queryCountryByName(
        @Path("countryName") countryName: String
    ): ErrorHandlingCall<List<CountryResponse>>
}