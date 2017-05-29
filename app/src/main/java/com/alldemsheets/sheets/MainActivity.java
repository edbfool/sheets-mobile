package com.alldemsheets.sheets;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class MainActivity extends AppCompatActivity {
    public final static String SHEET = "com.alldemsheets.sheets";
    ArrayList<String> sheetList = new ArrayList<>();
    String getParams = "";
    Button[] buttons = new Button[3];
    ListView sheetListView;
    ArrayAdapter<String> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sheetListView = (ListView) findViewById(R.id.sheetList);

        ///Instanciation d'un adapter pour lier des données à la listView
        adapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, sheetList);
        sheetListView.setAdapter(adapter);

        //instanciation de la classe héritée d'AsyncTasc newHttpCall
        final newHttpCall runner = new newHttpCall();

        //testHttps();
        //newHttp("");

        runner.execute("http://www.alldemsheets.com/mobile.php");

        buttons[0] = (Button) findViewById(R.id.button);
        buttons[1] = (Button) findViewById(R.id.button2);
        buttons[2] = (Button) findViewById(R.id.button3);

        //Liaison de chacun des boutons à un listener qui déclenchera l'instanciation d'une nouvelle classe newHttpCall
        //Chacun de ces boutons passe une chaîne de caractère en paramètre qui sera utilisé dans l'URL
        for (Button b : buttons){
            b.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    String newParams;
                    getParams = ((Button) v).getText().toString();
                    switch(getParams){
                        case "MOST DOWNLOADED":
                            newParams = "?type=most";
                            break;
                        case "TOP RATED":
                            newParams = "?type=top";
                            break;
                        case "NEWEST":
                            newParams= "?type=new";
                            break;
                        default:
                            newParams = "";
                            break;
                    }
                    new newHttpCall().execute("http://www.alldemsheets.com/mobile.php"+newParams);
                }
            });
        }

        //Liaison d'un listener sur chaque élément de la listView
        sheetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (parent.getItemAtPosition(position).toString());
                Toast.makeText(MainActivity.this, selected, Toast.LENGTH_SHORT).show();

                //Instanciation d'une nouvelle intention pour ouvrir la deuxième activité
                Intent newActivity = new Intent(MainActivity.this, IntentActivity.class);
                //Passage de données dans la nouvelle activité
                newActivity.putExtra(SHEET, selected);

                startActivity(newActivity);
            }
        });

    }

    //Création d'une classe hérité de AsyncTask
    private class newHttpCall extends AsyncTask<String, Integer, ArrayList>{

        //Méthode principal de la classe AsyncTask, c'est celle qui permet d'éxécuter des tâches de fond
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            System.out.println(strings[0]);
            try {
                //Instanciation de l'URL grâce aux paramètres passés
                URL url = new URL(strings[0]);

                //Ouverture de la connexion
                urlConnection = (HttpURLConnection) url.openConnection();

                //Conversion des données reçues en bytes en chaîne de caractère
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;

                //instantiation d'un nouvel ArrayList pour vider les précédentes données
                sheetList = new ArrayList<>();
                while ((line = reader.readLine()) != null){
                    //Pour chaque ligne du reader, l'élément sera ajouté à la fin de l'ArrayList
                    System.out.println(line);
                    sheetList.add(line);
                }
            }  catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return sheetList;
        }

        //Méthode appellée après l'éxécution de toutes les tâches de fond réalisées dans doInBackground()
        @Override
        protected void onPostExecute(ArrayList list){
            adapter.clear();
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }


    /*protected void newHttp(String argument){
        final String arg = argument;
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                URL url = null;
                try {
                    url = new URL("http://www.alldemsheets.com/mobile.php"+arg);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String line;
                    sheetList = new ArrayList<>();
                    while ((line = reader.readLine()) != null){
                        System.out.println(line);
                        sheetList.add(line);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            }
        });
        thread.interrupt();
        thread.start();
    }




    protected void testHttps(){
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run(){
                CertificateFactory cf;
                try {
                    cf = CertificateFactory.getInstance("X.509");
                } catch (CertificateException e) {
                    e.printStackTrace();
                    cf = null;
                }
                InputStream caInput = getResources().openRawResource(R.raw.certification);
                try {
                    System.out.println(caInput.available());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Certificate ca =

                newHttpCall runOnTouch = new newHttpCall();null;
                try {
                    //génère le certificat à partir des données brut récupérées dans le fichier .crt
                    ca = cf.generateCertificate(caInput);
                    System.out.println("ca="+((X509Certificate) ca).getSubjectDN());
                } catch (CertificateException e){
                    System.out.println(e);
                    try {
                        if (caInput != null) caInput.close();
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                }
                System.out.println("CERTIFICATE>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                System.out.println(ca);
                HttpsURLConnection urlConnection = null;
                try {
                    String keyStoreType = KeyStore.getDefaultType();
                    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
                    keyStore.load(null,null);
                    keyStore.setCertificateEntry("ca",ca);

                    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                    tmf.init(keyStore);


                    SSLContext context = SSLContext.getInstance("TLS");
                    context.init(null, tmf.getTrustManagers(), null);

                    URL url = new URL("https://www.alldemsheets.com/kaboum.php");
                    urlConnection = (HttpsURLConnection)url.openConnection();
                    urlConnection.setSSLSocketFactory(context.getSocketFactory());
                    System.out.println( urlConnection.getSSLSocketFactory());
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = reader.readLine()) != null){
                        System.out.println(line);
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyStoreException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (CertificateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            }
        });
        thread.interrupt();
        thread.start();
    }*/
}
