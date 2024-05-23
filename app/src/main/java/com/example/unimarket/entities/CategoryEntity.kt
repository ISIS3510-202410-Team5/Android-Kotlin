package com.example.unimarket.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="categories")
data class CategoryEntity (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "catName")
    val catName: String
) : Comparable<CategoryEntity>{

    override fun compareTo(other: CategoryEntity): Int {
        return this.id compareTo other.id
    }

}