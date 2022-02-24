package com.aov2099.baz

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FirestoreCalls {

    fun pushAddressData() {

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        val doc = HashMap<String, Any>()
        doc["date"] = currentDate
        doc["location"] = "F :("

        val db = Firebase.firestore

        db.collection("logs").document("user_01").collection("address") //hardcodeado por falta de tiempo jaja. De no ser así ahregaría un auth
            .add(doc)

            .addOnSuccessListener { documentReference ->
                Log.d("FIRESTORE", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("FIRESTORE", "Error adding document", e)
            }
    }
}