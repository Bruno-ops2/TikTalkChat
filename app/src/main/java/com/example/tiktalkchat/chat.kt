package com.example.tiktalkchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.constraintlayout.solver.widgets.Snapshot
import com.google.firebase.FirebaseError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class chat : AppCompatActivity() {

    lateinit var chatList:ListView
    lateinit var editText: EditText
    lateinit var imageButton: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var databaseReference: DatabaseReference
    lateinit var databaseReference2: DatabaseReference
    lateinit var databaseReference3: DatabaseReference

    lateinit var str:String
    lateinit var baatein:ArrayList<String>
    var chatz:String?=null
    var i:Int?=0
    var k:Int?=0
    var l:Int?=0
    lateinit var adapter:ArrayAdapter<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatList=findViewById(R.id.chatList)
        editText=findViewById(R.id.editText)
        imageButton=findViewById(R.id.button3)
        str=intent.getStringExtra("Title")
        setTitle(str)
        firebaseAuth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().getReference().child("chats")
//        hashCode(firebaseAuth.currentUser?.email.toString())
        baatein= ArrayList()
        adapter= ArrayAdapter(this,android.R.layout.simple_list_item_1,baatein)
        chatList.adapter=adapter


        databaseReference.addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                if(p0.key.equals(hash(str + ":" + firebaseAuth.currentUser?.email.toString()))!! || p0.key?.equals(hash(firebaseAuth.currentUser?.email.toString() + ":" + str))!!) {
                    Log.i("p000",p0.toString())
                    databaseReference2=databaseReference.child(p0.key.toString())
                    databaseReference2.addChildEventListener(object : ChildEventListener {
                        override fun onCancelled(p0: DatabaseError) {}
                        override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
                        override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
                        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                            Log.i("meowww", p0.toString())
                            databaseReference2.child(p0.key.toString())
                                .addChildEventListener(object : ChildEventListener{
                                    override fun onCancelled(p0: DatabaseError) {}
                                    override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
                                    override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
                                    override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                                        Log.i("chaichai",p0.toString())
                                        if(!baatein.contains(p0.key.toString())) {
                                            baatein.add(p0.value.toString()+"    :   "+p0.key.toString())
                                            adapter.notifyDataSetChanged()
                                        }
                                    }

                                    override fun onChildRemoved(p0: DataSnapshot) {}

                                })
                        }

                        override fun onChildRemoved(p0: DataSnapshot) {}
                    })

                    baatein.clear()
                }
            }
            override fun onChildRemoved(p0: DataSnapshot) {}

        })

    }

    fun send(view: View) {
        var msg = editText.text.toString()
        chatz = str + ":" + firebaseAuth.currentUser?.email.toString()

        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {

                if (p0.value?.equals(hash(str + ":" + firebaseAuth.currentUser?.email.toString()))!! || p0.value?.equals(hash(
                        firebaseAuth.currentUser?.email.toString() + ":" + str)
                    )!! && i == 0
                ) {
                    ivalchange(1, 1)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {}

        })

        databaseReference.addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildRemoved(p0: DataSnapshot) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                see()
            }
        })
        databaseReference.addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                if(p0.key.equals(hash(str + ":" + firebaseAuth.currentUser?.email.toString()))!! || p0.key?.equals(hash(firebaseAuth.currentUser?.email.toString() + ":" + str))!!){

                    databaseReference.child(p0.key.toString()).push().child(msg).setValue(firebaseAuth.currentUser?.email.toString())

                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {}
        })

    }

    fun ivalchange(k:Int, j: Int? =this.k){
        this.i=k
        this.k=j
        Log.i("i=",i.toString())
        Log.i("k=",k.toString())
    }
    fun see(){
        if(i==0&&k==0){
            databaseReference.child(hash(chatz.toString())).setValue("")
            Log.i("see i=",i.toString())
            Log.i("see k=",k.toString())
            i=1
            k=1

        }
    }

    fun hash(str:String): String {
        var hashCode:String=""
        for(x in str){
            hashCode+=x.toInt().toString()
        }
//        Log.i("hashCode is",hashCode)
        return hashCode
    }
}
