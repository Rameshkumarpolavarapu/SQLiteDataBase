package com.example.ramesh.sqlitedatabase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;


public class CustomAdapter extends ArrayAdapter<Contact>  {
    private Activity parentActivity;
    private Context mContext;
    private DatabaseHandler db  =   null;

    public CustomAdapter(ArrayList<Contact> data, Activity parentActivity,Context context) {
        super(context,R.layout.listdummy,data);
        this.mContext   =   context;
        this.parentActivity  =   parentActivity;
    }



    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //getting data item from position

        final Contact contact =   getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null){

            viewHolder  =   new ViewHolder();
            LayoutInflater inflater =   LayoutInflater.from(getContext());
            convertView =   inflater.inflate(R.layout.listdummy,parent,false);
            viewHolder.tv1  = convertView.findViewById(R.id.t1);
            viewHolder.tv2  = convertView.findViewById(R.id.t2);
            viewHolder.tv3  = convertView.findViewById(R.id.t3);
            viewHolder.update  =  convertView.findViewById(R.id.update);
            viewHolder.delete  =   convertView.findViewById(R.id.delete);
            convertView.setTag(viewHolder);

        }else {
            viewHolder  = (ViewHolder) convertView.getTag();
        }

        assert contact != null;
        viewHolder.tv1.setText(contact.getID());
        viewHolder.tv2.setText(contact.getName());
        viewHolder.tv3.setText(contact.getPhoneNumber());

        viewHolder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
               final String id    =    getItem(position).getID();
               final String name    =    getItem(position).getName();
               final String phone    =    getItem(position).getPhoneNumber();

                AlertDialog.Builder alertDialog =   new AlertDialog.Builder(parentActivity);
                alertDialog.setTitle("Updating");
                alertDialog.setCancelable(false);

                LayoutInflater layoutInflater   = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                 @SuppressLint("InflateParams") final View view1   =   layoutInflater.inflate(R.layout.dialog,null);
                alertDialog.setView(view1);
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                final EditText editText    =    view1.findViewById(R.id.name1);
                final EditText editText1    =    view1.findViewById(R.id.phone1);
                editText.setText(name);
                editText1.setText(phone);
                alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                            if (name.equals(editText.getText().toString().trim())&& phone.equals(editText1.getText().toString().trim())){
                                Toast.makeText(mContext, "No change in Old Record", Toast.LENGTH_SHORT).show();
                            }else {
                            String newName  =   editText.getText().toString().trim();
                            String newPhone  =   editText1.getText().toString().trim();
//                                Toast.makeText(parentActivity, id+" Name : "+newName+" Phone"+newPhone, Toast.LENGTH_SHORT).show();
//                                db.updateContact(new Contact(id,newName,newPhone));
                                Contact contact1    =   new Contact();
                                contact1.setID(id);
                                contact1.setName(newName);
                                contact1.setPhoneNumber(newPhone);
                                db  =   new DatabaseHandler(mContext);
                                db.updateContact(contact1);
                                MainActivity.dataModels.set(position,contact1);
                                notifyDataSetChanged();
                                db.close();

                            }
                    }
                });
                  alertDialog.create().show();

            }
        });

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = getItem(position).getID();
                db  =   new DatabaseHandler(mContext);
                db.deleteContact(id);
                CustomAdapter.this.remove(getItem(position));
                notifyDataSetChanged();
                db.close();
            }
        });

        return convertView;
    }
    private static class ViewHolder{
        TextView tv1;
        TextView tv2;
        TextView tv3;
        Button update;
        Button delete;
    }


}
