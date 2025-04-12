package com.arison62dev.findit.domain.repository

import android.util.Log
import com.arison62dev.findit.data.Result
import com.arison62dev.findit.data.local.AuthDataStore
import com.arison62dev.findit.data.model.PostDto
import com.arison62dev.findit.data.model.tableName
import com.arison62dev.findit.domain.model.Post
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject


interface PostRepository {
    suspend fun createPost(post: Post, imageName: String, imageFile: ByteArray): Result<PostDto>
    suspend fun getAllPosts(): Result<List<PostDto>>
    suspend fun getPostById(id: Int): Result<PostDto>

}

class PostRepositoryImpl @Inject constructor(
    private val postgrest: Postgrest,
    private val storage: Storage,
    private val authDataStore: AuthDataStore
) : PostRepository {
    override suspend fun createPost(
        post: Post,
        imageName: String,
        imageFile: ByteArray
    ): Result<PostDto> {

        return withContext(Dispatchers.IO) {
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
                    val imagePath = "post_$insertedPostId/${java.util.UUID.randomUUID().toString()}$imageName"
                    bucket.upload(imagePath, imageFile)
                    return@withContext Result.Success(insertedPost)
                }
            } catch (e: java.lang.Exception) {
                Log.d("PostRepository@createPost", "$e")
                return@withContext Result.Error(e)
            }
        }

    }

    override suspend fun getAllPosts(): Result<List<PostDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun getPostById(id: Int): Result<PostDto> {
        TODO("Not yet implemented")
    }
}