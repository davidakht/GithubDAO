package davidakht.githubdao.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import davidakht.githubdao.`object`.Retrofit2Github
import davidakht.githubdao.data.User
import davidakht.githubdao.data.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//class ListUserViewModel : ViewModel() {
class ListUserViewModel (application: Application): AndroidViewModel(application)  {
    val listUsers = MutableLiveData<ArrayList<User>>()
    fun setSearchUsers(query: String) {
        Retrofit2Github.apiInstance
            .getSearchUsers(query)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        listUsers.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("Request failed", t.message!!)
                }
            })
    }

    fun getSearchUser(): LiveData<ArrayList<User>> {
        return listUsers
    }
}