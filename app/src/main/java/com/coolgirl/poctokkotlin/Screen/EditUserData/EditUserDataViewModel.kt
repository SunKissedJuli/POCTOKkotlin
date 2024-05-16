package com.coolgirl.poctokkotlin.Screen.EditUserData

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.coolgirl.poctokkotlin.GetUser
import com.coolgirl.poctokkotlin.SetUser
import com.coolgirl.poctokkotlin.commons.EncodeImage
import com.coolgirl.poctokkotlin.commons.UserDataStore
import com.coolgirl.poctokkotlin.commons.copyStreamToFile
import com.coolgirl.poctokkotlin.commons.getResourceNameFromDrawableString
import com.coolgirl.poctokkotlin.data.dto.UserLoginDataResponse
import com.coolgirl.poctokkotlin.di.ApiClient
import com.coolgirl.poctokkotlin.navigate.Screen
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class EditUserDataViewModel : ViewModel() {
    var userNickname by mutableStateOf("")
    var userDescription by mutableStateOf("")
    var userImage by mutableStateOf("")
    val fileName = mutableStateOf(0)
    @OptIn(ExperimentalMaterialApi::class)
    var sheetState: ModalBottomSheetState? = null
    var scope: CoroutineScope? = null

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

        val call: Call<UserLoginDataResponse> = ApiClient().createUser(User)
        call.enqueue(object : Callback<UserLoginDataResponse> {
            override fun onResponse(call: Call<UserLoginDataResponse>, response: Response<UserLoginDataResponse>) {
                if(response.code()==200){
                    response.body()?.let { SetUser(it) }
                    response.body()?.let { UserDataStore.SetLoginData(it) }
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

    @SuppressLint("Range", "SuspiciousIndentation")
    @Composable
    fun OpenGalery(context: Context = LocalContext.current): ManagedActivityResultLauncher<String, Uri?> {
        var file by remember { mutableStateOf<File?>(null) }
        val coroutineScope = rememberCoroutineScope()
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            val cursor: Cursor? = context.getContentResolver().query(uri!!, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    var fileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    val iStream : InputStream = context.contentResolver.openInputStream(uri!!)!!
                    val outputDir : File = context.cacheDir
                    val outputFile : File = File(outputDir,fileName)
                    copyStreamToFile(iStream, outputFile)
                    iStream.close()
                    coroutineScope.launch() {
                        file = Compressor.compress(context, outputFile!!) {
                            default(width = 50, format = Bitmap.CompressFormat.JPEG) }
                        userImage = EncodeImage(file!!.path)
                    }
                }

            }finally { cursor!!.close() }
        }
        return launcher
    }
}
