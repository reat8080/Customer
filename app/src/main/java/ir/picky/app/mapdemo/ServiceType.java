package ir.picky.app.mapdemo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;

import java.io.IOException;
import java.io.InputStream;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ServiceType extends AppCompatActivity {

    boolean doubleBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/yekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_service_type);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void barButtonClicked(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("type" , 1);
        startActivity(intent);
    }

    public void asbabButtonClicked(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("type" , 2);
        startActivity(intent);
    }

    public void bargashtyButtonClicked(View view) {
        Intent intent = new Intent(this, BargashtyDetail.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (doubleBack) {
            super.onBackPressed();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            int pid = android.os.Process.myPid();
            android.os.Process.killProcess(pid);
            //not necessary
//            if (Build.VERSION.SDK_INT  >= Build.VERSION_CODES.LOLLIPOP)
//                finishAndRemoveTask();
//            else
//                finish();
        }

        this.doubleBack = true;
        Toast.makeText(this, "برای خروج از برنامه بازگشت را دوباره بزنید.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                doubleBack = false;
            }
        }, 2000);
    }
}
