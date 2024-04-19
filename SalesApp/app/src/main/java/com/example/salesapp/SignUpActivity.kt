package com.example.salesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        FirebaseApp.initializeApp(this);

        val emailet : TextInputEditText = findViewById(R.id.emailEt);
        val passwordet : TextInputEditText = findViewById(R.id.passET);
        val confirmet : TextInputEditText = findViewById(R.id.confirmPassEt);
        val signupbtn : Button = findViewById(R.id.buttonsignup);
        auth = Firebase.auth

        signupbtn.setOnClickListener {
            val email = emailet.text.toString();
            val password = passwordet.text.toString();
            val confirm = confirmet.text.toString();
            if(password !=confirm){
                Toast.makeText(baseContext, "Passwords don't match.", Toast.LENGTH_LONG).show()
            }else if(email.isEmpty() || password.isEmpty() || confirm.isEmpty()){
                Toast.makeText(baseContext, "Complete the fields to create your account.", Toast.LENGTH_LONG).show()
            }else{

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(baseContext, "Account created.", Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser;
                            val intent = Intent(applicationContext,LoginActivity::class.java);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText( baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }

            }

        }

    }
}