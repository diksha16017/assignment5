package com.example.ajay.downloadtask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends ActionBarActivity {

    TextView download_text;
    Button btn_txt;
    private static final String TAG = "MAIN_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        download_text = (TextView)findViewById(R.id.download_text);
        btn_txt = (Button)findViewById(R.id.btn_abt);
        Log.d(TAG,"in on create");

    }


    public void onClickDownload(View view)
    {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
        {
            Toast.makeText(this,"connected",Toast.LENGTH_SHORT).show();
            new DownloadFilesTask().execute();
        }
        else
        {
            Toast.makeText(this,"No internet connection is available ...",Toast.LENGTH_SHORT).show();
        }

    }

    public static String getHtml(String url) throws IOException {
        // Build and set timeout values for the request.
        URLConnection connection = (new URL(url)).openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();

        // Read and store the result line by line then return the entire string.
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder html = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            html.append(line);
        }
        in.close();

        return html.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private class DownloadFilesTask extends AsyncTask<Void, Void, String> {
        protected String doInBackground(Void... urls) {
            String document = "hello";
            try
            {
                document = getHtml("https://www.iiitd.ac.in/about");
            }
            catch (Exception e)
            {

            }
            return document;

        }



        String getResult(String res)
        {
            String removeHtml = res.replaceAll("<[^>]*>", "");

            int indexEstimate = removeHtml.indexOf("Indraprastha");

            String stringFinal = removeHtml.substring(indexEstimate + 16, removeHtml.length());

            int firstIndex=stringFinal.indexOf("Indraprastha");
            int lastIndex=stringFinal.indexOf("far");

            String result =stringFinal.substring(firstIndex, lastIndex + 3);

            return result;
        }
        protected void onPostExecute(String result) {


            if(result == null)
            {
                Toast.makeText(getApplicationContext(),"Downloaded Problem...",Toast.LENGTH_SHORT).show();
            }
            else
            {
                String finalResult = result.replaceAll("<[^>]*>", "");
                //String finalResult = result.replaceAll("\\<[^\\>]*\\>", "");

                System.out.println(finalResult);
                String myResult = getResult(finalResult);
                Log.d(TAG,finalResult);
                btn_txt.setVisibility(View.INVISIBLE);
                download_text.setText("");
                download_text.setTextSize(17);
                download_text.setText(myResult+".");

            }

        }
    }
}
