package com.example.baseconverter

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "BaseConverterDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD_HASH = "password_hash"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the users table with indexes for faster lookups
        val createTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL UNIQUE,
                $COLUMN_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_PASSWORD_HASH TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)

        // Add indexes for username and email to improve query performance
        db.execSQL("CREATE INDEX idx_username ON $TABLE_USERS($COLUMN_USERNAME)")
        db.execSQL("CREATE INDEX idx_email ON $TABLE_USERS($COLUMN_EMAIL)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop the table and recreate it, including indexes
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP INDEX IF EXISTS idx_username")
        db.execSQL("DROP INDEX IF EXISTS idx_email")
        onCreate(db)
    }

    // Hash password using SHA-256
    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // Register a new user with hashed password
    fun registerUser(username: String, email: String, password: String): Boolean {
        val db = writableDatabase
        try {
            // Hash the password using SHA-256
            val hashedPassword = hashPassword(password)

            val values = ContentValues().apply {
                put(COLUMN_USERNAME, username)
                put(COLUMN_EMAIL, email)
                put(COLUMN_PASSWORD_HASH, hashedPassword)
            }

            // Insert the user and check if successful (username/email uniqueness enforced by UNIQUE constraint)
            val result = db.insertWithOnConflict(TABLE_USERS, null, values, SQLiteDatabase.CONFLICT_FAIL)
            return result != -1L
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            db.close()
        }
    }

    // Check if login credentials are valid
    fun loginUser(username: String, password: String): Boolean {
        val db = readableDatabase
        var cursor: android.database.Cursor? = null // Declare cursor here
        try {
            cursor = db.rawQuery(
                "SELECT $COLUMN_PASSWORD_HASH FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?",
                arrayOf(username)
            )
            if (cursor.moveToFirst()) {
                val storedHash = cursor.getString(0)
                val inputHash = hashPassword(password)
                return storedHash == inputHash
            }
            return false
        } finally {
            cursor?.close() // Safely close cursor, check for null
            db.close()
        }
    }

    // DatabaseManager.kt
    fun getUserDetails(username: String): Pair<String, String>? {
        val db = readableDatabase
        var cursor: android.database.Cursor? = null
        try {
            cursor = db.rawQuery(
                "SELECT $COLUMN_USERNAME, $COLUMN_EMAIL FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?",
                arrayOf(username)
            )
            return if (cursor.moveToFirst()) {
                val userName = cursor.getString(0)
                val email = cursor.getString(1)
                Pair(userName, email)
            } else {
                null
            }
        } finally {
            cursor?.close()
            db.close()
        }
    }

    // Check if username or email already exists
    fun userExists(username: String, email: String): Pair<Boolean, Boolean> {
        val db = readableDatabase
        var usernameCursor: android.database.Cursor? = null
        var emailCursor: android.database.Cursor? = null
        try {
            var usernameExists = false
            var emailExists = false

            // Check username with index for performance
            usernameCursor = db.rawQuery(
                "SELECT COUNT(*) FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?",
                arrayOf(username)
            )
            if (usernameCursor.moveToFirst()) {
                usernameExists = usernameCursor.getInt(0) > 0
            }

            // Check email with index for performance
            emailCursor = db.rawQuery(
                "SELECT COUNT(*) FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?",
                arrayOf(email)
            )
            if (emailCursor.moveToFirst()) {
                emailExists = emailCursor.getInt(0) > 0
            }

            return Pair(usernameExists, emailExists)
        } finally {
            usernameCursor?.close() // Safely close username cursor
            emailCursor?.close() // Safely close email cursor
            db.close()
        }
    }
}
