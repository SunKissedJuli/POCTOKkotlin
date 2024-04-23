package com.coolgirl.poctokkotlin.Screen.Login

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.SetUser
import com.coolgirl.poctokkotlin.navigate.Screen
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class LoginViewModel : ViewModel() {
    private var isThisAutorize : Boolean = false
    private var bottomButtonText : Int = R.string.login_toAutorize
    private var headText : Int = R.string.login_registration
    private var mainButtonText : Int = R.string.login_button_registration
    var userLogin by mutableStateOf("")
    var userPassword by mutableStateOf("")
    var userPasswordRepeat by mutableStateOf("")
    var change by mutableStateOf("")

    fun IsThisAutorize() : Boolean{ return isThisAutorize }
    fun GetBottomButtonText(): Int{ return bottomButtonText }
    fun GetMainButtonText(): Int{ return mainButtonText }
    fun GetHeadText() : Int{ return headText }

    fun UpdateUserPassword(password: String) { userPassword = password }
    fun UpdateUserLogin(login : String) { userLogin = login }
    fun UpdateUserPasswordRepeat(passwordRepeat : String) { userPasswordRepeat = passwordRepeat }

    fun ChangeScreen(){
        if(isThisAutorize){
            isThisAutorize = false
            bottomButtonText = R.string.login_toAutorize
            headText = R.string.login_registration
            mainButtonText = R.string.login_button_registration
            change = RandomString()
        }else{
            isThisAutorize = true
            bottomButtonText = R.string.login_toRegister
            headText = R.string.login_autorization
            mainButtonText = R.string.login_button_autorization
            change = RandomString()
        }
    }

    fun AutorizeClient(navController: NavController){
        if(isThisAutorize){
            var apiClient = ApiClient.start().create(ApiController::class.java)
            val call: Call<UserLoginDataResponse> = apiClient.autorizeUser(userLogin,userPassword)
            call.enqueue(object :Callback<UserLoginDataResponse>{
                override fun onResponse(call: Call<UserLoginDataResponse>, response: Response<UserLoginDataResponse>) {
                    if(response.code()==200){
                        Log.d("Tag", "Проверка юзера (LoginViewModel) AutorizeClient user = " + response.body())
                        response.body()?.let { SetUser(it)}
                        navController.navigate(Screen.UserPage.user_id(response.body()!!.userid))
                    }
                }
                override fun onFailure(call: Call<UserLoginDataResponse>, t: Throwable) {
                    //дописать
                } })
        }else{ Registration(navController) }
    }

    fun Registration(navController: NavController){
        var newUser = UserLoginDataResponse(userLogin, userPassword, 0,null, null, null,null, null)
        SetUser(newUser)
        navController.navigate(Screen.Register.route)
    }

    fun RandomString() : String{
        val length = Random.nextInt(2, 4 + 1)
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charPool[Random.nextInt(0, charPool.size)] }
            .joinToString("")
    }
}