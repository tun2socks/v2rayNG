package com.tun2socks.extension

import android.content.Context
import android.os.Build
import com.tun2socks.AngApplication
import me.dozen.dpreference.DPreference
import org.json.JSONObject
import java.net.URLConnection

/**
 * Some extensions
 */

val Context.v2RayApplication: AngApplication
    get() = applicationContext as AngApplication

val Context.defaultDPreference: DPreference
    get() = v2RayApplication.defaultDPreference


fun JSONObject.putOpt(pair: Pair<String, Any>) = putOpt(pair.first, pair.second)!!
fun JSONObject.putOpt(pairs: Map<String, Any>) = pairs.forEach { putOpt(it.key to it.value) }

const val threshold = 1000
const val divisor = 1024F

fun Long.toSpeedString() = toTrafficString() + "/s"

fun Long.toTrafficString(): String {
    if (this < threshold)
        return "$this B"

    val kib = this / divisor
    if (kib < threshold)
        return "${kib.toShortString()} KB"

    val mib = kib / divisor
    if (mib < threshold)
        return "${mib.toShortString()} MB"

    val gib = mib / divisor
    if (gib < threshold)
        return "${gib.toShortString()} GB"

    val tib = gib / divisor
    if (tib < threshold)
        return "${tib.toShortString()} TB"

    val pib = tib / divisor
    if (pib < threshold)
        return "${pib.toShortString()} PB"

    return "∞"
}

private fun Float.toShortString(): String {
    val s = toString()
    if (s.length <= 4)
        return s
    return s.substring(0, 4).removeSuffix(".")
}

val URLConnection.responseLength: Long
    get() = if (Build.VERSION.SDK_INT >= 24) contentLengthLong else contentLength.toLong()
