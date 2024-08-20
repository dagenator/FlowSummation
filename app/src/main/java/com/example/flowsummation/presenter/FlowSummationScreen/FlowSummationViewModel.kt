package com.example.flowsummation.presenter.FlowSummationScreen

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.flowsummation.data.Repository
import com.example.flowsummation.data.Repository.Companion.TIME_DELAY
import com.example.flowsummation.domain.Interactor
import com.example.flowsummation.domain.WaysOfSum
import com.example.flowsummation.presenter.utils.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@Stable
class FlowSummationViewModel @Inject constructor(
    private val interactor: Interactor
) : ViewModel() {

    private val _state = MutableStateFlow(FlowSummationState())
    val state = _state.asStateFlow()

    fun onInputChange(input: String) {
        _state.update { it.copy(inputValue = input.toIntOrNull()) }
    }

    fun getFlowSum(waysOfSum: WaysOfSum) {
        _state.update { it.copy(output = emptyList<Int>().toImmutableList()) }
        CoroutineScope(Dispatchers.IO).launch {
            _state.value.inputValue?.let { input ->
                interactor.flowSum(input, waysOfSum).collect { newItem ->
                    val list = state.value.output.toMutableList()
                    list.add(newItem)
                    _state.update { it.copy(output = list.toImmutableList()) }
                }
            }
        }
    }
}