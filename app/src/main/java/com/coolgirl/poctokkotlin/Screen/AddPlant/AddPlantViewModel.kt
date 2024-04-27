package com.coolgirl.poctokkotlin.Screen.AddPlant

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.*
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.Models.WateringSchedule
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import com.coolgirl.poctokkotlin.navigate.Screen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPlantViewModel : ViewModel() {
    var plantNickname by mutableStateOf("")
    var plantDescription by mutableStateOf("")
    var plant : Plant? = null

    fun UpdatePlantNickname(nickname: String) {
        plantNickname = nickname
        plant!!.plantname = plantNickname
        SetPlant(plant!!)
    }
    fun UpdatePlantDescription(description : String){
        plantDescription = description
        plant!!.plantdescription = plantDescription
        SetPlant(plant!!)}

    fun CreatePlant(){
      //  SetPlant(Plant(null,null,0,null, GetUser()!!.userid, GetUserFor(),null,null))
        plant = GetPlant()
    }

    fun Save(navController: NavController){
        var plant = GetPlant()
        if(plant!!.wateringSchedule==null){
           plant.wateringSchedule = WateringSchedule(plant.plantid, plant.userid, "0000000", 0, GetPlantFor() )
        }
        if(plantNickname==null){
            //дописать
        }else{
            var apiClient = ApiClient.start().create(ApiController::class.java)
            val call: Call<Plant> = apiClient.postPlant(plant)
            call.enqueue(object : Callback<Plant> {
                override fun onResponse(call: Call<Plant>, response: Response<Plant>) {
                    if(response.code()==200){
                        response.body()?.let { SetPlant(it) }
                        navController.navigate(Screen.PlantPage.plant_id(response.body()!!.plantid!!))
                    }else if(response.code()==204){
                        navController.navigate(Screen.PlantPage.plant_id(response.body()!!.plantid!!))
                    }
                }
                override fun onFailure(call: Call<Plant>, t: Throwable) {
                } })
        }
    }
}