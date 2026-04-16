package com.catathutang.data.repository

import androidx.lifecycle.LiveData
import com.catathutang.data.CicilanDao
import com.catathutang.data.HutangDao
import com.catathutang.data.model.Cicilan
import com.catathutang.data.model.Hutang

class HutangRepository(private val hutangDao: HutangDao) {
    val allHutang: LiveData<List<Hutang>> = hutangDao.getAll()
    suspend fun insert(hutang: Hutang) = hutangDao.insert(hutang)
    suspend fun update(hutang: Hutang) = hutangDao.update(hutang)
    suspend fun delete(hutang: Hutang) = hutangDao.delete(hutang)
    suspend fun deleteById(id: Int) = hutangDao.deleteById(id)
}

class CicilanRepository(private val cicilanDao: CicilanDao) {
    val allCicilan: LiveData<List<Cicilan>> = cicilanDao.getAll()
    suspend fun insert(cicilan: Cicilan) = cicilanDao.insert(cicilan)
    suspend fun update(cicilan: Cicilan) = cicilanDao.update(cicilan)
    suspend fun delete(cicilan: Cicilan) = cicilanDao.delete(cicilan)
    suspend fun deleteById(id: Int) = cicilanDao.deleteById(id)
    suspend fun getById(id: Int): Cicilan? = cicilanDao.getById(id)
}
