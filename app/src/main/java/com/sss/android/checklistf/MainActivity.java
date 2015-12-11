package com.sss.android.checklistf;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;


/**
 * Application entry point.
 */
public class MainActivity extends AppCompatActivity
        implements  ListItemDlgFragment.OnCheckListItemEditedListener,
                    NowIntervalDlgFragment.OnIntervalNowEditedListener
{
    private final static String TAG = "MainActivity";

    private MainActivityFragment mMainActivityFragment;

    private int             mIntervalNow    = 0;
    private String          mIntervalUnits  = "Unknown";
    private DataCheckList   mDataCheckList  = null;
    private Context         mContext        = null;


    //==========================================================================
    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        // create and intialize checklist data singleton
        mContext       = getApplicationContext();
        mDataCheckList = DataCheckList.get(mContext);
        mDataCheckList.populateForDebug();
        mDataCheckList.getFromDatabase();


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
        //  getSupportFragmentManager().findFragmentById(R.id.fragment_container);



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

            case R.id.import_checklist:
                Log.d(TAG, "import checklist");
                importChecklist();
                return true;

            case R.id.export_checklist:
                exportChecklist();
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
     * Call back function to handle an edited CheckListItem.  The new item
     * parameters are copied to the associated checklist item in the list.
     *
     * @param checkListItem checklist item that has been modified
     */
    @Override
    public void onCheckListItemEdited(CheckListItem checkListItem)
    {
        Log.d(TAG, "onCheckListItemEdited(): " + checkListItem.toString());
        mDataCheckList.updateCheckListItem(checkListItem);

        MainActivityFragment main_fragment = MainActivityFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, main_fragment).commit();
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
        Log.d(TAG, "setNowIntervalStamp()");
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


    //=========================================================================
    /**
     * Import checklist parameters from xml file
     */
    private void importChecklist()
    {
        Log.d(TAG, "importChecklist()");
        File file = new File(Environment.DIRECTORY_DOCUMENTS);
    }


    //=========================================================================
    /**
     * Exports the current checklist.  A list of email clients is displayed
     * for the user to select.  The CheckList data is mailed to the specified
     * location as an attachment to the email.
     */
    private void exportChecklist()
    {
        Log.d(TAG, "exportCheckList()");

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, "pauldshep@gmail.com");
        i.putExtra(Intent.EXTRA_SUBJECT, "Check List Data");
        i.putExtra(Intent.EXTRA_TEXT, "This is the Check List Data in XML");

        // add the XML checklist description attachment here
        // i.putExtra(Intent.EXTRA_STREAM, FILE_URI);

        startActivity(Intent.createChooser(i, "Choose Export Client"));
    }   // end private void exportChecklist()

}  // end public class MainActivity extends AppCompatActivity
