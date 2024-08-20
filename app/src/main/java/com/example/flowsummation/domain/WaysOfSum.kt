package com.example.flowsummation.domain

sealed interface WaysOfSum {
    class First : WaysOfSum
    class Second : WaysOfSum
}