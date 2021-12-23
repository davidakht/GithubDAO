package davidakht.githubdao.viewmodel

import android.app.Application
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import davidakht.githubdao.datastore.SettingPreferences

class ViewModelFactory(
    private val mApplication: Application,
    private val pref: SettingPreferences
) : ViewModelProvider.NewInstanceFactory() {

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

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref) as T
        } else if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            return DetailUserViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(FavoriteUserViewModel::class.java)) {
            return FavoriteUserViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(DetailFavoriteUserViewModel::class.java)) {
            return DetailFavoriteUserViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(ListUserViewModel::class.java)) {
            return ListUserViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return when (modelClass) {
//                MainViewModel::class.java -> MainViewModel(pref) as T
//                DetailUserViewModel::class.java -> DetailUserViewModel(mApplication) as T
//                FavoriteUserViewModel::class.java -> FavoriteUserViewModel(mApplication) as T
//                DetailFavoriteUserViewModel::class.java -> DetailFavoriteUserViewModel(mApplication) as T
//                else -> throw IllegalStateException()
//            }
//        }
//    }