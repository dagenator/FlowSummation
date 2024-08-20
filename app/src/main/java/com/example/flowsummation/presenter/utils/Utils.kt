package com.example.flowsummation.presenter.utils

import com.google.common.collect.ImmutableList

fun <T> List<T>.toImmutableList(): ImmutableList<T> = ImmutableList.copyOf(this)