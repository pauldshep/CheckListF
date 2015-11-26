package com.sss.android.checklistf;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListItemDlgFragment} interface
 * to handle interaction events.
 * Use the {@link ListItemDlgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListItemDlgFragment extends DialogFragment
{
    private final static String TAG = "ListItemDlgFragment";

    // Parameter variables
    private CheckListItem mCheckListItem;
    private Button        mButtonUse;
    private Button        mButtonAbort;

    // system variables
    private Context                         mContext;
    private OnCheckListItemEditedListener   mOnEditedListener;


    //==========================================================================
    /**
     * This internal class interface must be implemented by the container
     * activity
     */
    public interface OnCheckListItemEditedListener
    {
        public void onCheckListItemEdited(CheckListItem checkListItem);
    }


    //==========================================================================
    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided check list item parameters.
     *
     * @param checkListItem check list item information to display/edit
     *
     * @return A new instance of fragment ListItemDlgFragment.
     */
    public static ListItemDlgFragment newInstance(CheckListItem checkListItem)
    {
        ListItemDlgFragment fragment = new ListItemDlgFragment();
        Bundle              args     = checkListItem.buildBundle();
        fragment.setArguments(args);
        return fragment;
    }


    //==========================================================================
    /**
     * Constructor: required empty public constructor
     */
    public ListItemDlgFragment() {}


    //==========================================================================
    /**
     * Called when the fragment is created
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);



        // get the check list item to display from savedInstatnceState
        if(getArguments() != null)
        {
            mCheckListItem = new CheckListItem(savedInstanceState);
        }
        else
        {
            // SNO
            mCheckListItem = new CheckListItem();
        }

        int style = DialogFragment.STYLE_NO_FRAME, theme = 0;
        theme     = android.R.style.Theme_Holo_Light;
        setStyle(style, theme);
    }


    //==========================================================================
    /**
     * Builds Fragment View for display by the application.  Called when it's
     * time for the fragment to draw its user interface for the first time.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return initialized fragment view for display
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup      container,
                             Bundle         savedInstanceState)
    {
        Log.d(TAG, "onCreateView(): CheckListItem = " + mCheckListItem.toString());
        // Inflate the layout for this fragment
        mContext  = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_list_item, container, false);

        // set list item title
        EditText item_title = (EditText)view.findViewById(R.id.editTextTitle);
        item_title.setText(mCheckListItem.mTitle);

        // set list item description
        EditText item_desc = (EditText)view.findViewById(R.id.editTextDesc);
        item_desc.setText(mCheckListItem.mDesc);

        // set list item checked
        CheckBox item_checked = (CheckBox)view.findViewById(R.id.checkBoxChecked);
        item_checked.setChecked(mCheckListItem.mChecked);

        // set list item Units
        EditText item_units = (EditText)view.findViewById(R.id.editTextUnits);
        item_units.setText(mCheckListItem.mCheckUnits);

        // set list item check interval
        EditText item_interval = (EditText)view.findViewById(R.id.editTextInterval);
        item_interval.setText(mCheckListItem.mCheckInterval.toString());

        // set list item check interval stamp
        EditText item_stamp = (EditText)view.findViewById(R.id.editTextIntervalStamp);
        item_stamp.setText(mCheckListItem.mCheckStamp.toString());

        // set list item checked time stamp
        EditText item_time = (EditText)view.findViewById(R.id.editTextTimeStamp);
        item_time.setText(new Date(mCheckListItem.mCheckTime).toString());

        //----------------------------------------------------------------------
        // setup message handlers
        //----------------------------------------------------------------------
        mButtonUse = (Button)view.findViewById(R.id.buttonListItemUse);
        mButtonUse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "ButtonUse.onClick()");
                mOnEditedListener.onCheckListItemEdited(mCheckListItem);
                dismiss();
            }
        });

        mButtonAbort = (Button)view.findViewById(R.id.buttonListItemAbort);
        mButtonAbort.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "ButtonAbort.onClick()");
                dismiss();
            }
        });

        return view;
    }  // end public View onCreateView(...

    //==========================================================================
    /**
     * Called once the fragment is associated with its activity.  Note that
     * Activity is a subclass of Context
     *
     * @param context
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");

        try
        {
            mOnEditedListener = (OnCheckListItemEditedListener)context;
        }
        catch(ClassCastException cce)
        {
            throw new ClassCastException(context.toString() +
                    " must implement OnCheckListItemEditedListener");
        }
    }


    //==========================================================================
    /**
     * Called as the first indication that the user is leaving the fragment.
     * Changes to the CheckListItem are comitted here?
     */
    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause()");

    }  // end public void onPause()



//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri)
//    {
//        if(mListener != null)
//        {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Activity activity)
//    {
//        super.onAttach(activity);
//        try
//        {
//            mListener = (OnFragmentInteractionListener)activity;
//        } catch(ClassCastException e)
//        {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach()
//    {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener
//    {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

}
