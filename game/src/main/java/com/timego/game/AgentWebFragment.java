package com.timego.game;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.timego.game.webview.Setting;
import com.timego.game.webview.WebChromeClient;
import com.timego.game.webview.WebViewClient;

public class AgentWebFragment extends Fragment {

    private AgentWeb mAgentWeb;

    RelativeLayout rlCtl;

    private String url;
    H5WebGameActivity activity;
//    private AudioManager audioManager;
//    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener;

    public AgentWebFragment(String domain) {
        this.url = domain;
    }

    public AgentWebFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_agent_webview, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.activity = (H5WebGameActivity) getActivity();
//        rlCtl = getActivity().findViewById(R.id.rl_ctl);
//        this.audioManager = ((AudioManager)this.activity.getSystemService(Context.AUDIO_SERVICE));
//        this.onAudioFocusChangeListener = focusChange -> {};

//      AgentWebConfig.clearDiskCache(this.getContext());
        WebChromeClient webChromeClient = new WebChromeClient(activity);
        WebViewClient webViewClient = new WebViewClient(url,activity, rlCtl);
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent((ViewGroup) view, -1, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
//                .setCustomIndicator(new CoolIndicatorLayout(this.getContext()))  // 自定义进度条
//                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                .closeIndicator() //关闭进度条
                .setWebViewClient(webViewClient)
                .setWebChromeClient(webChromeClient)// 为了调试 看控制台（F12）
//                .setWebLayout(getWebLayout())
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                .createAgentWeb()//创建AgentWeb。
                .ready()//设置 WebSettings。
                .go(url); //WebView载入该url地址的页面并显示。
//        SmartRefreshLayout srl = (SmartRefreshLayout) this.mSmartRefreshWebLayout.getLayout();
        Setting setting = new Setting(mAgentWeb, activity);
        setting.doSet();


//        mAgentWeb.getJsInterfaceHolder().addJavaObject("WebViewJavascriptBridge")

        /*View webParent = view.findViewById(R.id.web_parent_layout_id);
        ViewGroup webParentVp = (ViewGroup) webParent;
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams relativeLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(relativeLayoutParam);
//            relativeLayout.setId();
        ImageView imageView = new ImageView(activity);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(R.drawable.web_img1);
//            imageView.setId();
        relativeLayout.addView(imageView);
        webParentVp.addView(relativeLayout);*/

        // AgentWebConfig.debug();

        // AgentWeb 4.0 开始，删除该类以及删除相关的API
//        DefaultMsgConfig.DownloadMsgConfig mDownloadMsgConfig = mAgentWeb.getDefaultMsgConfig().getDownloadMsgConfig();
        //  mDownloadMsgConfig.setCancel("放弃");  // 修改下载提示信息，这里可以语言切换

    }


    @Override
    public void onStart() {
        super.onStart();
//        closeAudio();
    }

    @Override
    public void onResume() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onResume();//恢复
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onPause(); //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
        }
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mAgentWeb != null) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroyView();
//        openAudio();
    }

    public WebView getWebView() {
        return mAgentWeb.getWebCreator().getWebView();
    }

//    public void closeAudio() {
//        // 请求音频的焦点（关闭webview声音）
//        audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//    }
//    public void openAudio() {
//        // 放弃音频的焦点（打开webview声音）
//        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
//    }

}