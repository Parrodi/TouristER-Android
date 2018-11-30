package com.example.carlo.androidapp.actividades;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.carlo.androidapp.R;
import com.example.carlo.androidapp.adapters.InputFormatAuthentication;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    private static final int SIGN_IN_CODE = 777;
    private GoogleApiClient googleApiClient;
    Button fb, gg;
    EditText name, email, phone, pswd, pswdc;
    TextView em, nom, tel, psw1, psw2;
    CheckBox terminosycondiciones;
    CallbackManager callbackManager;
    SharedPreferences pref;
    Intent mintent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fb = (Button)findViewById(R.id.inicioFb);
        gg = (Button)findViewById(R.id.inicioG);
        name = (EditText)findViewById(R.id.nombreCompleto);
        email = (EditText)findViewById(R.id.correoElectrónico);
        phone = (EditText)findViewById(R.id.numeroTelefono);
        pswd = (EditText)findViewById(R.id.password);
        pswdc = (EditText)findViewById(R.id.passwordConfirm);
        em = (TextView) findViewById(R.id.textViewem);
        nom = (TextView) findViewById(R.id.textViewnom);
        tel = (TextView) findViewById(R.id.textViewtel);
        psw1 = (TextView) findViewById(R.id.textViewpsw1);
        psw2 = (TextView) findViewById(R.id.textViewpsw2);
        terminosycondiciones = (CheckBox) findViewById(R.id.terminosCondiciones);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        mintent = new Intent(RegisterActivity.this, MapsActivity.class);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String email = object.getString("email");
                                    String name = object.getString("name");
                                    String password = object.getString("id");
                                    loginSocialNetworks(name,email,password);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(RegisterActivity.this, "No se pudieron obtener datos de Facebook",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(RegisterActivity.this, "FacebookCancel",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(RegisterActivity.this, "Facebook Error",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        GoogleSignInOptions GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            /*Toast.makeText(LoginActivity.this, result.getSignInAccount().getEmail(),
                    Toast.LENGTH_SHORT).show();*/


            Toast.makeText(RegisterActivity.this, "Google Success!",
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(RegisterActivity.this, "Google Fail",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void loginSocialNetworks(final String name, final String email, final String password) throws JSONException {
        RequestQueue loginqueue = Volley.newRequestQueue(this);
        String url = "https://er-citytourister.appspot.com/user/login";
        JSONObject postparams = new JSONObject();
        postparams.put("email", email);
        postparams.put("password", password);
        JsonObjectRequest loginrequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String info = response.getString("info");
                    if(info.equals("Login success")){
                        int userid = response.getInt("id");
                        String token = response.getString("token");
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt("userid",userid);
                        editor.putString("token", token);
                        editor.putString("useremail",email);
                        editor.apply();
                        startActivity(mintent);
                    }else addUser(name,email,password, "");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        loginqueue.add(loginrequest);
    }

    public void addUser(String name, final String email, final String password, String phone) throws JSONException {
        RequestQueue addqueue = Volley.newRequestQueue(this);
        String url = "https://er-citytourister.appspot.com/user/add";
        JSONObject postparams = new JSONObject();
        postparams.put("name",name);
        postparams.put("email", email);
        postparams.put("password", password);
        postparams.put("phone_number", phone);

        JsonObjectRequest addrequest = new JsonObjectRequest(Request.Method.POST, url, postparams, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String info = response.getString("info");
                    if(info.equals("Email already in use")){
                        Toast.makeText(RegisterActivity.this, "El correo proporcionado no está disponible",
                                Toast.LENGTH_LONG).show();
                    }else if(info.equals("New user added")){
                        int userid = response.getInt("id");
                        String token = response.getString("token");
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putInt("userid",userid);
                        editor.putString("token", token);
                        editor.putString("useremail",email);
                        editor.apply();
                        Toast.makeText(RegisterActivity.this, "Nuevo usuario agregado!",
                                Toast.LENGTH_LONG).show();
                        startActivity(mintent);
                    }else
                        Toast.makeText(RegisterActivity.this, "Hubo un error al agregar tus datos",
                                Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    Toast.makeText(RegisterActivity.this, "Hubo un error al obtener información del servidor",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Hubo un error al conectarse con el servidor",
                        Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });
        addqueue.add(addrequest);
    }

    public void iniciarfb(View v){
        LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile"));
    }

    public void iniciargg(View v){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, SIGN_IN_CODE);
    }

    public void registro(View v) throws JSONException {
        boolean correctinput=true;
        String message;


        if (InputFormatAuthentication.isEmailValid(email.getText().toString())){
            em.setTextColor(Color.parseColor("#707070"));
        }else {
            email.setText("");
            em.setTextColor(Color.RED);
            correctinput = false;
        }

        if (InputFormatAuthentication.passwordsMatch(pswd.getText().toString(), pswdc.getText().toString())){
            psw1.setTextColor(Color.parseColor("#707070"));
            psw2.setTextColor(Color.parseColor("#707070"));
        }else {
            pswd.setText("");
            pswdc.setText("");
            psw1.setTextColor(Color.RED);
            psw2.setTextColor(Color.RED);
            correctinput = false;
        }

        if (InputFormatAuthentication.isNameValid(name.getText().toString())){
            nom.setTextColor(Color.parseColor("#707070"));
        }else {
            name.setText("");
            nom.setTextColor(Color.RED);
            correctinput = false;
        }

        if (InputFormatAuthentication.isNumberValid(phone.getText().toString())){
            tel.setTextColor(Color.parseColor("#707070"));
        }else {
            phone.setText("");
            tel.setTextColor(Color.RED);
            correctinput = false;
        }

        if(terminosycondiciones.isChecked())
            terminosycondiciones.setTextColor(Color.parseColor("#707070"));
        else {
            terminosycondiciones.setTextColor(Color.RED);
            correctinput = false;
        }

        if (correctinput){
            message = "Input Válido y Completo";
            addUser(name.getText().toString(), email.getText().toString(),pswd.getText().toString(), phone.getText().toString());
        } else message = "Input Inválido o Incompleto!";

        Toast.makeText(RegisterActivity.this, message,
                Toast.LENGTH_SHORT).show();
    }


}