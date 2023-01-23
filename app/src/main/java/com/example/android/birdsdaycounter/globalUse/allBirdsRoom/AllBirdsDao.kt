package com.example.android.birdsdaycounter.globalUse.allBirdsRoom

import androidx.room.*
import com.example.android.birdsdaycounter.allBirdsFragment.models.Bird

@Dao
interface AllBirdsDao {
    @Query("SELECT * FROM Birds")
    fun getAll(): List<Bird>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg bird:Bird )

    @Query("delete from Birds")
    fun deleteAll()

    @Delete
    fun delete(bird:Bird )

    @Query("DELETE FROM Birds WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Update
    fun update(bird:Bird )
}