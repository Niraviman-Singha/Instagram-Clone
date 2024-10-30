package com.example.instagramclone.Utils

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import javax.security.auth.callback.Callback

fun uploadImage(uri: Uri, folderName: String, callback: (String?) -> Unit) {
    var imageUrl: String? = null
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                callback(imageUrl)
            }
        }

}

fun uploadVideo(
    uri: Uri,
    folderName: String,
    progressdialog: ProgressDialog,
    callback: (String?) -> Unit
) {
    var videoUrl: String? = null
    progressdialog.setTitle("Uploading......")
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                videoUrl = it.toString()
                progressdialog.dismiss()
                callback(videoUrl)
            }
        }
        .addOnProgressListener {
            val uploadedValue: Long = it.bytesTransferred / it.totalByteCount
            progressdialog.setMessage("Uploaded $uploadedValue %")
        }

}