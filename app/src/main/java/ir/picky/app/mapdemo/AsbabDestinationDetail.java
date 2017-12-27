package ir.picky.app.mapdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ir.picky.app.mapdemo.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AsbabDestinationDetail extends Activity {

    public int level, unit;

    String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/yekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.asbab_destination_detail);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        if (width > height) {
            getWindow().setLayout((int) (width * .4), (int) (height * .7));
        } else {
            getWindow().setLayout((int) (width * .8), (int) (height * .4));
        }


    }
    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void nextstepdestinationHandler(View view) {
        EditText v;

        v = findViewById(R.id.Destinationunit);

        if (v.getText().length() == 0) {
            unit = 0;
        } else {
            unit = Integer.valueOf(v.getText().toString());
        }

        v= findViewById(R.id.destinationdetailbar);
        if (v.getText().length() == 0){
            desc = "NaN";
        } else {
            desc = v.getText().toString();
        }

        v= findViewById(R.id.Destinationfloor);
        if (v.getText().length() == 0){
            Toast.makeText(this, "طبقه را به درستی وارد کنید.", Toast.LENGTH_SHORT).show();
        } else {
            level = Integer.valueOf(v.getText().toString());
            startActivity(new Intent(this, CarType.class));
        }
    }

    public void cancelButtonHandler(View view) {
        finish();
    }
}
