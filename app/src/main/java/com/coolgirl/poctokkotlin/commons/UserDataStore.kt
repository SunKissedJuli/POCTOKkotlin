package com.coolgirl.poctokkotlin.commons

import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.preferencesKey
import androidx.datastore.preferences.toMutablePreferences
import com.coolgirl.poctokkotlin.data.dto.UserLoginDataResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object UserDataStore{
    private var userDataStore : DataStore<Preferences>? = null
    var coroutineScope : CoroutineScope? = null

    fun InitDataStore(initDataStore : DataStore<Preferences>, initCoroutineScope: CoroutineScope){
        userDataStore = initDataStore
        coroutineScope = initCoroutineScope
    }

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
                    if(newUser.userlogin==null){
                        set(UserScheme.IS_AUTORIZE,false)
                    }else{
                        set(UserScheme.IS_AUTORIZE,true)
                    }

                }
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