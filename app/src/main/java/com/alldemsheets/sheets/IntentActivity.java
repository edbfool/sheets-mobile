package com.alldemsheets.sheets;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by EdbFool on 10/04/2017.
 */

public class IntentActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String sheet = i.getStringExtra(MainActivity.SHEET);
        WebView webSheet = new WebView(IntentActivity.this);
        webSheet.getSettings().setJavaScriptEnabled(true);
        //webSheet.getSettings().setBuiltInZoomControls(true);
        webSheet.loadUrl("https://docs.google.com/gview?embedded=true&url=https://www.alldemsheets.com/res/"+sheet+".pdf");
        setContentView(webSheet);
    }
}
