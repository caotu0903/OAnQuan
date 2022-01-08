package com.mygdx.gameco;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ListRankAdapter extends BaseAdapter {

    private List<PlayerRankItem> listPlayerRank;
    private int idLayout;
    private Context context;

    public ListRankAdapter(Context context, int idLayout, List<PlayerRankItem> listPlayerRank) {
        this.listPlayerRank = listPlayerRank;
        this.idLayout = idLayout;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (listPlayerRank.size() != 0 && !listPlayerRank.isEmpty()) {
            return listPlayerRank.size();
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

        PlayerRankItem playerRankItem = listPlayerRank.get(position);

        TextView tv_Rank = convertView.findViewById(R.id.tv_rank_A_item_rank);
        TextView tv_PlayerName = convertView.findViewById(R.id.tv_player_name_A_item_rank);
        TextView tv_Point = convertView.findViewById(R.id.tv_point_A_item_rank);

        if(listPlayerRank != null && !listPlayerRank.isEmpty()) {
            if(playerRankItem.getRank() != 0) {
                tv_Rank.setText(playerRankItem.getRank() + "");
                tv_Rank.setSelected(true);
            }

            if(playerRankItem.getName() != null) {
                tv_PlayerName.setText(playerRankItem.getName());
                tv_PlayerName.setSelected(true);
            }

            tv_Point.setText(playerRankItem.getPoint() + "");
            tv_Point.setSelected(true);
        }

        return convertView;
    }
}
