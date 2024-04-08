package com.jprime.languagenotepad.presentation.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.jprime.languagenotepad.R

@Composable
fun LanguageTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var filledText by remember {
                mutableStateOf("")
            }
            OutlinedTextField(
                value = filledText, onValueChange = { filledText = it },
                label = {
                    Text(text = "Enter language")
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_flag),
                        contentDescription = "Language"
                    )
                },
                supportingText = {
                    Text(text = "Please enter a valid language")
                },
                isError = false,
                singleLine = true
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewLightLanguageTextField() {

    LanguageTextField(
        value = "",
        onValueChange = { /*TODO*/ }
    )
}
