package com.oxygensend.shoppinglist.api.context

import android.content.Context
import android.content.SharedPreferences

class Context {
    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor

        fun create(context: Context) {
            if (!Companion::sharedPreferences.isInitialized) {
                sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            }
            if (!Companion::editor.isInitialized) {
                editor = sharedPreferences.edit()
            }
        }

        fun saveString(key: String, value: String) {
            editor.putString(key, value)
            editor.apply()
        }

        fun getString(key: String, defaultValue: String = ""): String {
            return sharedPreferences.getString(key, defaultValue) ?: defaultValue
        }

        fun saveInt(key: String, value: Int) {
            editor.putInt(key, value)
            editor.apply()
        }

        fun getInt(key: String, defaultValue: Int = 0): Int {
            return sharedPreferences.getInt(key, defaultValue)
        }

        fun saveBoolean(key: String, value: Boolean) {
            editor.putBoolean(key, value)
            editor.apply()
        }

        fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
            return sharedPreferences.getBoolean(key, defaultValue)
        }

        fun saveFloat(key: String, value: Float) {
            editor.putFloat(key, value)
            editor.apply()
        }

        fun getFloat(key: String, defaultValue: Float = 0f): Float {
            return sharedPreferences.getFloat(key, defaultValue)
        }

        fun saveLong(key: String, value: Long) {
            editor.putLong(key, value)
            editor.apply()
        }

        fun getLong(key: String, defaultValue: Long = 0L): Long {
            return sharedPreferences.getLong(key, defaultValue)
        }

        fun removeKey(key: String) {
            editor.remove(key)
            editor.apply()
        }

        fun clear() {
            editor.clear()
            editor.apply()
        }
    }
}