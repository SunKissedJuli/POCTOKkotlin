package com.coolgirl.poctokkotlin.Screen.UserPage

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.coolgirl.poctokkotlin.Common.RandomString
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Models.Notes
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.SetUser
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class UserPageViewModel : ViewModel() {
    var user : UserLoginDataResponse? = GetUser()
    var change by mutableStateOf("")
    var LoadNotesStatus =  com.coolgirl.poctokkotlin.Screen.UserPage.LoadNotesStatus.NOT_STARTED;
    private var whatItIs = "plants"

    fun WhatItIs() : String{
        return whatItIs
    }

    fun ShowNotes(){
        whatItIs = "notes"
        change = RandomString()
    }

    fun ShowPlants(){
        whatItIs = "plants"
        change = RandomString()
    }

    fun ShowPhotos(){
        whatItIs = "photos"
        change = RandomString()
    }

    fun GetPhotos(): List<Notes?>?{
        Log.d("tag", "userPageScreen GetPhotos")
        var notes = mutableListOf<Notes>()
        for(item in user?.notes!!){
            if (item != null && item.image != null && !item.image.equals("")) {
                Log.d("tag", "userPageScreen GetPhotos add = " + item)
                notes.add(item)
            }
        }
        Log.d("tag", "userPageScreen GetPhotos return = " + notes)
        return notes
    }

    fun GetNotes() : List<Notes?>? {
        var notes = mutableListOf<Notes>()
        for(item in user?.notes!!){
            if (item != null && item.image == null || item!!.image.equals("")) {
                notes.add(item)
            }
        }
        return notes
    }

    fun GetPlants() : List<Plant?>? {
        return user?.plants
    }

    fun GetPlantName(plantId : Int) : String{
        if(user!=null && user!!.plants!=null){
            for(plant in user!!.plants!!){
                if (plant!!.plantid == plantId && plant.plantname!=null){
                    return plant!!.plantname!!
                }
            }
        }
        return "Общая запись"
    }

    fun LoadNotes(){
        var apiClient = ApiClient.start().create(ApiController::class.java)
        val call: Call<UserLoginDataResponse> = apiClient.getUserProfileData(user!!.userid)
        call.enqueue(object : Callback<UserLoginDataResponse> {
            override fun onResponse(call: Call<UserLoginDataResponse>, response: Response<UserLoginDataResponse>) {
                if(response.code()==200){
                    user = response.body()
                    user?.let { SetUser(it) }
                    Log.d("tag", "Проверка ноутс LoadNotes notes = " +  user?.notes)
                    Log.d("tag", "Проверка ноутс LoadNotes plants = " +  user?.plants)
                    var LoadNotesStatus =  com.coolgirl.poctokkotlin.Screen.UserPage.LoadNotesStatus.COMPLETED;
                    change = RandomString()
                }
            }
            override fun onFailure(call: Call<UserLoginDataResponse>, t: Throwable) {
                //дописать
            } })
    }

}