package ir.picky.app.mapdemo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import ir.picky.app.mapdemo.database.DBHelper;
import ir.picky.app.mapdemo.services.MyService;
import ir.picky.app.mapdemo.util.NetworkHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/yekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        if (!NetworkHelper.hasNetworkAccess(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("دسترسی به اینترنت مقدور نیست");
            builder.setTitle("پیکی");
            builder.setIcon(R.drawable.exclamation);
            builder.setNegativeButton("بستن", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int id) {
                    finish();
                }
            });
            builder.show();

        } else {

            try {

                database = this.openOrCreateDatabase("Picky", MODE_PRIVATE, null);

                database.execSQL("CREATE TABLE IF NOT EXISTS user (fname VARCHAR, lname VARCHAR , number VARCHAR , email VARCHAR , islogin int(1),aut_key VARCHAR)");

                //database.execSQL("INSERT INTO user (number) VALUES ('09133196928')");

                Cursor c = database.rawQuery("SELECT * FROM user", null);

                int number = c.getColumnIndex("number");
                int isloginIndex = c.getColumnIndex("islogin");

                c.moveToFirst();

                if (c.getCount() == 0) {
                    // TODO
                    Intent intent = new Intent(this, SignUpActivity.class);
                    startActivity(intent);
                } else if (c.getInt(isloginIndex) == 0) {

                    Intent intent = new Intent(this, SignUpActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, ServiceType.class);
                    startActivity(intent);
                }

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
