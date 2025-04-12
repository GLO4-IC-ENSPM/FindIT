package com.arison62dev.findit.presentation.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.arison62dev.findit.presentation.viewmodel.CreatePostViewModel
import com.arison62dev.findit.util.ImageConverter
import kotlinx.coroutines.launch

@Composable
fun CreatePostScreen(
    modifier: Modifier = Modifier,
    viewModel: CreatePostViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // États pour l'image sélectionnée
    val (imageByteArray, setImageByteArray) = remember { mutableStateOf<ByteArray?>(null) }
    val (imageUri, setImageUri) = remember { mutableStateOf<Uri?>(null) }

    // État pour afficher un indicateur de chargement
    var isLoading by viewModel.isLoading
    var isSucces by viewModel.isSuccess
    var isError by viewModel.errorMessage
    LaunchedEffect(Unit) {
        if(isSucces){
            Toast.makeText(context, "Post reuissi", Toast.LENGTH_SHORT).show()
        navController.popBackStack()
    }

    }
    // Préparation du lanceur pour la sélection d'image
    val pickMedia = rememberLauncherForActivityResult(
        contract = PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            isLoading = true
            setImageUri(uri)
            scope.launch {
                val bytes = ImageConverter.uriToByteArray(context, uri)
                setImageByteArray(bytes)
                isLoading = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Titre
        Text(
            text = "Nouveau Post",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Champ de titre
        OutlinedTextField(
            value = viewModel.postState.value.titre,
            onValueChange = { viewModel.updateTitle(it) },
            label = { Text("Titre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Champ de description
        // Aperçu de l'image sélectionnée
        imageUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Image sélectionnée",
                modifier = Modifier
                    .size(500.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
        }

        // Bouton pour sélectionner une image
        Button(
            onClick = {
                pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = if (imageUri == null) "Choisir une image" else "Changer d'image")
        }

        // Bouton de soumission
        Button(
            onClick = {
                if (imageByteArray != null) {
                    scope.launch {
                        isLoading = true
                        viewModel.createPostWithImage(
                            imageName = "post_${System.currentTimeMillis()}.jpg",
                            imageFile = imageByteArray
                        )
                    }
                } else {
                    Toast.makeText(context, "Veuillez sélectionner une image", Toast.LENGTH_SHORT).show()
                }
            },
            enabled = !isLoading && imageByteArray != null,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Publier")
            }
        }
    }
}