package io.antelli.plugin.svatky

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "svatek")
class SvatekEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "den")
    var den: Short,
    @ColumnInfo(name = "mesic")
    var mesic: Short,
    @ColumnInfo(name = "jmeno")
    var jmeno: String?,
    @ColumnInfo(name = "svatek")
    var svatek: String?
)