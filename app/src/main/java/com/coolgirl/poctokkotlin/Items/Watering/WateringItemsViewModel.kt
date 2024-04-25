package com.coolgirl.poctokkotlin.Items.Watering

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.Common.RandomString
import com.coolgirl.poctokkotlin.GetPlant
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.Models.WateringSchedule
import com.coolgirl.poctokkotlin.Screen.UserPage.PlantList
import com.coolgirl.poctokkotlin.SetPlant
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import com.coolgirl.poctokkotlin.navigate.Screen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WateringItemsViewModel : ViewModel() {

    var sheduleMl by mutableStateOf("150")

    private var shedule by mutableStateOf("0000000")

    fun SetShedule(newShedule : String){ shedule = newShedule }

    fun UpdateSheduleMl(ml: String) { sheduleMl = ml }

    fun UpdateLocalShedule(index: Int, state: Boolean) {
        val newShedule = StringBuilder(shedule)
        newShedule[index] = if (state) '1' else '0'
        shedule = newShedule.toString()
        var plant = GetPlant()
        if(plant!!.wateringSchedule!=null){
            plant!!.wateringSchedule!!.schedule = shedule
        }else{
           var wateringSheduleForPlant = WateringSchedule(plant.plantid, GetUser()!!.userid,shedule,0,null )
            plant!!.wateringSchedule = wateringSheduleForPlant
        }

        var user = GetUser()
        user!!.notes = null
        user!!.plants = null

        var plantForWateringSchedule = Plant(plant.plantname,plant.plantdescription,plant.plantid, plant.plantimage, plant.userid,user, null, null)
        plant.user = user
        plant!!.wateringSchedule!!.plant = plantForWateringSchedule
        SetPlant(plant)
    }

    fun UpdateShedule(){
        var plant = GetPlant()

        var apiClient = ApiClient.start().create(ApiController::class.java)
        val call: Call<Plant> = apiClient.postPlant(plant)
        call.enqueue(object : Callback<Plant> {
            override fun onResponse(call: Call<Plant>, response: Response<Plant>) {
                   if(response.code()==200){
                       response.body()?.let { SetPlant(it) }
                   }
            }
            override fun onFailure(call: Call<Plant>, t: Throwable) {
                Log.d("Tag", "Проверка plant (AddPlant) onFailure = " + t.message)
            } })
        }
    }
