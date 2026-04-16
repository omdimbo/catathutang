package com.catathutang.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.catathutang.data.model.Cicilan

@Dao
interface CicilanDao {
    @Query("SELECT * FROM cicilan ORDER BY createdAt DESC")
    fun getAll(): LiveData<List<Cicilan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cicilan: Cicilan): Long

    @Update
    suspend fun update(cicilan: Cicilan)

    @Delete
    suspend fun delete(cicilan: Cicilan)

    @Query("DELETE FROM cicilan WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM cicilan WHERE id = :id")
    suspend fun getById(id: Int): Cicilan?
}
