package mrj.example.flags.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import mrj.example.flags.R;
import mrj.example.flags.adapter.MainAdapter;
import mrj.example.flags.model.FlickrModel;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JavohirAI
 */

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView; 
    private List<FlickrModel> mFlags;

    private AlertDialog mDialog;
    private TextView dialogtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = getLayoutInflater().inflate(R.layout.dialog_textview,null,false);
        dialogtxt = view.findViewById(R.id.dialog_txt);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setView(view);
        mBuilder.setTitle(getResources().getString(R.string.dialog_title_download_data));
        mDialog = mBuilder.create();

        mFlags = new ArrayList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new LoadData(this).execute();
    }

    private class LoadData extends AsyncTask<Void, Integer, Integer> {

        private Context mContext;

        public LoadData(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            mDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            dialogtxt.setText(getResources().getString(R.string.dialog_item_download_data,values));
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            mFlags.clear();
            dialogtxt.setText(getResources().getString(R.string.dialog_title_open_connection));
            StringBuffer buffer = new StringBuffer();
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String urlstr = Uri.parse("https://api.flickr.com/services/feeds/photos_public.gne").buildUpon()
                    .appendQueryParameter("tagmode", "any")
                    .appendQueryParameter("lang", "en-us")
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .build().toString();

            try {
                URL url = new URL(urlstr);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
            String jsonStr = buffer.toString();
            if (jsonStr.equals("")) {
                return 0;
            }
            if (jsonStr != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");
                    // looping through All Contacts
                    for (int i = 0; i < jsonArray.length(); i++) {
                        publishProgress(i);
                        JSONObject c = jsonArray.getJSONObject(i);
                        String name = c.getString("title");
                        String urldisplay = c.getJSONObject("media").getString("m");
                        Bitmap mIcon11 = null;
                        try {
                            mIcon11 =BitmapFactory.decodeStream(new java.net.URL(urldisplay).openStream());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mFlags.add(new FlickrModel(name,urldisplay,mIcon11));
                    }
                } catch (final JSONException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
            return 1;
        }
        @Override
        protected void onPostExecute(Integer response) {
            super.onPostExecute(response);
            if(mDialog.isShowing()){
                mDialog.dismiss();
            }
            if(response==0){
                Toast.makeText(mContext,getResources().getString(R.string.dialog_title_bad_connection),Toast.LENGTH_SHORT).show();
            }
            MainAdapter adapter = new MainAdapter(mContext, mFlags);
            mRecyclerView.setAdapter(adapter);
        }
    }

}