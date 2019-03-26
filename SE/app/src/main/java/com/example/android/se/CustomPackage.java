package com.example.android.se;

import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class CustomPackage extends AppCompatActivity {

Button btcp,btncreate;
String testing="",cost="";
ListView lvcp;
    ArrayList<String> alist,alist1;
Spinner spcp,spcploc;
ArrayAdapter<String> adapter,adapter1,adapter2;
ArrayList<String> arrayList,itemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Intent(this,test.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_package);
        btcp=findViewById(R.id.btcustom);
        lvcp=findViewById(R.id.lvcustom);
        spcp=findViewById(R.id.spcustom);
        spcploc=findViewById(R.id.spcploc);
        btncreate=findViewById(R.id.btncreate);
        arrayList=new ArrayList<String>();
        itemList=new ArrayList<String>();

        new BG().execute("getlocs");
      //  arrayList.add("Default");
       // new BG().execute("getact",spcploc.getSelectedItem().toString());

        adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrayList);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,itemList);
        spcp.setAdapter(adapter1);
        btcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (spcp.getCount() > 0) {
                        itemList.add(spcp.getSelectedItem().toString());
                        arrayList.remove(spcp.getSelectedItem().toString());
                        adapter1.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), "No more activities left in this location", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),"Content loading.Please Wait a moment",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String uname = "eren11";
                    String lid = alist1.get(alist.indexOf(spcploc.getSelectedItem().toString()));
                    if (itemList.size() > 0) {
                        new BG().execute("create");
                        for (int i = 0; i < itemList.size(); i++) {
                            new BG().execute("make", lid, itemList.get(i));
                        }
                        new BG().execute("userup", uname);

                        //  Toast.makeText(getApplicationContext(),"Your custom package costs"+cost,Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select activities", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Content loading.Please Wait a moment",Toast.LENGTH_SHORT).show();
                }
            }
        });
        spcploc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             //   Toast.makeText(getApplicationContext(),spcploc.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
               itemList.clear();
               adapter.notifyDataSetChanged();
                new BG().execute("getact",spcploc.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        lvcp.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tv=(TextView)view;
                String str=tv.getText().toString();
              //  Toast.makeText(getApplicationContext(),str,Toast.LENGTH_SHORT).show();
                arrayList.add(str);
                itemList.remove(str);
                adapter1.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                return false;
            }
        });
       // spcp.setAdapter(adapter1);
        lvcp.setAdapter(adapter);
    }

    public class BG extends AsyncTask<String,Void,String>{



        @Override
        protected String doInBackground(String... voids) {
            String method=voids[0];
            if(method.equals("userup"))
            {
                try {

                    String uname=voids[1];
                    String entry_url = "https://androidcon.000webhostapp.com/userup.php";
                    URL url1 = new URL(entry_url);
                    HttpURLConnection httpURLConnection1 = (HttpURLConnection) url1.openConnection();
                    httpURLConnection1.setRequestMethod("POST");
                    httpURLConnection1.setDoOutput(true);
                    OutputStream OS = httpURLConnection1.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                    String data = URLEncoder.encode("uname", "UTF-8") + "=" + URLEncoder.encode(uname, "UTF-8") + "&" +
                            URLEncoder.encode("pid", "UTF-8") + "=" + URLEncoder.encode(testing, "UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS = httpURLConnection1.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(IS, "iso-8859-1"));
                    //     String response=" ";
                    String line = " ";
                    StringBuilder response = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.d("ans", "doInBackground: "+response.toString());
                    cost=response.toString();
                    bufferedReader.close();
                    IS.close();
                }catch (Exception ex)
                {
                    Log.d("error", "doInBackground: "+ex.toString());
                }
            }
            if(method.equals("make"))
            {try {

                String aname=voids[2],lid=voids[1];
                String entry_url = "https://androidcon.000webhostapp.com/addpkdata.php";
                URL url1 = new URL(entry_url);
                HttpURLConnection httpURLConnection1 = (HttpURLConnection) url1.openConnection();
                httpURLConnection1.setRequestMethod("POST");
                httpURLConnection1.setDoOutput(true);
                OutputStream OS = httpURLConnection1.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("pid", "UTF-8") + "=" + URLEncoder.encode(testing, "UTF-8") + "&" +
                        URLEncoder.encode("lid", "UTF-8") + "=" + URLEncoder.encode(lid, "UTF-8") + "&" +
                        URLEncoder.encode("aname", "UTF-8") + "=" + URLEncoder.encode(aname, "UTF-8");;
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection1.getInputStream();
                IS.close();
            }catch (Exception ex)
            {
                Log.d("error", "doInBackground: "+ex.toString());
            }
            }
            if(method.equals("create")) {
                try {
                    String locid;
                    String login_url = "https://androidcon.000webhostapp.com/nextpid.php";
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    //     String response=" ";
                    String line = " ";
                    StringBuilder response = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }

                    bufferedReader.close();
                    inputStream.close();
                    testing=response.toString();
                    locid=alist1.get(alist.indexOf(spcploc.getSelectedItem().toString()));
                    //testing=locid;

                    String entry_url = "https://androidcon.000webhostapp.com/makepk.php";
                    URL url1=new URL(entry_url);
                    HttpURLConnection httpURLConnection1=(HttpURLConnection)url1.openConnection();
                    httpURLConnection1.setRequestMethod("POST");
                    httpURLConnection1.setDoOutput(true);
                    OutputStream OS=httpURLConnection1.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                    String data=URLEncoder.encode("pid","UTF-8")+"="+URLEncoder.encode(testing,"UTF-8")+"&"+
                            URLEncoder.encode("lid","UTF-8")+"="+URLEncoder.encode(locid,"UTF-8")+"&"+
                            URLEncoder.encode("pname","UTF-8")+"="+URLEncoder.encode("Cust"+testing,"UTF-8");;
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream IS=httpURLConnection1.getInputStream();
                    IS.close();



                } catch (Exception ex) {
                    Log.d("Error", ex.toString());
                }
                return method;
            }
            if(method.equals("getlocs"))
            {
                try {
                    String login_url = "https://androidcon.000webhostapp.com/segetloc.php";
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    //     String response=" ";
                    String line = " ";
                    StringBuilder response = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line + '\n');
                    }
                    bufferedReader.close();
                    inputStream.close();
                    //    JOptionPane.showMessageDialog(null,response);

                    String locname,locid;
                    alist=new ArrayList<String>();
                    alist1=new ArrayList<String>();
                    JSONObject job =new JSONObject(response.toString());
                    JSONArray jar1 = (JSONArray) job.get("values");
                    for(int i=0;i<jar1.length();i++)
                    {
//Store the JSON objects in an array
//Get the index of the JSON object and print the values as per the index
                        JSONObject jval = (JSONObject)jar1.get(i);
                        locid=jval.get("lid").toString();

                        locname=jval.get("lname").toString();
                        alist.add(locname);
                        alist1.add(locid);
                        Log.d("size",alist1.size()+"");


                    }


                }catch (Exception ex)
                {
                    Log.d("Error",ex.toString());
                }
                return method;
            }

            if(method.equals("getact"))
            {
                try {
                    arrayList.clear();

                    String login_url = "https://androidcon.000webhostapp.com/segetact.php";
                    String lname=voids[1];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    OutputStream OS=httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                    String data= URLEncoder.encode("lname","UTF-8")+"="+URLEncoder.encode(lname,"UTF-8");
                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    OS.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    //     String response=" ";
                    String line = " ";
                    StringBuilder response = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line + '\n');
                    }
                    bufferedReader.close();
                    inputStream.close();
                    //    JOptionPane.showMessageDialog(null,response);

                    String actname;
                    //alist1=new ArrayList<String>();
                    JSONObject job =new JSONObject(response.toString());
                    JSONArray jar1 = (JSONArray) job.get("values");
                    for(int i=0;i<jar1.length();i++)
                    {
//Store the JSON objects in an array
//Get the index of the JSON object and print the values as per the index
                        JSONObject jval = (JSONObject)jar1.get(i);
                        actname=jval.get("aname").toString();
                        arrayList.add(actname);


                    }

                }catch (Exception ex)
                {
                    Log.d("Error",ex.toString());
                }
            }

            return method;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
           // Toast.makeText(getApplicationContext(),response, Toast.LENGTH_SHORT).show();
            if(response.equals("getlocs")) {
                adapter2 = new ArrayAdapter<String>(CustomPackage.this, android.R.layout.simple_list_item_1, alist);
                spcploc.setAdapter(adapter2);
            }
            else if(response.equals("getact")) {
                //Toast.makeText(getApplicationContext(),arrayList.get(0),Toast.LENGTH_SHORT).show();
                adapter1 = new ArrayAdapter<String>(CustomPackage.this, android.R.layout.simple_list_item_1, arrayList);
                spcp.setAdapter(adapter1);
            }
            else if(response.equals("userup"))
            {
                Toast.makeText(getApplicationContext(),"Cost of the custom package is Rs"+cost,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
