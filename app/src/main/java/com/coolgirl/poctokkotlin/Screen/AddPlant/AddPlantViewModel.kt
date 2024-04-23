package com.coolgirl.poctokkotlin.Screen.AddPlant

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.GetPlant
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.Models.WateringSchedule
import com.coolgirl.poctokkotlin.SetPlant
import com.coolgirl.poctokkotlin.SetUser
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import com.coolgirl.poctokkotlin.navigate.Screen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPlantViewModel : ViewModel() {
    var plantNickname by mutableStateOf("")
    var plantDescription by mutableStateOf("")

    fun UpdatePlantNickname(nickname: String) { plantNickname = nickname }
    fun UpdatePlantDescription(description : String) { plantDescription = description }

    fun Save(navController: NavController){
        var plant = GetPlant()
        if(plantNickname==null){
            //дописать
        }else{
            plant?.plantname = plantNickname
            plant?.plantdescription = plantDescription
            plant!!.wateringSchedule = WateringSchedule(0, plant.userid,
                "0000000", 0,Plant(plantNickname, plantDescription,0, null,plant.userid,
                GetUser(),null,null))
            var apiClient = ApiClient.start().create(ApiController::class.java)
            val call: Call<Plant> = apiClient.postPlant(plant)
            call.enqueue(object : Callback<Plant> {
                override fun onResponse(call: Call<Plant>, response: Response<Plant>) {
                    if(response.code()==200){
                        response.body()?.let { SetPlant(it) }
                        navController.navigate(Screen.PlantPage.plant_id(response.body()!!.plantid!!))
                    }
                }
                override fun onFailure(call: Call<Plant>, t: Throwable) {
                    Log.d("Tag", "Проверка plant (AddPlant) onFailure = " + t.message)
                } })
        }
    }
}