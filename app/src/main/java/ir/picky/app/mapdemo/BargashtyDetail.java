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
import android.widget.EditText;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BargashtyDetail extends AppCompatActivity {

    SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/yekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        setContentView(R.layout.activity_bargashty_detail);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void continuClicked(View view) {
        EditText source = findViewById(R.id.source);
        EditText des = findViewById(R.id.destination);
        EditText weight = findViewById(R.id.weight);
        if (source.getText().length() == 0 || des.getText().length() == 0 || weight.getText().length() == 0) {
            Toast.makeText(this, "لطفا اطلاعات را وارد نمایید.", Toast.LENGTH_SHORT).show();
        } else {
            storeInDatabase();
            Intent intent = new Intent(this, RequestDetail.class);
            intent.putExtra("cost" ,  8);
            startActivity(intent);
        }
    }
    private void storeInDatabase() {
        database = this.openOrCreateDatabase("Picky", MODE_PRIVATE, null);
        Cursor c = database.rawQuery("SELECT * FROM request", null);
        int lastRowId = c.getCount();
        ContentValues requestValue = new ContentValues();
        requestValue.put("cartype" , 5);
        database.update("request", requestValue, "rowid = "+lastRowId  ,null);
    }
}
