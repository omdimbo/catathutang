package com.catathutang

import android.app.Application
import android.content.Context
import com.catathutang.utils.LocaleHelper

class CatatHutangApp : Application() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.wrap(base))
    }
}
