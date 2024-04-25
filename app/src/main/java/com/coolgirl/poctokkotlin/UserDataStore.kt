package com.coolgirl.poctokkotlin

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.toMutablePreferences
import com.coolgirl.poctokkotlin.Models.UserLoginData
import com.coolgirl.poctokkotlin.Models.UserLoginDataResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private var userDataStore : DataStore<Preferences>? = null
var coroutineScope : CoroutineScope? = null


@Composable
fun InitDataStore(){
    userDataStore = LocalContext.current.createDataStore(name = "user_data_store")
    coroutineScope = rememberCoroutineScope()
}
@Composable
fun GetDataStore() : DataStore<Preferences>? {
    return userDataStore
}

fun SetLoginData(newUser: UserLoginDataResponse) {
    coroutineScope?.launch(Dispatchers.IO) {
        userDataStore?.updateData { prefs ->
            prefs.toMutablePreferences().apply {
                set(UserScheme.PASSWORD, newUser.userpassword.toString())
                set(UserScheme.EMAIL, newUser.userlogin.toString())
                set(UserScheme.ID, newUser.userid)
                set(UserScheme.IS_AUTORIZE,true)
            }
        }
    }
}

object UserScheme {
    val EMAIL = preferencesKey<String>("email")
    val PASSWORD = preferencesKey<String>("password")
    val IS_AUTORIZE = preferencesKey<Boolean>("is_autorize")
    val ID = preferencesKey<Int>("id")
}