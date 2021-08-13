package com.example.demomodetile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TimePicker
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.demomodetile.DemoContract.Actions
import com.example.demomodetile.DemoContract.Actions.DisableDemoMode
import com.example.demomodetile.DemoContract.Actions.EnableDemoMode
import com.example.demomodetile.DemoContract.Actions.HideNetworkIcon
import com.example.demomodetile.DemoContract.Actions.SetClock
import com.example.demomodetile.DemoContract.Actions.ShowClockDialog
import com.example.demomodetile.DemoContract.Actions.ShowNetworkIcon
import com.example.demomodetile.DemoContract.Actions.ToggleNotification
import com.example.demomodetile.DemoContract.ViewState
import com.example.demomodetile.databinding.ActivityDemoBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DemoActivity : AppCompatActivity() {

    private val viewModel by viewModels<DemoViewModel>()
    private lateinit var contentView: ActivityDemoBinding
    private val demoPreferences: DemoPreferences by lazy { DemoMode.getDemoPreferences() }
    private val demoModeInteractor = DemoModeInteractor(demoPreferences)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentView = DataBindingUtil.setContentView<ActivityDemoBinding>(this, R.layout.activity_demo)
            .apply {
                viewModel = this@DemoActivity.viewModel
            }


        lifecycleScope.launchWhenStarted {
            viewModel.actions
                .onEach(::handleActions)
                .collect()
        }
    }

    override fun onResume() {
        super.onResume()
        updateDemoState()
    }

    private fun updateDemoState() {
        lifecycleScope.launch {
            val context = this@DemoActivity
            val isDemoModeEnabled = demoModeInteractor.isDemoModeEnabled(context)
            val clockTime = demoModeInteractor.getClock(context)
            val isNetworkVisible = demoModeInteractor.isNetworkEnabled(context)
            val isNotificationVisible = demoModeInteractor.isNotificationsEnabled(context)
            contentView.viewState = ViewState(
                isDemoModeEnabled,
                clockTime,
                isNetworkVisible = isNetworkVisible,
                isNotificationVisible = isNotificationVisible
            )
            demoModeInteractor.sendCommand(context)
        }
    }

    private fun handleActions(actions: Actions) {
        when (actions) {
            EnableDemoMode -> {
                demoPreferences.setDemoMode(this, true)
                sendBroadcast(DemoMode.enterDemoIntent)
            }
            DisableDemoMode -> {
                demoPreferences.setDemoMode(this, false)
                sendBroadcast(DemoMode.exitDemoIntent)
            }

            ShowNetworkIcon -> {
                sendBroadcast(DemoMode.showNetworkIcon)
            }
            HideNetworkIcon -> {
                sendBroadcast(DemoMode.hideNetworkIcon)
                demoPreferences.setNetworkIconVisibility(this, false)
            }

            ShowClockDialog -> clockDialog()

            is ToggleNotification -> {
                sendBroadcast(DemoMode.toggleNotificationVisibility(actions.isVisible))
                demoPreferences.setNotificationIconVisibility(this, actions.isVisible)
            }
            is SetClock -> {
                sendBroadcast(DemoMode.clockIntent(actions.time))
                demoPreferences.setClock(this, actions.time)
            }

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
}