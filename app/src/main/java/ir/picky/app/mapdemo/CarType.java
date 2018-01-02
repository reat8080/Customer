package ir.picky.app.mapdemo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CarType extends AppCompatActivity {

    String carType ;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/yekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_car_type);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void neysanchoose(View view) {
        carType = "نیسان";
        storeInDatabase();
        startActivity(new Intent(this, RequestDetail.class));
    }

    public void vanetchoose(View view) {
        carType = "وانت";
        storeInDatabase();
        startActivity(new Intent(this, RequestDetail.class));
    }

    public void camiunetchoose(View view) {
        carType = "کامیونت";
        storeInDatabase();
        startActivity(new Intent(this, RequestDetail.class));
    }

    public void minicamiunetchoose(View view) {
        carType = "مینی کامیونت";
        storeInDatabase();
        startActivity(new Intent(this, RequestDetail.class));
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
