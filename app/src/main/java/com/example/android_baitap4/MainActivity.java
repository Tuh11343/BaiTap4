package com.example.android_baitap4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Official> officialList;
    private ArrayList<String> defaultLocationList;
    private RecyclerView rclOfficial;
    private TextView tvLocation;
    private OfficialAdapter mAdapter;
    private int numberOfRequest=0;

    public final static String OFFICIAL_DATA_KEY="OFFICIAL_DATA_KEY";
    public final static String LOCATION_DATA_KEY="LOCATION_DATA_KEY";
    private String ZIP_CODE="60601";
    private String CITY_STATE="Chicago, IL";
    public final static String LOCATION="Chicago, IL , 60601";
    public final static String KEY="AIzaSyA1RgK9pVXHWefmWkbMqJz97Pm2uB_kzlY";
    //https://civicinfo.googleapis.com/civicinfo/v2/representatives?address=Chicago&key=AIzaSyBbygnIEHrjTp90nMXmpx32iWPj868Ko5A

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpComponents();
        setUpDefaultLocationList();
    }

    public void setUpComponents()
    {
        officialList=new ArrayList<>();
        if(networkCheck())
            officialList=getOfficialList();
        else
        {
            final Dialog dialog=new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_nonetwork);
            Window window=dialog.getWindow();
            if(window==null)
                return;
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams windowAttributes=window.getAttributes();
            windowAttributes.gravity=Gravity.CENTER;
            window.setAttributes(windowAttributes);

            dialog.show();
        }
        rclOfficial=findViewById(R.id.rclMain);
        tvLocation=findViewById(R.id.location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.about:
                Intent intent=new Intent(this,AboutActivity.class);
                startActivity(intent);
                return true;

            case R.id.search:
                openDialog(Gravity.CENTER);
                return true;
            default:
                throw new IllegalStateException("Unexpected value: " + item);
        }
    }

    public void openDialog(int gravity)
    {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_search);
        Window window=dialog.getWindow();
        if(window==null)
            return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes=window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);

        EditText tvSearch=dialog.findViewById(R.id.searchBar);

        Button btnSearch=dialog.findViewById(R.id.btnSearch);
        Button btnCancel=dialog.findViewById(R.id.btnCancel);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                onSearchClickHandle(tvSearch);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void onOfficialClickHandle(Official official)
    {
        Intent intent=new Intent(this,OfficialActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable(OFFICIAL_DATA_KEY,official);
        bundle.putString(LOCATION_DATA_KEY, tvLocation.getText().toString());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public ArrayList<Official> getOfficialList()
    {
        RequestQueue mQueue= Volley.newRequestQueue(this);
        String DATA_URL = "https://www.googleapis.com/civicinfo/v2/representatives?key="+KEY+"&address="+ZIP_CODE;
        numberOfRequest++;

        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading Official");
        progressDialog.show();

        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, DATA_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                OfficialLoader officialLoader=new OfficialLoader(MainActivity.this);
                officialList=officialLoader.parseJSON(response);
                numberOfRequest-=1;

                if(numberOfRequest==0)
                {
                    mAdapter=new OfficialAdapter(officialList, new IOfficialClickHandle() {
                        @Override
                        public void onOfficialClick(Official official) {
                            onOfficialClickHandle(official);
                        }
                    });
                    rclOfficial.setLayoutManager(new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false));
                    DividerItemDecoration decoration=new DividerItemDecoration(MainActivity.this,DividerItemDecoration.VERTICAL);
                    decoration.setDrawable(getResources().getDrawable(R.drawable.rcl_divider));
                    rclOfficial.addItemDecoration(decoration);
                    rclOfficial.setAdapter(mAdapter);
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Call complete",Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Loi","Loi lay du lieu volley :"+error.toString());
            }
        });
        mQueue.add(request);
        return officialList;
    }

    public void onSearchClickHandle(EditText editText)
    {
        String zip_code=editText.getText().toString();
        if(zip_code==null||zip_code.length()==0)
        {
            Toast.makeText(this,"Can't find the location",Toast.LENGTH_SHORT).show();
            return;
        }
        for(String location :defaultLocationList)
        {
            String[] temp=location.split(",");
            if(temp[temp.length-1].trim().equals(zip_code))
            {
                Log.e("Loi",zip_code);
                CITY_STATE=temp[0];
                ZIP_CODE=temp[temp.length-1];
                //In case there no city,state
                if(CITY_STATE.equals(ZIP_CODE))
                    CITY_STATE="";
                officialList=getOfficialList();
                mAdapter.notifyDataSetChanged();
                return;
            }
        }
        Toast.makeText(this, "ZIP CODE is not correct please try again!", Toast.LENGTH_SHORT).show();
    }

    public void setUpDefaultLocationList()
    {
        defaultLocationList=new ArrayList<>();
        defaultLocationList.add("New York City, NY , 10002");
        defaultLocationList.add("Chicago, IL , 60601");
        defaultLocationList.add("MA , 02110");
        defaultLocationList.add("Seattle, WA , 98101");
        defaultLocationList.add("New Orleans, LA , 70032");
        defaultLocationList.add("Las Vegas,NV , 89104");
        defaultLocationList.add("Miami, FL , 33130");
        defaultLocationList.add("Los Angeles, CA, 90001");
        defaultLocationList.add("Washington, DC , 20001");
        defaultLocationList.add("Honolulu, HI, 96804");
        defaultLocationList.add("Dallas, TX ,75202");
        defaultLocationList.add("Key West, FL ,33040");
        defaultLocationList.add("Denver, CO , 80212");
        defaultLocationList.add("Atlanta, GA , 30301");
        defaultLocationList.add("Philadelphia, PA , 19019");
        defaultLocationList.add("Phoenix, AZ , 85001");
        defaultLocationList.add("Portland, OR , 97211");
        defaultLocationList.add("Nome, AK , 99762");
    }

    public boolean networkCheck()
    {
        ConnectivityManager cm= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm==null)
            return false;
        NetworkInfo networkInfo=cm.getActiveNetworkInfo();
        return networkInfo!=null&&networkInfo.isConnected();
    }

}