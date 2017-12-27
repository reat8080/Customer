package ir.picky.app.mapdemo;

import android.app.Dialog;
import android.content.Context;
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

    String barType;
    boolean isHazinedar = false;
    boolean isBime = false;
    int tedadKargar = 0;
    String tozihatKoli;
    Dialog dialog ;
    Dialog arzeshbarDialog ;
    int arzeshebar;
    Switch bimeSwitch;

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
        barTypeHadler(v);

        SeekBar kargarSeek = (SeekBar) findViewById(R.id.kargarSeekBar);
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



        final Switch hazinedarSwitch = (Switch) findViewById(R.id.hazinedarSwitch);
        hazinedarSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    hazinedarSwitch.setText(" بلی ");
                    isHazinedar = true;
                } else {
                    hazinedarSwitch.setText(" خیر ");
                    isHazinedar = false;
                }
            }
        });


        bimeSwitch = (Switch) findViewById(R.id.bimeSwitch);
        bimeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    bimeSwitch.setText(" بلی ");
                    isBime = true;
                    arzeshbarDialog = new Dialog(RequestDetail.this);
                    arzeshbarDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    arzeshbarDialog.setContentView(R.layout.arzesh_dialog);
                    arzeshbarDialog.show();
                } else {
                    bimeSwitch.setText(" خیر ");
                    isBime = false;
                }
            }
        });


    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    public void barTypeHadler (View view) {

        dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        dialog.show();
//        Spinner spinner = (Spinner) findViewById(R.id.spiner);
//        String stringss = String.valueOf(spinner.getSelectedItem());
//        Toast.makeText(this, stringss, Toast.LENGTH_SHORT).show();
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
        isBime = false;
        arzeshbarDialog.hide();

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


    public void detailSubmitHandler(View view) {
        EditText tozihat = (EditText) findViewById(R.id.tozihatKoli);
        tozihatKoli = tozihat.getText().toString();
    }
}


