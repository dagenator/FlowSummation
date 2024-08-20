package com.example.flowsummation.presenter.FlowSummationScreen

import com.example.flowsummation.presenter.utils.toImmutableList
import com.google.common.collect.ImmutableList

data class FlowSummationState (
    val inputValue:Int? = null,
    val output:ImmutableList<Int> = emptyList<Int>().toImmutableList()
)