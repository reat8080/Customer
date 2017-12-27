package ir.picky.app.mapdemo;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ir.picky.app.mapdemo.services.MyService;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity {

    SQLiteDatabase database;
    int verificationCode ;
    String phone;
    String aut_key;
    int hasLname ;
    Dialog signUpDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/yekan.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_sign_up);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(MyService.MY_SERVICE_MESSAGE));
    }

    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message =
                    intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            Log.i("codejson" , message);
            verificationCode =  getUserCode(message);
            aut_key = getUserAutKey(message);
            hasLname = getHasLname (message);
        }
    };

    public int getUserCode(String userCodeJson) {
        JSONObject json = null;
        try {
            json = new JSONObject(userCodeJson);
            Toast.makeText(this, json.getString("registercode") , Toast.LENGTH_SHORT).show();
            return Integer.valueOf( json.getString("registercode"));
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getUserAutKey(String userCodeJson) {
        JSONObject json = null;
        try {
            json = new JSONObject(userCodeJson);
            return ( json.getString("aut_key"));
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public int getHasLname (String userCodeJson) {
        JSONObject json = null;
        try {
            json = new JSONObject(userCodeJson);
            return ( Integer.valueOf(json.getString("haslname")) );
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void loginButtonHandler(View view) {

        Button button = findViewById(R.id.loginButton);
        EditText editTextCode = (EditText) findViewById(R.id.editTextCode);

        if ( String.valueOf(button.getText()).equals("ورود") ) {
            EditText number = (EditText) findViewById(R.id.editTextPhone);
            phone = String.valueOf (number.getText());
            if (phone.equals("") || phone.length() < 10 ) {
                Toast.makeText(this, "شماره را به درستی وارد کنید.", Toast.LENGTH_SHORT).show();
            } else {



//        ContentValues initialValues = new ContentValues();
//        initialValues.put("id", 1);
//        initialValues.put("name", "Geevarghese");
//        initialValues.put("mark", 100);
//        initialValues.put("address", "No.20, Cochin, Kerala");
//        database.insert("user", null, initialValues);


                LinearLayout linearLayout = findViewById(R.id.mobileNumber);
                linearLayout.setVisibility(View.INVISIBLE);

                button.setText("بررسی کد");

                TextView textView = findViewById(R.id.textView5);
                textView.setText("کد شناسایی برای شماره "+phone+ " ارسال شد. " );

                editTextCode.setVisibility(View.VISIBLE);

                String registerUrl = "http://admin:1234@comp.isfahanregister.com/api/register?mobile="+phone ;
                Intent intent = new Intent(this, MyService.class);
                intent.setData(Uri.parse(registerUrl));
                startService(intent);
            }
        } else {
            String  code = String.valueOf(editTextCode.getText());

            if ( code.equals(String.valueOf(verificationCode)) ) {

                if  (hasLname == 0) {
                    signUpDialog = new Dialog(this);
                    signUpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    signUpDialog.setContentView(R.layout.signup_dialog);
                    signUpDialog.show();
                } else {
                    database = this.openOrCreateDatabase("Picky", MODE_PRIVATE, null);
                    //database.execSQL("INSERT INTO user (number) VALUES (' " + phone + " ')");

                    ContentValues userValue = new ContentValues();
                    userValue.put("aut_key", aut_key); //These Fields should be your String values of actual column names
                    userValue.put("islogin", 1);
                    userValue.put("number" , ""+phone+"" );
                    //database.execSQL("UPDATE user SET aut_key = "+aut_key+ "WHERE number = "+ phone);
                    //database.execSQL("UPDATE user SET islogin =1 WHERE number = "+ phone);
                    //database.update("user" , userValue , "number="+phone , null );
                    database.insert("user" , null , userValue);
                    Intent intent = new Intent(this, MapsActivity.class);
                    startActivity(intent);
                }

            } else {
                Toast.makeText(this, "کد وارد شده صحیح نیست.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void signUpDetailHandler(View view) {

    }
}
