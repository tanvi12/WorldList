package com.example.worldlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.worldlist.ui.screens.countryList.CountryListViewModel
import com.example.worldlist.ui.screens.countryList.CountryListViewModelFactory
import com.example.worldlist.ui.screens.countryList.CountryRepository
import com.example.worldlist.ui.theme.WorldListTheme

class CountyListActivity : ComponentActivity() {
    private val viewModel: CountryListViewModel by viewModels {
        CountryListViewModelFactory(CountryRepository())
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorldListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                }
            }
        }
    }
}
