package com.arison62dev.findit.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arison62dev.findit.presentation.component.post.PostCard
import com.arison62dev.findit.presentation.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isRefreshing by viewModel.isPullingToRefresh.collectAsState()
    val error by viewModel.error.collectAsState()
    val hasMorePosts by viewModel.hasMorePosts.collectAsState()
    val isLoadingNextPage by viewModel.isLoadigNextPage.collectAsState()

    val listState = rememberLazyListState()

    // Amélioration de la détection de fin de défilement
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val lastVisibleItem = visibleItems.lastOrNull()

            if (lastVisibleItem != null) {
                // Calculer la position relative par rapport à la fin de la liste
                val isNearEnd = lastVisibleItem.index >= posts.size - 3 &&
                        posts.isNotEmpty() &&
                        hasMorePosts
                isNearEnd
            } else {
                false
            }
        }
            .distinctUntilChanged() // Important: évite les déclenchements multiples
            .collect { isNearEnd ->
                if (isNearEnd && !isLoadingNextPage && hasMorePosts) {
                    viewModel.nextPage()
                }
            }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = posts,
                key = { post -> post.idPost ?: post.hashCode() }, // S'assurer que la clé est stable
                contentType = { "post" } // Optimisation pour Compose
            ) { post ->
                PostCard(post = post)
            }

            // Indicateur de chargement pour la pagination en bas
            if (isLoadingNextPage) {
                item(key = "loading-indicator") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        // Indicateur de chargement initial
        if (isLoading && posts.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Affichage des erreurs
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Message si aucun post n'est disponible
        if (posts.isEmpty() && !isLoading && error == null) {
            Text(
                text = "Aucun post disponible",
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}