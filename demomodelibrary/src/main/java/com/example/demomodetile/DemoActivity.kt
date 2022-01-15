package com.example.demomodetile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.example.demomodetile.databinding.ActivityDemoBinding
import com.example.demomodetile.di.ViewModelFactory
import com.example.demomodetile.di.inject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class DemoActivity : AppCompatActivity() {

    @Inject
    lateinit var demoModeInteractor: DemoModeInteractor

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<DemoViewModel> { viewModelFactory }
    private lateinit var contentView: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        inject(this)
        contentView = DataBindingUtil.setContentView<ActivityDemoBinding>(this, R.layout.activity_demo)
            .apply {
                viewModel = this@DemoActivity.viewModel
                lifecycleOwner = this@DemoActivity
            }

        lifecycleScope.launchWhenStarted {
            viewModel.actions
                .onEach(::handleActions)
                .collect()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.initDemoState()
    }

    private fun handleActions(actions: Actions) {
        Log.d("LOG_TAG---", "DemoActivity#handleActions-80: $actions")
        when (actions) {
            EnableDemoMode -> sendBroadcast(DemoMode.enterDemoIntent)
            DisableDemoMode -> sendBroadcast(DemoMode.exitDemoIntent)

            ShowNetworkIcon -> sendBroadcast(DemoMode.showNetworkIcon)
            HideNetworkIcon -> sendBroadcast(DemoMode.hideNetworkIcon)

            ShowClockDialog -> clockDialog()

            is ToggleNotification -> sendBroadcast(DemoMode.toggleNotificationVisibility(actions.isVisible))
            is SetClock -> sendBroadcast(DemoMode.clockIntent(actions.time))

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
            .setPositiveButton(R.string.time_picker_positive_button) { _, _ ->
            }
            .setNegativeButton(R.string.time_picker_negative_button, null)
            .show()
    }
}