package com.example.mcbback.sportgo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class eventListAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<eventList> listVieweventList = null;
    private int listCnt = 0;



    public eventListAdapter(ArrayList<eventList> eventdata) {

        listVieweventList = eventdata;
        listCnt = listVieweventList.size();

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
            convertView = inflater.inflate(R.layout.event_list, parent, false);
        }



        TextView listName = (TextView) convertView.findViewById(R.id.list_name);
        TextView listSports = (TextView) convertView.findViewById(R.id.list_sports);
        TextView listDate = (TextView) convertView.findViewById(R.id.list_date);
        TextView listCapacity = (TextView) convertView.findViewById(R.id.event_capacity);


        listName.setText(listVieweventList.get(i).eventName);
        listSports.setText(listVieweventList.get(i).eventSports);
        listDate.setText(listVieweventList.get(i).eventDate);
        listCapacity.setText(listVieweventList.get(i).eventCapacity);
        convertView.setTag(i);
        return convertView;
    }


}
