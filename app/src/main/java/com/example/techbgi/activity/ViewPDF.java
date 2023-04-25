package com.example.techbgi.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.techbgi.R;
import com.example.techbgi.activity.fullscreen.BaseActivity;

import java.net.URLEncoder;

import kotlinx.coroutines.ExecutorCoroutineDispatcher;

public class ViewPDF extends AppCompatActivity {

    WebView pdfView;
    String filename,fileurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdf);

        pdfView = (WebView) findViewById(R.id.viewpdf);
        pdfView.getSettings().setJavaScriptEnabled(true);

        filename = getIntent().getStringExtra("filename");
        fileurl = getIntent().getStringExtra("fileurl");

        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle(filename);
        pd.setMessage("opening... ");

        String url = "";
        try{
            url = URLEncoder.encode(fileurl,"UTF-8");

        }catch (Exception e){
        }

        pdfView.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);

        pdfView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pd.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pd.dismiss();
            }
        });

    }
}