package com.example.android_baitap4;

import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OfficialLoader{

    private MainActivity mainActivity;
    private ArrayList<Official> officialList;

    public OfficialLoader(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public ArrayList<Official> parseJSON(JSONObject data)
    {
        ArrayList<Official> tempList = new ArrayList<>();
        setUpLocation(data);
        try
        {
            JSONArray offices = (JSONArray) data.get("offices");
            JSONArray officials = (JSONArray) data.get("officials");

            for(int i = 0; i<offices.length(); i++)
            {
                JSONObject office = (JSONObject) offices.get(i);
                JSONObject officialIndices = (JSONObject) offices.get(i);
                JSONArray index = officialIndices.getJSONArray("officialIndices");

                for(int j = 0; j< index.length(); j++)
                {
                    JSONObject officialData_JSON = (JSONObject) officials.get(index.getInt(j));
                    Official official = fetchOfficialDetails(officialData_JSON);
                    official.setTitle(office.getString("name"));
                    tempList.add(official);
                }
            }
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi parse Json:"+e.toString());
        }
        return  tempList;
    }

    private Official fetchOfficialDetails(JSONObject officialData_Json)
    {
        Official official=new Official();
        try{
            official.setAddress(getAddressFromData(officialData_Json));
            official.setParty(getPartyFromData(officialData_Json));
            official.setName(getNameFromData(officialData_Json));
            official.setChannels(getChannelsFromData(officialData_Json));
            official.setEmail(getEmailFromData(officialData_Json));
            official.setPhone(getPhoneFromData(officialData_Json));
            official.setPhotoURL(getPhotoURLFromData(officialData_Json));
            official.setUrls(getURLFromData(officialData_Json));

        }catch(Exception er)
        {
            Log.e("Loi","Loi fectch Loader:"+er.toString());
        }

        return official;
    }

    private String getAddressFromData(JSONObject officialData_json)
    {
        String finalAddress = "";
        try
        {
            JSONArray addresses = (JSONArray) officialData_json.get("address");
            JSONObject address = (JSONObject) addresses.get(0);
            String line1 = getLine1FromData(address);
            String line2 = getLine2FromData(address);
            String city = getCityFromData(address);
            String state = getStateFromData(address);
            String zip = getZIPFromData(address);
            finalAddress = line1 + ", " + (line2.equals("")?line2 + "":line2 + ", ") + city + ", " + state + ", " +zip;
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay dia chi loader:"+e.toString());
        }
        return finalAddress;
    }

    private String getLine1FromData(JSONObject address)
    {
        String line1 = "";
        try
        {
            line1 = address.getString("line1");
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay line 1 loader:"+e.toString());
        }
        return line1;
    }

    private String getLine2FromData(JSONObject address)
    {
        String line2 = "";
        try
        {
            line2 = address.getString("line2");
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay line 2 loader:"+e.toString());
        }
        return line2;
    }

    private String getCityFromData(JSONObject address)
    {
        String city = "";
        try
        {
            city = address.getString("city");
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay city loader:"+e.toString());
        }
        return city;
    }

    private String getStateFromData(JSONObject address)
    {
        String state = "";
        try
        {
            state = address.getString("state");
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay state loader:"+e.toString());
        }
        return state;
    }

    private String getZIPFromData(JSONObject address)
    {
        String zip = "";
        try
        {
            zip = address.getString("zip");
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay zip loader:"+e.toString());
        }
        return zip;
    }

    private String getNameFromData(JSONObject officialData_json)
    {
        String name = "";
        try
        {
            String nameTemp=officialData_json.getString("name");
            name = officialData_json.getString("name");
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay name loader:"+e.toString());
        }

        return name;
    }

    private String getURLFromData(JSONObject officialData_json)
    {
        String URL = "";
        try
        {
            JSONArray urls = (JSONArray) officialData_json.get("urls");
            String urlTemp=urls.get(0).toString();
            URL = urls.get(0).toString();
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay URL loader:"+e.toString());
        }

        return URL;
    }

    private String getEmailFromData(JSONObject officialData_json)
    {
        String email = "";

        try
        {
            JSONArray urls = (JSONArray) officialData_json.get("emails");
            String emailTemp=urls.get(0).toString();
            email = urls.get(0).toString();
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay email loader:"+e.toString());
        }

        return email.toLowerCase();
    }

    private String getPhoneFromData(JSONObject officialData_json)
    {
        String phone = "";

        try
        {
            JSONArray urls = (JSONArray) officialData_json.get("phones");
            phone = urls.get(0).toString();
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay phone loader:"+e.toString());
        }

        return phone;
    }

    private String getPartyFromData(JSONObject officialData_json)
    {
        String party = "";
        try {
            party = officialData_json.getString("party");
        }catch (Exception e)
        {
            Log.e("Loi","Loi lay party loader:"+e.toString());
        }

        return  party;
    }

    private ArrayList<Channel> getChannelsFromData(JSONObject officialData_json)
    {
        ArrayList<Channel> tempList = new ArrayList<>();
        Channel temp;

        try
        {
            JSONArray channels = (JSONArray) officialData_json.get("channels");
            for(int i = 0; i < channels.length(); i++)
            {
                JSONObject channel = (JSONObject) channels.get(i);
                temp = new Channel(channel.getString("type"), channel.getString("id"));
                tempList.add(temp);
            }
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay channels loader:"+e.toString());
        }
        return tempList;
    }

    private String getPhotoURLFromData(JSONObject officialData_json)
    {
        String photoURL = "";

        try
        {
            String photoURLTemp=officialData_json.getString("photoUrl");
            photoURL = officialData_json.getString("photoUrl");
        }
        catch (Exception e)
        {
            Log.e("Loi","Loi lay photoURL loader:"+e.toString());
        }

        return photoURL;
    }

    private void setUpLocation(JSONObject officialData_json)
    {
        TextView location = mainActivity.findViewById(R.id.location);
        try
        {
            JSONObject normalizedInput= (JSONObject) officialData_json.get("normalizedInput");
            String city = normalizedInput.getString("city");
            String state = normalizedInput.getString("state");
            String zip = normalizedInput.getString("zip");

            String locationText = (city.equals("")?"":city+", ") + (zip.equals("")?state:state+", ") + (zip.equals("")?"":zip);
            location.setText(locationText);
        }
        catch (Exception e)
        {
            Log.e("Loi","Lỗi set location Loader:"+e.toString());
        }
    }
}
