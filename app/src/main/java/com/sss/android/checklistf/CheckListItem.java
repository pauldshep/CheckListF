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

import android.os.Bundle;
import android.util.Log;

import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

/**
 * Class CheckListItem holds data for individual check list entries
 */
public class CheckListItem
{
    private static String TAG = "CheckListItem";

    public  static enum DispOpt {FULL_DESCRIPTION, NO_DESCRIPTION}

    // parameter identifiers used to bundle data
    public final static String PARM_UUID          = "PARM_UUID";
    public final static String PARM_TITLE         = "PARM_TITLE";
    public final static String PARM_DESC          = "PARM_DESC";
    public final static String PARM_CHECKED       = "PARM_CHECKED";
    public final static String PARM_CHECKUNITS    = "PARM_CHECKUNITS";
    public final static String PARM_CHECKINTERVAL = "PARM_CHECKINTERVAL";
    public final static String PARM_CHECKSTAMP    = "PARM_CHECKSTAMP";
    public final static String PARM_CHECKTIME     = "PARM_CHECKTIME";

    // parameters associated with a check list item
    public UUID    mUUID          = new UUID(0,0);
    public String  mTitle         = "Item Title";
    public String  mDesc          = "Item Description";
    public Boolean mChecked       = false;
    public String  mCheckUnits    = "None";
    public Integer mCheckInterval = 0;
    public Integer mCheckStamp    = 0;
    public Long    mCheckTime     = new Date().getTime();;

    // check list item support parameters
    public DispOpt mDispOpt       = DispOpt.FULL_DESCRIPTION;

    //==========================================================================
    /**
     * Constructor: Default
     */
    public CheckListItem()
    {
        mUUID          = new UUID(0,0);
        mTitle         = "No Title";
        mDesc          = "No Description";
        mChecked       = false;
        mCheckUnits    = "No Units";
        mCheckInterval = 0;
        mCheckStamp    = 0;
        mCheckTime     = new Date().getTime();
    }


    //==========================================================================
    /**
     * Constructor: initialize check list item from specified variables.
     *
     * @param checked set true if the item has been checked
     * @param title short description of check list item
     * @param desc long description of check list item
     * @param units item check units: hours, days, every time, ...
     * @param interval required check interval in above units
     * @param stamp check interval value when checked
     * @param time time stamp of last check in msecs since 1970
     */
    public CheckListItem(UUID    uuid,
                         boolean checked,
                         String  title,
                         String  desc,
                         String  units,
                         int     interval,
                         int     stamp,
                         long    time)
    {
        mUUID          = uuid;
        mTitle         = title;
        mDesc          = desc;
        mChecked       = checked;
        mCheckUnits    = units;
        mCheckInterval = interval;
        mCheckStamp    = stamp;
        mCheckTime     = time;

        mDispOpt       = DispOpt.NO_DESCRIPTION;
    }


    //==========================================================================
    /**
     * Constructor: creates instance of CheckListItem from parameters in a
     * bundle
     *
     * @param bundle
     */
    public CheckListItem(Bundle bundle)
    {
        if(bundle != null)
        {
            mTitle         = bundle.getString(PARM_TITLE);
            mDesc          = bundle.getString(PARM_DESC);
            mChecked       = bundle.getBoolean(PARM_CHECKED);
            mCheckUnits    = bundle.getString(PARM_CHECKUNITS);
            mCheckInterval = bundle.getInt(PARM_CHECKINTERVAL);
            mCheckStamp    = bundle.getInt(PARM_CHECKSTAMP);
            mCheckTime     = bundle.getLong(PARM_CHECKTIME);
        }
        else
        {
            Log.e(TAG, "CheckListItem Bundle is null");
        }
    }


    //==========================================================================
    /**
     * Builds bundle of check list parameters to pass to a fragments
     */
    public Bundle buildBundle()
    {
        Bundle bundle = new Bundle();

        bundle.putString(PARM_TITLE,      mTitle);
        bundle.putString(PARM_DESC,       mDesc);
        bundle.putBoolean(PARM_CHECKED,   mChecked);
        bundle.putString(PARM_CHECKUNITS, mCheckUnits);
        bundle.putInt(PARM_CHECKINTERVAL, mCheckInterval);
        bundle.putInt(PARM_CHECKSTAMP,    mCheckStamp);
        bundle.putLong(PARM_CHECKTIME,    mCheckTime);

        return bundle;
    }


    //==========================================================================
    /**
     * Implements class toString() function
     */
    @Override
    public String toString()
    {
        String str = "CheckListItem: Checked = "   + mChecked +
                     ", Title = "                  + mTitle   +
                     ", View = "                   + mDispOpt +
                     ", Desc = "                   + mDesc    +
                     "\n"                          +
                     "check units = "              + mCheckUnits +
                     ", check interval = "         + mCheckInterval +
                     ", check stamp = "            + mCheckStamp +
                     ", check time = "             + mCheckTime;


        return str;
    }


}   // end public class CheckListItem
