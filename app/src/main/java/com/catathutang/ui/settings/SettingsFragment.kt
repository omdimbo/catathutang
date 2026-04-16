package com.catathutang.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.catathutang.MainActivity
import com.catathutang.R
import com.catathutang.auth.LoginActivity
import com.catathutang.auth.UserSession
import com.catathutang.databinding.FragmentSettingsBinding
import com.catathutang.utils.LocaleHelper

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val session = UserSession(requireContext())
        val currentLang = LocaleHelper.getLanguage(requireContext())

        // Show user info
        session.userInfo?.let { user ->
            binding.tvUserName.text = if (user.isGuest) getString(R.string.login_guest) else user.displayName
            binding.tvUserEmail.text = if (user.isGuest) getString(R.string.type_berhutang).let { "Tamu" } else user.email
            val initials = if (user.isGuest) "TM"
                           else user.displayName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("").uppercase()
            binding.tvUserAvatar.text = initials
        }

        // Language indicator
        updateLanguageUI(currentLang)

        // Language switcher
        binding.btnLanguageId.setOnClickListener { switchLanguage(LocaleHelper.LANG_ID) }
        binding.btnLanguageEn.setOnClickListener { switchLanguage(LocaleHelper.LANG_EN) }

        // Logout
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.logout_title))
                .setMessage(getString(R.string.logout_msg))
                .setPositiveButton(getString(R.string.btn_yes)) { _, _ ->
                    session.logout()
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton(getString(R.string.btn_no), null)
                .show()
        }
    }

    private fun switchLanguage(langCode: String) {
        val currentLang = LocaleHelper.getLanguage(requireContext())
        if (currentLang == langCode) return

        LocaleHelper.setLanguage(requireContext(), langCode)

        // Restart MainActivity to apply new language
        requireActivity().apply {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun updateLanguageUI(langCode: String) {
        val isId = langCode == LocaleHelper.LANG_ID
        val activeColor = requireContext().getColor(R.color.white)
        val inactiveTextColor = requireContext().getColor(R.color.text_secondary)
        val activeBg = R.drawable.tab_active_bg
        val inactiveBg = R.drawable.tab_inactive_bg

        if (isId) {
            binding.btnLanguageId.setBackgroundResource(activeBg)
            binding.btnLanguageId.setTextColor(activeColor)
            binding.btnLanguageEn.setBackgroundResource(inactiveBg)
            binding.btnLanguageEn.setTextColor(inactiveTextColor)
        } else {
            binding.btnLanguageEn.setBackgroundResource(activeBg)
            binding.btnLanguageEn.setTextColor(activeColor)
            binding.btnLanguageId.setBackgroundResource(inactiveBg)
            binding.btnLanguageId.setTextColor(inactiveTextColor)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
