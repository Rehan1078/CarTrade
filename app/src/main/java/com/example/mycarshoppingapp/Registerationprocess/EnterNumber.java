package com.example.mycarshoppingapp.Registerationprocess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mycarshoppingapp.R;
import com.hbb20.CountryCodePicker;

public class EnterNumber extends AppCompatActivity {

    CountryCodePicker ccp;
    EditText et1;
    Button movenext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_number);
        init();
        ccp.registerCarrierNumberEditText(et1);
        movenext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ccp.isValidFullNumber()){
                    et1.setError("Phone no invalid");
                    return;
                }
                Intent intent = new Intent(EnterNumber.this, EnterOTP.class);
                intent.putExtra("phoneno",ccp.getFullNumberWithPlus());
                startActivity(intent);
            }
        });
    }

    void init(){
        ccp=findViewById(R.id.countryCodepicker);
        et1=findViewById(R.id.enter_number);
        movenext=findViewById(R.id.button_move_to_OTPScreen);
    }
}
