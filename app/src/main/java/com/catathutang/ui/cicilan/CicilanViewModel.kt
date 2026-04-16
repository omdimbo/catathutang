package com.catathutang.ui.cicilan

import android.app.Application
import androidx.lifecycle.*
import com.catathutang.data.AppDatabase
import com.catathutang.data.model.Cicilan
import com.catathutang.data.repository.CicilanRepository
import kotlinx.coroutines.launch

class CicilanViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = CicilanRepository(AppDatabase.getInstance(app).cicilanDao())
    val allCicilan: LiveData<List<Cicilan>> = repo.allCicilan

    val totalHutang: LiveData<Double> = allCicilan.map { list -> list.sumOf { it.total } }
    val totalPaid: LiveData<Double> = allCicilan.map { list -> list.sumOf { it.paidSum } }
    val totalSisa: LiveData<Double> = allCicilan.map { list -> list.sumOf { it.sisaAmt } }

    val activeCount: LiveData<Int> = allCicilan.map { list -> list.count { !it.isLunas } }
    val lunasCount: LiveData<Int> = allCicilan.map { list -> list.count { it.isLunas } }

    fun insert(cicilan: Cicilan) = viewModelScope.launch { repo.insert(cicilan) }

    fun addPayment(cicilan: Cicilan, amount: Double) = viewModelScope.launch {
        val updated = cicilan.copy(paid = cicilan.paid + amount)
        repo.update(updated)
    }

    fun undoPayment(cicilan: Cicilan) = viewModelScope.launch {
        if (cicilan.paid.isNotEmpty()) {
            val updated = cicilan.copy(paid = cicilan.paid.dropLast(1))
            repo.update(updated)
        }
    }

    fun delete(cicilan: Cicilan) = viewModelScope.launch { repo.delete(cicilan) }
}
