/***
 * Copyright (c) 2017 CommonsWare, LLC
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain	a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * Covered in detail in the book _The Busy Coder's Guide to Android Development_
 * https://commonsware.com/Android
 */
package com.example.myapplication.room.cipher

import android.util.SparseArray
import androidx.sqlite.db.SupportSQLiteProgram

internal class BindingsRecorder : SupportSQLiteProgram {
    private val bindings = SparseArray<Any?>()
    override fun bindNull(index: Int) {
        bindings.put(index, null)
    }

    override fun bindLong(index: Int, value: Long) {
        bindings.put(index, value)
    }

    override fun bindDouble(index: Int, value: Double) {
        bindings.put(index, value)
    }

    override fun bindString(index: Int, value: String) {
        bindings.put(index, value)
    }

    override fun bindBlob(index: Int, value: ByteArray) {
        bindings.put(index, value)
    }

    override fun clearBindings() {
        bindings.clear()
    }

    override fun close() {
        clearBindings()
    }

    fun getBindings(): Array<String?> {
        val result = arrayOfNulls<String>(bindings.size())
        for (i in 0 until bindings.size()) {
            val key = bindings.keyAt(i)
            val binding = bindings[key]
            if (binding != null) {
                result[i] = bindings[key].toString()
            } else {
                result[i] = "" // SQLCipher does not like null binding values
            }
        }
        return result
    }
}