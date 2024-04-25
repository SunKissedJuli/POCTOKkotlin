package com.coolgirl.poctokkotlin

import android.content.SharedPreferences
import android.util.Log
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.Models.WateringSchedule


private var user : UserLoginDataResponse? = null
private var plant : Plant? = null

fun GetUser() : UserLoginDataResponse?{
     return user
}

fun SetUser(newUser : UserLoginDataResponse){
    user = newUser
}

fun RemoveUser(){
    user = null
}

fun GetPlant() : Plant?{
    if(plant==null){
        plant = Plant(null, null, 0, null, user?.userid,user, null,null)
    }

    return plant
}

fun SetPlant(newPlant : Plant){
    plant = newPlant
}


