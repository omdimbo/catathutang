package com.catathutang.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.catathutang.data.model.Cicilan
import com.catathutang.data.model.Hutang
import com.catathutang.data.model.PaidListConverter

@Database(entities = [Hutang::class, Cicilan::class], version = 1, exportSchema = false)
@TypeConverters(PaidListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hutangDao(): HutangDao
    abstract fun cicilanDao(): CicilanDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "catathutang.db"
                ).build().also { INSTANCE = it }
            }
    }
}
