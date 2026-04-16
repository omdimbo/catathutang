package com.catathutang.auth

import android.content.Context
import android.content.SharedPreferences

data class UserInfo(
    val displayName: String,
    val email: String,
    val photoUrl: String?,
    val isGuest: Boolean
)

class UserSession(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    val isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_LOGGED_IN, false)

    val isGuest: Boolean
        get() = prefs.getBoolean(KEY_IS_GUEST, false)

    val userInfo: UserInfo?
        get() {
            if (!isLoggedIn) return null
            return UserInfo(
                displayName = prefs.getString(KEY_NAME, "Pengguna") ?: "Pengguna",
                email = prefs.getString(KEY_EMAIL, "") ?: "",
                photoUrl = prefs.getString(KEY_PHOTO, null),
                isGuest = isGuest
            )
        }

    fun saveGoogleUser(name: String, email: String, photoUrl: String?) {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putBoolean(KEY_IS_GUEST, false)
            .putString(KEY_NAME, name)
            .putString(KEY_EMAIL, email)
            .putString(KEY_PHOTO, photoUrl)
            .apply()
    }

    fun saveGuestUser() {
        prefs.edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putBoolean(KEY_IS_GUEST, true)
            .putString(KEY_NAME, "Tamu")
            .putString(KEY_EMAIL, "")
            .putString(KEY_PHOTO, null)
            .apply()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_IS_GUEST = "is_guest"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHOTO = "photo_url"
    }
}
