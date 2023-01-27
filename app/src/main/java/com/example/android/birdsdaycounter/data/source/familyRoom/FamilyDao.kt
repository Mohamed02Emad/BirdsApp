package com.example.android.birdsdaycounter.data.source.familyRoom

import androidx.room.*
import com.example.android.birdsdaycounter.data.models.Family

@Dao
interface FamilyDao {

    @Query("SELECT * FROM Family")
    fun getAll(): List<Family>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg family: Family)

    @Query("delete from Family")
    fun deleteAll()

    @Delete
    fun delete(family: Family)

    @Query("DELETE FROM Family WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Update
    fun update(family: Family)
}