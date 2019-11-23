package io.antelli.plugin.svatky

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SvatkyDao {

    @Query("SELECT * FROM svatek WHERE jmeno LIKE :name")
    suspend fun getNameday(name : String): List<SvatekEntity>

    @Query("SELECT * FROM svatek WHERE den=:day AND mesic=:month")
    suspend fun getNameday(day : Int, month: Int): List<SvatekEntity>
}