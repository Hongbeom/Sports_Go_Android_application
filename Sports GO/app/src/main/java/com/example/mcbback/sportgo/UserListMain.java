package com.example.mcbback.sportgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserListMain extends AppCompatActivity {
    public static Activity UserListActivity;
    private ListView mUserView = null;
    private Button btnback;
    private FirebaseUser user;
    private Profile userProfile;
    private Event event;
    private DatabaseReference mDatabase;
    private ArrayList<UserList> userdata;
    private UserListAdapter adapter;
    private String[] user_array;
    UserDialog ud;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(UserListMain.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        super.onCreate(savedInstanceState);
        UserListActivity = UserListMain.this;
        setContentView(R.layout.fragment_user);
        Intent intent = getIntent();
        userProfile = (Profile)intent.getSerializableExtra("userProfile");
        event = (Event)intent.getSerializableExtra("userEvent");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnback = (Button) findViewById(R.id.back_userlist);
        userdata = new ArrayList<>();
        user_array = event.users.toArray(new String[event.users.size()]);




        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Profile user_db = snapshot.getValue(Profile.class);
                    for(String u_email: user_array){
                        if(u_email.equals(user_db.email)){
                            UserList u_list = new UserList(user_db.name, user_db.email, user_db.sex, ""+Math.round(user_db.reliability*10f)/10f, user_db.city, user_db.gu);
                            userdata.add(u_list);
                            break;
                        }
                    }

                }
                mUserView = (ListView)findViewById(R.id.userListView);
                adapter = new UserListAdapter(userdata);
                mUserView.setAdapter(adapter);



                mUserView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        showUserInfo(userdata.get(position));
                    }
                });
                /*
                mUserView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        UserList list = userdata.get(position);
                        showUserInfo(list.list_userEmail);

                    }
                });
                */
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showUserInfo(UserList userinfo){
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비
        int height = dm.heightPixels; //디바이스 화면 높이
        ud = new UserDialog(UserListMain.this, userinfo, userProfile, event.holder);
        WindowManager.LayoutParams wm = ud.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(ud.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width-100; //화면 너비의 절반
        wm.height = height;  //화면 높이의 절반
        ud.show();

    }

    public void restartUserList(Profile user){

        Intent intent = new Intent(getApplicationContext(), UserListMain.class);
        intent.putExtra("userProfile", user);
        intent.putExtra("userEvent", this.event);
        finish();
        startActivity(intent);
    }


}