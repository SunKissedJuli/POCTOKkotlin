package com.coolgirl.poctokkotlin.Screen.Registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.SetLoginData
import com.coolgirl.poctokkotlin.SetUser
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import com.coolgirl.poctokkotlin.navigate.Screen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel : ViewModel() {

    var userNickname by mutableStateOf("")
    var userDescription by mutableStateOf("")

    fun UpdateUserNickname(nickname: String) { userNickname = nickname }
    fun UpdateUserDescription(description : String) { userDescription = description }

    fun Registration(navController: NavController){
        var User = GetUser()
        if (User != null) {
            User.username = userNickname
            User.userdescription = userDescription
        }
        var apiClient = ApiClient.start().create(ApiController::class.java)
        val call: Call<UserLoginDataResponse> = apiClient.createUser(User)
        call.enqueue(object : Callback<UserLoginDataResponse> {
            override fun onResponse(call: Call<UserLoginDataResponse>, response: Response<UserLoginDataResponse>) {
                if(response.code()==200){
                    response.body()?.let { SetUser(it) }
                    response.body()?.let { SetLoginData(it) }
                    navController.navigate(Screen.UserPage.user_id(response.body()!!.userid))
                }
            }
            override fun onFailure(call: Call<UserLoginDataResponse>, t: Throwable) {
                //дописать
            } })
    }
}