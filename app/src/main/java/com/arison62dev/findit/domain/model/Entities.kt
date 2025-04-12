package com.arison62dev.findit.domain.model

import java.time.LocalDateTime

data class Utilisateur(
    val idUtilisateur: Int? = null,
    val nom: String,
    val motDePasse: String,
    val email: String,
    val dateInscription: LocalDateTime
)

data class ProfileUtilisateur(
    val idProfile: Int? = null,
    val idUtilisateur: Int,
    val soldeTokens: Int,
    val imageProfile: String?,
    val bannerProfile: String?,
    val bio: String?,
    val nomProfile: String?,
    val estVerifie: Boolean
)

data class TransactionToken(
    val idTransaction: Int? = null,
    val idUtilisateur: Int,
    val typeTransaction: String,
    val montantTokens: Int,
    val details: String?,
    val dateTransaction: LocalDateTime
)

data class Localisation(
    val idLocation: Int? = null,
    val latitude: Double?,
    val longitude: Double?
)

data class Categorie(
    val idCategorie: Int? = null,
    val nom: String
)

enum class PostType {
    TROUVE, PERDU
}

enum class PostStatut {
    OUVERT, EN_COURS, RESTITUE, SIGNALE, ARCHIVE
}

data class Post(
    val idPost: Int? = null,
    val titre: String,
    val dateHeure: LocalDateTime?,
    val type: PostType,
    val estAnonyme: Boolean,
    val idUtilisateur: Int?,
    val datePublication: LocalDateTime,
    val statut: PostStatut,
    val nbSignalements: Int,
    val nbLikes: Int
)

data class Photo(
    val idPhoto: Int? = null,
    val idPost: Int,
    val chemin: String
)

data class PostsCategorie(
    val idPostCategorie: Int? = null,
    val idCategorie: Int,
    val idPost: Int
)

data class Signalement(
    val idSignalement: Int? = null,
    val idPost: Int,
    val idUtilisateur: Int,
    val dateSignalement: LocalDateTime,
    val raison: String?
)

data class PostLike(
    val idLike: Int? = null,
    val idUtilisateur: Int,
    val idPost: Int
)

data class Commentaire(
    val idCommentaire: Int? = null,
    val contenu: String,
    val idPost: Int,
    val idUtilisateur: Int,
    val dateCreation: LocalDateTime
)

enum class DiscussionStatut {
    ENCOURS, FERME
}

data class Discussion(
    val idDiscussion: Int? = null,
    val statut: DiscussionStatut,
    val dateCreation: LocalDateTime,
    val idPost: Int,
    val idAdmin: Int?
)

data class DiscussionsParticipant(
    val idParticipant: Int? = null,
    val idDiscussion: Int,
    val idUtilisateur: Int,
    val dateAjout: LocalDateTime
)

data class Message(
    val idMessage: Int? = null,
    val dateEnvoie: LocalDateTime,
    val idExpediteur: Int,
    val idDestinataire: Int,
    val contenu: String,
    val medias: String?
)