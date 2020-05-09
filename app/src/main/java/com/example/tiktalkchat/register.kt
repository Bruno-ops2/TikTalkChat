package com.example.tiktalkchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class register : AppCompatActivity() {
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var button: Button
    lateinit var textView: TextView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var progressBar: ProgressBar
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        textView=findViewById(R.id.textView)
        username=findViewById(R.id.username)
        password=findViewById(R.id.password)
        button=findViewById(R.id.button)
        progressBar=findViewById(R.id.progressBar)
        firebaseAuth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().getReference().child("users")
    }
    public fun see(view: View) {
        startActivity(Intent(this,MainActivity::class.java))
    }
    public fun reg(view: View){
     var email:String
     var pass:String

        email=username.text.toString().trim()
        pass=password.text.toString().trim()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            username.setError("Please enter a valid email")
            username.requestFocus()
            return
        }
        if(pass.length<6){
            password.setError("Password length should be minimum 6")
            password.requestFocus()
            return
        }

        button.isEnabled=false
        progressBar.visibility=View.VISIBLE
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this) { task ->

            button.isEnabled=true
            progressBar.visibility=View.GONE
            if (task.isSuccessful) {
                FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.uid.toString()).child("email").setValue(firebaseAuth.currentUser?.email.toString())
                Toast.makeText(this,"Successfully registered",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
            } else {
                Toast.makeText(this,task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
            }

        }
    }
}
