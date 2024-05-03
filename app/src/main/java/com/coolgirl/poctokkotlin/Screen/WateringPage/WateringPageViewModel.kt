package com.coolgirl.poctokkotlin.Screen.WateringPage

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.coolgirl.poctokkotlin.Common.RandomString
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Models.WateringHistory
import com.coolgirl.poctokkotlin.Models.WateringResponse
import com.coolgirl.poctokkotlin.Models.WateringSchedule
import com.coolgirl.poctokkotlin.Common.di.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WateringPageViewModel : ViewModel() {
    private var wateringHistory : List<WateringHistory?>? = null
    private var wateringSchedule : List<WateringSchedule?>? = null
    var DataLoaded by mutableStateOf("")

    fun GetShedule() : List<WateringSchedule?>?{
        return wateringSchedule
    }

    fun GetHistory() : List<WateringHistory?>?{
        return wateringHistory
    }

    fun GetPlantName(plantId : Int) : String?{
        if(GetUser()!!.plants!=null){
            for(item in GetUser()!!.plants!!){
                if(item!=null){
                    if(item.plantid==plantId){
                        return item.plantname
                    }
                }
            }
        }
        return null
    }

    fun GetData(){
        val call: Call<WateringResponse> = ApiClient().getWatering(GetUser()!!.userid)
        call.enqueue(object : Callback<WateringResponse> {
            override fun onResponse(call: Call<WateringResponse>, response: Response<WateringResponse>) {
                if(response.code()==200 && response.body()!=null){
                    response.body()!!.wateringHistories.let { wateringHistory = it }
                    response.body()!!.wateringSchedule.let { wateringSchedule = it }
                    DataLoaded = RandomString()
                }
            }
            override fun onFailure(call: Call<WateringResponse>, t: Throwable) {
                //дописать
            } })
    }
}