package it.polito.group06.models.time_slot_adv_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Advertisement::class], version = 1)
abstract class AdvertisementDatabase : RoomDatabase() {
    abstract fun adsDao(): AdvertisementDAO

    companion object {
        @Volatile
        private var INSTANCE: AdvertisementDatabase? = null

        /**
         * There's a single instance of the database and
         * this method checks whether it's already been instantiated and,
         * eventually, returns the reference to the unique object shared among
         * all the callers.
         */
        fun getDatabase(context: Context): AdvertisementDatabase =
            (
                    INSTANCE ?: synchronized(this) {
                        val i = INSTANCE ?: Room.databaseBuilder(
                            context.applicationContext,
                            AdvertisementDatabase::class.java,
                            "advertisementsDB"
                        ).build()
                        INSTANCE = i
                        INSTANCE
                    }
                    )!!

    }
}