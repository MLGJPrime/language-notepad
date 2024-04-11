package com.jprime.languagenotepad.presentation.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jprime.languagenotepad.ui.theme.ColorButton
import com.jprime.languagenotepad.ui.theme.ColorButtonIcon
import com.jprime.languagenotepad.ui.theme.ColorText
import com.jprime.languagenotepad.ui.theme.LanguageNotepadTheme

sealed class ButtonContent {
    data class Text(val value: String) : ButtonContent()
    data class Icon(val imageVector: ImageVector) : ButtonContent()
}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    content: ButtonContent,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = when (content) {
            is ButtonContent.Text -> ButtonDefaults.buttonColors(
                containerColor = ColorButton,
                contentColor = ColorText,
            )

            is ButtonContent.Icon -> ButtonDefaults.buttonColors(
                containerColor = ColorButton,
                contentColor = ColorButtonIcon,
            )
        },
        contentPadding = PaddingValues(16.dp)
    ) {
        when (content) {
            is ButtonContent.Text -> Text(text = content.value)
            is ButtonContent.Icon -> Icon(
                imageVector = content.imageVector,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light Mode")
@Composable
fun PreviewCustomButtonLight() {
    LanguageNotepadTheme {
        CustomButton(
            content = ButtonContent.Text("Button"),
            onClick = { /*TODO*/ }
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
fun PreviewCustomButtonDark() {
    LanguageNotepadTheme {
        CustomButton(
            content = ButtonContent.Icon(Icons.Filled.Favorite),
            onClick = { /*TODO*/ }
        )
    }
}