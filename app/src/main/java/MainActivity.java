package com.mycompany.myapphtk;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.*;
import android.widget.*;

public class MainActivity extends Activity {

    private WebView webView;
    private LinearLayout urlBar;
    private TextView titleBar;
    private EditText urlInput;
    private Button goBtn;

    private FrameLayout fullscreenContainer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    private boolean isUrlVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //  Bind view
        webView = (WebView) findViewById(R.id.webView);
        urlBar = (LinearLayout) findViewById(R.id.urlBar);
        titleBar = (TextView) findViewById(R.id.titleBar);
        urlInput = (EditText) findViewById(R.id.urlInput);
        goBtn = (Button) findViewById(R.id.goBtn);
        fullscreenContainer = (FrameLayout) findViewById(R.id.fullscreenContainer);

        //  WebView settings
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        //them
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //them-end
        webView.setWebViewClient(new WebViewClient());

        webView.setWebChromeClient(new WebChromeClient() {

				//  Fullscreen video
				@Override
				public void onShowCustomView(View view, CustomViewCallback callback) {
					customView = view;
					customViewCallback = callback;

					fullscreenContainer.addView(view);
					fullscreenContainer.setVisibility(View.VISIBLE);
					webView.setVisibility(View.GONE);
				}

				@Override
				public void onHideCustomView() {
					if (customView == null) return;

					fullscreenContainer.removeView(customView);
					fullscreenContainer.setVisibility(View.GONE);
					webView.setVisibility(View.VISIBLE);

					if (customViewCallback != null) {
						customViewCallback.onCustomViewHidden();
					}

					customView = null;
				}
			});

        //  Load mc nh
        webView.loadUrl("http://192.168.1.103:8080/TC/YTV/lbYTS-C@V.html");

        //  Click tiêu   hin/n URL
        titleBar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (isUrlVisible) {
						urlBar.setVisibility(View.GONE);
					} else {
						urlBar.setVisibility(View.VISIBLE);
					}
					isUrlVisible = !isUrlVisible;
				}
			});

        //  Nút GO
        goBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					loadUrl();
				}
			});

        //  Enter  load
        urlInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

					if (actionId == EditorInfo.IME_ACTION_GO ||
						actionId == EditorInfo.IME_ACTION_DONE) {

						loadUrl();
						return true;
					}
					return false;
				}
			});
    }

    //  Load URL + h tr PDF
    private void loadUrl() {
        String url = urlInput.getText().toString().trim();

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        //  Nu là PDF  dùng Google Viewer
        if (url.endsWith(".pdf")) {
            url = "https://docs.google.com/gview?embedded=true&url=" + url;
        }

        webView.loadUrl(url);
    }

    //  Back
    @Override
    public void onBackPressed() {
        if (customView != null) {
            ((WebChromeClient) webView.getWebChromeClient()).onHideCustomView();
        } else if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
