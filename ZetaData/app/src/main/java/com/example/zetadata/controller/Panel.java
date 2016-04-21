package com.example.zetadata.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class Panel extends LinearLayout{
    private static final String TAG = "Panel";
    private OnPanelStateChangeListenr mOnPanelStateChangeListenr;
    private Context mContext;
    private int MAX_MIDTH = 0;
    private static final int SPEED = 20;
    private boolean isOperator = false;
    private int direct = 0;

    public Panel(Context context, int width, int height) {
        super(context);
        mContext = context;
        LayoutParams lp = new LayoutParams(width, height);
        if (direct == 1) {
            lp.bottomMargin = -lp.height;
            MAX_MIDTH = Math.abs(lp.bottomMargin);
        } else {
            lp.topMargin = -lp.height;
            MAX_MIDTH = Math.abs(lp.topMargin);
        }

        this.setLayoutParams(lp);
    }


    public Panel(Context context, View bindView, View contentView, View viewBeside, int width, int height, int direct) {
        this(context,width,height);
        direct = direct;

        //必须改变Panel左侧组件的weight属性
        LayoutParams p=(LayoutParams) viewBeside.getLayoutParams();
        p.weight=1;//支持挤压   
        viewBeside.setLayoutParams(p);

        setBindView(bindView);

        setContentView(contentView);
    }

    public void setBindView(View bindView) {
        bindView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                LayoutParams lp = (LayoutParams)getLayoutParams();
                Log.e(TAG, "bindView onclick topMargin = "+lp.topMargin);
                if (isOperator == true) {
                    return;
                }else {
                    isOperator = true;
                }
                if (lp.bottomMargin < 0 || lp.topMargin < 0) {
                    new AsyncMove().execute(new Integer[] { SPEED });// 正数展开
                } else {
                    new AsyncMove().execute(new Integer[] { -SPEED });// 负数展开
                }

            }

        });
    }

    public void setContentView(View contentView) {
        this.addView(contentView);
    }

    public interface OnPanelStateChangeListenr {
        void onPanelOpened(Panel panel);
        void onPanelCloseed(Panel panel);
    }

    public void setOnPanelStateChangeListenr(OnPanelStateChangeListenr listener) {
        this.mOnPanelStateChangeListenr = listener;
    }

    class AsyncMove extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            int times;
            if (MAX_MIDTH % Math.abs(params[0]) == 0)// 整除   
                times = MAX_MIDTH / Math.abs(params[0]);
            else
                times = MAX_MIDTH / Math.abs(params[0]) + 1;// 有余数   
            Log.e(TAG, "doInBackground times = "+times);
            for (int i = 0; i < times; i++) {
                publishProgress(params);
                try {
                    Thread.sleep(Math.abs(params[0]));
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block   
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            LayoutParams lp = (LayoutParams)getLayoutParams();
            if (values[0] < 0){//关闭   
                if (direct == 1) {
                    lp.bottomMargin = Math.max(lp.bottomMargin + values[0],-MAX_MIDTH);
                } else {
                    lp.topMargin = Math.max(lp.topMargin + values[0],-MAX_MIDTH);
                }
            }
            else{//打开   
                if (direct == 1) {
                    lp.bottomMargin = Math.min(lp.bottomMargin + values[0],MAX_MIDTH);
                } else {
                    lp.topMargin = Math.min(lp.topMargin + values[0],MAX_MIDTH);
                }
            }
            if (direct == 1) {
                if(lp.bottomMargin==0 && mOnPanelStateChangeListenr!=null){//展开之后
                    mOnPanelStateChangeListenr.onPanelOpened(Panel.this);//调用OPEN回调函数
                }
                else if(lp.bottomMargin==-MAX_MIDTH && mOnPanelStateChangeListenr!=null){//收缩之后
                    mOnPanelStateChangeListenr.onPanelCloseed(Panel.this);//调用CLOSE回调函数
                }
            } else {
                if(lp.topMargin==0 && mOnPanelStateChangeListenr!=null){//展开之后
                    mOnPanelStateChangeListenr.onPanelOpened(Panel.this);//调用OPEN回调函数
                }
                else if(lp.topMargin==-MAX_MIDTH && mOnPanelStateChangeListenr!=null){//收缩之后
                    mOnPanelStateChangeListenr.onPanelCloseed(Panel.this);//调用CLOSE回调函数
                }
            }

            if (lp.bottomMargin==0 || lp.bottomMargin==-MAX_MIDTH || lp.topMargin==0 || lp.topMargin==-MAX_MIDTH) {
                isOperator = false;
            }

            Log.e(TAG, "onProgressUpdate");
            setLayoutParams(lp);
        }


    }
}
