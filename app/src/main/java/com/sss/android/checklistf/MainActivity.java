package com.sss.android.checklistf;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements ListItemDlgFragment.OnCheckListItemEditedListener
{
    private final static String TAG = "MainActivity";

    private MainActivityFragment mMainActivityFragment;


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
        //MainActivityFragment main_fragment = MainActivityFragment.newInstance();
        //FragmentTransaction  transaction   = getSupportFragmentManager().beginTransaction();
        //transaction.replace(R.id.fragment_container, main_fragment);
        //transaction.commit();

        mMainActivityFragment = (MainActivityFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);

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


    //==========================================================================
    /**
     * Call back function to handle an edited CheckListItem
     *
     * @param checkListItem
     */
    @Override
    public void onCheckListItemEdited(CheckListItem checkListItem)
    {
        Log.d(TAG, "onCheckListItemEdited");


    }
}  // end public class MainActivity extends AppCompatActivity
