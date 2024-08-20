package com.example.flowsummation.presenter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.flowsummation.core.FlowSummationApplication
import com.example.flowsummation.presenter.FlowSummationScreen.FlowSummationViewModel
import com.example.flowsummation.presenter.FlowSummationScreen.compose.FlowSummation
import com.example.flowsummation.presenter.factory.FlowSummationViewModelFactory
import com.example.flowsummation.ui.theme.FlowSummationTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: FlowSummationViewModelFactory
    private val viewModel: FlowSummationViewModel by viewModels<FlowSummationViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as FlowSummationApplication).appComponent.inject(this)
        enableEdgeToEdge()
        setContent {
            FlowSummationTheme {
                FlowSummation(viewModel)
            }
        }
    }
}