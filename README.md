# 📱 FindIT — Application Mobile Android

**FindIT** est une application Android développée avec **Jetpack Compose**. Elle permet aux utilisateurs de rechercher, publier et retrouver des objets perdus dans leur communauté.

---

## 🚀 Fonctionnalités principales

- Authentification sécurisée via Supabase
- Création et consultation de publications
- Recherche d’objets par mots-clés
---

## 🛠️ Stack technique

- **Frontend** : Android (Kotlin, Jetpack Compose, Material Design 3)
- **Backend** : Supabase (PostgreSQL, Auth, Storage)

---

## ⚙️ Configuration et exécution

### 1. Cloner le projet

```bash
git clone ttps://github.com/GLO4-IC-ENSPM/FindIT.git
cd FindIT
```

### 2. Ajouter les clés Supabase

Ajoutez les variables suivantes dans le fichier `local.properties` à la racine du module Android :

```properties
SUPABASE_URL=your_supabase_url
SUPABASE_ANON_KEY=your_supabase_anon_key
```

Remplacez `your_supabase_url` et `your_supabase_anon_key` par les valeurs de votre projet Supabase.

### 3. Lancer l'application

Ouvrez le projet avec **Android Studio**, synchronisez les dépendances, puis lancez l'application sur un émulateur ou un appareil Android.

---


Merci pour votre intérêt pour **FindIT** ✨

![Presentation interface ](FindIT.png)

