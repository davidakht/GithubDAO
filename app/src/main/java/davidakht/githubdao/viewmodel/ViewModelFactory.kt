package davidakht.githubdao.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import davidakht.githubdao.datastore.SettingPreferences

class ViewModelFactory (
    private val mApplication: Application,
    private val pref: SettingPreferences
): ViewModelProvider.Factory {
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, pref: SettingPreferences): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, pref)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return when (modelClass) {
                MainViewModel::class.java -> MainViewModel(mApplication, pref) as T
                DetailUserViewModel::class.java -> DetailUserViewModel(mApplication) as T
                FavoriteUserViewModel::class.java -> FavoriteUserViewModel(mApplication) as T
                DetailFavoriteUserViewModel::class.java -> DetailFavoriteUserViewModel(mApplication) as T
                else -> throw IllegalStateException()
            }
        }
    }