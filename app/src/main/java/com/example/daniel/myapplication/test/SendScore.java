package com.example.daniel.myapplication.test;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.daniel.myapplication.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


/**
 * @author Group 11 shootemupandroid
 */

/**
 * Sendscore activity
 * called when user clicks "sendscore" upon finishing game
 */

public class SendScore extends ActionBarActivity implements View.OnClickListener {

    /**
     * Variables which relate to xml of activity
     */
    private EditText value;
    private Button btn;
    private ProgressBar pb;
    private int playerscore;

    /**
     * Initializes the Activity values and creates the text field and button.
     * Recieves intent from game test activity (thescore)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        playerscore = intent.getIntExtra("thescore",0);
        setContentView(R.layout.activity_send_score);
        value=(EditText)findViewById(R.id.editText1);
        btn=(Button)findViewById(R.id.button1);
        pb=(ProgressBar)findViewById(R.id.progressBar1);
        pb.setVisibility(View.GONE);
        btn.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_score, menu);
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

    /**
     * Displays a message when button is clicked. If user inputs(atleast 1 character, otherwise toast is displayed) and clicks  the input to the database.
     * Executes asynchronous task, sends name string
     * @param view
     */
    public void onClick(View view) {
        // TODO Auto-generated method stub
        if(value.getText().toString().length()<1){                                                 //atleast 1 character
            // out of range
            Toast.makeText(this, "please enter name", Toast.LENGTH_LONG).show();
        }else{
            pb.setVisibility(View.VISIBLE);
            new MyAsyncTask().execute(value.getText().toString());
        }


    }

    /**
     * Asynchornous subclass, MyAsyncTask
     * extends asynchronouse task
     * overrides background method and execuutes http actions
     */
    private class MyAsyncTask extends AsyncTask<String, Integer, Double> {

        @Override
        protected Double doInBackground(String... params) {
            // TODO Auto-generated method stub
            postData(params[0]);
            return null;
        }

        /**
         * onPosttExecute-displays toast upon http execute completion
         * exits application
         * @param result
         */
        protected void onPostExecute(Double result) {
            pb.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Score sent, Thank you for playing!!", Toast.LENGTH_LONG).show();
        }

        /**
         * onProgressUpdate-
         * updates progress bar
         * @param progress
         */
        protected void onProgressUpdate(Integer... progress) {
            pb.setProgress(progress[0]);
        }

        /**
         * poData-handles http actions
         * called when user clicks submit-then asynchronous task is created and calls this method
         * Uses http address of our servlet code for onPost (for our webapplciation which holds all the scores)
         * @param valueIWantToSend
         */
        public void postData(String valueIWantToSend) {
            // Create new HttpClient and HTTPPOST
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://1-dot-galacticlashandroid.appspot.com/test");     //this is the url of our post servlet for our web application
            try {
                // Add the score and player name
                String scorestring=""+playerscore;
                httppost.addHeader("thename", valueIWantToSend);               //name added, given the identifier "thename"
                httppost.addHeader("score", scorestring);                      //score added, given the identifier "score"
                HttpResponse response = httpclient.execute(httppost);           //currently, no response is returned by webiste
                finish();                                                       //done, our webapp will use the identifiers to get the score.
            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
        }
    }
}

