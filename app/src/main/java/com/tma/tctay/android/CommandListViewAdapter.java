package com.tma.tctay.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tma.tctay.activity.R;
import com.tma.tctay.asynctask.SendCommandToDeviceTask;
import com.tma.tctay.models.AccessToken;
import com.tma.tctay.models.ListViewCommandItemModel;
import com.tma.tctay.utility.NetworkUtility;

import java.util.ArrayList;


/**
 * Created by tctay on 6/28/2017.
 */

public class CommandListViewAdapter extends ArrayAdapter<ListViewCommandItemModel>{
    private final Context context;
    private final ArrayList<ListViewCommandItemModel> modelsArrayList;
    private AccessToken accessToken;
    private String systemUid;
    public CommandListViewAdapter(Context context, ArrayList<ListViewCommandItemModel> modelsArrayList, AccessToken accessToken, String systemUid)
    {
        super(context, R.layout.listview_command_target_item,modelsArrayList);
        this.context = context;
        this.modelsArrayList = modelsArrayList;
        this.accessToken = accessToken;
        this.systemUid = systemUid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;

        if(!modelsArrayList.get(position).isHeader()){
            rowView = layoutInflater.inflate(R.layout.listview_command_target_item, parent, false);

            // 3. Get icon,title & counter views from the rowView
            ImageView imgView = (ImageView) rowView.findViewById(R.id.listView_imageView);
            TextView titleView = (TextView) rowView.findViewById(R.id.listView_commandTextView);
            Switch buttonSwitch = (Switch) rowView.findViewById(R.id.listView_buttonSwitch);

            // 4. Set the text for textView
            imgView.setImageResource(modelsArrayList.get(position).getIcon());
            titleView.setText(modelsArrayList.get(position).getTitle());
            buttonSwitch.setChecked(true);
            buttonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    System.out.println("Command Turn LED: " + b);
                    if (NetworkUtility.isNetworkAvailable(getContext())) {
                        if (b) {
                            System.out.println("Turned on!");
                            new SendCommandToDeviceTask(systemUid, accessToken, b, getContext()).execute();
                        } else {
                            System.out.println("Turned off!");
                            new SendCommandToDeviceTask(systemUid, accessToken, b, getContext()).execute();
                        }
                    }
                    else
                    {
                        buttonSwitch.setChecked(!b);
                        Toast.makeText(getContext(), "No network connection!",Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            rowView = layoutInflater.inflate(R.layout.listview_group_header_item, parent, false);
            TextView titleView = (TextView) rowView.findViewById(R.id.listview_header);
            titleView.setText(modelsArrayList.get(position).getTitle());

        }

        // 5. retrn rowView
        return rowView;
    }
}
