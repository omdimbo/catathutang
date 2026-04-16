package com.catathutang.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.catathutang.data.model.Hutang

@Dao
interface HutangDao {
    @Query("SELECT * FROM hutang ORDER BY createdAt DESC")
    fun getAll(): LiveData<List<Hutang>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(hutang: Hutang): Long

    @Update
    suspend fun update(hutang: Hutang)

    @Delete
    suspend fun delete(hutang: Hutang)

    @Query("DELETE FROM hutang WHERE id = :id")
    suspend fun deleteById(id: Int)
}
