package com.example.playgroundAug

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TimePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.playgroundAug.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<DemoViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
            .apply {
                viewModel = this@MainActivity.viewModel
            }
        lifecycleScope.launchWhenStarted {
            viewModel.actions
                .onEach(::handleActions)
                .collect()
        }
    }

    private fun handleActions(actions: DemoContract.Actions) {
        when (actions) {
            DemoContract.Actions.EnableDemoMode -> sendBroadcast(DemoMode.enterDemoIntent)
            DemoContract.Actions.DisableDemoMode -> sendBroadcast(DemoMode.exitDemoIntent)

            DemoContract.Actions.ShowNetworkIcon -> sendBroadcast(DemoMode.showNetworkIcon)
            DemoContract.Actions.HideNetworkIcon -> sendBroadcast(DemoMode.hideNetworkIcon)

            DemoContract.Actions.ShowClockDialog -> clockDialog()

            is DemoContract.Actions.SetClock -> sendBroadcast(DemoMode.clockIntent(actions.time))

        }
    }

    private fun clockDialog() {
        val timePicker = TimePicker(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setOnTimeChangedListener { _, hourOfDay, minute ->
                viewModel.setClock("$hourOfDay$minute")
            }
        }
        AlertDialog.Builder(this)
            .setView(timePicker)
            .setPositiveButton("Set") { _, _ ->
            }
            .setNegativeButton("Cancel", null)
            .show()
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

