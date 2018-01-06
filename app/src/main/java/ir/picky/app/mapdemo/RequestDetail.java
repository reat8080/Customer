package ir.picky.app.mapdemo;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RequestDetail extends AppCompatActivity {

    String barType , tozihatKoli;
    Dialog dialog , arzeshbarDialog , hazinedarDialog ;
    int arzeshebar = 0 , isHazinedar = 0 , isBime=0 , tedadKargar = 0;
    Switch bimeSwitch , hazinedarSwitch;
    SQLiteDatabase database;
    //Intent intent = getIntent();
    int  barTypeHaml ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/yekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_request_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        View v = findViewById(R.id.barTypeButton);
        Intent intent = getIntent();
        barTypeHaml = intent.getIntExtra("key" , barTypeHaml);
        if ( barTypeHaml == 1) {
            Button button = findViewById(R.id.barTypeButton);
            button.setText("وسایل منزل");
        }
        barTypeHadler(v);


        SeekBar kargarSeek =  findViewById(R.id.kargarSeekBar);
        kargarSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            TextView tedadKargar = (TextView) findViewById(R.id.tedadKargar);

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                RequestDetail.this.tedadKargar = i;
                if(i==0){
                    tedadKargar.setText("خیر");
                } else {
                    tedadKargar.setText(String.valueOf(i) + " کارگر ");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(RequestDetail.this, RequestDetail.this.tedadKargar +" کارگر انتخاب شد. ", Toast.LENGTH_SHORT).show();
            }
        });

         hazinedarSwitch = (Switch) findViewById(R.id.hazinedarSwitch);

        if (Build.VERSION.SDK_INT < 21)
            hazinedarSwitch.setText("");
        hazinedarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    hazinedarSwitch.setText(" بلی ");
                    isHazinedar = 1;
                    hazinedarDialog = new Dialog(RequestDetail.this);
                    hazinedarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    hazinedarDialog.setContentView(R.layout.hazinedar_dialog);
                    hazinedarDialog.show();
                } else {
                    hazinedarSwitch.setText(" خیر ");
                    isHazinedar = 0;
                }
                if (Build.VERSION.SDK_INT < 21)
                    hazinedarSwitch.setText("");
            }
        });

        bimeSwitch = (Switch) findViewById(R.id.bimeSwitch);
        if (Build.VERSION.SDK_INT < 21)
            bimeSwitch.setText("");
        bimeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    bimeSwitch.setText(" بلی ");
                    isBime = 1;
                    arzeshbarDialog = new Dialog(RequestDetail.this);
                    arzeshbarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    arzeshbarDialog.setContentView(R.layout.arzesh_dialog);
                    arzeshbarDialog.show();
                } else {
                    bimeSwitch.setText(" خیر ");
                    isBime = 0;
                }
                if (Build.VERSION.SDK_INT < 21)
                    bimeSwitch.setText("");
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void barTypeHadler (View view) {
        if ( barTypeHaml == 1);
        else {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.radiobutton_dialog);
            dialog.show();
        }
    }

    public void barTypeButtonHandler(View view) {
        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.barTypeRadioGroup);
        int radioCheckId = radioGroup.getCheckedRadioButtonId();
        if(radioCheckId== -1){
            Toast.makeText(this, "نوع بار را مشخص کنید.", Toast.LENGTH_SHORT).show();
        } else {
            RadioButton radioButton = (RadioButton) dialog.findViewById(radioCheckId);
            Button button = (Button) findViewById(R.id.barTypeButton);
            button.setText(radioButton.getText());
            barType = radioButton.getText().toString();
            dialog.hide();
        }

    }

    public void cancelArzeshHandler(View view) {
        bimeSwitch.setText(" خیر ");
        bimeSwitch.setChecked(false);
        isBime = 0;
        arzeshbarDialog.hide();

    }
    public void cancelHazinedar(View view) {
        hazinedarSwitch.setText(" خیر ");
        hazinedarSwitch.setChecked(false);
        isHazinedar = 0;
        hazinedarDialog.hide();

    }

    public void arzeshButtonHandler(View view) {
        EditText editText = (EditText) arzeshbarDialog.findViewById(R.id.arzeshValue);
        if (editText.length() == 0) {
            Toast.makeText(this, "ارزش بار را وارد کنید.", Toast.LENGTH_SHORT).show();
        }
        else {
            arzeshebar = Integer.valueOf(editText.getText().toString());
            arzeshbarDialog.hide();
        }

    }

    public void hazinedarButtonHandler(View view) {
        EditText editText = (EditText) hazinedarDialog.findViewById(R.id.hazinedarValue);
        if (editText.length() == 0) {
            Toast.makeText(this, "تعداد اقلام حجیم را وارد کنید", Toast.LENGTH_SHORT).show();
        }
        else {
            isHazinedar = Integer.valueOf(editText.getText().toString());
            hazinedarDialog.hide();
        }

    }

    public void detailSubmitHandler(View view) {
        EditText tozihat = findViewById(R.id.tozihatKoli);
        tozihatKoli = tozihat.getText().toString();
        storeInDatabase();
        Intent intent = new Intent(this, Invoice.class);
        startActivity(intent);
    }

    private void storeInDatabase() {
        database = this.openOrCreateDatabase("Picky", MODE_PRIVATE, null);
        Cursor c = database.rawQuery("SELECT * FROM request", null);
        int lastRowId = c.getCount();
        ContentValues requestValue = new ContentValues();
        requestValue.put("tozihat" , tozihatKoli);
        requestValue.put("is_hazinedar" , isHazinedar);
        requestValue.put("is_bime" , isBime);
        requestValue.put("tedad_kargar" , tedadKargar);
        requestValue.put("bartype" , barType);
        requestValue.put("arzeshebar" , arzeshebar);

        database.update("request", requestValue, "rowid = "+lastRowId  ,null);
    }
}


