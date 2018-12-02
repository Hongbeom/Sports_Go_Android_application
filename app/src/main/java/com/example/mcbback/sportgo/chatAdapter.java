package com.example.mcbback.sportgo;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class chatAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<chatList> chatData;
    private LayoutInflater inflater;
    private String id;


    public chatAdapter(Context applicationContext, int chat, ArrayList<chatList> list, String id) {
        this.context = applicationContext;
        this.layout = chat;
        this.chatData = list;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.id= id;
    }

    @Override
    public int getCount() { // 전체 데이터 개수
        return chatData.size();
    }

    @Override
    public Object getItem(int position) { // position번째 아이템
        return chatData.get(position);
    }

    @Override
    public long getItemId(int position) { // position번째 항목의 id
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder;

        if(convertView == null){
//어떤 레이아웃을 만들어 줄 것인지,
            convertView = inflater.inflate(layout, parent, false); //아이디를 가지고 view를 만든다
            holder = new ViewHolder();
            holder.img= (ImageView)convertView.findViewById(R.id.image_message_profile);
            holder.tv_msg = (TextView)convertView.findViewById(R.id.text_message_body);
            holder.tv_name = (TextView)convertView.findViewById(R.id.text_message_name);
            holder.tv_time = (TextView)convertView.findViewById(R.id.text_message_time);
            holder.my_msg = (TextView)convertView.findViewById(R.id.my_message_body);
            holder.my_time = (TextView)convertView.findViewById(R.id.my_message_time);

            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

//누군지 판별
        if(chatData.get(position).getId().equals(id)){
            holder.tv_time.setVisibility(View.GONE);
            holder.tv_name.setVisibility(View.GONE);
            holder.tv_msg.setVisibility(View.GONE);
            holder.img.setVisibility(View.GONE);

            holder.my_msg.setVisibility(View.VISIBLE);
            holder.my_time.setVisibility(View.VISIBLE);

            holder.my_time.setText(chatData.get(position).getTime());
            holder.my_msg.setText(chatData.get(position).getContent());
        }else{
            holder.tv_time.setVisibility(View.VISIBLE);
            holder.tv_name.setVisibility(View.VISIBLE);
            holder.tv_msg.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.VISIBLE);

            holder.my_msg.setVisibility(View.GONE);
            holder.my_time.setVisibility(View.GONE);

            // holder.img.setImageResource(chatData.get(position).getImageID()); // 해당 사람의 프사 가져옴
            holder.tv_msg.setText(chatData.get(position).getContent());
            holder.tv_time.setText(chatData.get(position).getTime());
            holder.tv_name.setText(chatData.get(position).getId());
        }

        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


        return convertView;
    }

    //뷰홀더패턴
    public class ViewHolder{
        ImageView img;
        TextView tv_msg;
        TextView tv_time;
        TextView tv_name;
        TextView my_time;
        TextView my_msg;
    }

}
