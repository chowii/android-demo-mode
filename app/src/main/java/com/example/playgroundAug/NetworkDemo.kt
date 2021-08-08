package com.example.playgroundAug

import android.content.Intent

object NetworkDemo {

    private val networkIntent: Intent
        get() = Intent("com.android.systemui.demo")
            .putExtra("command", "network");

    internal fun showNetwork(): Intent = networkIntent.putExtra("mobile", "show")
        .putExtra("fully", "true")
        .putExtra("level", "3")
        .putExtra("datatype", "lte")

    internal fun hideNetwork(): Intent = networkIntent.putExtra("mobile", "hide")

}