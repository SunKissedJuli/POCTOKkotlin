package com.coolgirl.poctokkotlin.Screen.Login

import android.util.Log
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.datastore.preferences.Preferences
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.*
import com.coolgirl.poctokkotlin.Common.RandomString
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.R
import com.coolgirl.poctokkotlin.navigate.Screen
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class LoginViewModel : ViewModel() {
    private var isThisAutorize : Boolean = false
    private var errorMessage : Int = 0
    private var bottomButtonText : Int = R.string.login_toAutorize
    private var headText : Int = R.string.login_registration
    private var mainButtonText : Int = R.string.login_button_registration
    var userLogin by mutableStateOf("")
    var userPassword by mutableStateOf("")
    var userPasswordRepeat by mutableStateOf("")
    var change by mutableStateOf("")
    var error by mutableStateOf(false)

    fun IsThisAutorize() : Boolean{ return isThisAutorize }
    fun GetBottomButtonText(): Int{ return bottomButtonText }
    fun GetMainButtonText(): Int{ return mainButtonText }
    fun GetHeadText() : Int{ return headText }

    fun UpdateUserPassword(password: String) { userPassword = password }
    fun UpdateUserLogin(login : String) { userLogin = login }
    fun UpdateUserPasswordRepeat(passwordRepeat : String) { userPasswordRepeat = passwordRepeat }
    fun GetErrorMessage() : Int {return errorMessage!!}

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

    @Composable
    fun LoadData(){
        val userDataStore = GetDataStore()
        var login by remember { mutableStateOf("") }
        coroutineScope?.launch {
            userDataStore?.data?.collect { pref: Preferences ->
                login = pref[UserScheme.EMAIL].toString()!!
             //   SetLoginData(UserLoginDataResponse(null,null,0,null,null,null,null,null))
            }
        }
        if(!login.equals("")&&login!==null&&!login.equals("null")){
            userLogin = login
        }
    }

    fun AutorizeClient(navController: NavController){
        if(isThisAutorize){
            if(userPassword!=null&&!userPassword.equals("")&&!userLogin.equals("")&&userLogin!=null){
                var apiClient = ApiClient.start().create(ApiController::class.java)
                val call: Call<UserLoginDataResponse> = apiClient.autorizeUser(userLogin,userPassword)
                call.enqueue(object :Callback<UserLoginDataResponse>{
                    override fun onResponse(call: Call<UserLoginDataResponse>, response: Response<UserLoginDataResponse>) {
                        if(response.code()==200){
                            response.body()?.let { SetUser(it)}
                            response.body()?.let { SetLoginData(it) }
                            navController.navigate(Screen.UserPage.user_id(response.body()!!.userid))
                        }
                    }
                    override fun onFailure(call: Call<UserLoginDataResponse>, t: Throwable) {
                        //дописать
                    } })
            }else{
                error = true
                errorMessage = R.string.login_error_no_data
            }

        }else{ Registration(navController) }
    }

    fun Registration(navController: NavController){
        if(userPassword!=null&&!userPassword.equals("")&&!userLogin.equals("")&&userLogin!=null&&userPasswordRepeat!=null&&!userPasswordRepeat.equals("")){
            if(userPassword.equals(userPasswordRepeat)){
                var newUser = UserLoginDataResponse(userLogin, userPassword, 0,null, null, null,null, null)
                SetUser(newUser)
                navController.navigate(Screen.Register.route)
            }
            else{
                error = true
                errorMessage = R.string.login_error_password_mismatch
            }
        }
       else{
            error = true
            errorMessage = R.string.login_error_no_data
        }

    }
}