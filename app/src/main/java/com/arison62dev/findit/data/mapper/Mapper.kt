package com.arison62dev.findit.data.mapper

import com.arison62dev.findit.data.model.*
import com.arison62dev.findit.domain.model.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun UtilisateurDto.toDomain(): Utilisateur {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val dateInscription = LocalDateTime.parse(this.dateInscription, formatter)
    return Utilisateur(
        idUtilisateur = this.idUtilisateur,
        nom = this.nom,
        motDePasse = this.motDePasse,
        email = this.email,
        dateInscription = dateInscription
    )
}

fun ProfileUtilisateurDto.toDomain(): ProfileUtilisateur {
    return ProfileUtilisateur(
        idProfile = this.idProfile,
        idUtilisateur = this.idUtilisateur,
        soldeTokens = this.soldeTokens,
        imageProfile = this.imageProfile,
        bannerProfile = this.bannerProfile,
        bio = this.bio,
        nomProfile = this.nomProfile,
        estVerifie = this.estVerifie
    )
}

fun TransactionTokenDto.toDomain(): TransactionToken {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val dateTransaction = LocalDateTime.parse(this.dateTransaction, formatter)
    return TransactionToken(
        idTransaction = this.idTransaction,
        idUtilisateur = this.idUtilisateur,
        typeTransaction = this.typeTransaction,
        montantTokens = this.montantTokens,
        details = this.details,
        dateTransaction = dateTransaction
    )
}

fun LocalisationDto.toDomain(): Localisation {
    return Localisation(
        idLocation = this.idLocation,
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun CategorieDto.toDomain(): Categorie {
    return Categorie(
        idCategorie = this.idCategorie,
        nom = this.nom
    )
}


fun String.toDomain(): PostStatut {
    return when (this.uppercase()) {
        "OUVERT" -> PostStatut.OUVERT
        "EN_COURS" -> PostStatut.EN_COURS
        "RESTITUE" -> PostStatut.RESTITUE
        "SIGNALE" -> PostStatut.SIGNALE
        "ARCHIVE" -> PostStatut.ARCHIVE
        else -> PostStatut.OUVERT
    }
}

fun PostDto.toDomain(): Post {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val dateHeure = this.dateHeure?.let { LocalDateTime.parse(it, formatter) }
    val datePublication = LocalDateTime.parse(this.datePublication, formatter)
    return Post(
        idPost = this.idPost,
        titre = this.titre,
        dateHeure = dateHeure,
        type = when(this.type){
            "TROUVE" -> PostType.TROUVE
            "PERDU" -> PostType.PERDU
            else -> PostType.TROUVE
        },
        estAnonyme = this.estAnonyme,
        idUtilisateur = this.idUtilisateur,
        datePublication = datePublication,
        statut = when(this.statut){
            "Ouvert" -> PostStatut.OUVERT
            "En cours" -> PostStatut.EN_COURS
            "Restitue" -> PostStatut.RESTITUE
            "Signale" -> PostStatut.SIGNALE
            "Archive" -> PostStatut.ARCHIVE
            else -> PostStatut.OUVERT
        },
        nbSignalements = this.nbSignalements,
        nbLikes = this.nbLikes,
    )
}

fun PhotoDto.toDomain(): Photo {
    return Photo(
        idPhoto = this.idPhoto,
        idPost = this.idPost,
        chemin = this.chemin
    )
}

fun PostsCategorieDto.toDomain(): PostsCategorie {
    return PostsCategorie(
        idPostCategorie = this.idPostCategorie,
        idCategorie = this.idCategorie,
        idPost = this.idPost
    )
}

fun SignalementDto.toDomain(): Signalement {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val dateSignalement = LocalDateTime.parse(this.dateSignalement, formatter)
    return Signalement(
        idSignalement = this.idSignalement,
        idPost = this.idPost,
        idUtilisateur = this.idUtilisateur,
        dateSignalement = dateSignalement,
        raison = this.raison
    )
}

fun PostLikeDto.toDomain(): PostLike {
    return PostLike(
        idLike = this.idLike,
        idUtilisateur = this.idUtilisateur,
        idPost = this.idPost
    )
}

fun CommentaireDto.toDomain(): Commentaire {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val dateCreation = LocalDateTime.parse(this.dateCreation, formatter)
    return Commentaire(
        idCommentaire = this.idCommentaire,
        contenu = this.contenu,
        idPost = this.idPost,
        idUtilisateur = this.idUtilisateur,
        dateCreation = dateCreation
    )
}

fun DiscussionStatutDto.toDomain(): DiscussionStatut {
    return when (this) {
        DiscussionStatutDto.ENCOURS -> DiscussionStatut.ENCOURS
        DiscussionStatutDto.FERME -> DiscussionStatut.FERME
    }
}

fun DiscussionDto.toDomain(): Discussion {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val dateCreation = LocalDateTime.parse(this.dateCreation, formatter)
    return Discussion(
        idDiscussion = this.idDiscussion,
        statut = this.statut.toDomain(),
        dateCreation = dateCreation,
        idPost = this.idPost,
        idAdmin = this.idAdmin
    )
}

fun DiscussionsParticipantDto.toDomain(): DiscussionsParticipant {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val dateAjout = LocalDateTime.parse(this.dateAjout, formatter)
    return DiscussionsParticipant(
        idParticipant = this.idParticipant,
        idDiscussion = this.idDiscussion,
        idUtilisateur = this.idUtilisateur,
        dateAjout = dateAjout
    )
}

fun MessageDto.toDomain(): Message {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val dateEnvoie = LocalDateTime.parse(this.dateEnvoie, formatter)
    return Message(
        idMessage = this.idMessage,
        dateEnvoie = dateEnvoie,
        idExpediteur = this.idExpediteur,
        idDestinataire = this.idDestinataire,
        contenu = this.contenu,
        medias = this.medias
    )
}