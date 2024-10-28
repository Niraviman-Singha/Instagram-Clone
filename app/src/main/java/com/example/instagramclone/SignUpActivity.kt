package com.example.instagramclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instagramclone.Models.User
import com.example.instagramclone.Utils.USER_NODE
import com.example.instagramclone.Utils.USER_PROFILE_FOLDER
import com.example.instagramclone.Utils.uploadImage
import com.example.instagramclone.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    lateinit var user: com.example.instagramclone.Models.User
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()){
        uri->
        uri?.let{
            uploadImage(uri, USER_PROFILE_FOLDER){
                if (it==null){

                }else{
                    user.image = it
                    binding.addImage.setImageURI(uri)

                }
            }

    }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        user = User()

        binding.signUpBtn.setOnClickListener {
            if (binding.nameET.text.toString() == "" && binding.emailET.text.toString() == "" && binding.passwordET.text.toString() == "") {
                Toast.makeText(
                    this@SignUpActivity,
                    "Please fill up all the Information",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.emailET.text.toString(), binding.passwordET.text.toString()
                ).addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        user.name = binding.nameET.text.toString()
                        user.email = binding.emailET.text.toString()
                        user.password = binding.passwordET.text.toString()
                        Firebase.firestore.collection(USER_NODE)
                            .document(Firebase.auth.currentUser!!.uid).set(user)
                            .addOnSuccessListener {
                                Toast.makeText(this@SignUpActivity, "Login", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(
                            this@SignUpActivity,
                            result.exception?.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

        }
        binding.addImage.setOnClickListener {
            launcher.launch("image/*")
        }
    }
}