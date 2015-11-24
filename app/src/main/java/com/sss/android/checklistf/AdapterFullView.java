package com.sss.android.checklistf;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * ListView adapter for the checklist item full view.  This view displays the
 * checklist item: title, full description, and checkbox.
 */
public class AdapterFullView extends BaseAdapter
{
    private DataCheckList mDataCheckList;
    private Context       mContext;

    /**
     * Constructor: called when class is created
     *
     * @param context global information about application environment
     * @param checkList check list data to display
     */
    public AdapterFullView(Context context, DataCheckList checkList)
    {
        mContext       = context;
        mDataCheckList = checkList;
    }

    @Override
    public int getCount()
    {
        return mDataCheckList.numItems();
    }

    @Override
    public Object getItem(int position)
    {
        return mDataCheckList.getItem(position);
    }


    /**
     * Override getItemId().
     *
     * @param position identifies checklist item by position in listview
     * @return argument that was passed.  Helps ListView to map its row to the
     * data set elements.
     */
    @Override
    public long getItemId(int position)
    {
        return position;
    }

    /***************************************************************************
     * Build the view to display in the ListView row.
     *
     * @param position - identifies layout to be added
     * @param listItem - list item to define
     * @param parent - the parent where the layout is being added
     * @return view for display
     **************************************************************************/
    @Override
    public View getView(int position, View listItem, ViewGroup parent)
    {
        // Inflate the layout and get the associated view if it does not exist.
        // Note that the list view may recycle unseen items.
        if(listItem == null)
        {
            LayoutInflater inflater = (LayoutInflater)mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listItem = inflater.inflate(R.layout.cklst_item_full, parent, false);
        }

        CheckListItem list_item = mDataCheckList.getItem(position);

        // Check list item title, short description
        TextView title     = (TextView)listItem.findViewById(R.id.text_view_item_title);
        title.setText(list_item.mTitle);

        // set the check box true when the check list item is verified
        CheckBox check_box = (CheckBox)listItem.findViewById(R.id.check_box_item);
        check_box.setChecked(list_item.mChecked);

        // description of the check list item
        TextView desc      = (TextView)listItem.findViewById(R.id.text_view_item_desc);

        if(list_item.mDispOpt == CheckListItem.DispOpt.FULL_DESCRIPTION)
        {
            // display check list item details only if long view is enabled
            desc.setText(mDataCheckList.getItem(position).mDesc);
        }
        else
        {
            desc.setText("Select for Full Description");
        }

        return listItem;
    }   // end public View getView(int position, View listItem, ViewGroup parent)

    /**
     * Updates check list data
     *
     * @param checkList new check list data
     */
    public void setList(DataCheckList checkList)
    {
        mDataCheckList = checkList;
    }
}
