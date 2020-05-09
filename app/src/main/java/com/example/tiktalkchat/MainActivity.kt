package com.example.tiktalkchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var username:EditText
    lateinit var password:EditText
    lateinit var button:Button
    lateinit var textView: TextView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView=findViewById(R.id.textView)
        username=findViewById(R.id.username)
        password=findViewById(R.id.password)
        button=findViewById(R.id.button)
        progressBar=findViewById(R.id.progressBar)
        firebaseAuth= FirebaseAuth.getInstance()


    }
    public fun see(view:View) {
        startActivity(Intent(this,register::class.java))
    }

    override fun onStart() {
        super.onStart()
        if(firebaseAuth.currentUser!=null){
            finish()
            startActivity(Intent(this,home::class.java))
        }
    }

    public fun reg(view: View){
        var email:String
        var pass:String

        email=username.text.toString().trim()
        pass=password.text.toString().trim()

        button.isEnabled=false
        progressBar.visibility=View.VISIBLE
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(this) { task ->

            button.isEnabled=true
            progressBar.visibility=View.GONE
            if (task.isSuccessful) {
                Toast.makeText(this,"login successful",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,home::class.java))
            } else {
                Toast.makeText(this,task.exception?.message.toString(),Toast.LENGTH_SHORT).show()
            }

        }
    }
}
