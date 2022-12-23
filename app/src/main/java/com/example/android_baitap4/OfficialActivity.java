package com.example.android_baitap4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity {

    private TextView title, name, party, address, phone, email, website, location;
    private ImageButton imgFacebook, imgYoutube, imgGoogle, imgTwitter;
    private ImageView imgOfficial;
    private View officialLayout;
    private Official official;
    private Channel cnFacebook, cnYoutube, cnGoogle, cnTwitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        setUpComponent();
        setUpData();
    }


    public void setUpComponent() {
        title = findViewById(R.id.title);
        name = findViewById(R.id.name);
        party = findViewById(R.id.party);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        website = findViewById(R.id.website);
        location = findViewById(R.id.location);
        imgFacebook = findViewById(R.id.imgFacebook);
        imgYoutube = findViewById(R.id.imgYoutube);
        imgGoogle = findViewById(R.id.imgGoogle);
        imgTwitter = findViewById(R.id.imgTwitter);
        imgOfficial = findViewById(R.id.imgOfficial);
        officialLayout = findViewById(R.id.officialLayout);
    }

    public void setUpData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        official = (Official) bundle.getSerializable(MainActivity.OFFICIAL_DATA_KEY);

        if (!(official.getTitle() == null || official.getTitle().length() == 0))
            title.setText(official.getTitle());

        if (!(official.getName() == null || official.getName().length() == 0))
            name.setText(official.getName());

        if (!(official.getParty() == null || official.getParty().length() == 0))
            party.setText(official.getParty());
        officialLayout.setBackgroundColor(getColor(party.getText().toString()));

        if (!(official.getName() == null || official.getName().length() == 0))
            name.setText(official.getName());

        if (!(official.getAddress() == null || official.getAddress().length() == 0))
            address.setText(official.getAddress());

        if (!(official.getPhone() == null || official.getAddress().length() == 0))
            phone.setText(official.getPhone());

        if (!(official.getEmail() == null || official.getEmail().length() == 0))
            email.setText(official.getEmail());

        if (!(official.getUrls() == null || official.getUrls().length() == 0))
            website.setText(official.getUrls());

        for (Channel channel : official.getChannels()) {
            if (channel.getType().equals("Facebook")) {
                cnFacebook = channel;
            } else if (channel.getType().equals("Youtube")) {
                cnYoutube = channel;
            } else if (channel.getType().equals("GooglePlus")) {
                cnGoogle = channel;
            } else if (channel.getType().equals("Twitter")) {
                cnTwitter = channel;
            }
        }
        if (cnFacebook == null)
            imgFacebook.setVisibility(View.INVISIBLE);
        if (cnYoutube == null)
            imgYoutube.setVisibility(View.INVISIBLE);
        if (cnGoogle == null)
            imgGoogle.setVisibility(View.INVISIBLE);
        if (cnTwitter == null)
            imgTwitter.setVisibility(View.INVISIBLE);

        location.setText(bundle.getString(MainActivity.LOCATION_DATA_KEY));
        setUpOfficialImg(official);
    }

    private int getColor(String party) {
        int color;
        switch (party) {
            case "Republican Party":
                color = Color.RED;
                return color;
            case "Democratic Party":
                color = Color.BLUE;
                return color;
            default:
                color = Color.BLACK;
                return color;
        }
    }

    public void facebookClicked(View v) {
        String FACEBOOK_URL = "https://www.facebook.com/" + cnFacebook.getId();
        String urlToUse;
        PackageManager packageManager = getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else {
                urlToUse = "fb://page/" + cnFacebook.getId();
            }
        } catch (PackageManager.NameNotFoundException e) {
            urlToUse = FACEBOOK_URL;
        }
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToUse));
        startActivity(facebookIntent);
    }

    public void twitterClicked(View v) {
        Intent intent = null;
        String name = cnTwitter.getId();
        try {
            // get the Twitter app if possible
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + name));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } catch (Exception e) {
            // no Twitter app, revert to browser
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + name));
        }
        startActivity(intent);
    }

    public void googlePlusClicked(View v) {
        String name = cnGoogle.getId();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setClassName("com.google.android.apps.plus", "com.google.android.apps.plus.phone.UrlGatewayActivity");
            intent.putExtra("customAppUri", name);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/" + name)));
        }
    }

    public void youTubeClicked(View v) {
        String name = cnYoutube.getId();
        Intent intent = null;
        try {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.google.android.youtube");
            intent.setData(Uri.parse("https://www.youtube.com/" + name));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/" + name)));
        }
    }

    public void imgOfficialClickHandle(View v) {
        Intent intent = new Intent(this, PhotoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MainActivity.LOCATION_DATA_KEY, location.getText().toString());
        bundle.putSerializable(MainActivity.OFFICIAL_DATA_KEY, official);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void setUpOfficialImg(Official official)
    {
        if (official.getPhotoURL()!=null&&official.getPhotoURL().length()!=0) {
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    final String changedUrl = official.getPhotoURL().replace("http:", "https:");
                    picasso.load(changedUrl).into(imgOfficial);
                    Log.e("Loi", exception.toString());
                }
            }).build();
            picasso.load(official.getPhotoURL()).into(imgOfficial);
        }
        if(!networkCheck())
        {
            imgOfficial.setImageResource(R.drawable.loadingscreen);
        }
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