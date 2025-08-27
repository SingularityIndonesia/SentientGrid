package designsystemold.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchResultsHelper(
    resultsCount: Int,
    modifier: Modifier = Modifier,
    title: String = "Search Results"
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LabelMediumText(
            text = title,
            color = MaterialTheme.colorScheme.onSurface
        )
        LabelSmallText(
            text = "$resultsCount found",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview
@Composable
private fun SearchResultsHeaderPreview() {
    MaterialTheme {
        SearchResultsHelper(
            resultsCount = 5,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
private fun SearchResultsHeaderSingleResultPreview() {
    MaterialTheme {
        SearchResultsHelper(
            resultsCount = 1,
            modifier = Modifier.padding(16.dp)
        )
    }
}
