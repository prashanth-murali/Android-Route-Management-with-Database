/*
* Created by Prashanth Murali on 4/17/17.
* Copyright © 2017 Prashanth Murali. All rights reserved.
* Right To Use for the instructor and the University to build and evaluate the software package
* @author Prashanth Murali mail to: pmurali10@asu.edu
* @version 1.0 April 17, 2017
 */
package edu.asu.msse.pmural10.Assignment7;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        DialogInterface.OnClickListener, TextView.OnEditorActionListener  {

    private Button addButt, removeButt;
    private EditText nametext, descriptiontext, categorytext, addressline1, addressline2, elevationtext, latdata, longdata;
    private Spinner placeSpinner;
    private String selectedPlace;
    private String[] places;
    private ArrayAdapter<String> placeAdapter;

    int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButt = (Button)findViewById(R.id.addButt);
        removeButt = (Button)findViewById(R.id.removeButt);

        nametext = (EditText) findViewById(R.id.namedata);
        descriptiontext = (EditText) findViewById(R.id.description);
        categorytext = (EditText) findViewById(R.id.categorydata);
        addressline1 = (EditText) findViewById(R.id.addressline1);
        addressline2 = (EditText) findViewById(R.id.addressline2);
        elevationtext = (EditText) findViewById(R.id.elevationdata);
        latdata = (EditText) findViewById(R.id.latdata);
        longdata = (EditText) findViewById(R.id.longdata);

        nametext.setOnEditorActionListener(this);
        descriptiontext.setOnEditorActionListener(this);
        categorytext.setOnEditorActionListener(this);
        addressline1.setOnEditorActionListener(this);
        addressline2.setOnEditorActionListener(this);
        elevationtext.setOnEditorActionListener(this);
        latdata.setOnEditorActionListener(this);
        longdata.setOnEditorActionListener(this);

        placeSpinner = (Spinner)findViewById(R.id.placeNamesSpinner);

        this.selectedPlace = this.setupPlaceSpinner();
        loadFields();
    }


    private String setupPlaceSpinner(){
        String ret = "unknown";
        try{
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase plcDB = db.openDB();
            Cursor cur = plcDB.rawQuery("select name from places;", new String[]{});
            ArrayList<String> al = new ArrayList<String>();
            while(cur.moveToNext()){
                try{
                    al.add(cur.getString(0));
                }catch(Exception ex){
                    android.util.Log.w(this.getClass().getSimpleName(),"exception stepping thru cursor"+ex.getMessage());
                }
            }
            places = (String[]) al.toArray(new String[al.size()]);
            ret = (places.length>0 ? places[0] : "unknown");
            placeAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, places);
            placeSpinner.setAdapter(placeAdapter);
            placeSpinner.setOnItemSelectedListener(this);
        }catch(Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"unable to setup place spinner");
        }
        return ret;
    }

    private void loadFields(){
        try{
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase plcDB = db.openDB();
            Cursor cur = plcDB.rawQuery("select description,category,address_title,address_street,elevation,latitude,longitude from places where name=? ;",
                    new String[]{selectedPlace});
            String description = "unknown";
            String category = "unknown";
            String address_title = "unknown";
            String address_street = "unknown";
            String elevation = "unknown";
            String latitude = "unknown";
            String longitude = "unknown";

            while (cur.moveToNext()){
                description = cur.getString(0);
                category = cur.getString(1);
                address_title = cur.getString(2);
                address_street = cur.getString(3);
                elevation = cur.getString(4);
                latitude = cur.getString(5);
                longitude = cur.getString(6);

            }
            nametext.setText(selectedPlace);
            descriptiontext.setText(description);
            categorytext.setText(category);
            addressline1.setText(address_title);
            addressline2.setText(address_street);
            elevationtext.setText(elevation);
            latdata.setText(latitude);
            longdata.setText(longitude);
            cur.close();
            plcDB.close();
            db.close();
        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception getting place info: "+
                    ex.getMessage());
        }
    }

    public void addClicked(View v){
        android.util.Log.d(this.getClass().getSimpleName(), "add Clicked. Adding: " + this.nametext.getText().toString());
        try{
            PlaceDB db = new PlaceDB((Context)this);
            SQLiteDatabase plcDB = db.openDB();
            String checkName=this.nametext.getText().toString();
            for(String i : places)
            {
                if(checkName.equals(i))
                {
                    String insert = "update places set description='"+this.descriptiontext.getText().toString()+"', category='"+this.categorytext.getText().toString()+"', address_title='"+this.addressline1.getText().toString()+"', address_street='"+this.addressline2.getText().toString()+"', elevation='"+this.elevationtext.getText().toString()+"', latitude='"+this.latdata.getText().toString()+"', longitude='"+this.longdata.getText().toString()+"' where name='"+checkName+"'";
                    plcDB.execSQL(insert);
                    plcDB.close();
                    db.close();
                    flag=1;
                }

            }


            if(flag==0)
            {
                String insert = "insert into places values('" + this.nametext.getText().toString() + "','" +
                        this.descriptiontext.getText().toString() + "','" + this.categorytext.getText().toString() + "','" + this.addressline1.getText().toString() + "','" + this.addressline2.getText().toString() + "','" + this.elevationtext.getText().toString() + "','" + this.latdata.getText().toString() + "','" + this.longdata.getText().toString() + "')";
                plcDB.execSQL(insert);
                plcDB.close();
                db.close();
                String addedName = this.nametext.getText().toString();
                setupPlaceSpinner();
                this.selectedPlace = addedName;
                this.placeSpinner.setSelection(Arrays.asList(places).indexOf(this.selectedPlace));
            }
            flag=0;

        } catch (Exception ex){
            android.util.Log.w(this.getClass().getSimpleName(),"Exception adding place information: "+
                    ex.getMessage());
        }

    }

    public void removeClicked(View v){
        android.util.Log.d(this.getClass().getSimpleName(), "remove Clicked");
        String delete = "delete from places where places.name='"+this.selectedPlace+"';";

        try {
            PlaceDB db = new PlaceDB((Context) this);
            SQLiteDatabase plcDB = db.openDB();
            plcDB.execSQL(delete);
            plcDB.close();
            db.close();
        }catch(Exception e){
            android.util.Log.w(this.getClass().getSimpleName()," error trying to delete place");
        }
        this.selectedPlace = this.setupPlaceSpinner();
        this.loadFields();
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event){

        android.util.Log.d(this.getClass().getSimpleName(), "onEditorAction: keycode " +
                ((event == null) ? "null" : event.toString()) + " actionId " + actionId);
        if(actionId== EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE){
            android.util.Log.d(this.getClass().getSimpleName(),"entry is: "+v.getText().toString());
        }
        return false;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        android.util.Log.d(this.getClass().getSimpleName(), "onClick with which= " +which);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.placeNamesSpinner) {
            this.selectedPlace = placeSpinner.getSelectedItem().toString();
            android.util.Log.d(this.getClass().getSimpleName(), "placeSpinner item selected " + selectedPlace);
            this.loadFields();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        android.util.Log.d(this.getClass().getSimpleName(), "onNothingSelected");
    }

}