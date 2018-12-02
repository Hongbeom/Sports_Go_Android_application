package com.example.mcbback.sportgo;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class chatmain extends AppCompatActivity {

    ArrayList<chatList> list = new ArrayList<>();
    ListView lv;
    Button btn;
    EditText edt;

    private DatabaseReference mdatabase;
    private FirebaseUser user;
    private Profile userProfile;
    private Event event;


    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(chatmain.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        Intent intent = getIntent();
        userProfile = (Profile) intent.getSerializableExtra("userProfile");
        event = (Event) intent.getSerializableExtra("userEvent");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_list);

        lv = findViewById(R.id.message_list);
        edt = findViewById(R.id.edit_chat);
        btn = findViewById(R.id.button_send);

// Write a message to the database
        mdatabase = FirebaseDatabase.getInstance().getReference();


//로그인한 아이디
        id = userProfile.name;
        final chatAdapter adapter = new chatAdapter(getApplicationContext(), R.layout.chat, list, id);
        ((ListView) findViewById(R.id.message_list)).setAdapter(adapter);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edt.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "내용을 입력하세요.", Toast.LENGTH_LONG).show();
                } else {
                    Date today = new Date();
                    SimpleDateFormat timeNow = new SimpleDateFormat("a K:mm");

                    StringBuffer sb = new StringBuffer(edt.getText().toString());
                    if (sb.length() >= 15) {
                        for (int i = 1; i <= sb.length() / 25; i++) {
                            sb.insert(15 * i, "\n");
                        }
                    }

                    mdatabase.child("chat").child(event.holder).push().setValue(new chatList(id, sb.toString(), timeNow.format(today)));
                    edt.setText("");

                }
            }
        });

        mdatabase.child("chat").child(event.holder).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                chatList value = dataSnapshot.getValue(chatList.class);
                list.add(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
