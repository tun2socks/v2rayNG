package com.tun2socks.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.tun2socks.R
import com.tun2socks.util.Utils
import kotlinx.android.synthetic.main.activity_logcat.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

import java.io.IOException
import java.util.LinkedHashSet

class LogcatActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logcat)

        title = getString(R.string.title_logcat)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        logcat(false)
    }

    private fun logcat(shouldFlushLog: Boolean) {

        try {
            pb_waiting.visibility = View.VISIBLE

            doAsync {
                if (shouldFlushLog) {
                    val lst = LinkedHashSet<String>()
                    lst.add("logcat")
                    lst.add("-c")
                    val process = Runtime.getRuntime().exec(lst.toTypedArray())
                    process.waitFor()
                }
                val lst = LinkedHashSet<String>()
                lst.add("logcat")
                lst.add("-d")
                lst.add("-v")
                lst.add("time")
                lst.add("-s")
                lst.add("GoLog,tun2socks,com.tun2socks")
                val process = Runtime.getRuntime().exec(lst.toTypedArray())
//                val bufferedReader = BufferedReader(
//                        InputStreamReader(process.inputStream))
//                val allText = bufferedReader.use(BufferedReader::readText)
                val allText = process.inputStream.bufferedReader().use { it.readText() }
                uiThread {
                    tv_logcat.text = allText
                    tv_logcat.movementMethod = ScrollingMovementMethod()
                    pb_waiting.visibility = View.GONE
                    Handler(Looper.getMainLooper()).post { sv_logcat.fullScroll(View.FOCUS_DOWN) }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logcat, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.copy_all -> {
            Utils.setClipboard(this, tv_logcat.text.toString())
            toast(R.string.toast_success)
            true
        }
        R.id.delete -> {
            logcat(true)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
