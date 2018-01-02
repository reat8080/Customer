package ir.picky.app.mapdemo;

import android.content.Context;
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
    int srclevel, srcunit, declevel, decunit, distance, tedad_kargar, hazinedar, is_bime;

    //srcdesc VARCHAR , decdesc VARCHAR , is_active INT(1), cartype VARCHAR ,  tozihat VARCHAR  , bartype VARCHAR

    int hazineHaml = 0 , hazineKargar = 0 , hazineKol = 0 , hazineBime = 0 , hazineLevel = 0 , hazineHajim = 0;

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
            invoiceCalculate();
            updateUi();
        }

    }

    private void invoiceCalculate() {

        if (distance > 5000) {
            hazineHaml = distance - 5000;
            hazineHaml += 25000;
            hazineHaml -= hazineHaml % 500 ;
        } else {
            hazineHaml = 25000;
        }

        if (tedad_kargar > 0) {
            hazineKargar = tedad_kargar * 45000;
            RelativeLayout kargarRL = findViewById(R.id.kargarRelative);
            kargarRL.setVisibility(View.VISIBLE);
        }

        if(hazinedar > 0){
            hazineHajim = 10000 * (srclevel + declevel) ;
            
        }

        hazineKol =  hazineHaml+hazineKargar+hazineBime+hazineHajim+hazineLevel;
    }

    private void updateUi() {
        TextView hazineHamlTV = findViewById(R.id.hazineHaml);
        hazineHamlTV.setText(String.valueOf(hazineHaml) + " تومان ");

        TextView hazineKargarTV = findViewById(R.id.hazineKargar);
        hazineKargarTV.setText(String.valueOf(hazineKargar) + " تومان ");

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

    }
}
