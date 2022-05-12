package it.polito.group06.models.userlogin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.polito.group06.models.userprofile.ArrayListConverter

@Database(entities = [UserLogin::class], version = 1)
@TypeConverters(ArrayListConverter::class)
abstract class UserLoginDatabase : RoomDatabase() {
    abstract fun profileDao(): UserLoginDAO

    companion object {
        @Volatile private var INSTANCE: UserLoginDatabase? = null

        /**
         * There's a single instance of the database and
         * this method checks whether it's already been instantiated and,
         * eventually, returns the reference to the unique object shared among
         * all the callers.
         */
        fun getDatabase(context: Context): UserLoginDatabase =
            (
                    INSTANCE ?: synchronized(this) {
                        val i = INSTANCE ?: Room.databaseBuilder(
                            context.applicationContext,
                            UserLoginDatabase::class.java,
                            "userProfileDB"
                        ).build()
                        INSTANCE = i
                        INSTANCE
                    }
                    )!!

    }
}
