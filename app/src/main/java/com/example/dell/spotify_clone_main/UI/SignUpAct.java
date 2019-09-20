package com.example.dell.spotify_clone_main.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.spotify_clone_main.R;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
// signup page
public class SignUpAct extends AppCompatActivity {

    Animation frombottom, fromtop;
    Button btnjoin_signup, btnLogin_signup;
    TextView textView2;
    EditText email_signup, password_signup;
    ProgressBar loading;

    public String URL = "https://aasthamalik31.pythonanywhere.com/user/signup/";
    public static final String KEY_USERNAME = "name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        TextView text = findViewById(R.id.textviewtext);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        loading = (ProgressBar) findViewById(R.id.loading_signup);
        btnjoin_signup = (Button) findViewById(R.id.btnjoin_signup);
        btnLogin_signup = (Button) findViewById(R.id.btnLogin_signup);

        textView2 = (TextView) findViewById(R.id.textView2);

        email_signup = (EditText) findViewById(R.id.Email_Sign_Up);

        password_signup = (EditText) findViewById(R.id.password_signup);

        btnjoin_signup.startAnimation(frombottom);
        btnLogin_signup.startAnimation(frombottom);

        text.startAnimation(frombottom);
        textView2.startAnimation(fromtop);

        email_signup.startAnimation(fromtop);
        password_signup.startAnimation(fromtop);

        btnLogin_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpAct.this, LoginAct.class);
                startActivity(intent);
            }
        });

        btnjoin_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });

    }
// create account fucntion
    private void CreateAccount() {

        String email = email_signup.getText().toString().trim();
        String password = password_signup.getText().toString().trim();
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
//            writeToLog("Software Keyboard was shown");
            hideSoftKeyboard(SignUpAct.this);
        } else {
//            writeToLog("Software Keyboard was not shown");
        }


        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter valid email id", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();

        } else if (password.length() < 6) {
            Toast.makeText(this, "Password must contain at least 6 characters!", Toast.LENGTH_SHORT).show();

        } else {
            loading.setVisibility(View.VISIBLE);
            register(email, password);
        }
    }

    // registering you through server
    private void register(final String email, final String password) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Boolean UserCreated = jsonObject.getBoolean("User Created");


                    if (UserCreated) {
                        Toast.makeText(SignUpAct.this, "Successfully Created your account", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpAct.this, LoginAct.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpAct.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btnjoin_signup.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SignUpAct.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btnjoin_signup.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put(KEY_EMAIL, email);
                params.put(KEY_PASSWORD, password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
