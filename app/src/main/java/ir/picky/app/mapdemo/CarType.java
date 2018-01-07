package ir.picky.app.mapdemo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CarType extends AppCompatActivity {


    int barTypeHaml = 0 , carType =1;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/yekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_car_type);

    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void vanetchoose(View view) {
        carType = 1;
        storeInDatabase();
        startActivityRequest();
    }

    public void neysanchoose(View view) {
        carType = 2 ;
        storeInDatabase();
        startActivityRequest();
    }

    public void minicamiunetchoose(View view) {
        carType = 3;
        storeInDatabase();
        startActivityRequest();
    }

    public void camiunetchoose(View view) {
        carType = 4;
        storeInDatabase();
        startActivityRequest();
    }

    public void startActivityRequest() {

        Intent intentEx = getIntent();
        barTypeHaml = intentEx.getIntExtra("barTypeHaml" , 0);
        Intent intent = new Intent(this, RequestDetail.class) ;
        intent.putExtra("key" , barTypeHaml);
        startActivity(intent);
    }

    private void storeInDatabase() {
        database = this.openOrCreateDatabase("Picky", MODE_PRIVATE, null);
        Cursor c = database.rawQuery("SELECT * FROM request", null);
        int lastRowId = c.getCount();
        ContentValues requestValue = new ContentValues();
        requestValue.put("cartype" , carType);
        database.update("request", requestValue, "rowid = "+lastRowId  ,null);
    }



}
