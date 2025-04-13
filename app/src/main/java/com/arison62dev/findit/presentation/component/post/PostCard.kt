package com.arison62dev.findit.presentation.component.post

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Comment
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arison62dev.findit.domain.model.Post
import com.arison62dev.findit.presentation.viewmodel.post.PostCardViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun PostCard(
    post: Post,
    viewModel: PostCardViewModel = hiltViewModel()
) {
    var likes by remember { mutableStateOf(post.nbLikes) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Partie 1 : Informations utilisateur (si pas anonyme)
            if (!post.estAnonyme && post.idUtilisateur != null) {
                CardUser(idUser = post.idUtilisateur)
            } else {
                Text(
                    text = "Post anonyme",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Partie 2 : Titre et date de publication
            Column {
                Text(
                    text = post.titre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = formatDate(post.datePublication),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            PostImage(postId = post.idPost!!)

            Spacer(modifier = Modifier.height(12.dp))

            // Partie 4 : Boutons Like et Commenter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LikeButton(likes = likes) {
                    likes++
                    // Appel à une fonction dans le ViewModel pour enregistrer le like côté serveur
                }

                CommentButton {
                    // Action lorsqu'on clique sur le bouton "Commenter"
                }
            }
        }

}

/**
 * Formate une date pour l'affichage.
 */
fun formatDate(date: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    return date.format(formatter)
}
/**
 * Bouton "Like" avec un compteur de likes.
 */
@Composable
fun LikeButton(
    likes: Int,
    onLike: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onLike)
    ) {
        Icon(
            imageVector = Icons.Default.ThumbUp,
            contentDescription = "Like",
            tint = MaterialTheme.colorScheme.primary // Utilise la couleur primaire du thème
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$likes Likes",
            color = MaterialTheme.colorScheme.primary // Utilise la couleur primaire du thème
        )
    }
}

/**
 * Bouton "Commenter".
 */
@Composable
fun CommentButton(
    onComment: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable(onClick = onComment)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Comment,
            contentDescription = "Commenter",
            tint = MaterialTheme.colorScheme.secondary // Utilise la couleur secondaire du thème
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Commenter",
            color = MaterialTheme.colorScheme.secondary // Utilise la couleur secondaire du thème
        )
    }
}