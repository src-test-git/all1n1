package com.quoinsight.legacy;
/*
*/


public class WebViewActivity extends android.app.Activity {

  //////////////////////////////////////////////////////////////////////

  @Override protected void onCreate(android.os.Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.webviewactivity);

    try {

      android.webkit.WebView web1 = (android.webkit.WebView)findViewById(R.id.web1);
        web1.getSettings().setJavaScriptEnabled(true);
        web1.setWebViewClient(new android.webkit.WebViewClient(){
          @Override public void onPageFinished(android.webkit.WebView webView, String url) {
            // webView.loadUrl("javascript:document.getElementById('txt1').value='testing ...';");
          }
        });
        web1.loadUrl("file:///android_asset/about.html"); // [ .\src\main\assets ]
        web1.requestFocus();

    } catch(Exception e) {

      commonGui.writeMessage(this, "CalendarActivity.findViewById", e.getMessage());
      return;

    }

  }

}
