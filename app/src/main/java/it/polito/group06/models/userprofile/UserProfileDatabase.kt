package it.polito.group06.models.userprofile

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [UserProfile::class], version = 1)
@TypeConverters(ArrayListConverter::class)
abstract class UserProfileDatabase : RoomDatabase() {
    abstract fun profileDao(): UserProfileDao

    companion object {
        @Volatile private var INSTANCE: UserProfileDatabase? = null

        /**
         * There's a single instance of the database and
         * this method checks whether it's already been instantiated and,
         * eventually, returns the reference to the unique object shared among
         * all the callers.
         */
        fun getDatabase(context: Context): UserProfileDatabase =
            (
                    INSTANCE ?: synchronized(this) {
                        val i = INSTANCE ?: Room.databaseBuilder(
                            context.applicationContext,
                            UserProfileDatabase::class.java,
                            "userProfileDB"
                        ).build()
                        INSTANCE = i
                        INSTANCE
                    }
                    )!!

    }
}
