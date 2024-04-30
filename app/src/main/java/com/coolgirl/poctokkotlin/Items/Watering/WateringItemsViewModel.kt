package com.coolgirl.poctokkotlin.Items.Watering

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.*
import com.coolgirl.poctokkotlin.Common.RandomString
import com.coolgirl.poctokkotlin.Models.*
import com.coolgirl.poctokkotlin.Screen.UserPage.PlantList
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import com.coolgirl.poctokkotlin.navigate.Screen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WateringItemsViewModel : ViewModel() {

    var sheduleMl by mutableStateOf("150")
    var wateringMl by mutableStateOf("150")
    var wateringDate by mutableStateOf("")
    private var shedule by mutableStateOf("0000000")

    fun SetShedule(newShedule : String){ shedule = newShedule }

    fun UpdateSheduleMl(ml: String) { sheduleMl = ml }

    fun UpdateWateringMl(ml : String){ wateringMl = ml }

    fun UpdateWateringDate(date : String){ wateringDate = date }

    fun UpdateLocalShedule(index: Int, state: Boolean) {
        val newShedule = StringBuilder(shedule)
        newShedule[index] = if (state) '1' else '0'
        shedule = newShedule.toString()
        var plant = GetPlant()
        if(plant!!.wateringSchedule!=null){
            plant!!.wateringSchedule!!.schedule = shedule
        }else{
            plant!!.wateringSchedule = WateringSchedule(plant.plantid, GetUser()!!.userid,shedule,0,
                GetPlantFor() )
        }
      //  plant.user = GetUserFor()
        SetPlant(plant)
    }

    fun UpdateShedule(plantId: Int){
        var wateringShedule = WateringScheduleAdd(plantId, GetUser()!!.userid, shedule)
        var apiClient = ApiClient.start().create(ApiController::class.java)
        val call: Call<Plant> = apiClient.postWateringShedule(wateringShedule)
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

    fun AddWatering(){
        var wateringHistory = WateringHistoryAdd(GetUser()!!.userid, GetPlant()!!.plantid, wateringDate, wateringMl.toInt())
        if(wateringDate!=null&&!wateringDate.equals("")){
            var apiClient = ApiClient.start().create(ApiController::class.java)
            val call: Call<Plant> = apiClient.postWateringHistory(wateringHistory)
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
}


