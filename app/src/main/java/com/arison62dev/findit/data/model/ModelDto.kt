package com.arison62dev.findit.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class UtilisateurDto(
    @SerialName("id_utilisateur")
    val idUtilisateur: Int? = null,
    @SerialName("nom")
    val nom: String,
    @SerialName("mot_de_passe")
    val motDePasse: String,
    @SerialName("email")
    val email: String,
    @SerialName("date_inscription")
    val dateInscription: String = LocalDateTime.now().toString()
)

@Serializable
data class ProfileUtilisateurDto(
    @SerialName("id_profile")
    val idProfile: Int? = null,
    @SerialName("id_utilisateur")
    val idUtilisateur: Int,
    @SerialName("solde_tokens")
    val soldeTokens: Int = 0,
    @SerialName("image_profile")
    val imageProfile: String? = null,
    @SerialName("banner_profile")
    val bannerProfile: String? = null,
    @SerialName("bio")
    val bio: String? = null,
    @SerialName("nom_profile")
    val nomProfile: String? = null,
    @SerialName("est_verifie")
    val estVerifie: Boolean = false
)

@Serializable
data class TransactionTokenDto(
    @SerialName("id_transaction")
    val idTransaction: Int? = null,
    @SerialName("id_utilisateur")
    val idUtilisateur: Int,
    @SerialName("type_transaction")
    val typeTransaction: String,
    @SerialName("montant_tokens")
    val montantTokens: Int,
    @SerialName("details")
    val details: String? = null,
    @SerialName("date_transaction")
    val dateTransaction: String = LocalDateTime.now().toString()
)

@Serializable
data class LocalisationDto(
    @SerialName("id_location")
    val idLocation: Int? = null,
    @SerialName("latitude")
    val latitude: Double? = null,
    @SerialName("longitude")
    val longitude: Double? = null
)

@Serializable
data class CategorieDto(
    @SerialName("id_categorie")
    val idCategorie: Int? = null,
    @SerialName("nom")
    val nom: String
)

@Serializable
enum class PostTypeDto {
    @SerialName("Trouve") TROUVE,
    @SerialName("Perdu") PERDU
}

@Serializable
enum class PostStatutDto {
    @SerialName("Ouvert") OUVERT,
    @SerialName("En cours") EN_COURS,
    @SerialName("Restitue") RESTITUE,
    @SerialName("Signale") SIGNALE,
    @SerialName("Archive") ARCHIVE
}

@Serializable
data class PostDto(
    @SerialName("id_post")
    val idPost: Int? = null,
    @SerialName("titre")
    val titre: String,
    @SerialName("description")
    val description: String? = null,
    @SerialName("date_heure")
    val dateHeure: String? = null,
    @SerialName("type")
    val type: PostTypeDto,
    @SerialName("est_anonyme")
    val estAnonyme: Boolean = false,
    @SerialName("lieu_description")
    val lieuDescription: String? = null,
    @SerialName("id_utilisateur")
    val idUtilisateur: Int? = null,
    @SerialName("id_localisation")
    val idLocalisation: Int? = null,
    @SerialName("date_publication")
    val datePublication: String = LocalDateTime.now().toString(),
    @SerialName("statut")
    val statut: PostStatutDto = PostStatutDto.OUVERT,
    @SerialName("nb_signalements")
    val nbSignalements: Int = 0,
    @SerialName("nb_likes")
    val nbLikes: Int = 0
)

@Serializable
data class PhotoDto(
    @SerialName("id_photo")
    val idPhoto: Int? = null,
    @SerialName("id_post")
    val idPost: Int,
    @SerialName("chemin")
    val chemin: String
)

@Serializable
data class PostsCategorieDto(
    @SerialName("id_post_categorie")
    val idPostCategorie: Int? = null,
    @SerialName("id_categorie")
    val idCategorie: Int,
    @SerialName("id_post")
    val idPost: Int
)

@Serializable
data class SignalementDto(
    @SerialName("id_signalement")
    val idSignalement: Int? = null,
    @SerialName("id_post")
    val idPost: Int,
    @SerialName("id_utilisateur")
    val idUtilisateur: Int,
    @SerialName("date_signalement")
    val dateSignalement: String = LocalDateTime.now().toString(),
    @SerialName("raison")
    val raison: String? = null
)

@Serializable
data class PostLikeDto(
    @SerialName("id_like")
    val idLike: Int? = null,
    @SerialName("id_utilisateur")
    val idUtilisateur: Int,
    @SerialName("id_post")
    val idPost: Int
)

@Serializable
data class CommentaireDto(
    @SerialName("id_commentaire")
    val idCommentaire: Int? = null,
    @SerialName("contenu")
    val contenu: String,
    @SerialName("id_post")
    val idPost: Int,
    @SerialName("id_utilisateur")
    val idUtilisateur: Int,
    @SerialName("date_creation")
    val dateCreation: String = LocalDateTime.now().toString()
)

@Serializable
enum class DiscussionStatutDto {
    @SerialName("Encours") ENCOURS,
    @SerialName("Ferme") FERME
}

@Serializable
data class DiscussionDto(
    @SerialName("id_discussion")
    val idDiscussion: Int? = null,
    @SerialName("statut")
    val statut: DiscussionStatutDto = DiscussionStatutDto.ENCOURS,
    @SerialName("date_creation")
    val dateCreation: String = LocalDateTime.now().toString(),
    @SerialName("id_post")
    val idPost: Int,
    @SerialName("id_admin")
    val idAdmin: Int? = null
)

@Serializable
data class DiscussionsParticipantDto(
    @SerialName("id_participant")
    val idParticipant: Int? = null,
    @SerialName("id_discussion")
    val idDiscussion: Int,
    @SerialName("id_utilisateur")
    val idUtilisateur: Int,
    @SerialName("date_ajout")
    val dateAjout: String = LocalDateTime.now().toString()
)

@Serializable
data class MessageDto(
    @SerialName("id_message")
    val idMessage: Int? = null,
    @SerialName("date_envoie")
    val dateEnvoie: String = LocalDateTime.now().toString(),
    @SerialName("id_expediteur")
    val idExpediteur: Int,
    @SerialName("id_destinataire")
    val idDestinataire: Int,
    @SerialName("contenu")
    val contenu: String,
    @SerialName("medias")
    val medias: String? = null
)