package com.example.worldlist.network.utils.interfaces

import com.example.worldlist.data.CountryRegion
import com.example.worldlist.data.CountryResponse

interface ServiceInterface {

    suspend fun fetchCountryList(
        filterOption : CountryRegion,
        searchQuery : String? = null
    ) : List<CountryResponse>

    suspend fun getCountryDetail(countyName : String) : CountryResponse
}