package com.example.worldlist.ui.screens.countryList

import com.example.worldlist.data.CountryRegion
import com.example.worldlist.data.CountryResponse
import com.example.worldlist.network.CountryRestService
import com.example.worldlist.network.utils.interfaces.RepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountryRepository(private val countyRestService: CountryRestService = CountryRestService()) :
    RepositoryInterface {
    override fun getCountryList(
        filterOption: CountryRegion,
        searchQuery: String?
    ): Flow<List<CountryResponse>> =
        flow { emit(countyRestService.fetchCountryList(filterOption, searchQuery)) }


    override fun getCountryDetails(countryName: String): Flow<CountryResponse> = flow {
        emit(countyRestService.getCountryDetail(countryName))
    }

}