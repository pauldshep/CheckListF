package com.sss.android.checklistf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageViewFragment extends Fragment
{
    private final String             TAG = "ImageViewFragment";
    private final String CHECK_LIST_ITEM = "CHECK_LIST_ITEM";

    private CheckListItem       mCheckListItem;
    private OnEditImageListener mEditImageListener;
    private ImageView           mPhotoView;
    private Bitmap              mPhotoBitmap;
    private ImageButton         mRotateCW;
    private ImageButton         mRotateCCW;



    //==========================================================================
    /**
     * This internal class interface must be implemented by the container
     * activity.  This instructs the container activity to switch to the
     * image View/Edit fragment.
     */
    public interface OnEditImageListener
    {
        public void onEditImage(CheckListItem checkListItem);
    }


    //==========================================================================
    public ImageViewFragment()
    {
        // Required empty public constructor
    }


    //==========================================================================
    /**
     * Use this factory method to create a new instance of this fragment
     * using the provided parameters.
     *
     * @param checkListItem Parameter 1.
     * @return A new instance of fragment ImageViewFragment.
     */
    public static ImageViewFragment newInstance(CheckListItem checkListItem)
    {
        ImageViewFragment fragment = new ImageViewFragment();
        Bundle            args     = checkListItem.buildBundle();
        fragment.setArguments(args);
        return fragment;
    }


    //==========================================================================
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        Log.d(TAG, "onCreate()");
        mCheckListItem = new CheckListItem(args);
    }


    //==========================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreateView()");
        int  color_black = Color.parseColor("#000000");

        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_image_view, container, false);

        mPhotoView = (ImageView)view.findViewById(R.id.imageViewMain);
        mPhotoView.setColorFilter(color_black);

        mRotateCW  = (ImageButton)view.findViewById(R.id.imageButtonRotateCW);
        mRotateCW.setColorFilter(color_black);

        mRotateCCW = (ImageButton)view.findViewById(R.id.imageButtonRotateCCW);
        mRotateCCW.setColorFilter(color_black);

        return view;
    }


    //==========================================================================
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
        updateChecklistItemPhoto();
    }


    //==========================================================================
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof OnEditImageListener)
        {
            mEditImageListener = (OnEditImageListener)context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    //==========================================================================
    @Override
    public void onDetach()
    {
        super.onDetach();
        mEditImageListener = null;
    }


    //==========================================================================
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //==========================================================================
    /**
     * Updates the checklist item photo from the bitmap file
     */
    private void updateChecklistItemPhoto()
    {
        Log.d(TAG, "updateChecklistItemPhoto(file path = " + mCheckListItem.mPhotoFilePath + ")");
        if(mCheckListItem.mPhotoFilePath == null)  return;
        if(mPhotoView                    == null)  return;

        // get the dimensions of the display ImageView
        int targetMaxW = mPhotoView.getMaxWidth();
        int targetMaxH = mPhotoView.getMaxHeight();
        Log.d(TAG, "updateChecklistItemPhoto(targetMaxW = " + targetMaxW +
                ", targetMaxH = " + targetMaxH + ")");

        // get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds    = true;
        BitmapFactory.decodeFile(mCheckListItem.mPhotoFilePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        Log.d(TAG, "updateChecklistItemPhoto(photoW = " + photoW +
                ", photoH = " + photoH + ")");

        // calculate bitmap scale factor so it will fit in the ImageView
        int scaleFactor = Math.max(photoW / targetMaxW, photoH / targetMaxH);
        Log.d(TAG, "updateChecklistItemPhoto(scale factor = " + scaleFactor + ")");

        // decode the image file to a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize       = scaleFactor;

        mPhotoBitmap = BitmapFactory.decodeFile(mCheckListItem.mPhotoFilePath, bmOptions);

        if(mPhotoBitmap != null)
        {
            mPhotoView.setImageBitmap(mPhotoBitmap);
            // mCheckListItem.mPhotoFilePath = mPhotoFilePath;
        }
    }
}   // end public class ImageViewFragment extends Fragment
