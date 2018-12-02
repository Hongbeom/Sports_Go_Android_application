package com.example.mcbback.sportgo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Category extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button logoutBtn;
    private Button searchBtn;
    private Button createBtn;
    private Button myeventBtn;
    private DatabaseReference mDatabase;
    private Profile userProfile;
    private Event event;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(Category.this, "You should Log-in First.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        if(getIntent()!=null){
            stopService(new Intent(getApplicationContext(), CancelEventNotificationService.class));
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();



        logoutBtn = (Button) findViewById(R.id.userLogOut);
        searchBtn = (Button) findViewById(R.id.searchEvent);
        createBtn = (Button) findViewById(R.id.createEvent);
        myeventBtn = (Button) findViewById(R.id.myEvent);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                userProfile = dataSnapshot.getValue(Profile.class);
                                Intent intent = new Intent(getApplicationContext(), searchEvent.class);
                                intent.putExtra("userProfile", userProfile);
                                startActivity(intent);


                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(Category.this, "db error! -- get user information error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        myeventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadUserProfile().execute();

            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                userProfile = dataSnapshot.getValue(Profile.class);
                                if(userProfile.eventInfo.length()>0){
                                    Toast.makeText(Category.this, "You already has an sports Event!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Intent intent = new Intent(getApplicationContext(), createEvent.class);
                                intent.putExtra("userProfile", userProfile);
                                startActivity(intent);


                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(Category.this, "db error! -- get user information error", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Category.this);
                builder.setTitle("Alert");
                builder.setMessage("Are you sure to Log-out?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        stopService(new Intent(getApplicationContext(), CancelEventNotificationService.class));
                        stopService(new Intent(getApplicationContext(), AlarmService.class));
                        stopService(new Intent(getApplicationContext(), MatchPlanService.class));

                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        Category.this.finish();
                    }
                });
                builder.show();
            }
        });
    }


    @Override
    public void onBackPressed(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert");
            builder.setMessage("Are you sure to exit?");
            builder.setNegativeButton("No", null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                }
            });
            builder.show();
        }

        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_info) {
            mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            userProfile = dataSnapshot.getValue(Profile.class);
                            Intent intent = new Intent(getApplicationContext(), MyInfo.class);
                            intent.putExtra("userProfile", userProfile);
                            startActivity(intent);


                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Category.this, "db error! -- get user information error", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (id == R.id.nav_around_event_alarm) {
            mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            userProfile = dataSnapshot.getValue(Profile.class);
                            Intent intent = new Intent(getApplicationContext(), AlarmEvent.class);
                            intent.putExtra("userProfile", userProfile);
                            startActivity(intent);


                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Category.this, "db error! -- get user information error", Toast.LENGTH_SHORT).show();
                        }
                    });


        }  else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Category.this);
            builder.setTitle("Alert");
            builder.setMessage("Are you sure to Log-out?");
            builder.setNegativeButton("No", null);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    stopService(new Intent(getApplicationContext(), CancelEventNotificationService.class));
                    Category.this.finish();
                }
            });
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

private class LoadUserProfile extends AsyncTask<Void, Void, Void> {

    ProgressDialog asyncDialog = new ProgressDialog(
            Category.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("Please wait...");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... args) {
            try {

                mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                userProfile = dataSnapshot.getValue(Profile.class);
                                if(userProfile.eventInfo.length()==0){
                                    Toast.makeText(Category.this,"You don't have any sports event",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                mDatabase.child("event").child(userProfile.eventInfo).addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                // Get user value
                                                Event event = dataSnapshot.getValue(Event.class);
                                                Intent intent = new Intent(getApplicationContext(), myEvent.class);
                                                intent.putExtra("userProfile", userProfile);
                                                intent.putExtra("userEvent", event);
                                                startActivity(intent);


                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                Toast.makeText(Category.this, "db error! -- get user information error", Toast.LENGTH_SHORT).show();
                                            }
                                        });



                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(Category.this, "db error! -- get user information error", Toast.LENGTH_SHORT).show();
                            }
                        });
            } catch (Exception e) {
                Toast.makeText(Category.this,""+e,Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
            super.onPostExecute(result);
        }
    }
}