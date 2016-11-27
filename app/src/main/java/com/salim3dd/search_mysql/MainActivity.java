package com.salim3dd.search_mysql;

import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private String Search_Name = "";
    private ArrayList<ListItem> listUsers = new ArrayList();
    private ProgressDialog progressDialog;
    private int LimitS = 0;
    private ListView listView;
    private ListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        listAdapter = new ListAdapter(listUsers);
        listView.setAdapter(listAdapter);
        progressDialog = new ProgressDialog(this);

        Get_All_Users(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_page, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_searchable));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.m_users) {
            Search_Name = "";
            Get_All_Users(0);
        }
        if (id == R.id.m_close) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (query.length() > 1) {
            Search_Name = query.trim();
            listUsers.clear();
            Get_All_Users(0);
        } else {
            Toast.makeText(this, "كلمة البحث حرفين أو أكثر", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void Get_All_Users(int limit) {

        CheckInternetConnection cic = new CheckInternetConnection(getApplicationContext());
        Boolean Ch = cic.isConnectingToInternet();
        if (!Ch) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage(getString(R.string.Waite));
            progressDialog.setCancelable(true);
            progressDialog.show();

            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    "http://salim3dd.esy.es/show_all_users.php?Search_Name=" + Search_Name + "&limit=" + limit
                    ,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonResponse = jsonArray.getJSONObject(0);
                                JSONArray jsonArray_usersS = jsonResponse.getJSONArray("All_users");
                                if (LimitS == 0) {
                                    listUsers.clear();
                                }
                                for (int i = 0; i < jsonArray_usersS.length(); i++) {
                                    JSONObject responsS = jsonArray_usersS.getJSONObject(i);

                                    int id = responsS.getInt("id");
                                    String name = responsS.getString("name");

                                    listUsers.add(new ListItem(id, name));

                                }
                                listAdapter.notifyDataSetChanged();

                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(stringRequest);
            stringRequest.setShouldCache(false);

            new CountDownTimer(2000, 1000) {
                public void onTick(long millisUntilFinshed) {
                }
                public void onFinish() {
                    if (progressDialog.isShowing()) {
                        progressDialog.hide();
                        Get_All_Users(LimitS);
                    }
                }
            }.start();

        }

    }

    class ListAdapter extends BaseAdapter {
        ArrayList<ListItem> listUser = new ArrayList<>();

        ListAdapter(ArrayList<ListItem> list_all) {

            this.listUser = list_all;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

        }

        @Override
        public int getCount() {
            return listUser.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return listUser.get(position);
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.row_itme_all_users, null);
            TextView name = (TextView) view.findViewById(R.id.text_row_itme_all_users_name);

            name.setText(listUser.get(i).getName());

            return view;
        }
    }
}
