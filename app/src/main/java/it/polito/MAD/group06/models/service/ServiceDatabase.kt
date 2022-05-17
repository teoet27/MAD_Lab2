package it.polito.MAD.group06.models.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Service::class], version = 1)
abstract class ServiceDatabase : RoomDatabase() {
    abstract fun serviceDao(): ServiceDAO

    companion object {
        @Volatile
        private var INSTANCE: ServiceDatabase? = null

        /**
         * There's a single instance of the database and
         * this method checks whether it's already been instantiated and,
         * eventually, returns the reference to the unique object shared among
         * all the callers.
         */
        fun getDatabase(context: Context): ServiceDatabase =
            (
                    INSTANCE ?: synchronized(this) {
                        val i = INSTANCE ?: Room.databaseBuilder(
                            context.applicationContext,
                            ServiceDatabase::class.java,
                            "serviceDB"
                        ).build()
                        INSTANCE = i
                        INSTANCE
                    }
                    )!!

    }
}