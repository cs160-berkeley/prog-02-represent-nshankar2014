//package com.neilshankar.prog02ww;
//
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.content.Context;
//import android.graphics.drawable.Drawable;
//import android.support.wearable.view.CardFragment;
//import android.support.wearable.view.FragmentGridPagerAdapter;
//import android.support.wearable.view.GridPagerAdapter;
//import android.view.View;
//import android.view.ViewGroup;
//
//public class SampleGridPagerAdaper extends FragmentGridPagerAdapter {
//
//    private final Context mContext;
//
//    public SampleGridPagerAdapter(Context ctx, FragmentManager fm) {
//        super(fm);
//        mContext = ctx;
//    }
//
//    static final int[] BG_IMAGES = new int[]{
//            R.drawable.face1, R.drawable.face2, R.drawable.face3,
//            R.drawable.face4, R.drawable.face5, R.drawable.face6,
//            R.drawable.face7, R.drawable.face8, R.drawable.face9,
//    };
//
//    // A simple container for static data in each page
//    private static class Page {
//        // static resources
//        int titleRes;
//        int textRes;
//        int iconRes;
//    }
//
//    // Create a static set of pages in a 2D array
//    private final View[][] PAGES = {
//
//    };
//
//    // The adapter calls getFragment() and getBackgroundForRow() to retrieve the content to display for each row:
//    // Obtain the UI fragment at the specified position
//    @Override
//    public Fragment getFragment(int row, int col) {
//        Page page = PAGES[row][col];
//        String title =
//                page.titleRes != 0 ? mContext.getString(page.titleRes) : null;
//        String text =
//                page.textRes != 0 ? mContext.getString(page.textRes) : null;
//        return CardFragment.create(title, text, page.iconRes);
//    }
//
//    // Obtain the background image for the row
//    @Override
//    public Drawable getBackgroundForRow(int row) {
//        return mContext.getResources().getDrawable(
//                (BG_IMAGES[row % BG_IMAGES.length]), null);
//    }
//
//    // Obtain the background image for the specific page
//    @Override
//    public Drawable getBackgroundForPage(int row, int column) {
//        if (row == 2 && column == 1) {
//            // Place image at specified position
//            return mContext.getResources().getDrawable(R.drawable.face1, null);
//        } else {
//            return GridPagerAdapter.BACKGROUND_NONE;
//        }
//    }
//
//    // Obtain the number of pages (vertical)
//    @Override
//    public int getRowCount() {
//        return PAGES.length;
//    }
//
//    // Obtain the number of pages (horizontal)
//    @Override
//    public int getColumnCount(int rowNum) {
//        return PAGES[rowNum].length;
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup viewGroup, int i, int i1) {
//        return null;
//    }
//
//    @Override
//    public void destroyItem(ViewGroup viewGroup, int i, int i1, Object o) {
//        return;
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object o) {
//        return false;
//    }
//}