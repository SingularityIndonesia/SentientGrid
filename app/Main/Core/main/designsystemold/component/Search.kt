package designsystemold.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchSymbolic(
    query: String = "",
    modifier: Modifier = Modifier,
    buffered: Boolean = false,
    resultCount: Int = 0,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onSearch: (String) -> Unit = {},
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val buffer = remember { mutableStateOf("") }
        SearchBar(
            query = if (buffered) buffer.value else query,
            onQueryChange = {
                if (buffered)
                    buffer.value = it

                onSearch(it)
            },
            placeholder = "Search settings...",
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        if (query.isNotEmpty()) {
            SearchResultsHelper(
                resultsCount = resultCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
            )
        }
    }
}

@Preview
@Composable
private fun SearchSectionPreview() {
    Surface {
        SearchSymbolic()
    }
}

@Preview
@Composable
private fun SearchSectionPreviewOnSearch() {
    Surface {
        SearchSymbolic("asda")
    }
}