package com.coolgirl.poctokkotlin.Screen.UserPage

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.coolgirl.poctokkotlin.commons.RandomString
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.data.dto.Notes
import com.coolgirl.poctokkotlin.data.dto.Plant
import com.coolgirl.poctokkotlin.data.dto.UserLoginDataResponse
import com.coolgirl.poctokkotlin.SetUser
import com.coolgirl.poctokkotlin.di.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPageViewModel : ViewModel() {
    var user : UserLoginDataResponse? = GetUser()
    var change by mutableStateOf("")
    var DataLoaded by mutableStateOf("")
    private var whatItIs = "plants"

    fun WhatItIs() : String{ return whatItIs }

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
        var notes = mutableListOf<Notes>()
        user = GetUser()
        if(user?.notes!=null){
            for(item in user?.notes!!){
                if (item != null && item.image != null && !item.image.equals("")) {
                    notes.add(item)
                }
            }
            return notes.sortedByDescending { it?.noteid }
        }
       return null
    }

    fun GetNotes() : List<Notes?>? {
        var notes = mutableListOf<Notes>()
        user = GetUser()
        if(user?.notes!=null){
            for(item in user?.notes!!){
                if(item==null){
                }else if (item != null && item.image == null || item!!.image.equals("")) {
                    notes.add(item)
                }
            }
            return notes.sortedByDescending { it?.noteid }
        }
        return null

    }

    fun GetPlants() : List<Plant?>? {
        user = GetUser()
        return user?.plants?.sortedByDescending { it?.plantid }
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

    fun LoadNotes(userId : Int){
        var id = userId
        if(id==0){ id = GetUser()!!.userid }

        val call: Call<UserLoginDataResponse> = ApiClient().getUserProfileData(id)
        call.enqueue(object : Callback<UserLoginDataResponse> {
            override fun onResponse(call: Call<UserLoginDataResponse>, response: Response<UserLoginDataResponse>) {
                if(response.code()==200){
                    user = response.body()
                    user?.let { SetUser(it) }
                    change = RandomString()
                    DataLoaded = RandomString()
                }
            }
            override fun onFailure(call: Call<UserLoginDataResponse>, t: Throwable) {
                //дописать
            } })
    }

}