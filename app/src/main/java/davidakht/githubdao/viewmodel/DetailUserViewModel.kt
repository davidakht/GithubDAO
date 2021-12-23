package davidakht.githubdao.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import davidakht.githubdao.`object`.Retrofit2Github
import davidakht.githubdao.dao.FavoriteUserDao
import davidakht.githubdao.data.FavoriteUser
import davidakht.githubdao.data.FavoriteUserRoomDatabase
import davidakht.githubdao.data.UserDetailResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    val detailuser = MutableLiveData<UserDetailResponse>()
    private var userDao: FavoriteUserDao?
    private var userDb: FavoriteUserRoomDatabase?

    init {
        userDb = FavoriteUserRoomDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(username: String) {
        Retrofit2Github.apiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    detailResponse: Response<UserDetailResponse>
                ) {
                    if (detailResponse.isSuccessful) {
                        detailuser.postValue(detailResponse.body())
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    Log.d("Request failed", t.message!!)
                }
            })
    }

    fun getUserDetail(): LiveData<UserDetailResponse> {
        return detailuser
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(
                username,
                id,
                avatarUrl
            )
            userDao?.addToFavorite(user)
        }
    }
}