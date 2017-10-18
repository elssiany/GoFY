package com.dkbrothers.app.gofy.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dkbrothers.app.gofy.R;

/**
 * Created by kens on 12/10/17.
 */

public class UserActivityFragment extends Fragment {

    View rootView=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_user_activity, container, false);

        }

        return rootView;

    }


}