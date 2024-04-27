package com.coolgirl.poctokkotlin.api
import com.coolgirl.poctokkotlin.Models.Notes
import com.coolgirl.poctokkotlin.Models.Plant
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
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
    @PUT("user/postnote")
    fun postNote(@Body note: Notes?): Call<Notes>

    @Headers("Accept: application/json")
    @PUT("user/postplant")
    fun postPlant(@Body plant: Plant?): Call<Plant>

}