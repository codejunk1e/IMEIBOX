package com.robinsguide.imeibox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    private String calCheckDigit;
    Calculator calc = new Calculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void myIMEI(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager Tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            String stro = Tm.getDeviceId().toString();
            editText = (EditText) findViewById(R.id.editText);
            editText.setText(stro);
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);

        }

    }


    public void bclear(View view) {

        TextView tv = (TextView) findViewById(R.id.textView3);
        TextView tv1 = (TextView) findViewById(R.id.textView5);
        TextView tv2 = (TextView) findViewById(R.id.textView4);
        editText = (EditText) findViewById(R.id.editText);
        ImageView Iv = (ImageView) findViewById(R.id.imageView);


        tv1.setText("");
        tv.setText("");
        tv2.setText("");
        editText.setText("");
        Iv.setImageResource(0);


    }

    public void bAnalyze(View view) {
        editText = (EditText) findViewById(R.id.editText);
        TextView tv1 = (TextView) findViewById(R.id.textView5);
        TextView tv = (TextView) findViewById(R.id.textView3);
        ImageView Iv = (ImageView) findViewById(R.id.imageView);


        if (editText.length()>15 || editText.length()<14) {
            String dada = "Invalid IMEI number. \nThe IMEI should be 14 or 15 digits in lenght";
            tv1.setText(dada);
            tv.setText("");
            tv1.setTextColor(Color.RED);
            Iv.setImageResource(R.drawable.ic_highlight_off_black_18dp);

        }
        else if(editText.length()== 15){
            String input = editText.getText().toString();
            tv1.setTextColor(Color.BLACK);

            if ( calc.luhnCheck(input)){
                String dada = "The Check Digit is correct. \nThe IMEI looks valid";
                tv1.setTextColor(Color.BLUE);
                Iv.setImageResource(R.drawable.ic_check_circle_black_18dp);
                tv1.setText(dada);
                calCheckDigit =  calc.calculateCheckDigit(input.substring(0,14));

                String Body15 = "Type Allocation Code: " +input.substring(0,6) +
                        "\nReporting Body Identifier: " +input.substring(0,2) +
                        "\nMobile Equipment Type: " + input.substring(2,6) +
                        "\nFinal Assembly code: " + input.substring(6,8) +
                        "\nSerial Number: " + input.substring(8, 14) +
                        "\nCheck Digit:  "+ input.substring(14) +
                        "\nLuhn Check Digit:  " + calCheckDigit;

                tv.setText(Body15);
            }
            else {
                String dada = "Invalid IMEI number. \nThe Check Digit is not correct.";
                tv1.setTextColor(Color.RED);
                Iv.setImageResource(R.drawable.ic_highlight_off_black_18dp);
                tv1.setText(dada);
                calCheckDigit =  calc.calculateCheckDigit(input.substring(0,14));


                String Body15 = "Type Allocation Code: " +input.substring(0,6) +
                        "\nReporting Body Identifier: " +input.substring(0,2) +
                        "\nMobile Equipment Type: " + input.substring(2,6) +
                        "\nFinal Assembly code: " + input.substring(6,8) +
                        "\nSerial Number: " + input.substring(8, 14) +
                        "\nCheck Digit: "+ input.substring(14) +
                        "\nLuhn Check Digit:  " + calCheckDigit;

                tv.setText(Body15);
            }
        }
        else {
            String input = editText.getText().toString();
            calCheckDigit = calc.calculateCheckDigit(input);

            String dada = "Full IMEI: " + input + calCheckDigit;
            tv1.setTextColor(Color.BLUE);
            tv1.setText(dada);

            String Body14 = "Type Allocation Code: " +input.substring(0,6) +
                    "\nReporting Body Identifier: " +input.substring(0,2) +
                    "\nMobile Equipment Type: " + input.substring(2,6) +
                    "\nFinal Assembly code: " + input.substring(6,8) +
                    "\nSerial Number: " + input.substring(8, 14) +
                    "\nCheck Digit:(missing)  "+
                    "\nLuhn Check Digit:  " +calCheckDigit;

            tv.setText(Body14);
        }

    }
}
