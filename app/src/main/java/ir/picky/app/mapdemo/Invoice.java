package ir.picky.app.mapdemo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLData;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Invoice extends AppCompatActivity {

    SQLiteDatabase database;
    int srclevel, srcunit, declevel, decunit, distance, tedad_kargar, hazinedar, is_bime , arzeshebar , carType;

    //srcdesc VARCHAR , decdesc VARCHAR , is_active INT(1), cartype VARCHAR ,  tozihat VARCHAR  , bartype VARCHAR

    int hazineHaml = 0 , hazineKargar = 0 , hazineKol = 0 , hazineBime = 0 , hazineLevel = 0 ,
            hazineHajim = 0 , hazineHamlPaye = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/yekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_invoice);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        database = this.openOrCreateDatabase("Picky", MODE_PRIVATE, null);
        Cursor c = database.rawQuery("SELECT * FROM request", null);

        //srclat DOUBLE, srclon DOUBLE ," +
        //declat DOUBLE, declon DOUBLE, srclevel INT(3) , srcunit  INT(2), declevel  INT(3), decunit  INT(2) ," +
        // distance INT , srcdesc VARCHAR , decdesc VARCHAR , is_active INT(1), cartype VARCHAR ," +
        // tozihat VARCHAR ,  is_hazinedar INT(2) , is_bime INT(1) , tedad_kargar INT(2), bartype VARCHAR
        int srcunitIndex = c.getColumnIndex("srcunit");
        int srclevelIndex = c.getColumnIndex("srclevel");
        int decunitIndex = c.getColumnIndex("decunit");
        int declevelIndex = c.getColumnIndex("declevel");
        int distanceIndex = c.getColumnIndex("distance");
        int isbimeIndex = c.getColumnIndex("is_bime");
        int kargarIndex = c.getColumnIndex("tedad_kargar");
        int bartypeIndex = c.getColumnIndex("bartype");
        int hazinedarIndex = c.getColumnIndex("is_hazinedar");
        int cartypeIndex = c.getColumnIndex("cartype");
        int arzeshebarIndex = c.getColumnIndex("arzeshebar");

        c.moveToLast();
        if (c.getCount() == 0) {
            Toast.makeText(this, "در محاسبه قیمت خطایی رخ داده است.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            srclevel = c.getInt(srclevelIndex);
            srcunit = c.getInt(srcunitIndex);
            declevel = c.getInt(declevelIndex);
            decunit = c.getInt(decunitIndex);
            distance = c.getInt(distanceIndex);
            tedad_kargar = c.getInt(kargarIndex);
            hazinedar = c.getInt(hazinedarIndex);
            is_bime = c.getInt(isbimeIndex);
            arzeshebar = c.getInt(arzeshebarIndex);
            carType = c.getInt(cartypeIndex);
            invoiceCalculate();
            updateUi();
        }

    }

    private void invoiceCalculate() {
        if (carType == 1) hazineHamlPaye = 25000;
        else if (carType == 2) hazineHamlPaye = 30000;
        else if (carType == 3) hazineHamlPaye = 60000;
        else if (carType == 4) hazineHamlPaye = 80000;
        if (distance > 5000) {
            hazineHaml = distance - 5000;
            hazineHaml += hazineHamlPaye;
            hazineHaml -= hazineHaml % 500 ;
        } else {
            hazineHaml = hazineHamlPaye;
        }

        if (tedad_kargar > 0) {
            hazineKargar = tedad_kargar * 45000;
            RelativeLayout kargarRL = findViewById(R.id.kargarRelative);
            kargarRL.setVisibility(View.VISIBLE);
            if (srclevel >1 || declevel>1) {
                hazineLevel = 5000 * (srclevel + declevel - 2);
                RelativeLayout levelRL = findViewById(R.id.levelRelative);
                levelRL.setVisibility(View.VISIBLE);
            }
            if(hazinedar > 0){
                hazineHajim = (10000 * (srclevel + declevel)) * tedad_kargar ;
                RelativeLayout hazineRL = findViewById(R.id.hazinedarRL);
                hazineRL.setVisibility(View.VISIBLE);
            }

        }
        if (is_bime > 0){
            if (arzeshebar > 1000000) {
                int zaribbime = arzeshebar/1000000 ;
                hazineBime = 30000 * zaribbime ;
                hazineBime -= hazineBime % 10000 ;
                TextView bime = findViewById(R.id.hazineBime);
                bime.setText(String.valueOf(hazineBime) + " تومان ");
            } else {
                TextView bime = findViewById(R.id.hazineBime);
                bime.setText("دارد");
            }
        }

        hazineKol =  hazineHaml+hazineKargar+hazineBime+hazineHajim+hazineLevel;
    }

    private void updateUi() {
        TextView hazineHamlTV = findViewById(R.id.hazineHaml);
        hazineHamlTV.setText(String.valueOf(hazineHaml) + " تومان ");

        TextView hazineKargarTV = findViewById(R.id.hazineKargar);
        hazineKargarTV.setText(String.valueOf(hazineKargar) + " تومان ");

        TextView hazineLevelTV = findViewById(R.id.hazineLevel);
        hazineLevelTV.setText(String.valueOf(hazineLevel) + " تومان ");

        TextView hazineHajimTV = findViewById(R.id.hazineHajim);
        hazineHajimTV.setText(String.valueOf(hazineHajim) + " تومان ");

        TextView hazineKolTV = findViewById(R.id.hazineKol);
        hazineKolTV.setText(String.valueOf(hazineKol) + " تومان ");
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void checkTakhfif(View view) {

    }

    public void requestSubmitHandler(View view) {
        startActivity(new Intent(this , RequestsActivity.class));
    }
}
