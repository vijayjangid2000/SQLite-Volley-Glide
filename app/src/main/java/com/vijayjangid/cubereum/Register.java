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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    EditText firstName_ET, lastName_ET, phone_ET, address_ET;
    EditText usernameET, passwordET, comfirmET;
    TextView registerButton, gotoLoginButton;
    DatabaseHelperClass databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstName_ET = findViewById(R.id.firstName_textView);
        lastName_ET = findViewById(R.id.lastName_textView);
        phone_ET = findViewById(R.id.phone_textView);
        address_ET = findViewById(R.id.address_textView);
        usernameET = findViewById(R.id.userNameR_textView);
        passwordET = findViewById(R.id.passwordR_textView);
        comfirmET = findViewById(R.id.passwordR_textViewC);

        registerButton = findViewById(R.id.register_button);
        gotoLoginButton = findViewById(R.id.gotoLogin_Button);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelperClass(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String firstName = firstName_ET.getText().toString().trim().toLowerCase();
                String lastName = lastName_ET.getText().toString().trim().toLowerCase();
                String phoneNumber = phone_ET.getText().toString().trim();
                String address = address_ET.getText().toString().trim();
                String email = usernameET.getText().toString().toLowerCase().trim();
                String password = passwordET.getText().toString().trim();
                String confirmPassword = comfirmET.getText().toString().trim();

                if (firstName.length() < 2)
                    toast("Too short First Name");
                else if (!firstName.matches("^[a-zA-Z]*$"))
                    toast("First Name should contain Alphabets Only");
                else if (lastName.length() < 2)
                    toast("Too short Last Name");
                else if (!lastName.matches("^[a-zA-Z]*$"))
                    toast("Last Name should contain Alphabets Only");
                else if (!isValidPhone(phoneNumber))
                    toast("Invalid Phone Number");
                else if (address.length() < 6)
                    toast("Invalid Address");
                else if (!isValidEmail(email))
                    toast("Invalid Email");
                else if (password.length() < 6)
                    toast("Too Short Password");
                else if (!confirmPassword.equals(password))
                    toast("Password does not Match, try again");
                else if (databaseHelper.checkEmailExists(email))
                    toast("Email already registered");
                else if (databaseHelper.checkPhoneExists(phoneNumber))
                    toast("Phone Number already registered");
                else {
                    if (databaseHelper.addNewRecord(firstName, lastName, phoneNumber,
                            address, email, password)) {

                        SharedPreferences sharedPreferences = getSharedPreferences("userStatus", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("alreadyLoggedIn", true);
                        editor.putString("email", email);
                        editor.apply();

                        toast("Registered Successfully");

                        startActivity(new Intent(Register.this, Home_Navigation.class).
                                putExtra("email", email));
                        finish();
                    } else {
                        toast("Error - Registration Failed");
                    }
                }

            }
        });

        gotoLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
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

    public static boolean isValidPhone(String number) {
        // 1) Begins with 0 or 91
        // 2) Then contains 7 or 8 or 9.
        // 3) Then contains 9 digits
        Pattern p = Pattern.compile("[7-9][0-9]{9}");
        Matcher m = p.matcher(number);
        return (m.find() && m.group().equals(number));
    }

    void toast(String stringToast) {
        Toast.makeText(this, stringToast, Toast.LENGTH_SHORT).show();
    }


}