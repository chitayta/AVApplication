package com.tma.tctay.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tma.tctay.activity.R;
import com.tma.tctay.models.AccessToken;
import com.tma.tctay.models.ListViewSystemItemModel;

import java.util.ArrayList;

/**
 * Created by tctay on 7/5/2017.
 */

public class SystemListViewAdapter extends ArrayAdapter<ListViewSystemItemModel> {
    private final Context context;
    private final ArrayList<ListViewSystemItemModel> modelsArrayList;
    private AccessToken accessToken;
    public SystemListViewAdapter(Context context, ArrayList<ListViewSystemItemModel> modelsArrayList, AccessToken accessToken)
    {
        super(context, R.layout.listview_system_target_item, modelsArrayList);
        this.context = context;
        this.modelsArrayList = modelsArrayList;
        this.accessToken = accessToken;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;

        if(!modelsArrayList.get(position).isHeader()){
            rowView = layoutInflater.inflate(R.layout.listview_system_target_item, parent, false);

            // 3. Get icon,title & counter views from the rowView
            TextView commentView = (TextView) rowView.findViewById(R.id.listview_title);
            TextView titleView = (TextView) rowView.findViewById(R.id.listview_comment);

            titleView.setText(modelsArrayList.get(position).getTitle());
            commentView.setText(modelsArrayList.get(position).getComment());
        }
        else
        {
            rowView = layoutInflater.inflate(R.layout.listview_group_header_item, parent, false);

            TextView headerTextView = (TextView) rowView.findViewById(R.id.listview_header);
            headerTextView.setText(modelsArrayList.get(position).getTitle());
        }
        // 5. retrn rowView
        return rowView;
    }
}
