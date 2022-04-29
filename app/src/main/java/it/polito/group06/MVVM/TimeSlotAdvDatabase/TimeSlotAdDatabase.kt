package com.example.mvvm_profile.MVVM

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TimeSlotAd::class], version = 1)
abstract class TimeSlotAdDatabase: RoomDatabase() {
    abstract fun adsDao():TimeSlotAdDao

    companion object{
        @Volatile
        private var INSTANCE : TimeSlotAdDatabase?=null

        fun getDatabase(context: Context): TimeSlotAdDatabase =
            (
                INSTANCE ?: synchronized(this) {
                    val i = INSTANCE ?: Room.databaseBuilder(
                        context.applicationContext,
                        TimeSlotAdDatabase::class.java,
                        "profile"
                    ).build()
                    INSTANCE=i
                    INSTANCE
                }
            )!!

    }
}