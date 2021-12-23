package davidakht.githubdao.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import davidakht.githubdao.dao.FavoriteUserDao

@Database(
    entities = [FavoriteUser::class],
    version = 1
)
abstract class FavoriteUserRoomDatabase : RoomDatabase() {
    companion object {
        var INSTANCE: FavoriteUserRoomDatabase? = null
        fun getDatabase(context: Context): FavoriteUserRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(FavoriteUserRoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteUserRoomDatabase::class.java,
                        "user_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun favoriteUserDao(): FavoriteUserDao
}