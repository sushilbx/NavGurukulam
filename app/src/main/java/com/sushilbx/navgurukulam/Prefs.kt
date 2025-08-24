package com.sushilbx.navgurukulam

import android.content.Context
import kotlinx.datetime.Instant

class Prefs(ctx: Context) {
    private val p = ctx.getSharedPreferences("sync", Context.MODE_PRIVATE)
    fun lastStudentPull(): Instant? = p.getString("lastStudentPull", null)?.let(Instant::parse)
    fun lastScorePull(): Instant? = p.getString("lastScorePull", null)?.let(Instant::parse)
    fun setLastStudentPull(ts: Instant) { p.edit().putString("lastStudentPull", ts.toString()).apply() }
    fun setLastScorePull(ts: Instant) { p.edit().putString("lastScorePull", ts.toString()).apply() }
}
