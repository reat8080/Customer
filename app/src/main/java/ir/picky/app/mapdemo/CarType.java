package ir.picky.app.mapdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CarType extends AppCompatActivity {

    String carType ;

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
        startActivity(new Intent(this, RequestDetail.class));
    }

    public void vanetchoose(View view) {
        carType = "وانت";
        startActivity(new Intent(this, RequestDetail.class));
    }

    public void camiunetchoose(View view) {
        carType = "کامیونت";
        startActivity(new Intent(this, RequestDetail.class));
    }

    public void minicamiunetchoose(View view) {
        carType = "مینی کامیونت";
        startActivity(new Intent(this, RequestDetail.class));
    }
}
