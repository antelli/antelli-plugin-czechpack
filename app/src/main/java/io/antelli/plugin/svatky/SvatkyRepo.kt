package io.antelli.plugin.svatky

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SvatkyRepo() {

    var dao = SvatkyDatabase.getInstance().dao()

    init {

    }

    suspend fun getNameday(name: String): List<SvatekEntity> {
        return GlobalScope.async(Dispatchers.IO) {
            return@async dao.getNameday(name)
        }.await()
    }

    suspend fun getNameday(day: Int, month: Int): List<SvatekEntity> {
        return GlobalScope.async(Dispatchers.IO) {
            return@async dao.getNameday(day, month)
        }.await()
    }

}