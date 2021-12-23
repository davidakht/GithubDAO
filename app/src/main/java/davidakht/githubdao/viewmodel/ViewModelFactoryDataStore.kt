package davidakht.githubdao.viewmodel

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import davidakht.githubdao.datastore.SettingPreferences
//
//class ViewModelFactoryDataStore(private val pref: SettingPreferences) :
//    ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//            return MainViewModel(pref) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//    }
//}