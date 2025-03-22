package com.example.baseconverter

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.security.MessageDigest

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "BaseConverterDB"
        private const val DATABASE_VERSION = 3 // Incremented to force schema update
        private const val TABLE_USERS = "users"
        private const val TABLE_REQUESTS = "requests"
        // Users table columns
        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD_HASH = "password_hash"
        private const val COLUMN_ROLE = "role"
        // Requests table columns
        private const val COLUMN_REQUEST_ID = "id"
        private const val COLUMN_STUDENT_USERNAME = "student_username"
        private const val COLUMN_TUTOR_USERNAME = "tutor_username"
        private const val COLUMN_STATUS = "status"
        private const val TAG = "DatabaseManager"
    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d(TAG, "Creating database schema")
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USERNAME TEXT NOT NULL UNIQUE,
                $COLUMN_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_PASSWORD_HASH TEXT NOT NULL,
                $COLUMN_ROLE TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createUsersTable)

        val createRequestsTable = """
            CREATE TABLE $TABLE_REQUESTS (
                $COLUMN_REQUEST_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_STUDENT_USERNAME TEXT NOT NULL,
                $COLUMN_TUTOR_USERNAME TEXT NOT NULL,
                $COLUMN_STATUS TEXT NOT NULL DEFAULT 'Pending',
                FOREIGN KEY ($COLUMN_STUDENT_USERNAME) REFERENCES $TABLE_USERS($COLUMN_USERNAME),
                FOREIGN KEY ($COLUMN_TUTOR_USERNAME) REFERENCES $TABLE_USERS($COLUMN_USERNAME)
            )
        """.trimIndent()
        db.execSQL(createRequestsTable)

        db.execSQL("CREATE INDEX idx_username ON $TABLE_USERS($COLUMN_USERNAME)")
        db.execSQL("CREATE INDEX idx_email ON $TABLE_USERS($COLUMN_EMAIL)")
        db.execSQL("CREATE INDEX idx_tutor_username ON $TABLE_REQUESTS($COLUMN_TUTOR_USERNAME)")
        Log.d(TAG, "Database schema created successfully")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "Upgrading database from version $oldVersion to $newVersion")
        // Drop and recreate tables for any upgrade to ensure schema consistency
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REQUESTS")
        db.execSQL("DROP INDEX IF EXISTS idx_username")
        db.execSQL("DROP INDEX IF EXISTS idx_email")
        db.execSQL("DROP INDEX IF EXISTS idx_tutor_username")
        onCreate(db)
        Log.d(TAG, "Database schema upgraded successfully")
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun registerUserWithRole(username: String, email: String, password: String, role: String): Pair<Boolean, String> {
        val db = writableDatabase
        return try {
            val hashedPassword = hashPassword(password)
            val values = ContentValues().apply {
                put(COLUMN_USERNAME, username)
                put(COLUMN_EMAIL, email)
                put(COLUMN_PASSWORD_HASH, hashedPassword)
                put(COLUMN_ROLE, role)
            }
            Log.d(TAG, "Attempting to register user: $username, Email: $email, Role: $role")
            val result = db.insertOrThrow(TABLE_USERS, null, values)
            val success = result != -1L
            Log.d(TAG, "Registration result for $username: $success (ID: $result)")
            Pair(success, if (success) "Success" else "Unknown error")
        } catch (e: android.database.sqlite.SQLiteConstraintException) {
            Log.e(TAG, "Constraint violation for $username: ${e.message}", e)
            Pair(false, "Username or email already exists")
        } catch (e: android.database.sqlite.SQLiteException) {
            Log.e(TAG, "Database error for $username: ${e.message}", e)
            Pair(false, "Database error: ${e.message}")
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected error for $username: ${e.message}", e)
            Pair(false, "Unexpected error: ${e.message}")
        } finally {
            db.close()
        }
    }

    fun registerUser(username: String, email: String, password: String): Pair<Boolean, String> {
        return registerUserWithRole(username, email, password, "Student")
    }

    fun loginUserWithRole(username: String, password: String, role: String): Pair<Boolean, String> {
        val db = readableDatabase
        var cursor: android.database.Cursor? = null
        try {
            cursor = db.rawQuery(
                "SELECT $COLUMN_PASSWORD_HASH, $COLUMN_ROLE FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?",
                arrayOf(username)
            )
            if (cursor.moveToFirst()) {
                val storedHash = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD_HASH))
                val storedRole = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE))
                val inputHash = hashPassword(password)
                val isPasswordCorrect = storedHash == inputHash
                val isRoleCorrect = storedRole == role
                Log.d(TAG, "Login check for $username: Password match: $isPasswordCorrect, Role match: $isRoleCorrect")
                return Pair(isPasswordCorrect && isRoleCorrect, storedRole)
            }
            Log.d(TAG, "No user found with username: $username")
            return Pair(false, "")
        } catch (e: Exception) {
            Log.e(TAG, "Login failed for $username: ${e.message}", e)
            return Pair(false, "")
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun loginUser(username: String, password: String): Boolean {
        val (success, _) = loginUserWithRole(username, password, "")
        return success
    }

    fun getUserDetails(username: String): Triple<String, String, String>? {
        val db = readableDatabase
        var cursor: android.database.Cursor? = null
        try {
            cursor = db.rawQuery(
                "SELECT $COLUMN_USERNAME, $COLUMN_EMAIL, $COLUMN_ROLE FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?",
                arrayOf(username)
            )
            if (cursor.moveToFirst()) {
                val userName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USERNAME))
                val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
                val role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE))
                return Triple(userName, email, role)
            }
            return null
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get details for $username: ${e.message}", e)
            return null
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun userExists(username: String, email: String): Pair<Boolean, Boolean> {
        val db = readableDatabase
        var usernameCursor: android.database.Cursor? = null
        var emailCursor: android.database.Cursor? = null
        try {
            var usernameExists = false
            var emailExists = false
            usernameCursor = db.rawQuery(
                "SELECT COUNT(*) FROM $TABLE_USERS WHERE $COLUMN_USERNAME = ?",
                arrayOf(username)
            )
            if (usernameCursor.moveToFirst()) {
                usernameExists = usernameCursor.getInt(0) > 0
            }
            emailCursor = db.rawQuery(
                "SELECT COUNT(*) FROM $TABLE_USERS WHERE $COLUMN_EMAIL = ?",
                arrayOf(email)
            )
            if (emailCursor.moveToFirst()) {
                emailExists = emailCursor.getInt(0) > 0
            }
            Log.d(TAG, "User exists check: Username $username: $usernameExists, Email $email: $emailExists")
            return Pair(usernameExists, emailExists)
        } catch (e: Exception) {
            Log.e(TAG, "Error checking user existence: ${e.message}", e)
            return Pair(false, false)
        } finally {
            usernameCursor?.close()
            emailCursor?.close()
            db.close()
        }
    }

    fun requestTutor(studentUsername: String, tutorUsername: String): Boolean {
        val db = writableDatabase
        try {
            val values = ContentValues().apply {
                put(COLUMN_STUDENT_USERNAME, studentUsername)
                put(COLUMN_TUTOR_USERNAME, tutorUsername)
                put(COLUMN_STATUS, "Pending")
            }
            val result = db.insertOrThrow(TABLE_REQUESTS, null, values)
            return result != -1L
        } catch (e: Exception) {
            Log.e(TAG, "Failed to request tutor: ${e.message}", e)
            return false
        } finally {
            db.close()
        }
    }

    fun getStudentRequests(tutorUsername: String): List<Pair<String, String>> {
        val db = readableDatabase
        var cursor: android.database.Cursor? = null
        val requests = mutableListOf<Pair<String, String>>()
        try {
            cursor = db.rawQuery(
                "SELECT $COLUMN_STUDENT_USERNAME, $COLUMN_STATUS FROM $TABLE_REQUESTS WHERE $COLUMN_TUTOR_USERNAME = ?",
                arrayOf(tutorUsername)
            )
            while (cursor.moveToNext()) {
                val studentUsername = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STUDENT_USERNAME))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))
                requests.add(Pair(studentUsername, status))
            }
            return requests
        } catch (e: Exception) {
            Log.e(TAG, "Failed to get student requests: ${e.message}", e)
            return emptyList()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun updateRequestStatus(studentUsername: String, tutorUsername: String, newStatus: String): Boolean {
        val db = writableDatabase
        try {
            val values = ContentValues().apply {
                put(COLUMN_STATUS, newStatus)
            }
            val rowsAffected = db.update(
                TABLE_REQUESTS,
                values,
                "$COLUMN_STUDENT_USERNAME = ? AND $COLUMN_TUTOR_USERNAME = ?",
                arrayOf(studentUsername, tutorUsername)
            )
            return rowsAffected > 0
        } catch (e: Exception) {
            Log.e(TAG, "Failed to update request status: ${e.message}", e)
            return false
        } finally {
            db.close()
        }
    }
}
