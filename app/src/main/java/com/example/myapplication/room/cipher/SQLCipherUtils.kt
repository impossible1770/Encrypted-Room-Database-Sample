package com.example.myapplication.room.cipher

import android.content.Context
import android.util.Base64
import android.util.Log
import com.example.myapplication.DB_NAME
import java.io.File
import java.io.FileNotFoundException
import net.sqlcipher.database.SQLiteDatabase


fun getDatabaseState(dbPath: File): CipherState? {
    if (dbPath.exists()) {
        var db: SQLiteDatabase? = null
        return try {
            db = SQLiteDatabase.openDatabase(
                dbPath.absolutePath, "",
                null, SQLiteDatabase.OPEN_READONLY
            )
            db.version
            CipherState.UNENCRYPTED
        } catch (e: Exception) {
            CipherState.ENCRYPTED
        } finally {
            db?.close()
        }
    }
    return CipherState.DOES_NOT_EXIST
}

fun prepareAndEncryptDatabase(context: Context, passphrase: String, enabledEncryption: Boolean){
    SQLiteDatabase.loadLibs(context)
    val dbPath = context.getDatabasePath(DB_NAME)
    val state: CipherState? = getDatabaseState(dbPath)
    if (state == CipherState.UNENCRYPTED && enabledEncryption) {
        encryptDatabase(
            context = context,
            originalFile = dbPath,
            passphrase = Base64.decode(passphrase, Base64.DEFAULT)
        )
    }
    Log.e("AppDatabase", "state: $state", )
}


private fun encryptDatabase(context: Context, originalFile: File, passphrase: ByteArray?) {
    SQLiteDatabase.loadLibs(context)
    if (originalFile.exists()) {
        val newFile = File.createTempFile(
            "sqlcipherutils", "tmp",
            context.cacheDir
        )
        var db = SQLiteDatabase.openDatabase(
            originalFile.absolutePath,
            "", null, SQLiteDatabase.OPEN_READWRITE
        )
        val version: Int = db.version
        db.close()
        db = SQLiteDatabase.openDatabase(
            newFile.absolutePath, passphrase,
            null, SQLiteDatabase.OPEN_READWRITE, null, null
        )
        val st = db.compileStatement("ATTACH DATABASE ? AS plaintext KEY ''")
        st.bindString(1, originalFile.absolutePath)
        st.execute()
        db.rawExecSQL("SELECT sqlcipher_export('main', 'plaintext')")
        db.rawExecSQL("DETACH DATABASE plaintext")
        db.version = version
        st.close()
        db.close()
        originalFile.delete()
        newFile.renameTo(originalFile)
    } else {
        throw FileNotFoundException(originalFile.absolutePath + " not found")
    }
}