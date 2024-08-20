package com.example.flowsummation.domain

import kotlinx.coroutines.flow.Flow

interface Interactor {
    suspend fun flowSum(n:Int, waysOfSum: WaysOfSum): Flow<Int>
}