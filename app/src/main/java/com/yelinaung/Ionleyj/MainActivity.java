package com.yelinaung.Ionleyj;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yelinaung.volley.R;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends ActionBarActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        Button ion, loopj, volley, clear;
        TextView result;
        String url = "http://salty-beyond-8843.herokuapp.com/";

        public PlaceholderFragment() {
        }

        @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.
                    inflate(R.layout.fragment_main, container, false);
            assert rootView != null;

            final Context mContext = getActivity().getApplicationContext();
            ion = (Button) rootView.findViewById(R.id.ion_download);
            loopj = (Button) rootView.findViewById(R.id.loopj_download);
            volley = (Button) rootView.findViewById(R.id.volley_download);
            clear = (Button) rootView.findViewById(R.id.clear);
            result = (TextView) rootView.findViewById(R.id.result);

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    result.setText("");
                }
            });

            assert mContext != null;
            ion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    result.setText("");
                    Calendar cal = Calendar.getInstance();
                    cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                    result.append("Ion - onClick at " + sdf.format(cal.getTime()) + "\n");

                    Ion.with(mContext)
                            .load(url)
                            .asString()
                            .withResponse()
                            .setCallback(
                                    // THIS IS SO BAD!!
                                    new FutureCallback<com.koushikdutta.ion.Response<String>>() {
                                @Override public void onCompleted(Exception e,
                                                                  com.koushikdutta.ion.Response<String>
                                                                          stringResponse) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                                    result.append("Ion - onCompletedd at " +
                                            sdf.format(cal.getTime()) + "\n");
                                    if (stringResponse != null) {
                                        Log.d("code", "req code -> " +
                                                stringResponse.getHeaders().getResponseCode());
                                        Log.d("req", "req -> " + stringResponse.getResult());
                                    } else {
                                        Log.d("null", "null response");
                                    }

                                }
                            });
//                            .setCallback(new FutureCallback<String>() {
//                                @Override
//                                public void onCompleted(Exception e, String s) {
//                                    String[] line = s.split("\\r?\\n");
//                                    for (int i = 0; i < line.length; i++) {
//                                        Log.d("row ",
//                                                "row[" + i + "] -> " + line[i]);
//                                        String data[] = line[i].split("\\^");
//                                        for (int j = 0; j < data.length; j++) {
//                                            Log.d("data ", "row[" + i + "], " +
//                                                    "data[" + j + "] -> " + data[j]);
//                                        }
//                                    }
//                                }
//                            });

//                    Ion.with(mContext, url).asInputStream().setCallback(new FutureCallback<InputStream>() {
//                        @Override
//                        public void onCompleted(Exception e, InputStream inputStream) {
//                            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//                            try {
//                                String line;
//                                while ((line = reader.readLine()) != null) {
//                                    String[] RowData = line.split("\\r?\\n");
//                                    for (int i = 0; i < RowData.length; i++) {
//                                        Log.d("row ", "row[" + i + "] -> " + RowData[i]);
//                                        String data[] = RowData[i].split("\\^");
//                                        for (int j = 0; j < data.length; j++) {
//                                            Log.d("data ", "row[" + i + "], data[" + j + "] -> " + data[j]);
//                                        }
//                                    }
//                                }
//                            } catch (IOException ex) {
//                                ex.printStackTrace();
//                            } finally {
//                                try {
//                                    inputStream.close();
//                                } catch (IOException f) {
//                                    f.printStackTrace();
//                                }
//                            }
//                        }
//                    });
                }
            });

            loopj.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                    result.append("Loopj - onClick at " + sdf.format(cal.getTime()) + "\n");

                    AsyncHttpClient httpClient = new AsyncHttpClient();
                    httpClient.get(url, new TextHttpResponseHandler() {

                        @Override public void onStart() {
                            super.onStart();
                            Calendar cal = Calendar.getInstance();
                            cal.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                            result.append("Loopj - onStart at " +
                                    sdf.format(cal.getTime()) + "\n");
                        }

                        @Override public void onSuccess(int statusCode, Header[] headers,
                                              String responseBody) {
                            super.onSuccess(statusCode, headers, responseBody);
                            Calendar cal = Calendar.getInstance();
                            cal.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                            result.append("Loopj - onSuccess at " +
                                    sdf.format(cal.getTime()) + "\n");

                            String[] line = responseBody.split("\\r?\\n");
                            for (int i = 0; i < line.length; i++) {
                                Log.d("row ", "row[" + i + "] -> " + line[i]);
                                String data[] = line[i].split("\\^");
                                for (int j = 0; j < data.length; j++) {
                                    Log.d("data ", "row[" + i + "], " +
                                            "data[" + j + "] -> " + data[j]);
                                }
                            }
                        }

                        @Override public void onFinish() {
                            super.onFinish();
                            Calendar cal = Calendar.getInstance();
                            cal.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                            result.append("Loopj - onFinish at " +
                                    sdf.format(cal.getTime()) + "\n");
                        }

                        @Override public void onFailure(String responseBody, Throwable error) {
                            super.onFailure(responseBody, error);
                            Calendar cal = Calendar.getInstance();
                            cal.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                            result.append("Loopj - onFailure at " +
                                    sdf.format(cal.getTime()) + "\n");
                        }
                    });
                }
            });

            volley.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                    Calendar cal = Calendar.getInstance();
                    cal.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                    result.append("Volley - onClick at " +
                            sdf.format(cal.getTime()) + "\n");

                    StringRequest request = new StringRequest(url,
                            new Response.Listener<String>() {
                                @Override public void onResponse(String response) {
                                    Calendar cal = Calendar.getInstance();
                                    cal.getTime();
                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                                    result.append("Volley - onResponse at " +
                                            sdf.format(cal.getTime()) + "\n");

                                    String[] line = response.split("\\r?\\n");
                                    for (int i = 0; i < line.length; i++) {
                                        Log.d("row ", "row[" + i + "] -> " + line[i]);
                                        String data[] = line[i].split("\\^");
                                        for (int j = 0; j < data.length; j++) {
                                            Log.d("data ", "row[" + i + "], " +
                                                    "data[" + j + "] -> " + data[j]);
                                        }
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override public void onErrorResponse(VolleyError error) {
                            Log.d("error", error.getMessage());
                            Calendar cal = Calendar.getInstance();
                            cal.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
                            result.append("Volley - onErrorResponse at " +
                                    sdf.format(cal.getTime()) + "\n");
                        }
                    }
                    );
                    RequestQueue queue = Volley.newRequestQueue(mContext);
                    queue.add(request);
                }
            });

            return rootView;
        }
    }
}
