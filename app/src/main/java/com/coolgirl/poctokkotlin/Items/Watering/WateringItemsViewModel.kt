package com.coolgirl.poctokkotlin.Items.Watering

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class WateringItemsViewModel : ViewModel() {

    var sheduleMl by mutableStateOf("150")

    fun UpdateSheduleMl(ml: String) { sheduleMl = ml }

}