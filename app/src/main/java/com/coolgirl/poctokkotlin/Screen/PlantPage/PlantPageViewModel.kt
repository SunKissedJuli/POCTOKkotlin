package com.coolgirl.poctokkotlin.Screen.PlantPage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.coolgirl.poctokkotlin.commons.RandomString
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.data.dto.Notes
import com.coolgirl.poctokkotlin.data.dto.Plant
import com.coolgirl.poctokkotlin.data.dto.WateringHistory
import com.coolgirl.poctokkotlin.SetPlant
import com.coolgirl.poctokkotlin.di.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantPageViewModel : ViewModel() {
    var plant : Plant? = null
    var change by mutableStateOf("")
    var dataLoaded by mutableStateOf("")
    private var whatItIs = "notes"

    fun WhatItIs() : String{ return whatItIs }

    fun ShowNotes(){
        whatItIs = "notes"
        change = RandomString()
    }

    fun ShowShedule(){
        whatItIs = "shedule"
        change = RandomString()
    }

    fun ShowPhotos(){
        whatItIs = "photos"
        change = RandomString()
    }

    fun GetNotes() : List<Notes?>? {
        if(plant!=null){
            var allNotesList :List<Notes?>? = GetUser()?.notes
            var newNoteList : MutableList<Notes?>? = mutableListOf()
            for(note in allNotesList.orEmpty()){
                if(note!!.plantid==plant?.plantid&&note.image==null){
                    newNoteList?.add(note!!)
                }
            }
            if (newNoteList != null) {
                return newNoteList!!.sortedByDescending { it?.noteid }
            }
        }
        return null
    }

    fun GetHistoryList() : List<WateringHistory?>?{
        if(plant!=null&& plant!!.wateringHistories!=null){
            return plant!!.wateringHistories
        }
        return null
    }

    fun GetPhotos(): List<Notes?>?{
        var photos = mutableListOf<Notes>()
        val user = GetUser()
        if(plant!=null && user!=null && user.notes!=null){
            for(item in user?.notes!!){
                if (item != null && item.image != null && !item.image.equals("")){
                    if(item.plantid == plant!!.plantid) {
                        photos.add(item)
                    }
                }
            }
            return photos.sortedByDescending { it?.noteid }
        }
        return null
    }

    fun LoadPlantData(plantId : Int){
        val call: Call<Plant> = ApiClient().getPlantProfileData(plantId!!)
        call.enqueue(object : Callback<Plant> {
            override fun onResponse(call: Call<Plant>, response: Response<Plant>) {
                if(response.code()==200){
                    plant = response.body()
                    plant?.let { SetPlant(it) }
                    dataLoaded = RandomString()
                    change = RandomString()

                }
            }
            override fun onFailure(call: Call<Plant>, t: Throwable) {
            } })
    }

}