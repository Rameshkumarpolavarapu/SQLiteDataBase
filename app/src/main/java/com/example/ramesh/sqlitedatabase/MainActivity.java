package com.example.ramesh.sqlitedatabase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private DatabaseHandler db  =   null;
    @SuppressLint("StaticFieldLeak")
    public static EditText et1,et2;
    CustomAdapter adapter;
    //    private  String[] names;
    //    private  String[] phones;
    //    private  int[] ids;
    static ArrayList<Contact> dataModels;
    Runnable run;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1         = (EditText) findViewById(R.id.name);
        et2         = (EditText) findViewById(R.id.phone);
        db          = new DatabaseHandler(this);
        ListView listView = (ListView) findViewById(R.id.list);
        dataModels  =   new ArrayList<>();
        final List<Contact> contacts = db.getAllContacts();

        for (Contact cn : contacts) {
            dataModels.add(new Contact(cn.getID(),cn.getName(),cn.getPhoneNumber()));
        }
         adapter = new CustomAdapter(dataModels,MainActivity.this,this);
        listView.setAdapter(adapter);




//         run = new Runnable() {
//            public void run() {
//                //reload content
//                adapter.clear();
//                adapter.addAll(contacts);
//                adapter.notifyDataSetChanged();
//                listView.invalidateViews();
//                listView.refreshDrawableState();
//            }
//        };

        //        // Inserting Contacts
//        Log.d("Insert: ", "Inserting ..");
//        db.addContact(new Contact("Ravi", "9100000000"));
//        db.addContact(new Contact("Srinivas", "9199999999"));
//        db.addContact(new Contact("Tommy", "9522222222"));
//        db.addContact(new Contact("Karthik", "9533333333"));
//
//        // Reading all contacts
//        Log.d("Reading: ", "Reading all contacts..");
//        List<Contact> contacts = db.getAllContacts();
//
//        for (Contact cn : contacts) {
//            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
//            // Writing Contacts to log
//            Log.d("Name -->", log);
//        }
    }

    public void clickAdd(View view){
        String name;
        String phone;

        if (!et1.getText().toString().trim().equals("")) {
            name =   et1.getText().toString().trim();

        }else {
            et1.setError("Empty Field");
            name =   null;
        }
        if (!et2.getText().toString().trim().equals("")) {
            phone =   et2.getText().toString().trim();
        } else {
            et2.setError("Empty Field");
            phone =   null;
        }
        if (name ==null | phone ==null){
            Toast.makeText(this, "Both Field are Mandatory", Toast.LENGTH_SHORT).show();
        }else {
            long result = db.addContact(new Contact(name, phone));
            adapter.add(new Contact(String.valueOf(result),name,phone));
            adapter.notifyDataSetChanged();
            if (result>1){
                Toast.makeText(this, "Record Inserted Successfully At"+ result+" row" , Toast.LENGTH_SHORT).show();}
        }

    }


    public void clickSearch(View view) {

        AlertDialog.Builder builder =   new AlertDialog.Builder(this);
        builder.setTitle("Search");

        LinearLayout linearLayout   =   new LinearLayout(this);

        TextInputLayout textInputLayout =   new TextInputLayout(this);

        LinearLayout.LayoutParams params  =   new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(params);
        textInputLayout.setLayoutParams(params);
        textInputLayout.setHint("Enter Name");
        final EditText editText   =   new EditText(this);
        editText.setLayoutParams(params);
        final String search   =   editText.getText().toString().trim();
        textInputLayout.addView(editText);

        linearLayout.addView(textInputLayout);
        builder.setView(linearLayout);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                String searchWord   =   editText.getText().toString().trim();
                searchText(MainActivity.this, searchWord);
                dialogInterface.dismiss();
            }
        });
        builder.create().show();



    }

    public void searchText(MainActivity mainActivity, String name){
        String s;

        AlertDialog.Builder b =   new AlertDialog.Builder(mainActivity);
        b.setTitle("Search");

        b.setPositiveButton("okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        db  =   new DatabaseHandler(mainActivity);
        Contact contact = null;
        try {
            contact = db.getContact(name);
        }
        catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        if (contact!=null) {
             s = "" + contact.getID() + " " + contact.getName() + " " + contact.getPhoneNumber();
        }else {
             s = "No Matching Data";
        }
            LinearLayout layout   =   new LinearLayout(mainActivity);
        LinearLayout.LayoutParams params  =   new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params1  =   new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1.setMargins(10,30,10,10);

        layout.setLayoutParams(params);
        TextView textView   =   new TextView(mainActivity);
        textView.setTextSize(20.0f);
        textView.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setLayoutParams(params1);

        textView.setText(s);
        layout.addView(textView);
        b.setView(layout);




        b.create().show();

    }
}
