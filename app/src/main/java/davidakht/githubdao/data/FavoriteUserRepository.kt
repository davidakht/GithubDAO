package davidakht.githubdao.data

import android.app.Application
import androidx.lifecycle.LiveData
import davidakht.githubdao.dao.FavoriteUserDao

class FavoriteUserRepository(application: Application) {
    private val mFavoriteUsersDao: FavoriteUserDao

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteUsersDao = db!!.favoriteUserDao()
    }

    fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> =
        mFavoriteUsersDao.getAllFavoriteUsers()
}