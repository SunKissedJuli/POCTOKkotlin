package com.coolgirl.poctokkotlin

import androidx.compose.runtime.*
import androidx.datastore.preferences.Preferences
import androidx.navigation.compose.rememberNavController
import com.coolgirl.poctokkotlin.commons.*
import com.coolgirl.poctokkotlin.commons.UserDataStore.coroutineScope
import com.coolgirl.poctokkotlin.data.dto.UserLoginDataResponse
import com.coolgirl.poctokkotlin.navigate.AppNavHost
import com.coolgirl.poctokkotlin.navigate.Screen
import kotlinx.coroutines.launch

@Composable
fun IsAutorize() {
    val userDataStore = UserDataStore.GetDataStore()
    var isAutorize by remember { mutableStateOf(false) }
    var id by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        coroutineScope?.launch {
            userDataStore?.data?.collect { pref: Preferences ->
                isAutorize = pref[UserScheme.IS_AUTORIZE] == true
                id = pref[UserScheme.ID].toString()!!
            }
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