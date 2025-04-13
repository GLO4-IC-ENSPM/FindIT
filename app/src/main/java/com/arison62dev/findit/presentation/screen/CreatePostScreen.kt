package com.arison62dev.findit.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.arison62dev.findit.domain.model.PostType
import com.arison62dev.findit.presentation.viewmodel.CreatePostViewModel
import com.arison62dev.findit.util.ImageConverter
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    // État pour afficher un indicateur de chargement
    var isLoading by viewModel.isLoading
    var isSuccess by viewModel.isSuccess
    var isError by viewModel.errorMessage

    // Créer un fichier temporaire pour les photos prises avec la caméra
    fun createImageFile(): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = context.getExternalFilesDir("Pictures")
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            image
        )
    }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess && tempCameraUri != null) {
            isLoading = true
            setImageUri(tempCameraUri)
            scope.launch {
                val bytes = ImageConverter.uriToByteArray(context, tempCameraUri!!)
                setImageByteArray(bytes)
                isLoading = false
            }
        }
    }

    // Permission de caméra
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            tempCameraUri = createImageFile()
            takePictureLauncher.launch(tempCameraUri!!)
        } else {
            Toast.makeText(context, "Permission de caméra refusée", Toast.LENGTH_SHORT).show()
        }
    }

    // Lanceur pour prendre une photo

    // Lanceur pour sélectionner une image depuis la galerie
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

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Post publié avec succès", Toast.LENGTH_SHORT).show()
            navController.popBackStack()
        }
    }

    // Dialogue pour choisir la source de l'image
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Choisir une image") },
            text = { Text("Comment souhaitez-vous ajouter une image ?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showImageSourceDialog = false
                        pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PhotoLibrary,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Galerie")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showImageSourceDialog = false
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) -> {
                                tempCameraUri = createImageFile()
                                takePictureLauncher.launch(tempCameraUri!!)
                            }

                            else -> {
                                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        }
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Appareil photo")
                    }
                }
            }
        )
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            // Champ de titre WYSIWYG
            TextField(
                value = viewModel.postState.value.titre,
                onValueChange = { viewModel.updateTitle(it) },
                placeholder = { Text("Titre de votre post...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                textStyle = MaterialTheme.typography.headlineSmall
            )

            // Zone de prévisualisation WYSIWYG
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(
                        enabled = !isLoading,
                        onClick = {
                            showImageSourceDialog = true
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Image sélectionnée",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Bouton flottant pour changer l'image
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable(enabled = !isLoading) { showImageSourceDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Changer d'image",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Ajouter une image",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Cliquez pour ajouter une image",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Indicateur de chargement qui apparaît pendant le traitement de l'image
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Poster anonymement",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(checked = viewModel.postState.value.estAnonyme, onCheckedChange = {
                    viewModel.updateIsAnonymous(it)
                })
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(text="J'ai trouvé")
                    RadioButton(
                        selected = viewModel.postState.value.type == PostType.TROUVE,
                        onClick = { viewModel.updateType(PostType.TROUVE) },
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Text(text="J'ai perdu")
                    RadioButton(
                        selected = viewModel.postState.value.type == PostType.PERDU,
                        onClick = { viewModel.updateType(PostType.PERDU) },
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            // Bouton de publication (avec icône)
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
                        Toast.makeText(
                            context,
                            "Veuillez sélectionner une image",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                enabled = !isLoading && imageByteArray != null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Publier", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.size(8.dp))
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Publier",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            if (isError != null && isError!!.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = isError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}