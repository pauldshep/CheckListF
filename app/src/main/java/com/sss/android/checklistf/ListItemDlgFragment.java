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


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import java.io.File;
import java.io.IOException;
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
    private final static String TAG                 = "ListItemDlgFragment";
    private final static String KEY_PHOTO_BITMAP    = "key_photo_bitmap";
    private final static String KEY_PHOTO_FILE_PATH = "key_photo_file_path";
    private final static int    TAG_REQUEST_PHOTO   = 1;


    // GUI Parameter variables
    private CheckListItem mCheckListItem;
    private ImageView     mPhotoView;
    private ImageButton   mImageButtonTakePicture;
    private Bitmap        mPhotoBitmap;

    // system variables
    private Context                         mContext;
    private OnCheckListItemEditedListener   mOnEditedListener;
    private String                          mPhotoFileName;
    private String                          mPhotoFilePath;
    private File                            mPhotoFile;

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

        Button button_use = (Button)view.findViewById(R.id.buttonListItemUse);
        button_use.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "ButtonUse.onClick()");
                mOnEditedListener.onCheckListItemEdited(mCheckListItem);
                dismiss();
            }
        });

        Button button_abort = (Button)view.findViewById(R.id.buttonListItemAbort);
        button_abort.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "ButtonAbort.onClick()");
                dismiss();
            }
        });


        //----------------------------------------------------------------------
        // ImageView that displays the thumbnail of the picture associated
        // with the checklist item
        //----------------------------------------------------------------------
        mPhotoView = (ImageView)view.findViewById(R.id.imageViewItem);

        if((savedInstanceState != null))
        {
            if(savedInstanceState.containsKey(KEY_PHOTO_BITMAP))
            {
                mPhotoBitmap = (Bitmap)savedInstanceState.get(KEY_PHOTO_BITMAP);
            }
            if(savedInstanceState.containsKey(KEY_PHOTO_FILE_PATH))
            {
                mPhotoFilePath = savedInstanceState.getString(KEY_PHOTO_FILE_PATH);
            }
        }

        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // display item image viewer/editor implemented as a fragment dialog
                Log.d(TAG, "mImageViewItem::onClick()");
            }
        });

        //----------------------------------------------------------------------
        // ImageButton that causes a picture to be taken to associate with
        // the check list item.
        //----------------------------------------------------------------------
        mImageButtonTakePicture = (ImageButton)view.findViewById(R.id.imageButtonTakePicture);
        mImageButtonTakePicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "mImageButtonTakePicture::onClick()");
                final Intent   captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                PackageManager pkgMgr       = getActivity().getPackageManager();

                // verify there is a camera available to take the picture
                if(captureImage.resolveActivity(pkgMgr) != null)
                {
                    // this gets the full bitmap of the camera image, not just
                    // a thumb nail, and saves it to the specified file.
                    Uri uri = createFileForPhotograph();
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(captureImage, TAG_REQUEST_PHOTO);
                }
            }
        });

        updateChecklistItemPhoto();
        return view;
    }  // end public View onCreateView(...


    //==========================================================================
    /**
     * Processes the activity result from a call to startActivityForResult()
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode)
        {
            case TAG_REQUEST_PHOTO:
                Log.d(TAG, "onActivityResult(requestCode = TAG_REQUEST_PHOTO)");
                Log.d(TAG, "onActivityResult(resultCode = " + resultCode + ")");
                if(resultCode == Activity.RESULT_OK)
                {
                    updateChecklistItemPhoto();
                }
                break;

            default:
                Log.e(TAG, "onActivityResult()::unsupported request code = " + requestCode);
                break;
        }
    }   // end public void onActivityResult(int requestCode, int resultCode, Intent data)


    //==========================================================================
    /**
     * Called to save information between screen rotations
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelable(KEY_PHOTO_BITMAP,    mPhotoBitmap);
        savedInstanceState.putString(KEY_PHOTO_FILE_PATH, mPhotoFilePath);
    }


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


    //==========================================================================
    /**
     * Updates the checklist item photo from the bitmap file
     */
    private void updateChecklistItemPhoto()
    {
        Log.d(TAG, "updateChecklistItemPhoto(file path = " + mPhotoFilePath + ")");
        if(mPhotoFilePath == null)  return;
        if(mPhotoView     == null)  return;

        // get the dimensions of the display ImageView
        int targetW = mPhotoView.getMaxWidth();
        int targetH = mPhotoView.getMaxHeight();
        Log.d(TAG, "updateChecklistItemPhoto(targetW = " + targetW +
                                          ", targetH = " + targetH + ")");

        // get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds    = true;
        BitmapFactory.decodeFile(mPhotoFilePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        Log.d(TAG, "updateChecklistItemPhoto(photoW = " + photoW +
                                          ", photoH = " + photoH + ")");

        // calculate bitmap scale factor so it will fit in the ImageView
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        Log.d(TAG, "updateChecklistItemPhoto(scale factor = " + scaleFactor + ")");

        // decode the image file to a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize       = scaleFactor;

        mPhotoBitmap = BitmapFactory.decodeFile(mPhotoFilePath, bmOptions);

        if(mPhotoBitmap != null)
        {
            mPhotoView.setImageBitmap(mPhotoBitmap);
        }
    }


    //==========================================================================
    /**
     * Create file to save check list item photograph based on the item UUID.
     * @return universal resource identifier (Uri) for the photograph file
     */
    private Uri createFileForPhotograph()
    {
        File pict_dir;

        // Here the storage directory is the public pictures directory that
        // can be accessed by any program.
        //pict_dir = Environment.getExternalStoragePublicDirectory(
        //      Environment.DIRECTORY_PICTURES);

        // Here the stroage directory is the private storage that only this
        // application can access.  Information in this directory is lost if
        // the app is uninstalled.
        pict_dir = getContext().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);

        if(pict_dir == null) return null;


        // try to create the file for the picture
        mPhotoFileName = mCheckListItem.buildFileName();
        mPhotoFile     = new File(pict_dir, mPhotoFileName);
        mPhotoFilePath = mPhotoFile.getAbsolutePath();

        Log.d(TAG, "createFileForPhotograph(file = " + mPhotoFilePath + ")");

        return Uri.fromFile(mPhotoFile);
    }   // end private Uri createFileForPhotograph()


}
