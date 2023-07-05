package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.myapplication.room.dao.Car
import com.example.myapplication.room.dao.User


val DB_NAME = "first_database.db"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = AppDatabase.getInstance(this, true)
       val userDao =  database.userDao()
       val carDao =  database.carDao()
        findViewById<Button>(R.id.addButton).setOnClickListener {

            userDao.insert(User(name = "John Doe1"))
            userDao.insert(User(name = "John Doe2"))
            carDao.insert(Car(carName = "Car1"))
            carDao.insert(Car(carName = "Car2"))
        }

        Log.e("MyTest", "onCreate:\nusers: ${userDao.getAll()} \ncars:${carDao.getAll()}", )
    }

}