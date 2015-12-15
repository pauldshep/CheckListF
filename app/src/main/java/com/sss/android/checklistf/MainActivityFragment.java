/*******************************************************************************
 * Copyright (c) 2015 Shepherd Software Solutions (S3). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Shepherd Software Solutions or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
package com.sss.android.checklistf;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;


/**
 * Implements a general check list application.  This version of Checklist
 * adds check list items to a list view and processes user input.
 */
public class MainActivityFragment extends Fragment
{
    // class information for the trace
    private final static String TAG = "MainActivityFragment";

    private ListView        mListView;
    private DataCheckList   mDataCheckList;
    private AdapterFullView mAdapterFullView;
    private Context         mContext;

    private OnMainActivityFragmentListener mMainActivityFragmentListener;


    //==========================================================================
    /**
     * This internal class interface must be implemented by the container
     * activity (MainActivity).
     */
    public interface OnMainActivityFragmentListener
    {
        public void onMainActivityFragment(CheckListItem listItem);
    }


    //==========================================================================
    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     * @return A new instance of fragment MainActivityFragment.
     */
    public static MainActivityFragment newInstance()
    {
        MainActivityFragment fragment = new MainActivityFragment();

        // set parameters to be passed to the fragment
        //Bundle args = new Bundle();
        //args.putParcelable(KEY_DATA_CHECKLIST, dataCheckList);
        //fragment.setArguments(args);

        return fragment;
    }


    //==========================================================================
    /**
     * Constructor, required empty constructor
     */
    public MainActivityFragment() {}


    //==========================================================================
    /**
     * Implement onCreateView function
     *
     * @param savedInstanceState
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup      container,
                             Bundle         savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        //setContentView(R.layout.activity_main);

        Log.d(TAG, "OnCreateView()");
        View view      = inflater.inflate(R.layout.fragment_main, container, false);

        // get checklist data
        mContext       = getActivity().getApplicationContext();
        mDataCheckList = DataCheckList.get(mContext);


        // set checklist title & display at top of view
        TextView chklst_name = (TextView)view.findViewById(R.id.text_view_chklist_name);
        chklst_name.setText("Name: " + mDataCheckList.getName());
        //this.setTitle("Check List: " + check_list.mCheckListName);

        // get list view and set its display with the adapter
        mListView        = (ListView)view.findViewById(R.id.list_view_main);
        mAdapterFullView = new AdapterFullView(mContext, mDataCheckList);
        mListView.setAdapter(mAdapterFullView);

        // set checklist name message handler
        chklst_name.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "CheckList Name OnClick");
            }
        });

        // set ListView on click message handler
        mListView.setClickable(true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.d(TAG, "ListView OnItemClick: position = " + position + ", id = " + id);
                Toast.makeText(mContext, "ListView OnItemClick", Toast.LENGTH_SHORT).show();

                // get selected list view item from the list view adapter
                CheckListItem list_item = (CheckListItem)parent.getItemAtPosition(position);
                Log.d(TAG, list_item.toString());

                // get selected item from raw check list data
                list_item = mDataCheckList.getItem(position);
                Log.d(TAG, list_item.toString());

                // toggle check list item view
                Log.d(TAG, "toggle item view, pos = " + position);

                if(list_item.mDispOpt == CheckListItem.DispOpt.FULL_DESCRIPTION)
                {
                    mDataCheckList.setItemDisplay(position, CheckListItem.DispOpt.NO_DESCRIPTION);
                }
                else
                {
                    mDataCheckList.setItemDisplay(position, CheckListItem.DispOpt.FULL_DESCRIPTION);
                }

                mAdapterFullView.setList(mDataCheckList);
                mAdapterFullView.notifyDataSetChanged();

                list_item = (CheckListItem)mAdapterFullView.getItem(position);
                Log.d(TAG, list_item.toString());
            }
        });


        //----------------------------------------------------------------------
        // set ListView on long click message handler
        mListView.setLongClickable(true);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                // Toast.makeText(mContext, "ListView OnItemLongClick", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onItemLongClick()");

                // create the list item editor fragment to display/edit the
                // currently selected list item
                CheckListItem list_item = (CheckListItem)mAdapterFullView.getItem(position);
                Log.d(TAG, "list item = " + list_item.toString());

                // display the dialog fragment
                FragmentTransaction frag_trans = getFragmentManager().beginTransaction();

                // remove any existing instances of the dialog
                Fragment pre_dlg = getFragmentManager().findFragmentByTag("list_item_dialog");
                if(pre_dlg != null)
                {
                    frag_trans.remove(pre_dlg);
                }

                // display the dialog
                //DialogFragment list_item_dlg = ListItemFragment.newInstance(list_item);
                //list_item_dlg.show(frag_trans, "list_item_dialog");

                // replace this fragment with the ListItemFragment
                mMainActivityFragmentListener.onMainActivityFragment(list_item);

                return true;
            }
        });

        return view;

    }   // end protected void onCreate(Bundle savedInstanceState)


    //==========================================================================
    /**
     * Called once the fragment is associated with its activity.  Note that
     * Activity is a subclass of Context
     */
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        Log.d(TAG, "onAttach()");

        try
        {
            mMainActivityFragmentListener = (OnMainActivityFragmentListener)context;
        }
        catch(ClassCastException cce)
        {
            throw new ClassCastException(context.toString() +
                    " must implement OnCheckListItemEditedListener");
        }
    }

}   // end public class MainActivityFragment extends Fragment
