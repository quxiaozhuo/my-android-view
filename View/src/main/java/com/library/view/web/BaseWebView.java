package com.library.view.web;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * qu_xi
 * 2020-04-09
 */
public class BaseWebView extends WVJBWebView {

    public BaseWebView(Context context, AttributeSet attrs) {
        super(getFixedContext(context), attrs);
        initWebSettings();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyle) {
        super(getFixedContext(context), attrs, defStyle);
        initWebSettings();
    }

    public BaseWebView(Context context) {
        super(getFixedContext(context));
        initWebSettings();
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }

    private OnScrollChangeListener mOnScrollChangeListener;

    public void setOnWebScrollChangeListener(OnScrollChangeListener mOnScrollChangeListener) {
        this.mOnScrollChangeListener = mOnScrollChangeListener;
    }

    public void loadPdfUrl(String url) {
        super.loadUrl("file:///android_asset/pdf/index.html?" + url);
    }

    public interface OnScrollChangeListener {

        public void onScrollChanged(View view, int l, int t, int oldl, int oldt);
    }

    @Override
    public void loadDataWithBaseURL(@Nullable String baseUrl, String data, @Nullable String mimeType, @Nullable String encoding, @Nullable String historyUrl) {
//        Document doc = Jsoup.parse(data);
//        Elements elements = doc.getElementsByTag("img");
//        for (Element element : elements) {
//            element.attr("width", "100%").attr("height", "auto");
//        }
//        super.loadDataWithBaseURL(baseUrl, doc.toString(), mimeType, encoding, historyUrl);
        super.loadDataWithBaseURL(baseUrl, getHtmlData(data), mimeType, encoding, historyUrl);
    }

    @Override
    public void loadUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (url.contains("?")) {
            super.loadUrl(url + "&os_type=android&client_type=android");
        } else {
            super.loadUrl(url + "?os_type=android&client_type=android");
        }
    }

//    @Override
//    public void loadUrl(String url) {
//        if (url.contains("?")) {
//            super.loadUrl(url + "&os_type=android&client_type=android&appscene=android");
//        } else {
//            super.loadUrl(url + "?os_type=android&appscene=android&client_type=android");
//        }
//    }

    /**
     * 富文本适配
     */
    private String getHtmlData(String bodyHTML) {
        String head = "<head>"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> "
                + "<style>img{max-width: 100%; width:auto; height:auto;}</style>"
                + "</head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    public static Context getFixedContext(Context context) {
        if (Build.VERSION.SDK_INT >= 21 && Build.VERSION.SDK_INT < 23) // Android Lollipop 5.0 & 5.1
            return context.createConfigurationContext(new Configuration());
        return context;
    }

    private void initWebSettings() {
        disableJavascriptAlertBoxSafetyTimeout(true);
        WebSettings webSetting = getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setAllowFileAccess(true);
//        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setDisplayZoomControls(false);
        webSetting.setDefaultTextEncodingName("utf-8"); // 设置默认编码格式
        webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//高版本https问题
        webSetting.setLoadsImagesAutomatically(true); // 自动加载图片
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true); // 适应屏幕
//         webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);

        webSetting.setSupportMultipleWindows(false);
//        webSetting.setDatabasePath(getContext().getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(getContext().getDir("geolocation", 0)
                .getPath());
        webSetting.setBlockNetworkImage(false);//解决图片不显示
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSetting.setAppCachePath(SD_FileUtil.createRootPath() + "/cache/");// 1. 设置缓存路径
//        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);// 2. 设置缓存大小
        webSetting.setAppCacheEnabled(true); // 3. 开启Application Cache存储机制

        webSetting.setAllowFileAccessFromFileURLs(true);
        webSetting.setAllowUniversalAccessFromFileURLs(true);
    }


    public void initWebInterface(final WebInterface mWebInterface) {
        setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(String arg0, String arg1, String arg2,
                                        String arg3, long arg4) {
//                Toast.getInstance().showToast("下载任务开始");
            }
        });
        setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                Log.e("web======", "onPageFinished+url：" + s);
                if (mWebInterface != null) {
                    mWebInterface.onPageFinished(s);
                }
            }

            //https的处理方式
            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                Log.e("web======", "onReceivedSslError");
                sslErrorHandler.proceed();
            }



            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (mWebInterface != null) {
                    mWebInterface.errorOperation();
                }
            }


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("web======", "shouldOverrideUrlLoading+request：" + view.getUrl());
                if (request.toString().startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
                    getContext().startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

        });
        setWebChromeClient(new WebChromeClient() {


            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                if (mWebInterface != null) {
                    mWebInterface.openFileChooser(uploadMsg);
                }
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                if (mWebInterface != null) {
                    mWebInterface.openFileChooser(uploadMsg);
                }
            }

            // For Android  > 4.1.1
            @Override
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                if (mWebInterface != null) {
                    mWebInterface.openFileChooser(uploadMsg);
                }
            }

            // For Android  >= 5.0
            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                if (mWebInterface != null) {
                    mWebInterface.openFileChooser5(filePathCallback);
                }
                return true;
            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.e("web======", "onReceivedTitle：" + title);
                if (mWebInterface != null) {
                    mWebInterface.onReceivedTitle(title);
                }
            }

            @Override
            public void onProgressChanged(WebView webView, int newProgress) {
                super.onProgressChanged(webView, newProgress);
                Log.e("web======", "onProgressChanged：" + newProgress);
                if (mWebInterface != null) {
                    mWebInterface.onProgressChanged(newProgress);
                }
            }


        });
    }

    public interface WebInterface {
        void errorOperation();

        void openFileChooser(ValueCallback<Uri> uploadMsg);

        void openFileChooser5(ValueCallback<Uri[]> filePathCallback);

        void onReceivedTitle(String title);

        void onProgressChanged(int newProgress);

        void onPageFinished(String url);

    }
}
