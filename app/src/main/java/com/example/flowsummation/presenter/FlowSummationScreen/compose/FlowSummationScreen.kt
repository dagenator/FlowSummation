package com.example.flowsummation.presenter.FlowSummationScreen.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.flowsummation.domain.WaysOfSum
import com.example.flowsummation.presenter.FlowSummationScreen.FlowSummationState
import com.example.flowsummation.presenter.FlowSummationScreen.FlowSummationViewModel
import com.example.flowsummation.presenter.utils.toImmutableList
import com.example.flowsummation.ui.theme.FlowSummationTheme

@Composable
fun FlowSummation(
    viewModel: FlowSummationViewModel
) {
    val state by viewModel.state.collectAsState()
    with(viewModel) {
        FlowSummationContent(
            state = state,
            onButtonClick = ::getFlowSum,
            onInputChange = ::onInputChange
        )
    }
}

@Composable
fun FlowSummationContent(
    state: FlowSummationState,
    onButtonClick: (WaysOfSum) -> Unit,
    onInputChange: (String) -> Unit
) {
    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .animateContentSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(horizontal = 32.dp, vertical = 32.dp)
                        .width(100.dp),
                    value = (state.inputValue ?: "").toString(),
                    onValueChange = { value: String -> onInputChange(value) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                )
                Button(onClick = {onButtonClick(WaysOfSum.First())}) {
                    Text(text = "click to work first way")
                }
                Button(onClick = {onButtonClick(WaysOfSum.Second())}) {
                    Text(text = "click to work second way")
                }
            }
            items(state.output) { item ->
                Text(text = item.toString())
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FlowSummationTheme {
        FlowSummationContent(
            state = FlowSummationState(
                inputValue = 3,
                output = listOf(1, 2, 3).toImmutableList()
            ),
            onInputChange = {},
            onButtonClick = {}
        )
    }
}