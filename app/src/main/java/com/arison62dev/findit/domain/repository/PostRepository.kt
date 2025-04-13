package com.arison62dev.findit.domain.repository

import android.util.Log
import com.arison62dev.findit.data.Result
import com.arison62dev.findit.data.local.AuthDataStore
import com.arison62dev.findit.data.model.PhotoDto
import com.arison62dev.findit.data.model.PostDto
import com.arison62dev.findit.data.model.tableName
import com.arison62dev.findit.domain.model.Post
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.delay


interface PostRepository {
    suspend fun createPost(post: Post, imageName: String, imageFile: ByteArray): Result<PostDto>
    suspend fun getAllPosts(
        limit: Long = 10, offset: Long = 0
    ): Result<List<PostDto>>
    suspend fun searchPostsByTitle(
        query: String,
        limit: Long = 10,
        offset: Long = 0
    ): Result<List<PostDto>>
    suspend fun getPostById(id: Int): Result<PostDto>
    suspend fun getImagePostByPostId(idPost: Int): Result<PhotoDto>

}

class PostRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val authDataStore: AuthDataStore
) : PostRepository {

    private var currentSearchJob: Job? = null

    override suspend fun createPost(
        post: Post, imageName: String, imageFile: ByteArray
    ): Result<PostDto> = withContext(Dispatchers.IO) {
        try {
            val userId = authDataStore.userId.first()
            val postDto = PostDto(
                idUtilisateur = userId,
                titre = post.titre,
                dateHeure = post.dateHeure.toString(),
                type = post.type.toString(),
                estAnonyme = post.estAnonyme,
                datePublication = post.datePublication.toString(),
                statut = post.statut.toString(),
                nbSignalements = post.nbSignalements,
                nbLikes = post.nbLikes
            )
            Log.d("PostRepositoryImpl", "postDto: $postDto")
            val insertedPost = postgrest.from(PostDto.tableName).insert(postDto) {
                select()
            }.decodeSingleOrNull<PostDto>();
            Log.d("PostRepositoryImpl", "$insertedPost")
            if (insertedPost == null) {
                return@withContext Result.Error(Exception("Erreur creation"))
            } else {
                val insertedPostId = insertedPost.idPost!!
                val bucket = storage.from("post.images")
                val imagePath =
                    "post_$insertedPostId/${java.util.UUID.randomUUID().toString()}$imageName"
                bucket.upload(imagePath, imageFile)
                val insertedPhoto = postgrest.from(PhotoDto.tableName).insert(
                    PhotoDto(
                        idPost = insertedPostId, chemin = imagePath
                    )
                ) {
                    select()
                }.decodeSingleOrNull<PhotoDto>()
                if (insertedPhoto == null) {
                    return@withContext Result.Error(Exception("Erreur creation photo"))
                }
                return@withContext Result.Success(insertedPost)
            }
        } catch (e: java.lang.Exception) {
            Log.d("PostRepository@createPost", "$e")
            return@withContext Result.Error(e)
        }
    }

    override suspend fun getAllPosts(limit: Long, offset: Long): Result<List<PostDto>> = withContext(Dispatchers.IO){
        try {
            val posts = postgrest.from(PostDto.tableName).select {
                range(
                    from = offset, to = offset + limit - 1
                )
                order("date_publication", order = Order.DESCENDING)
            }.decodeList<PostDto>()
            Log.d("PostRepositoryImpl", "$posts")

            if(posts.isEmpty()){
                return@withContext Result.Error(Exception("Erreur chargement posts"))
            } else {
                return@withContext Result.Success(posts)
            }
        }catch (e: java.lang.Exception){
            Log.d("PostRepository@getAllPosts", "$e")
            return@withContext Result.Error(e)
        }
    }




    override suspend fun getPostById(id: Int): Result<PostDto> {
        TODO("Not yet implemented")
    }

    override suspend fun getImagePostByPostId(idPost: Int): Result<PhotoDto> = withContext(Dispatchers.IO){
        try {
            val photo = postgrest.from(PhotoDto.tableName).select {
                filter {
                    eq("id_post", idPost)
                }

            }.decodeSingleOrNull<PhotoDto>()
            if(photo == null){
                return@withContext Result.Error(Exception("Erreur chargement photo"))
            } else {
                val imageUrl = storage.from("post.images").publicUrl(photo.chemin)
                return@withContext Result.Success(photo.copy(chemin = imageUrl))
            }
        } catch (e: java.lang.Exception){
            Log.d("PostRepository@getImagePostByPostId", "$e")
            return@withContext Result.Error(e)
        }
    }



    override suspend fun searchPostsByTitle(
        query: String,
        limit: Long,
        offset: Long
    ): Result<List<PostDto>> = withContext(Dispatchers.IO) {
        try {
            // Annule la recherche précédente si elle est en cours
            currentSearchJob?.cancel()

            val deferred = CoroutineScope(Dispatchers.IO).async {
                // Attendre un délai minimum pour éviter les requêtes trop fréquentes
                delay(300) // 300ms de debounce

                val posts = postgrest.from(PostDto.tableName).select {

                    if (query.isNotBlank()) {
                        filter {
                            ilike("titre", "%$query%")
                        }
                    }
                    range(from = offset, to = offset + limit - 1)
                    order("date_publication", order = Order.DESCENDING)
                }.decodeList<PostDto>()

                if (posts.isEmpty()) {
                    Result.Error(Exception("Aucun post trouvé pour '$query'"))
                } else {
                    Result.Success(posts)
                }
            }

            currentSearchJob = deferred
            deferred.await()
        } catch (e: Exception) {
            if (e is CancellationException) {
                // La recherche a été annulée volontairement
                Result.Error(Exception("Recherche annulée"))
            } else {
                Log.e("PostRepository@search", "Erreur recherche: ${e.message}")
                Result.Error(e)
            }
        }
    }

}