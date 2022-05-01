package it.polito.group06.MVVM.UserProfileDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserProfile::class], version = 1)
abstract class UserProfileDatabase: RoomDatabase() {
    abstract fun profileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: UserProfileDatabase? = null

        fun getDatabase(context: Context): UserProfileDatabase =
            (
                    INSTANCE ?: synchronized(this) {
                        val i = INSTANCE ?: Room.databaseBuilder(
                            context.applicationContext,
                            UserProfileDatabase::class.java,
                            "User Profile"
                        ).build()
                        INSTANCE = i
                        INSTANCE
                    }
                    )!!

    }
}
