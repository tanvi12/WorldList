package com.example.worldlist.network.utils.interfaces

import com.example.worldlist.data.CountryRegion
import com.example.worldlist.data.CountryResponse
import kotlinx.coroutines.flow.Flow

interface RepositoryInterface {

    fun getCountryList(
        filterOption: CountryRegion,
        searchQuery: String? = null,
    ): Flow<List<CountryResponse>>

    fun getCountryDetails(
        countryName: String,
    ): Flow<CountryResponse>
}