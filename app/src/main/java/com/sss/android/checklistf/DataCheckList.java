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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Class DataCheckList is a container for the check list items that make up a
 * check list.  The Checklist is saved to a database.  This data container is
 * implemented with a singleton design pattern
 */
public class DataCheckList
{
    private final static String TAG = "DataCheckList";

    private static DataCheckList     sDataCheckList;
    private ArrayList<CheckListItem> mCheckListItemList;
    private Context                  mContext;
    private SQLiteDatabase           mDataBase;
    private String                   mCheckListName = "Name Of Checklist";
    private int                      mIteratorIndex = 0;


    /**
     * Get single instance of DataCheckList.  If it doesn't already exist
     * then create one.
     *
     * @param context application level context
     * @return the single instance of DataCheckList
     */
    public static DataCheckList get(Context context)
    {
        if(sDataCheckList == null)
        {
            sDataCheckList = new DataCheckList(context);
        }
        return sDataCheckList;
    }   // end public static DataCheckList get(Context context)


    /**
     * Private Constructor for singleton called by static get() function
     *
     * @param context application level context
     */
    private DataCheckList(Context context)
    {
        mContext  = context.getApplicationContext();
        mDataBase = new DatabaseHelper(mContext).getWritableDatabase();
    }   // end private void DataCheckList()


    /**
     * Returns checklist name
     */
    public String getName()
    {
        return mCheckListName;
    }

    /**
     * Get checklist list of items from database
     */
    public void getFromDatabase()
    {
        mCheckListItemList = getItems();
    }

    /**
     * get the number of items in the check list
     * @return
     */
    public int numItems()
    {
        return mCheckListItemList.size();
    }

    /**
     * get item at specified position
     */
    public CheckListItem getItem(int position)
    {
        return mCheckListItemList.get(position);
    }

    /**
     * Get a list of checklist items from the database
     *
     * @return list of checklist items
     */
    private ArrayList<CheckListItem> getItems()
    {
        ArrayList<CheckListItem> items = new ArrayList<>();

        // cursor points to particular place in query
        DatabaseCursor cursor = queryItems(null, null);

        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                items.add(cursor.getItem());
                cursor.moveToNext();

            }
        }
        finally
        {
            cursor.close();
        }

        return items;
    }   // end public List<Crime> getCrimes()


    //==========================================================================
    /**
     * Get the number of checklist entries from the database by iterating
     * through the database
     *
     * @return number of checklist entries
     */
    private int getDatabaseSize()
    {
        int num_entries = 0;

        // cursor points to particular place in query
        DatabaseCursor cursor = queryItems(null, null);

        try
        {
            cursor.moveToFirst();
            while(!cursor.isAfterLast())
            {
                num_entries++;
                cursor.moveToNext();

            }
        }
        finally
        {
            cursor.close();
        }

        return num_entries;
    }   // end private getNumbDatabaseEntries()


    //==========================================================================
    /**
     * Adds checklist item to the database
     * @param checkListItem check list item to add
     * @return
     */
    public void addItemDatabase(CheckListItem checkListItem)
    {
        ContentValues contentValues = getContentValues(checkListItem);
        mDataBase.insert(DatabaseSchema.TableCheckList.NAME, null, contentValues);
    }

    /**
     * delete specified item from the database
     * @return
     */
    public void deleteItemDatabase()
    {

    }

    /**
     * Update specified item in database
     * @return
     */
    public void updateItemDatabase(CheckListItem checkListItem)
    {
        String        uuidString = checkListItem.mUUID.toString();
        ContentValues values     = getContentValues(checkListItem);

        mDataBase.update(DatabaseSchema.TableCheckList.NAME,      // table name
                values,                            // table ContentValues
                DatabaseSchema.TableCheckList.Cols.UUID + " = ?", // if UUDI matches string
                new String[]{uuidString});         // string to match
    }

    /**
     * Sets specified check list item display option
     *
     * @param dispOpt specifies how item is to be displayed
     * @param index identifies check list item to modify
     */
    public void setItemDisplay(int index, CheckListItem.DispOpt dispOpt)
    {
        CheckListItem item = mCheckListItemList.get(index);
        item.mDispOpt = dispOpt;
    }


    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////// PRIVATE MEMBER FUNCTIONS ////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    //==========================================================================
    /**
     * Creates a ContentValues instance containing a Checklist database row.
     * The row is populated with the specified Checklist values.  ContentValues
     * is a Map like object specifically for databases.
     *
     * @param item checklist item
     * @return ContentValues structure with the Crime values
     */
    private static ContentValues getContentValues(CheckListItem item)
    {
        ContentValues values = new ContentValues();

        values.put(DatabaseSchema.TableCheckList.Cols.TITLE,       item.mTitle);
        values.put(DatabaseSchema.TableCheckList.Cols.DESCRIPTION, item.mDesc);
        values.put(DatabaseSchema.TableCheckList.Cols.CHECKED,     item.mChecked ? 1 : 0);
        values.put(DatabaseSchema.TableCheckList.Cols.UNITS,       item.mCheckUnits);
        values.put(DatabaseSchema.TableCheckList.Cols.INTERVAL,    item.mCheckInterval);
        values.put(DatabaseSchema.TableCheckList.Cols.STAMP,       item.mCheckStamp);
        values.put(DatabaseSchema.TableCheckList.Cols.DATE,        item.mCheckTime);

        return values;
    }

    /**
     * Get database row information in DatabaseCursor format.
     *
     * @param whereClause identifies rows get, null is all rows
     * @param whereArgs row identification string
     *
     * @return Cursor wrapped in a utility class
     */
    private DatabaseCursor queryItems(String whereClause, String[] whereArgs)
    {
        Cursor cursor = mDataBase.query(
                DatabaseSchema.TableCheckList.NAME,    // table to query
                null,                   // column values and order received - null is all columns
                whereClause,            // where row identification logic - null is all rows
                whereArgs,              // where arguments specify row
                null,                   // group by
                null,                   // having
                null,                   // order by
                null                    // limit
        );

        return new DatabaseCursor(cursor);
    }


    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// DEBUG FUNCTIONS /////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    private final static int DEBUG_LIST_SIZE = 10;

    /**
     * Populates the checklist with items for debug if it is empty.
     */
    public void populateForDebug()
    {
        int database_size = getDatabaseSize();
        Log.d(TAG, "populateForDebug(): database entries = " + database_size);

        if(database_size == 0)
        {
            Log.d(TAG, "populateForDebug(Num_Items = " + DEBUG_LIST_SIZE + ")");
            mCheckListName    = "2013 KTM 500 EXC";

            UUID uuid;
            boolean checked;
            String title;
            String desc     = "Dummy String For Debug.  ";
            String units    = "Hours";
            int    interval = 15;
            int    stamp    = 10;
            long   time     = new Date().getTime();

            for(int i = 0; i < DEBUG_LIST_SIZE; i++)
            {
                uuid     = new UUID(0, i);
                checked  = (i % 2 == 0);
                title    = "Check List Item " + i;
                desc    += "This is more check list content.  ";


                CheckListItem list_item = new CheckListItem(uuid, checked, title,
                        desc, units, interval, stamp++, time);
                //mCheckListItemList.add(list_item);
                addItemDatabase(list_item);
            }
        }
        else
        {
            Log.d(TAG, "populateForDebug(): database already has entries");
        }
    }


    ////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// PRIVATE CLASSES //////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Defines database column entries for CheckList.  These are String
     * constants
     */
    private class DatabaseSchema
    {
        public final class TableCheckList
        {
            // name of table in database
            public final static String NAME = "checklist";

            /**
             * string constants used to describe the table columns
             */
            public final class Cols
            {
                public final static String UUID        = "uuid";
                public final static String TITLE       = "title";
                public final static String DESCRIPTION = "description";
                public final static String CHECKED     = "checked";
                public final static String UNITS       = "units";
                public final static String INTERVAL    = "interval";
                public final static String STAMP       = "stamp";
                public final static String DATE        = "date";

            }
        }
    }   // end private class DatabaseSchema

    /**
     * Database helper for Checklist application.  Manages database creation and
     * version management
     */
    private class DatabaseHelper extends SQLiteOpenHelper
    {
        private final static int    VERSION       = 1;
        private final static String DATABASE_NAME = "checklist.db";

        /**
         * Default Constructor
         */
        public DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, VERSION);
        }


        //======================================================================
        /**
         * Implements the system state function onCreate.  Creates the column
         * entries for the database.
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL("create table " + DatabaseSchema.TableCheckList.NAME +
                    "(" + " _id integer primary key autoincrement, "      +
                    DatabaseSchema.TableCheckList.Cols.UUID        + ", " +
                    DatabaseSchema.TableCheckList.Cols.TITLE       + ", " +
                    DatabaseSchema.TableCheckList.Cols.DESCRIPTION + ", " +
                    DatabaseSchema.TableCheckList.Cols.CHECKED     + ", " +
                    DatabaseSchema.TableCheckList.Cols.UNITS       + ", " +
                    DatabaseSchema.TableCheckList.Cols.INTERVAL    + ", " +
                    DatabaseSchema.TableCheckList.Cols.STAMP       + ", " +
                    DatabaseSchema.TableCheckList.Cols.DATE        + ")");
        }


        //======================================================================
        /**
         * For now just uninstall the app when the database structure is
         * changed
         *
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {

        }
    }   // end private class DatabaseHelper extends SQLiteOpenHelper


    /**
     * Encapsulates a database Cursor object for the Checklist database.  The
     * cursor points to a position in the database
     */
    private class DatabaseCursor extends CursorWrapper
    {
        /**
         * Creates the cursor wrapper for the CheckList database.  The cursor
         * points to a position in the database.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public DatabaseCursor(Cursor cursor)
        {
            super(cursor);
        }


        /**
         * Get specified checklist item from the database.
         * @return specified CheckListItem
         */
        public CheckListItem getItem()
        {
            // get Crime info from database
            String  uuidString  = getString(getColumnIndex(DatabaseSchema.TableCheckList.Cols.UUID));
            UUID    uuid        = UUID.fromString(uuidString);

            int     is_checked  = getInt(getColumnIndex(DatabaseSchema.TableCheckList.Cols.CHECKED));
            boolean checked     = is_checked == 1 ? true : false;

            String  title       = getString(getColumnIndex(DatabaseSchema.TableCheckList.Cols.TITLE));
            String  desc        = getString(getColumnIndex(DatabaseSchema.TableCheckList.Cols.DESCRIPTION));
            String  units       = getString(getColumnIndex(DatabaseSchema.TableCheckList.Cols.UNITS));
            int interval = getInt(getColumnIndex(DatabaseSchema.TableCheckList.Cols.INTERVAL));
            int stamp = getInt(getColumnIndex(DatabaseSchema.TableCheckList.Cols.CHECKED));
            long time = getLong(getColumnIndex(DatabaseSchema.TableCheckList.Cols.DATE));

            // build and return Crime object from database information
            CheckListItem item = new CheckListItem(uuid, checked, title, desc, units, interval, stamp, time);

            return item;
        }   // end public CheckListItem getItem()
    }   // end public class DatabaseCursor extends CursorWrapper
}   // end public class DataCheckList
