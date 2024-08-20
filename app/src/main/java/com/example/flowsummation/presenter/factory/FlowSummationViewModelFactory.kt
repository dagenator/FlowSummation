package com.example.flowsummation.presenter.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flowsummation.domain.Interactor
import com.example.flowsummation.presenter.FlowSummationScreen.FlowSummationViewModel
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class FlowSummationViewModelFactory @Inject constructor(
    val interactor: Interactor
) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlowSummationViewModel::class.java)) {
            return FlowSummationViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}