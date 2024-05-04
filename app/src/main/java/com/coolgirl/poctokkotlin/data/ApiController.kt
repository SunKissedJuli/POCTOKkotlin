package com.coolgirl.poctokkotlin.data
import com.coolgirl.poctokkotlin.data.dto.*
import retrofit2.Call
import retrofit2.http.*

interface ApiController {

    @Headers("Accept: application/json")
    @GET("user/autorize/{login}&{password}")
    fun autorizeUser(@Path("login") login : String, @Path("password") password : String) : Call<UserLoginDataResponse>

    @Headers("Accept: application/json")
    @PUT("user/postuser")
    fun createUser(@Body user: UserLoginDataResponse?): Call<UserLoginDataResponse>

    @Headers("Accept: application/json")
    @GET("user/getprofiledata/{id}")
    fun getUserProfileData(@Path("id") id: Int): Call<UserLoginDataResponse>

    @Headers("Accept: application/json")
    @GET("user/getplantdata/{id}")
    fun getPlantProfileData(@Path("id") id: Int): Call<Plant>

    @Headers("Accept: application/json")
    @GET("user/getwatering/{id}")
    fun getWatering(@Path("id") id: Int): Call<WateringResponse>

    @Headers("Accept: application/json")
    @PUT("user/postnote")
    fun postNote(@Body note: Notes?): Call<Notes>

    @Headers("Accept: application/json")
    @POST("user/posthistory")
    fun postWateringHistory(@Body history: WateringHistoryAdd): Call<Plant>

    @Headers("Accept: application/json")
    @PUT("user/postshedule")
    fun postWateringShedule(@Body shedule: WateringScheduleAdd): Call<Plant>

    @Headers("Accept: application/json")
    @PUT("user/postplant")
    fun postPlant(@Body plant: Plant?): Call<Plant>

}