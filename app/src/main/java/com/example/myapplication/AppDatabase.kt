package com.example.myapplication

import android.content.Context
import android.util.Base64
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.room.dao.Car
import com.example.myapplication.room.dao.CarDao
import com.example.myapplication.room.dao.User
import com.example.myapplication.room.dao.UserDao
import com.example.myapplication.room.cipher.SafeHelperFactory
import com.example.myapplication.room.cipher.prepareAndEncryptDatabase

@Database(entities = [User::class, Car::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun carDao(): CarDao

    companion object {

        fun getInstance(context: Context, enabledEncryption: Boolean): AppDatabase{
            val passphrase = Base64.decode("Passphraze", Base64.DEFAULT)

            prepareAndEncryptDatabase(
                context = context,
                passphrase = passphrase,
                enabledEncryption = enabledEncryption
            )
            val factory = SafeHelperFactory(passphrase)

            val databaseBuilder = Room
                .databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .allowMainThreadQueries()

            if(enabledEncryption) {
                databaseBuilder.openHelperFactory(factory)
            }

            return databaseBuilder.build()
        }

    }
}