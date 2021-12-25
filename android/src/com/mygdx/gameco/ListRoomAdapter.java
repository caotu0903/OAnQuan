package com.mygdx.gameco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListRoomAdapter extends BaseAdapter {

    private List<Room> listRoom;
    private int idLayout;
    private Context context;

    public ListRoomAdapter(Context context, int idLayout, List<Room> listRoom) {
        this.context = context;
        this.idLayout = idLayout;
        this.listRoom = listRoom;
    }

    @Override
    public int getCount() {
        if (listRoom.size() != 0 && !listRoom.isEmpty()) {
            return listRoom.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false);
        }

        Room room = listRoom.get(position);

        ImageView iv_ImageRoom = convertView.findViewById(R.id.iv_icon_item_room);
        TextView tv_RoomName = convertView.findViewById(R.id.tv_roomname_item_room);
        TextView tv_NumberPlayer = convertView.findViewById(R.id.tv_numberplayer_item_room);

        if(listRoom != null && !listRoom.isEmpty()) {
            if(room.getRoomName() != null) {
                tv_RoomName.setText(room.getRoomName());
                tv_RoomName.setSelected(true);
            }

            if(room.getRoomImage() != 0) {
                iv_ImageRoom.setBackground(context.getResources().getDrawable(room.getRoomImage()));
            }

            if(room.getNumberPlayer() != 0) {
                tv_NumberPlayer.setText(room.getNumberPlayer() + "");
            }
        }

        return  convertView;
    }
}
