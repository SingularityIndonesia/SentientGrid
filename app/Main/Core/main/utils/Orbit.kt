package utils

import androidx.compose.runtime.Composable
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun <S : Any, E : Any> CollectSideEffect(
    containerHost: ContainerHost<S, E>,
    onEffect: suspend (E) -> Unit
) {
    containerHost.collectSideEffect { onEffect.invoke(it) }
}