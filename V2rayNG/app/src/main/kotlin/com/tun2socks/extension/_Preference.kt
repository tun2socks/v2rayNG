package com.tun2socks.extension

import android.preference.Preference

fun Preference.onClick(listener: () -> Unit) {
    setOnPreferenceClickListener {
        listener()
        true
    }
}