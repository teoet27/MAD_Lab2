package it.polito.MAD.group06.models.advertisement

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import it.polito.MAD.group06.models.userprofile.ArrayListConverter

@Database(entities = [Advertisement::class], version = 1)
@TypeConverters(ArrayListConverter::class)
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
                            "advDB"
                        ).build()
                        INSTANCE = i
                        INSTANCE
                    }
                    )!!

    }
}