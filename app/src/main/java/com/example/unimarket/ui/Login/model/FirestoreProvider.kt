package com.example.unimarket.ui.Login.model

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreProvider {
    val instance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
}