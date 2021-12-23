package davidakht.githubdao.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import davidakht.githubdao.datastore.SettingPreferences
import kotlinx.coroutines.launch

class MainViewModel(private val application: Application, pref: SettingPreferences) : ViewModel() {

//class MainViewModel(private val pref: SettingPreferences) : ViewModel() {
//    fun getThemeSettings(): LiveData<Boolean> {
//        return pref.getThemeSetting().asLiveData()
//    }
//
//    fun saveThemeSetting(isDarkModeActive: Boolean) {
//        viewModelScope.launch {
//            pref.saveThemeSetting(isDarkModeActive)
//        }
//    }
}