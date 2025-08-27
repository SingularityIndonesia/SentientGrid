package designsystemold.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import designsystemold.SingularityTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopAppBar(
    titleText: String? = null,
    subTitle: String? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onNavigateBack: (() -> Unit)? = null,
    trailingActions: (@Composable RowScope.() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        onNavigateBack?.let {
            Back { onNavigateBack.invoke() }
        } ?: Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            titleText?.let {
                TitleLargeText(
                    text = it,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            subTitle?.let {
                LabelSmallText(
                    text = it,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (trailingActions != null) {
                trailingActions()
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    SingularityTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Surface {
                CommonTopAppBar(
                    titleText = "Home",
                )
            }
            Surface {
                CommonTopAppBar(
                    titleText = "Home",
                    subTitle = "June 12, 2025",
                    onNavigateBack = {}
                )
            }

            Surface {
                CommonTopAppBar(
                    titleText = "Home",
                    subTitle = "June 12, 2025",
                    onNavigateBack = {}
                ) {
                    AddMedia { }
                }
            }

            Surface {
                CommonTopAppBar(
                    titleText = "Home\nsdfsdf sdf sd fs dfs dfsdfsdsdfsdf",
                    subTitle = "June 12, 2025 \nsdf sdf sfsd fds fs f sdf dsf sdf  sdf ds asd sd sdf sd fsdf fsd f sdf sdf",
                    onNavigateBack = {}
                ) {
                    AddMedia { }
                }
            }
        }
    }
}