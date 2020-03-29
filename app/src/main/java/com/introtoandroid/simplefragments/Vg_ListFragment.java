package com.introtoandroid.simplefragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Let’s begin by defining a custom Vg_ListFragment class called Vg_ListFragment to host
 * our fruit and vegetable names. This class will need to determine whether the second Fragment,
 * the Vg_WebViewFragment, should be loaded or if ListView clicks should simply cause
 * the Vg_ViewActivity_portrait to be launched:*/
public class Vg_ListFragment extends android.app.ListFragment implements
        FragmentManager.OnBackStackChangedListener{

    private static final String DEBUG_TAG = "VGListFragment";
    int mCurPosition = -1;
    boolean mShowTwoFragments;

    /**Most of the Fragment control’s initialization happens in the onActivityCreated() callback method so that we initialize the ListView only once. We then check to see which display mode we want to be in by checking to see if our second component is defined in the layout. Finally, we leave the display details to the helper method called viewVeggieInfo(), which is also called whenever an item in the ListView control is clicked*/
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);


        // Populate our ListView control within the Fragment
        String[] veggies = getResources().getStringArray(
                R.array.veggies_array);
        setListAdapter(new ArrayAdapter<>(getActivity(), //MainActivity.this
                android.R.layout.simple_list_item_activated_1, veggies));

        Log.i("***********", ""+ getActivity().toString());


        if (savedInstanceState != null) {  //Henter Activ-item sin posisjon fra savedInstanceState ,, se linje 121 onSaveInstanceState()
            mCurPosition = savedInstanceState.getInt("curChoice", 0);
        }

        // Check which state we're in
        View detailsFrame = getActivity().findViewById(R.id.Vg_WebViewFragment_entry);
        mShowTwoFragments = detailsFrame != null
                && detailsFrame.getVisibility() == View.VISIBLE;

        if (mShowTwoFragments || mCurPosition != -1) {
            // Set the initial url to our default blog post, or the last one shown
            viewVeggieInfo(mCurPosition);
        }

        // monitor back stack changes to update list view
        getFragmentManager().addOnBackStackChangedListener(this);

    }

    @Override
    public void onBackStackChanged() {
        // update position
        Vg_WebViewFragment details = (Vg_WebViewFragment) getFragmentManager()
                .findFragmentById(R.id.Vg_WebViewFragment_entry);
        if (details != null) {
            mCurPosition = details.getShownIndex();
            getListView().setItemChecked(mCurPosition, true);

            // if we're in single pane, then we need to switch forward to the viewer
            if (!mShowTwoFragments) {
                viewVeggieInfo(mCurPosition);
            }
        }
    }

    void viewVeggieInfo(int index) {
        mCurPosition = index;
        if (mShowTwoFragments) {
            // Check what fragment is currently shown, replace if needed.
            Vg_WebViewFragment vg_fragment_landscape = (Vg_WebViewFragment) getFragmentManager()
                    .findFragmentById(R.id.Vg_WebViewFragment_entry);
            if (vg_fragment_landscape == null || vg_fragment_landscape.getShownIndex() != index) {

                // Make new fragment to show this selection.
                Vg_WebViewFragment new_vg_fragment_landscape = Vg_WebViewFragment
                        .newInstance(index);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.Vg_WebViewFragment_entry, new_vg_fragment_landscape);
                // Add this fragment instance to the back-stack of the Activity
                // so we can backtrack through our veggies
                if (index != -1) {
                    String[] veggies = getResources().getStringArray(
                            R.array.veggies_array);
                    String strBackStackTagName = veggies[index];
                    ft.addToBackStack(strBackStackTagName);
                }
                // Fade between Urls
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), Vg_ViewActivity_portrait.class);
            intent.putExtra("index", index);
            startActivity(intent);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        viewVeggieInfo(position);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("curChoice", mCurPosition);
    }

























    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "FRAGMENT LIFECYCLE EVENT: onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(DEBUG_TAG, "FRAGMENT LIFECYCLE EVENT: onStart()");
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity) {
        Log.d(DEBUG_TAG, "FRAGMENT LIFECYCLE EVENT: onAttach()");
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        Log.d(DEBUG_TAG, "FRAGMENT LIFECYCLE EVENT: onDetach()");
        super.onDetach();
    }

    @Override
    public void onPause() {
        Log.d(DEBUG_TAG, "FRAGMENT LIFECYCLE EVENT: onPause()");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.d(DEBUG_TAG, "FRAGMENT LIFECYCLE EVENT: onResume(): "
                + mCurPosition);
        super.onResume();
    }

    @Override
    public void onStop() {
        Log.d(DEBUG_TAG, "FRAGMENT LIFECYCLE EVENT: onStop()");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG, "FRAGMENT LIFECYCLE EVENT: onDestroy()");
        super.onDestroy();
    }

}
