package com.example.mycarshoppingapp.Registerationprocess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mycarshoppingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class EnterOTP extends AppCompatActivity {
    String phoneno;
    TextView resendit, didntgetcode;
    String verificationcode;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    ProgressBar pb;
    Button verifyotp;
    EditText et1, et2, et3, et4, et5, et6;
    FirebaseAuth myauth = FirebaseAuth.getInstance();

    Long timeoutseconds = 60L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        init();
    }

    void init() {
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);
        phoneno = getIntent().getStringExtra("phoneno");
        Toast.makeText(this, phoneno, Toast.LENGTH_SHORT).show();
        verifyotp = findViewById(R.id.button_next1);
        resendit = findViewById(R.id.resendtextview);
        didntgetcode = findViewById(R.id.textviewresendinseconds);
        et1 = findViewById(R.id.editText1);
        et2 = findViewById(R.id.editText2);
        et3 = findViewById(R.id.editText3);
        et4 = findViewById(R.id.editText4);
        et5 = findViewById(R.id.editText5);
        et6 = findViewById(R.id.editText6);

        setupEditTexts();

        // Assign OnClickListener to verifyotp button
        verifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otpentered = et1.getText().toString() +
                        et2.getText().toString() +
                        et3.getText().toString() +
                        et4.getText().toString() +
                        et5.getText().toString() +
                        et6.getText().toString();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationcode, otpentered);
                signin(credential);
                setInProgress(true);
            }
        });
        resendit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP(phoneno, true);
            }
        });

        sendOTP(phoneno, false);
    }

    void setupEditTexts() {
        EditText[] editTexts = {et1, et2, et3, et4, et5, et6};

        for (int i = 0; i < editTexts.length; i++) {
            final int index = i;
            editTexts[index].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 1) {
                        if (index < editTexts.length - 1) {
                            editTexts[index + 1].requestFocus();
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            editTexts[index].setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                        if (editTexts[index].getText().length() == 0 && index > 0) {
                            editTexts[index - 1].requestFocus();
                        }
                    }
                    return false;
                }
            });
        }
    }

    void sendOTP(String phoneno, boolean isResend) {
        setInProgress(true);
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder()
                .setPhoneNumber(phoneno)
                .setTimeout(timeoutseconds, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signin(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(EnterOTP.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        setInProgress(false);
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationcode = s;
                        resendingToken = forceResendingToken;
                        Toast.makeText(EnterOTP.this, "OTP Sent Successfully", Toast.LENGTH_SHORT).show();
                        setInProgress(false);
                    }
                });
        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    void setInProgress(boolean inprogress) {
        if (inprogress) {
            pb.setVisibility(View.VISIBLE);
            verifyotp.setVisibility(View.GONE);
        } else {
            pb.setVisibility(View.GONE);
            verifyotp.setVisibility(View.VISIBLE);
        }
    }

    void signin(PhoneAuthCredential phoneAuthCredential) {
//        startResendTimer();
        setInProgress(true);
        myauth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                setInProgress(false);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(EnterOTP.this, EnterUsername.class);
                    intent.putExtra("phoneno", phoneno);
                    startActivity(intent);
                } else {
                    Toast.makeText(EnterOTP.this, "Not Completed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void startResendTimer() {
        didntgetcode.setEnabled(false);
        resendit.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutseconds--;
                didntgetcode.setText("Resend OTP in " + timeoutseconds + " seconds");
                if (timeoutseconds <= 0) {
                    timeoutseconds = 60L;
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            didntgetcode.setEnabled(true);
                            resendit.setEnabled(true);
                        }
                    });
                }
            }
        }, 0, 1000);
    }
}