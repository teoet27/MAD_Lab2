package it.polito.group06.models.service

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
    @Query("SELECT * FROM advertisementTable")
    fun findAll(): LiveData<List<Service>>

    /**
     * Delete Service
     */
    @Query("DELETE FROM advertisementTable WHERE serviceName = :name")
    fun removeServiceWithName(name: String)
}