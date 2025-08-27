package designsystemold.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import font.resources.Res
import font.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NoResultsFound(
    searchQuery: String,
    modifier: Modifier = Modifier,
    title: String = "No results found",
    description: String? = null
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_search),
            contentDescription = "No results",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TitleMediumText(
            text = title,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        val message = buildAnnotatedString {
            check(description !=null) {
                append("Try searching for ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("\"$searchQuery\"")
                }
                append(" with different keywords")
                return@buildAnnotatedString
            }

            append(description)
        }

        BodyMediumText(
            text =  message,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun NoResultsFoundPreview() {
    MaterialTheme {
        NoResultsFound(
            searchQuery = "example",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun NoResultsFoundCustomPreview() {
    MaterialTheme {
        NoResultsFound(
            searchQuery = "test",
            title = "No settings found",
            description = "Try searching with different terms",
            modifier = Modifier.padding(16.dp)
        )
    }
}
