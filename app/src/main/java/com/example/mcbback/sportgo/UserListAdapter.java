package com.example.mcbback.sportgo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class UserListAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<UserList> listViewUserList = null;
    private int listCnt = 0;



    public UserListAdapter(ArrayList<UserList> userdata) {

        listViewUserList = userdata;
        listCnt = listViewUserList.size();

    }

    @Override
    public int getCount() {

        Log.i("TAG", "getCount");
        return listCnt;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        if (convertView == null) {

            final Context context = parent.getContext();
            if(inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.user_list, parent, false);
        }



        TextView userName = (TextView) convertView.findViewById(R.id.userlist_name);
        TextView userEmail = (TextView) convertView.findViewById(R.id.userlist_email);
        TextView userSex = (TextView) convertView.findViewById(R.id.userlist_sex);
        TextView userReliability = (TextView) convertView.findViewById(R.id.userlist_reliability);


        userName.setText(listViewUserList.get(i).list_userName);
        userEmail.setText(listViewUserList.get(i).list_userEmail);
        userSex.setText(listViewUserList.get(i).list_userSex);
        userReliability.setText(listViewUserList.get(i).list_userReliability);
        convertView.setTag(i);
        return convertView;
    }


}
