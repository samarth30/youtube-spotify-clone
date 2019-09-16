package com.example.dell.spotify_clone_main.UI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
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

// login activity

public class LoginAct extends AppCompatActivity {

    Animation frombottom, fromtop;
    Button btnjoin_login, btnLogin_login;
    TextView textView1;
    EditText password_login, email_login;
    ProgressBar loading;


    String URL = "https://aasthamalik31.pythonanywhere.com/user/login/";
    public static String TokenFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(LoginAct.this, Platform_choose.class));
        }


        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        loading = (ProgressBar) findViewById(R.id.loading_login);
        btnjoin_login = (Button) findViewById(R.id.btnjoin_login);
        btnLogin_login = (Button) findViewById(R.id.btnlogin_login);

        textView1 = (TextView) findViewById(R.id.textView1);

        btnjoin_login.startAnimation(frombottom);
        btnLogin_login.startAnimation(frombottom);

        email_login = (EditText) findViewById(R.id.email_login);
        password_login = (EditText) findViewById(R.id.password_login);

        email_login.startAnimation(fromtop);
        password_login.startAnimation(fromtop);

        btnjoin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAct.this, SignUpAct.class);
                startActivity(intent);

            }
        });

        btnLogin_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
// login fucntion
    private void login() {
        String email = email_login.getText().toString().trim();
        String password = password_login.getText().toString().trim();

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {

            hideSoftKeyboard(LoginAct.this);
        } else {

        }


        if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter valid email id", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_SHORT).show();

        } else if (password.length() < 6) {
            Toast.makeText(this, "Password must contain at least 6 characters!", Toast.LENGTH_SHORT).show();

        } else {
            loading.setVisibility(View.VISIBLE);

            AllowAcessToAccount(email, password);
        }

    }

// checking the login through server
    private void AllowAcessToAccount(final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    Boolean Logged = jsonObject.getBoolean("Logged In");


                    if (Logged) {
                        String token = jsonObject.getString("token");
                        TokenFinal = token;
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(token,email);

                        loading.setVisibility(View.GONE);
                        btnLogin_login.setVisibility(View.VISIBLE);
                        startActivity(new Intent(LoginAct.this, Platform_choose.class));
                        finish();

                    } else {
                        Toast.makeText(LoginAct.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginAct.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                    loading.setVisibility(View.GONE);
                    btnLogin_login.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginAct.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.GONE);
                btnLogin_login.setVisibility(View.VISIBLE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
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
