package com.example.mcbback.sportgo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FacilityAdapter  extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<FacilityList> listViewFacilityList = null;
    private int listCnt = 0;



    public FacilityAdapter(ArrayList<FacilityList> facilitydata) {

        listViewFacilityList = facilitydata;
        listCnt = listViewFacilityList.size();

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
            convertView = inflater.inflate(R.layout.facility_list, parent, false);
        }



        TextView facilityName = (TextView) convertView.findViewById(R.id.facility_name);
        TextView facilityAddress = (TextView) convertView.findViewById(R.id.facility_address);



        facilityName.setText(listViewFacilityList.get(i).list_facilityName);
        facilityAddress.setText(listViewFacilityList.get(i).list_facilityAddress);

        convertView.setTag(i);
        return convertView;
    }


}
