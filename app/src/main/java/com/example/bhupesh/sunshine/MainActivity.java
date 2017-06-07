package com.example.bhupesh.sunshine;

import android.os.AsyncTask;
import android.support.v4.media.VolumeProviderCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    EditText mSearchBoxEditText;
    TextView mUrlDisplayTextView;
    TextView mSearchResultsTextView;
    TextView merrorMessageTextView;
    ProgressBar mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchBoxEditText=(EditText)findViewById(R.id.et_search_box);
        mUrlDisplayTextView=(TextView)findViewById(R.id.tv_url_display);
        mSearchResultsTextView=(TextView)findViewById(R.id.tv_github_search_results_json);
        merrorMessageTextView=(TextView)findViewById(R.id.tv_error_message_display);
        mProgress=(ProgressBar)findViewById(R.id.pb_loading_indicator);
    }

    private void makeGithubSearchQuery() throws IOException {
        String githubQuery=mSearchBoxEditText.getText().toString();
        URL githubSearchUrl=NetWorkUtils.buildUrl(githubQuery);
        mUrlDisplayTextView.setText(githubSearchUrl.toString());
       new GithubQueryTask().execute(githubSearchUrl);

    }

    private void showJsonDataView()
    {
        merrorMessageTextView.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage()
    {
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        merrorMessageTextView.setVisibility(View.VISIBLE);
    }

    public class GithubQueryTask extends AsyncTask<URL, Void, String>
    {


        @Override
        protected void onPreExecute() {

            mProgress.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl=urls[0];
            String githubSearchResult = null;
            try {
                githubSearchResult=NetWorkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return githubSearchResult;
        }

        @Override
        protected void onPostExecute(String s) {

            mProgress.setVisibility(View.INVISIBLE);
            if (s!=null && !s.equals(""))
            {
                showJsonDataView();
                mSearchResultsTextView.setText(s);
            }
            showErrorMessage();


            super.onPostExecute(s);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemId=item.getItemId();

        if (menuItemId==R.id.action_search)
        {
            try {
                makeGithubSearchQuery();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
