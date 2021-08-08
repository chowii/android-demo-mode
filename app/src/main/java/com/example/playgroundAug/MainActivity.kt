package com.example.playgroundAug

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<SwitchCompat>(R.id.demo_mode_switch).apply {
            setOnCheckedChangeListener { buttonView, isChecked ->
                Log.d("LOG_TAG---", "MainActivity#onCreate-13: $isChecked")
                broadcastDemoMode(isChecked)
            }
        }
    }

    private fun broadcastDemoMode(isChecked: Boolean) {
        val intent = Intent("com.android.systemui.demo")
        if (!isChecked) {
            intent.putExtra("command", "exit")
            sendBroadcast(intent)
        } else {
            intent.putExtra("command", "enter");
            sendBroadcast(intent);

            //Set the clock to 12:00
            intent.putExtra("command", "clock");
            intent.putExtra("hhmm", "1200");
            sendBroadcast(intent);

            //Show the LTE icon at 75% strength
            intent.putExtra("command", "network");
            intent.putExtra("mobile", "show");
            intent.putExtra("fully", "true");
            intent.putExtra("level", "3");
            intent.putExtra("datatype", "lte");
            sendBroadcast(intent);

            //Show the Wifi icon at 75% strength
            intent.removeExtra("mobile");
            intent.removeExtra("datatype");
            intent.putExtra("wifi", "show");
            sendBroadcast(intent);

            //Show the battery unplugged at 50%
            intent.putExtra("command", "battery");
            intent.putExtra("level", "50");
            intent.putExtra("plugged", "false");
            sendBroadcast(intent);

            //Hide all notifications
            intent.putExtra("command", "notifications");
            intent.putExtra("visible", "false");
            sendBroadcast(intent);

            //Make the status bar a fixed black background
            intent.putExtra("command", "bars");
            intent.putExtra("mode", "opaque");
            sendBroadcast(intent);
        }
        Log.d("LOG_TAG---", "MainActivity#onCreate-38: $isChecked")
    }

}

