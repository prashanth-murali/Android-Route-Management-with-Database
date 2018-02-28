/*
* Created by Prashanth Murali on 4/17/17.
* Copyright © 2017 Prashanth Murali. All rights reserved.
* Right To Use for the instructor and the University to build and evaluate the software package
* @author Prashanth Murali mail to: pmurali10@asu.edu
* @version 1.0 April 17, 2017
 */
package edu.asu.msse.pmural10.Assignment7;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public class PlaceDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static String dbName = "places";
    private String dbPath;
    private SQLiteDatabase plcDB;
    private final Context context;



    public PlaceDB(Context context){
        super(context,dbName, null, DATABASE_VERSION);
        this.context = context;
        dbPath = context.getFilesDir().getPath()+"/";
        android.util.Log.d(this.getClass().getSimpleName(),"dbpath: "+dbPath);
    }



    public void createDB() throws IOException {
        this.getReadableDatabase();
        try {
            copyDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean checkDB(){
        SQLiteDatabase checkDB = null;
        boolean ret = false;
        try{
            String path = dbPath + dbName + ".db";

            File aFile = new File(path);
            if(aFile.exists()){
                checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
                if (checkDB!=null) {

                    Cursor tabChk = checkDB.rawQuery("SELECT name FROM sqlite_master where type='table' and name='places';", null);
                    boolean plcTabExists = false;
                    if(tabChk == null){

                    }else{
                        tabChk.moveToNext();

                        plcTabExists = !tabChk.isAfterLast();
                    }
                    if(plcTabExists){
                        Cursor c= checkDB.rawQuery("SELECT * FROM places", null);
                        c.moveToFirst();
                        while(! c.isAfterLast()) {
                            String plcName = c.getString(0);


                            c.moveToNext();
                        }
                        ret = true;
                    }
                }
            }
        }catch(SQLiteException e){
            e.printStackTrace();
        }
        if(checkDB != null){
            checkDB.close();
        }
        return ret;
    }

    public void copyDB() throws IOException{
        try {
            if(!checkDB()){

                InputStream ip =  context.getResources().openRawResource(R.raw.placesdb);

                File aFile = new File(dbPath);
                if(!aFile.exists()){
                    aFile.mkdirs();
                }
                String op=  dbPath  +  dbName +".db";
                OutputStream output = new FileOutputStream(op);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ip.read(buffer))>0){
                    output.write(buffer, 0, length);
                }
                output.flush();
                output.close();
                ip.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public SQLiteDatabase openDB() throws SQLException {
        String myPath = dbPath + dbName + ".db";
        if(checkDB()) {
            plcDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        }else{
            try {
                this.copyDB();
                plcDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        return plcDB;
    }

    @Override
    public synchronized void close() {
        if(plcDB != null)
            plcDB.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
