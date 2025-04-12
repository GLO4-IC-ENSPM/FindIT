package com.arison62dev.findit.util

import com.arison62dev.findit.BuildConfig
import org.mindrot.jbcrypt.BCrypt

object PasswordHasher {

    /**
     * Hashe le mot de passe en utilisant l'algorithme bcrypt.
     *
     * @param password Le mot de passe à hasher.
     * @return Le mot de passe hashé.
     */
    fun hash(password: String): String {
        // Génère un "salt" aléatoire et hashe le mot de passe
        return BCrypt.hashpw(password,  BCrypt.gensalt())
    }

    /**
     * Vérifie si le mot de passe en clair correspond au hash.
     *
     * @param password Le mot de passe en clair à vérifier.
     * @param hashedPassword Le hash du mot de passe stocké.
     * @return True si le mot de passe correspond au hash, false sinon.
     */
    fun check(password: String, hashedPassword: String): Boolean {
        return BCrypt.checkpw(password, hashedPassword)
    }
}