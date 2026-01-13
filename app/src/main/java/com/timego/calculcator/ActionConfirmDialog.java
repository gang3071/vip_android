package com.timego.calculcator;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


/**
 * 通用弹窗
 */
public class ActionConfirmDialog extends Dialog {
    private final Context context;
    private View lineV;

    private TextView contentTv;
    private TextView cancelTv;
    private TextView sumbitTv;
    String title;
    String content;
    String cancel = null;
    String sure = null;
    boolean showCancel = true;
    OnToActionListener onToActionListener;

    public interface OnToActionListener {
        void toSumbit();
        void toCancel();

    }

    public void setOnToActionListener(OnToActionListener onNextCallListener) {
        this.onToActionListener = onNextCallListener;
    }


    public ActionConfirmDialog(Context context, String content,boolean showCancel) {
        super(context, R.style.MaterialDesignDialog);
        this.context = context;
        this.content = content;
        this.showCancel = showCancel;
    }
    public ActionConfirmDialog(Context context, String content, String cancel, String sure) {
        super(context, R.style.MaterialDesignDialog);
        this.context = context;
        this.content = content;
        this.cancel = cancel;
        this.sure = sure;
    }

    public ActionConfirmDialog(Context context, String content, String cancel, String sure,boolean showCancel) {
        super(context, R.style.MaterialDesignDialog);
        this.context = context;
        this.content = content;
        this.cancel = cancel;
        this.sure = sure;
        this.showCancel = showCancel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_action_confirm);

        contentTv = (TextView) findViewById(R.id.content_tv);
        cancelTv = (TextView) findViewById(R.id.cancel_tv);
        sumbitTv = (TextView) findViewById(R.id.sumbit_tv);

        lineV = (View) findViewById(R.id.line_v);

        contentTv.setText(content);
        contentTv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //这个监听的回调是异步的，在监听完以后一定要把绘制监听移除，不然这个会一直回调，导致界面错乱
                contentTv.getViewTreeObserver().removeOnPreDrawListener(this);
                int line = contentTv.getLineCount();
                if(line>1){
                    contentTv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                }

                return true;
            }
        });
        if(!TextUtils.isEmpty(cancel)){
            cancelTv.setText(cancel);
        }
        if(!TextUtils.isEmpty(sure)){
            sumbitTv.setText(sure);
        }
        if(!showCancel){
            cancelTv.setVisibility(View.GONE);
            lineV.setVisibility(View.GONE);
        }

        sumbitTv.setOnClickListener(v -> {
            dismiss();
            if(onToActionListener!=null){
                onToActionListener.toSumbit();
            }
        });
        cancelTv.setOnClickListener(v -> {
            dismiss();
            if(onToActionListener!=null){
                onToActionListener.toCancel();
            }
        });

        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(wlp);
    }

}
