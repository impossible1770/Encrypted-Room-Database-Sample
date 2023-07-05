package com.example.myapplication.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CarDao {
    @Query("SELECT * FROM cars")
    fun getAll(): List<Car>

    @Insert
    fun insert(user: Car)
}