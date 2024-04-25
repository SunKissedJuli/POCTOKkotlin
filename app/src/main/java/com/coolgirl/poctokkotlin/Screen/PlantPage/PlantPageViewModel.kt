package com.coolgirl.poctokkotlin.Screen.PlantPage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.coolgirl.poctokkotlin.Common.RandomString
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Models.Notes
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.SetPlant
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlantPageViewModel : ViewModel() {
    var plant : Plant? = null
    var change by mutableStateOf("")
    private var whatItIs = "notes"

    fun WhatItIs() : String{
        return whatItIs
    }

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

    fun GetNotes() : List<Notes>? {
        if(plant!=null){
            var allNotesList :List<Notes?>? = GetUser()?.notes
            var noteList : MutableList<Notes>? = mutableListOf()
            for(note in allNotesList.orEmpty()){
                if(note!!.plantid==plant?.plantid){
                    noteList?.add(note!!)
                }
            }
            if (noteList != null) {
                return noteList.sortedByDescending { it?.noteid }
            }
        }
        return null
    }

    fun GetPhotos(): List<Notes?>?{
        var notes = mutableListOf<Notes>()
        val user = GetUser()
        if(plant!=null && user!=null && user.notes!=null){
            for(item in user?.notes!!){
                if (item != null && item.image != null && !item.image.equals("")){
                    if(item.plantid == plant!!.plantid) {
                        notes.add(item)
                    }
                }
            }
            return notes.sortedByDescending { it?.noteid }
        }
        return null
    }

    fun LoadPlantData(plantId : Int){
        var apiClient = ApiClient.start().create(ApiController::class.java)
        val call: Call<Plant> = apiClient.getPlantProfileData(plantId!!)
        call.enqueue(object : Callback<Plant> {
            override fun onResponse(call: Call<Plant>, response: Response<Plant>) {
                if(response.code()==200){
                    plant = response.body()
                    plant?.let { SetPlant(it) }
                    change = RandomString()
                }
            }
            override fun onFailure(call: Call<Plant>, t: Throwable) {
            } })
    }

}