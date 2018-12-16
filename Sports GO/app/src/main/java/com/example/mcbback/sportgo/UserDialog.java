package com.example.mcbback.sportgo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDialog extends Dialog {
    TextView mDialogEmail;
    TextView mDialogName;
    TextView mDialogSex;
    TextView mDialogRegion;
    TextView mDialogEvaluation;
    Profile newUser;
    Profile userProfile;
    Button btnEval;
    Button btnCanel;
    RatingBar ratingBar;

    float score = -1;
    private DatabaseReference mDatabase;

    public UserDialog(final Context context, final UserList userinfo, Profile user, final String holder) {
        super(context);
        userProfile = user;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.userdialog);
        btnEval = (Button) findViewById(R.id.btn_deval);
        btnCanel = (Button) findViewById(R.id.btn_dcancel);
        mDialogEmail = (TextView) findViewById(R.id.dialog_email);
        mDialogName = (TextView) findViewById(R.id.dialog_name);
        mDialogSex = (TextView) findViewById(R.id.dialog_sex);
        mDialogRegion = (TextView) findViewById(R.id.dialog_region);
        mDialogEvaluation = (TextView) findViewById(R.id.dialog_evaluation);
        ratingBar = (RatingBar)findViewById(R.id.dialog_rating);


        mDialogEmail.setText(userinfo.list_userEmail);
        mDialogName.setText(userinfo.list_userName);
        mDialogSex.setText(userinfo.list_userSex);
        mDialogRegion.setText(userinfo.list_userCity+"-"+userinfo.list_userGu);
        mDialogEvaluation.setText(""+Math.round(Float.parseFloat(userinfo.list_userReliability)*10f)/10f);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                score = rating;
            }
        });

        btnCanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();

            }
        });

        btnEval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userProfile.email.equals(userinfo.list_userEmail)){
                    Toast.makeText(context, "You cannot evaluate yourself!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(score == -1){
                    Toast.makeText(context, R.string.eval_warning, Toast.LENGTH_SHORT).show();
                    return;
                }
                mDatabase.child("eval").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Eval tmp = snapshot.getValue(Eval.class);
                            if(tmp.email.equals(userProfile.email) && tmp.evaled_email.equals(userinfo.list_userEmail) && tmp.holder.equals(holder)){
                                Toast.makeText(context, "You can not evaluate twice user in one event!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                    Profile tmp = snapshot.getValue(Profile.class);
                                    if(tmp.email.equals(userinfo.list_userEmail) ){
                                        tmp.eval(score);
                                        mDatabase.child("users").child(snapshot.getKey()).setValue(tmp);
                                        mDatabase.child("eval").push().setValue(new Eval(userProfile.email, holder, userinfo.list_userEmail));
                                        newUser=tmp;
                                        break;
                                    }

                                }
                                Toast.makeText(context, "Evaluation completed!", Toast.LENGTH_SHORT).show();
                                dismiss();
                                UserListMain userList = (UserListMain) UserListMain.UserListActivity;
                                userList.restartUserList(userProfile);
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


            }
        });

    }

}
