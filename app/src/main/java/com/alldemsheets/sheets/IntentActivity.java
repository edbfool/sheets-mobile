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

        //Récupération des données passées par la MainActivity
        Intent i = getIntent();
        String sheet = i.getStringExtra(MainActivity.SHEET);

        //Instanciation d'une nouvelle webView pour afficher le fichier PDF sans avoir à le télécharger.
        WebView webSheet = new WebView(IntentActivity.this);
        webSheet.getSettings().setJavaScriptEnabled(true);

        //Utilisation de l'API Google Docs pour afficher un PDF grâce à une url passée en paramètres
        webSheet.loadUrl("https://docs.google.com/gview?embedded=true&url=https://www.alldemsheets.com/res/"+sheet+".pdf");

        //Affichage de la webView dans la vue.
        setContentView(webSheet);
    }
}
