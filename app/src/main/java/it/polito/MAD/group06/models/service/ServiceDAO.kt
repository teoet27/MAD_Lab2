package it.polito.MAD.group06.models.service

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ServiceDAO {

    /**
     * Add(Update) to database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertService(service: Service)

    /**
     * Read from database
     */
    @Query("SELECT * FROM serviceTable")
    fun findAll(): LiveData<List<Service>>

    /**
     * Delete Service
     */
    @Query("DELETE FROM serviceTable WHERE serviceName = :name")
    fun removeServiceWithName(name: String)
}