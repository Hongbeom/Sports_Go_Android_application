package com.example.mcbback.sportgo;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.PendingIntent.FLAG_CANCEL_CURRENT;

public class CancelEventNotificationService extends Service{
    private int lastShwonNotificationId = -1;
    private DatabaseReference mDatabase;
    private Profile userProfile;
    private FirebaseUser user;
    private int flags_;
    private int startId_;

    @Override
    public void onCreate(){

        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){

            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            this.stopSelf();
        }

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        flags_ = flags;
        startId_ = startId;
        mDatabase.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userProfile = dataSnapshot.getValue(Profile.class);
                mDatabase.child("event").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        Event removed = dataSnapshot.getValue(Event.class);
                        if(removed.holder.equals(userProfile.eventInfo)&& user.getUid()!=removed.holder){
                            final NotificationCompat.Builder builder = getNotificationBuilder(CancelEventNotificationService.this, "com.example.mcbback.sportgo.CHANNEL_ID_CANCELEVENT",
                                    NotificationManagerCompat.IMPORTANCE_LOW);
                            Intent intent = new Intent(getApplicationContext(), Category.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra("stopValue", 0);
                            PendingIntent pi = PendingIntent.getActivity(CancelEventNotificationService.this, 0, intent, FLAG_CANCEL_CURRENT);
                            builder.setSmallIcon(R.drawable.sports_go)
                                    .setAutoCancel(true)
                                    .setContentTitle(removed.eventName+" has been canceled")
                                    .setContentText("Your event has been canceled. Please check")
                                    .setContentIntent(pi);
                            Toast.makeText(CancelEventNotificationService.this, "ㅁㅁㅁㅁㅁ", Toast.LENGTH_SHORT).show();
                            Notification notification = builder.build();
                            notification.flags = Notification.FLAG_AUTO_CANCEL;
                            startForeground(startId_, notification);



                            if(startId_ != lastShwonNotificationId){
                                final NotificationManager nm = (NotificationManager) getSystemService(Activity.NOTIFICATION_SERVICE);
                                nm.cancel(lastShwonNotificationId);
                            }
                            lastShwonNotificationId = startId_;
                            Toast.makeText(CancelEventNotificationService.this, "?????", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        return START_STICKY;
    }

    public static NotificationCompat.Builder getNotificationBuilder(Context context, String channelId, int importance){
        NotificationCompat.Builder builder;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            prepareChannel(context, channelId, importance);
            builder = new NotificationCompat.Builder(context, channelId);
        } else{
            builder = new NotificationCompat.Builder(context);
        }
        return builder;
    }

    private static void prepareChannel(Context context, String id, int importance){
        final String appName = context.getString(R.string.app_name);
        String description = context.getString(R.string.deletion_notification);
        final NotificationManager nm = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);

        if(nm != null){
            NotificationChannel nChannel = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                nChannel = nm.getNotificationChannel(id);
            }

            if(nChannel == null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    nChannel = new NotificationChannel(id, appName, importance);
                    nChannel.setDescription(description);
                    nm.createNotificationChannel(nChannel);
                }

            }
        }
    }
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

}
