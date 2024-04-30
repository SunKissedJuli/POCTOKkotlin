package com.coolgirl.poctokkotlin.Screen.Registration

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.Common.EncodeImage
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import com.coolgirl.poctokkotlin.SetLoginData
import com.coolgirl.poctokkotlin.SetUser
import com.coolgirl.poctokkotlin.api.ApiClient
import com.coolgirl.poctokkotlin.api.ApiController
import com.coolgirl.poctokkotlin.coroutineScope
import com.coolgirl.poctokkotlin.navigate.Screen
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class RegistrationViewModel : ViewModel() {

    var userNickname by mutableStateOf("")
    var userDescription by mutableStateOf("")
    var userImage by mutableStateOf("")

    fun UpdateUserNickname(nickname: String) { userNickname = nickname }
    fun UpdateUserDescription(description : String) { userDescription = description }

    fun Registration(navController: NavController){
        var User = GetUser()
        if (User != null) {
            User.username = userNickname
            User.userdescription = userDescription
            if(userImage!=null&&!userImage.equals("")){
                User.userimage = userImage
            }
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

    @Composable
    fun GetFileFromDrawable(image: Int) {
        if (image != 0) {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            var file by remember { mutableStateOf<File?>(null) }
            val bitmap = BitmapFactory.decodeResource(context.resources, image)

            coroutineScope.launch {
                val extStorageDirectory = context.cacheDir
                val name = getResourceNameFromDrawableString(image.toString()) + ".JPEG"
                val outputFile = File(extStorageDirectory, name)
                FileOutputStream(outputFile).use { outStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream)
                }
                file = Compressor.compress(context, outputFile) {
                    default(width = 50, format = Bitmap.CompressFormat.JPEG)
                }
                userImage = EncodeImage(file!!.path)
            }
        }
    }

    fun getResourceNameFromDrawableString(drawableString: String): String {
        val parts = drawableString.split(".")
        return if (parts.size == 3 && parts[0] == "R" && parts[1] == "drawable") {
            parts[2]
        } else {
            ""
        }
    }
}