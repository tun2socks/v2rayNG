package com.tun2socks.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.google.zxing.WriterException
import com.tun2socks.AppConfig
import com.tun2socks.util.Utils


class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("tun2socks", "enter onReceive!")
        val action = intent.action
        if (action != null) {
            if (action == Intent.ACTION_BOOT_COMPLETED) {
                // TO-DO: Code to handle BOOT COMPLETED EVENT
                // TO-DO: I can start an service.. display a notification... start an activity
                try {

                    Utils.startVService(context)
                    Log.e("tun2socks", "startVService succeeded on BOOT!")
                } catch (e: WriterException) {
                    e.printStackTrace()
                    Log.e("tun2socks", "startVService failed on BOOT!")
                }
            }
        }
    }
}