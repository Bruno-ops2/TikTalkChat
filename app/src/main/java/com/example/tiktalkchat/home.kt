package com.example.tiktalkchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.view.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.zip.Inflater

class home : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var listView: ListView
    lateinit var emails:ArrayList<String>
    lateinit var snaps:ArrayList<DataSnapshot>
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        firebaseAuth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().getReference().child("users")
        listView=findViewById(R.id.listView)
        emails= ArrayList()
        val adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,emails)
        listView.adapter=adapter

        databaseReference.addChildEventListener(object:ChildEventListener{
            override fun onCancelled(p0: DatabaseError) { }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                var p2=p0.children
//                p2=p0.children
                p2.forEach{
                    Log.i("p2",it.value.toString())
                    if(!it.value.toString().equals(firebaseAuth.currentUser?.email.toString()))
                    emails.add(it.value.toString())
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {}

        })
       listView.onItemClickListener= AdapterView.OnItemClickListener{ adapterView, view, i, l->

           var intent= Intent(this,chat::class.java)
           intent.putExtra("Title",emails.get(i))
           startActivity(intent)
        }
 }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.menu_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logout){
            firebaseAuth.signOut()
            finish()
            startActivity(Intent(this,MainActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }
}
