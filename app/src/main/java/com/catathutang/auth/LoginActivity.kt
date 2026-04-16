package com.catathutang.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.catathutang.MainActivity
import com.catathutang.R
import com.catathutang.databinding.ActivityLoginBinding
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

class LoginActivity : com.catathutang.utils.BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var session: UserSession

    // ⚠️ REPLACE with your actual Web Client ID from Google Cloud Console
    // Steps: https://console.cloud.google.com → APIs & Services → Credentials
    // Create OAuth 2.0 Client ID → Web application → copy the client ID
    private val WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = UserSession(this)

        // Auto-skip login if already logged in
        if (session.isLoggedIn) {
            goToMain()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.btnGoogle.setOnClickListener { signInWithGoogle() }
        binding.btnGuest.setOnClickListener { signInAsGuest() }
    }

    private fun signInWithGoogle() {
        if (WEB_CLIENT_ID == "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com") {
            // Demo mode — no real client ID configured yet
            Toast.makeText(this,
                "⚠️ Konfigurasi Web Client ID di LoginActivity.kt dulu!\nSekarang masuk sebagai demo.",
                Toast.LENGTH_LONG).show()
            session.saveGoogleUser(
                name = "Demo Google User",
                email = "demo@gmail.com",
                photoUrl = null
            )
            goToMain()
            return
        }

        setLoading(true)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(WEB_CLIENT_ID)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val credentialManager = CredentialManager.create(this)

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity
                )
                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleCredential = GoogleIdTokenCredential.createFrom(credential.data)
                    session.saveGoogleUser(
                        name = googleCredential.displayName ?: "Pengguna Google",
                        email = googleCredential.id,
                        photoUrl = googleCredential.profilePictureUri?.toString()
                    )
                    goToMain()
                } else {
                    setLoading(false)
                    showError("Login gagal. Coba lagi.")
                }
            } catch (e: GetCredentialException) {
                setLoading(false)
                showError("Login dibatalkan atau gagal: ${e.message}")
            }
        }
    }

    private fun signInAsGuest() {
        session.saveGuestUser()
        Toast.makeText(this, "Masuk sebagai Tamu 👋", Toast.LENGTH_SHORT).show()
        goToMain()
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun setLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnGoogle.isEnabled = !isLoading
        binding.btnGuest.isEnabled = !isLoading
    }

    private fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}
