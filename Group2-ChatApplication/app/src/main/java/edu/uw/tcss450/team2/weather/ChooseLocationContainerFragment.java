package edu.uw.tcss450.team2.weather;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.team2.R;

/**
 * A fragment containing the choose location fragment plus a zip code enter box
 *
 * @author Sam Spillers
 * @version 1.0
 */
public class ChooseLocationContainerFragment extends Fragment {

    public ChooseLocationContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_location_container, container, false);
    }
}