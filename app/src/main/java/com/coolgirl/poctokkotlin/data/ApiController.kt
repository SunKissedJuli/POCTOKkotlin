package com.coolgirl.poctokkotlin.data
import com.coolgirl.poctokkotlin.data.dto.*
import retrofit2.Call
import retrofit2.http.*

interface ApiController {

    @GET("user/autorize/{login}&{password}")
    fun autorizeUser(@Path("login") login : String, @Path("password") password : String) : Call<UserLoginDataResponse>

    @PUT("user/postuser")
    fun createUser(@Body user: UserLoginDataResponse?): Call<UserLoginDataResponse>

    @GET("user/getprofiledata/{id}")
    fun getUserProfileData(@Path("id") id: Int): Call<UserLoginDataResponse>

    @GET("user/getplantdata/{id}")
    fun getPlantProfileData(@Path("id") id: Int): Call<Plant>

    @GET("user/getwatering/{id}")
    fun getWatering(@Path("id") id: Int): Call<WateringResponse>

    @PUT("user/postnote")
    fun postNote(@Body note: Notes?): Call<Notes>

    @POST("user/posthistory")
    fun postWateringHistory(@Body history: WateringHistoryAdd): Call<Plant>

    @PUT("user/postshedule")
    fun postWateringShedule(@Body shedule: WateringScheduleAdd): Call<Plant>

    @PUT("user/postplant")
    fun postPlant(@Body plant: Plant?): Call<Plant>

}