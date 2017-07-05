package com.tma.tctay.tabfragment;

/**
 * Created by tctay on 6/27/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Switch;

import com.tma.tctay.activity.R;
import com.tma.tctay.android.CommandListViewAdapter;
import com.tma.tctay.models.AccessToken;
import com.tma.tctay.models.ListViewCommandItemModel;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class CommandPlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ListView mListView;
    private CommandListViewAdapter mListAdapter;
    private Switch buttonSwitch;
    private AccessToken accessToken;
    private String systemUid;

    public CommandPlaceholderFragment() {
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public void setSystemUid(String systemUid) {
        this.systemUid = systemUid;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CommandPlaceholderFragment newInstance(int sectionNumber, AccessToken accessToken, String systemUid) {
        CommandPlaceholderFragment fragment = new CommandPlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setAccessToken(accessToken);
        fragment.setSystemUid(systemUid);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_command_main_tab_data_view, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstaneState)
    {
        super.onViewCreated(view, savedInstaneState);
        mListView = (ListView) view.findViewById(R.id.command_listView);
        ArrayList<ListViewCommandItemModel> modelArrayList = new ArrayList<ListViewCommandItemModel>();
        //modelArrayList.add(new ListViewCommandItemModel("Commands"));
        modelArrayList.add(new ListViewCommandItemModel(R.drawable.led, "Turn led on/off", R.id.listView_buttonSwitch));

        mListAdapter = new CommandListViewAdapter(getContext(), modelArrayList, accessToken, systemUid);
        mListView.setAdapter(mListAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
}