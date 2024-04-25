package com.coolgirl.poctokkotlin

import android.util.Log
import androidx.compose.runtime.*
import androidx.datastore.preferences.Preferences
import androidx.navigation.compose.rememberNavController
import com.coolgirl.poctokkotlin.Common.RandomString
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import com.coolgirl.poctokkotlin.navigate.AppNavHost
import com.coolgirl.poctokkotlin.navigate.Screen
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private var change by mutableStateOf("")

@Composable
fun IsAutorize() {
    InitDataStore()
    val userDataStore = GetDataStore()
    var isAutorize by remember { mutableStateOf(false) }
    var id by remember { mutableStateOf("") }

    coroutineScope?.launch {
        userDataStore?.data?.collect { pref: Preferences ->
            isAutorize = pref[UserScheme.IS_AUTORIZE] == true
            id = pref[UserScheme.ID].toString()!!
        }
    }
    if (isAutorize) {
        SetUser(UserLoginDataResponse(null,null,id.toInt(),null,null,null,null,null))
        AppNavHost(navController = rememberNavController(), startDestination = Screen.UserPage.user_id(id.toInt()))
    }
    else {
        AppNavHost(navController = rememberNavController(), startDestination = Screen.Login.route)
    }
}

/*fun LoadUserData(userId : Int){
    var id = userId
    if(id==0){ id = GetUser()!!.userid }
    var apiClient = ApiClient.start().create(ApiController::class.java)
    val call: Call<UserLoginDataResponse> = apiClient.getUserProfileData(id)
    call.enqueue(object : Callback<UserLoginDataResponse> {
        override fun onResponse(call: Call<UserLoginDataResponse>, response: Response<UserLoginDataResponse>) {
            if(response.code()==200){
                var user = response.body()
                user?.let { SetUser(it) }
                Log.d("tag", "хуй Мы разместили юзера)))) ")
                change = "change"
            }
        }
        override fun onFailure(call: Call<UserLoginDataResponse>, t: Throwable) {
            //дописать
        } })
}*/