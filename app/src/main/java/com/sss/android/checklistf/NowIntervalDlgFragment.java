package com.sss.android.checklistf;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NowIntervalDlgFragment.OnIntervalNowEditedListener} interface
 * to handle interaction events.
 * Use the {@link NowIntervalDlgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NowIntervalDlgFragment extends DialogFragment
{
    private static final String TAG = "NowIntervalDlgFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INTERVAL_UNITS = "interval_units";
    private static final String INTERVAL_VALUE = "interval_value";

    // dialog data
    private String  mIntervalUnits;
    private Integer mIntervalValue;

    // access to dialog UI elements
    private TextView mTextViewIntervalUnits;
    private EditText mEditTextIntervalValue;
    private Button   mButtonIntervalUse;
    private Button   mButtonIntervalAbort;

    // fragment system variables
    private OnIntervalNowEditedListener mOnIntervalNowEditedListener;
    private Context                     mContext;


    //==========================================================================
    /**
     * Constructor: required empty
     */
    public NowIntervalDlgFragment()
    {
        // Required empty public constructor
    }


    //==========================================================================
    /**
     * Use this factory method to create a new instance of this fragment
     * using the provided parameters.
     *
     * @param intervalUnits
     * @param intervalValue
     *
     * @return A new instance of fragment NowIntervalDlgFragment.
     */
    public static NowIntervalDlgFragment newInstance(String intervalUnits, int intervalValue)
    {
        NowIntervalDlgFragment fragment = new NowIntervalDlgFragment();
        Bundle args = new Bundle();
        args.putString(INTERVAL_UNITS, intervalUnits);
        args.putInt(   INTERVAL_VALUE, intervalValue);
        fragment.setArguments(args);
        return fragment;
    }


    //==========================================================================
    /**
     * Called when fragment is created
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {
            mIntervalUnits = getArguments().getString(INTERVAL_UNITS);
            mIntervalValue = getArguments().getInt(INTERVAL_VALUE);
        }

        Log.d(TAG, "onCreate: units = " + mIntervalUnits + ", value = " + mIntervalValue);

        // set dialog style and theme
        int style = DialogFragment.STYLE_NO_FRAME, theme = 0;
        theme     = android.R.style.Theme_Holo_Light;
        setStyle(style, theme);
    }


    //==========================================================================
    /**
     * Creates Fragment view to display, obtains access to various UI elements,
     * and creates message handlers.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return view for display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        mContext  = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_now_interval_dlg, container, false);

        // get access to UI components
        mTextViewIntervalUnits = (TextView)view.findViewById(R.id.textViewIntervalUnits);
        mEditTextIntervalValue = (EditText)view.findViewById(R.id.editTextIntervalValue);
        mButtonIntervalAbort   = (Button)view.findViewById(R.id.buttonIntervalAbort);
        mButtonIntervalUse     = (Button)view.findViewById(R.id.buttonIntervalUse);

        // set UI initial values
        mTextViewIntervalUnits.setText(mIntervalUnits);
        mEditTextIntervalValue.setText(mIntervalValue.toString());

        // Handle the Abort button
        mButtonIntervalAbort.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick(): abort button");
                dismiss();
            }
        });


        // Handle the Use Button
        mButtonIntervalUse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick(): use button");
                mIntervalValue = Integer.parseInt(mEditTextIntervalValue.getText().toString());
                mOnIntervalNowEditedListener.onIntervalNowEdited(mIntervalValue);
                dismiss();
            }
        });

        return view;
    }  // end public View onCreateView(


    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if(context instanceof OnIntervalNowEditedListener)
        {
            mOnIntervalNowEditedListener = (OnIntervalNowEditedListener)context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mOnIntervalNowEditedListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnIntervalNowEditedListener
    {
        void onIntervalNowEdited(int intervalNow);
    }
}
