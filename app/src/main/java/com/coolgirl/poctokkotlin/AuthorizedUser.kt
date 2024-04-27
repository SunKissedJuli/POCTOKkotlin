package com.coolgirl.poctokkotlin

import android.util.Log
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.Models.WateringSchedule


private var user : UserLoginDataResponse? = null
private var plant : Plant? = null

fun GetUser() : UserLoginDataResponse?{ return user }

fun SetUser(newUser : UserLoginDataResponse){ user = newUser }

fun RemoveUser(){ user = null }

fun GetPlant() : Plant?{
    if(plant==null){
        plant = Plant(null, null, 0, null, user?.userid, GetUserFor(), null,null)
    }
    return plant
}

fun SetPlant(newPlant : Plant){ plant = newPlant }

fun GetPlantFor() : Plant{
    var plantFor= Plant(plant!!.plantname,plant!!.plantdescription,plant!!.plantid, plant!!.plantimage, plant!!.userid,
        GetUserFor(), null, null)
    return plantFor
}

fun GetUserFor() : UserLoginDataResponse{
    var user = GetUser()
    user!!.notes = null
    user!!.plants = null
    return user
}


