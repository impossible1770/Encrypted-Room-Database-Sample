package com.example.myapplication.room.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey(autoGenerate = true) val cardId: Long = 0,
    val carName: String
)