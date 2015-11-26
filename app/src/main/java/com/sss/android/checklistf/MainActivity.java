package com.sss.android.checklistf;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity
        implements ListItemDlgFragment.OnCheckListItemEditedListener,
        NowIntervalDlgFragment.OnIntervalNowEditedListener
{
    private final static String TAG = "MainActivity";

    private MainActivityFragment mMainActivityFragment;

    private int    mIntervalNow = 0;
    private String mIntervalUnits = "Unknown";


    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //---------------------------------------------------------------------
        // Set initial view from layout resource.  Note this fragment cannot
        // be replaced with another at runtime.
        setContentView(R.layout.activity_main);

        //----------------------------------------------------------------------
        // here the fragment is being created at runtime and can be replaced

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if(findViewById(R.id.fragment_container) != null)
        {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if(savedInstanceState != null)
            {
                return;
            }


            MainActivityFragment main_fragment = MainActivityFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, main_fragment).commit();
        }

        //mMainActivityFragment = (MainActivityFragment)
        //        getSupportFragmentManager().findFragmentById(R.id.fragment_container);

    }  // end protected void onCreate(Bundle savedInstanceState)


    //--------------------------------------------------------------------------
    /**
     * Creates the Options Menu
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //--------------------------------------------------------------------------
    /**
     * Handles Options menu selections
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id)
        {
            case R.id.set_now:
                Log.d(TAG, "set now");
                setNowIntervalStamp();
                return true;

            case R.id.full_view:
                Log.d(TAG, "full view");
                return true;

            case R.id.short_view:
                Log.d(TAG, "short view");
                return true;

            case R.id.set_checklist:
                Log.d(TAG, "set checklist");
                return true;

            case R.id.import_db:
                Log.d(TAG, "import database");
                return true;

            case R.id.export_db:
                Log.d(TAG, "export database");
                return true;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }  // end public boolean onOptionsItemSelected(MenuItem item)


    ////////////////////////////////////////////////////////////////////////////
    /////////////////////// FRAGMENT CALLBACK FUNCTIONS ////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    //==========================================================================
    /**
     * Call back function to handle an edited CheckListItem
     *
     * @param checkListItem
     */
    @Override
    public void onCheckListItemEdited(CheckListItem checkListItem)
    {
        Log.d(TAG, "onCheckListItemEdited(): " + checkListItem.toString());


    }


    //==========================================================================
    /**
     * Call back function to handle an edited CheckList Interval now stamp
     *
     * @param intervalNow
     */
    @Override
    public void onIntervalNowEdited(int intervalNow)
    {
        Log.d(TAG, "OnIntervalNowEdited(): interval = " + intervalNow);
        mIntervalNow = intervalNow;
    }


    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////// PRIVATE MEMBER FUNCTIONS /////////////////////////
    ////////////////////////////////////////////////////////////////////////////

    //==========================================================================
    /**
     * Display dialog fragment that sets the the "Now" interval stamp.  Units
     * are checklist dependent and apply to all items in the checklist.
     */
    private void setNowIntervalStamp()
    {
        FragmentTransaction frag_trans = getSupportFragmentManager().beginTransaction();

        // remove any existing instances of the dialog
        Fragment frag_dlg = getSupportFragmentManager().findFragmentByTag("interval_dialog");
        if(frag_dlg != null)
        {
            frag_trans.remove(frag_dlg);
        }

        // display the dialog
        //todo: get interval parameters from better place
        mIntervalUnits = "Hours";
        mIntervalNow   = 4321;

        DialogFragment interval_dlg = NowIntervalDlgFragment.newInstance(mIntervalUnits, mIntervalNow);
        interval_dlg.show(frag_trans, "interval_dialog");


    }  // end private void setNowIntervalStamp()

}  // end public class MainActivity extends AppCompatActivity
