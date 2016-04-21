package com.example.zetadata.Acty;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.zetadata.R;
import com.example.zetadata.controller.Panel;
import com.example.zetadata.controller.Panel.OnPanelStateChangeListenr;


public class MyPanelActivity extends Activity {
    private static final String TAG = "MainActivity";
    private Panel mPanel;
    private LinearLayout mLinearLayout;
    //private ImageView mPanelButton;
    private ImageView mImageview;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypanel);

        mLinearLayout = (LinearLayout) findViewById(R.id.mLinearlayout_id);
        //mPanelButton = (ImageView) findViewById(R.id.mImageView_id);
        mImageview = (ImageView) findViewById(R.id.mImageView_id);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View contentView = mInflater.inflate(R.layout.content__layout, null);


        mPanel = new Panel(this, mImageview, contentView, mImageview, LayoutParams.FILL_PARENT,100, 0);
        mPanel.setOnPanelStateChangeListenr(new OnPanelStateChangeListenr() {

            @Override
            public void onPanelCloseed(Panel panel) {
                // TODO Auto-generated method stub
                //Toast.makeText(PanelViewDemoActivity.this, "panel Close!!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onPanelCloseed");
            }

            @Override
            public void onPanelOpened(Panel panel) {
                // TODO Auto-generated method stub
                //Toast.makeText(PanelViewDemoActivity.this, "panel open!!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onPanelOpened");
            }

        });
        mLinearLayout.addView(mPanel,0);
    }


}