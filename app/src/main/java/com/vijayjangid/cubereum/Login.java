package com.vijayjangid.cubereum;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    TextView loginButton, gotoRegistrationButton;
    DatabaseHelperClass databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        SharedPreferences sharedPreferences = getSharedPreferences("userStatus", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("alreadyLoggedIn", false)) {
            startActivity(new Intent(Login.this, Home_Navigation.class));
            finish();
        }

        usernameEditText = findViewById(R.id.userName_textView);
        passwordEditText = findViewById(R.id.password_textView);
        loginButton = findViewById(R.id.login_button);
        gotoRegistrationButton = findViewById(R.id.gotoRegister_button);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelperClass(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailInput = usernameEditText.getText().toString().toLowerCase().trim();
                String passwordInput = passwordEditText.getText().toString();

                if (!isValidEmail(emailInput)) {
                    toast("Email should be Valid");
                } else if (!databaseHelper.checkEmailExists(emailInput)) {
                    toast("Email Not Registered");
                } else if (!databaseHelper.checkPassword(emailInput, passwordInput))
                    toast("Wrong Password");
                else{
                    SharedPreferences sharedPreferences = getSharedPreferences("userStatus", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("alreadyLoggedIn", true);
                    editor.putString("email", emailInput);
                    editor.apply();

                    toast("Login Successful");

                    startActivity(new Intent(Login.this, Home_Navigation.class));
                    finish();
                }
            }
        });

        gotoRegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });

    }

    public boolean isValidEmail(String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) return false;
        return pat.matcher(email).matches();
    }

    void toast(String stringToast) {
        Toast.makeText(this, stringToast, Toast.LENGTH_SHORT).show();
    }
}