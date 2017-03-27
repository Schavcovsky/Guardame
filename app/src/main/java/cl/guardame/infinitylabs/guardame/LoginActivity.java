package cl.guardame.infinitylabs.guardame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button registerBtn, loginBtn;
    String u, p;

    /*Login*/
    private EditText email, password;
    private Button login;
    private RequestQueue requestQueue;
    private static final String URL = "http://.../user_control.php";
    private StringRequest request;
    ///

    TextView tx, tx2;
    SharedPreferences ps;
    final Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ps = getSharedPreferences("ElementoSesion", Context.MODE_PRIVATE);

        if(!ps.getString("user","").equals("")){
            Intent i = new Intent(LoginActivity.this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        //CustomFont
        tx = (TextView) findViewById(R.id.userTv);
        tx2 = (TextView) findViewById(R.id.pwTv);
        TextView tx3 = (TextView) findViewById(R.id.loginBtn);

        //FontColor
        EditText emailEt = (EditText)findViewById(R.id.userTv);
        emailEt.setTextColor(0xAAFFFFFF);

        EditText pwEt = (EditText)findViewById(R.id.pwTv);
        pwEt.setTextColor(0xAAFFFFFF);
        //FontColor

        loginBtn = (Button) findViewById(R.id.loginBtn);

        Typeface thin_font = Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Display-Thin.otf");
        tx.setTypeface(thin_font);
        tx2.setTypeface(thin_font);
        loginBtn.setTypeface(thin_font);

        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Display-Semibold.otf");
        tx3.setTypeface(regular_font);
        //End CustomFont

        final EditText user = (EditText) findViewById(R.id.userTv);
        final EditText pass = (EditText) findViewById(R.id.pwTv);

        email = (EditText) findViewById(R.id.userTv);
        password = (EditText) findViewById(R.id.pwTv);
        login = (Button) findViewById(R.id.loginBtn);

        requestQueue = Volley.newRequestQueue(this);

        Button singupBtn = (Button) findViewById(R.id.singupBtn);
        singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SingUpActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("INFO", "Enter pressed");

                u = user.getText().toString();
                p = pass.getText().toString();

                Log.i("USER", u);
                Log.i("PASS", p);

                //Text Validator
                if (email.getText().toString().length() == 0) {
                    email.setError("Ingresa tu email");
                } else if (password.getText().toString().length() == 0) {
                    password.setError("Ingresa tu contraseña");
                } else {
                    request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.names().get(0).equals("success")) {
                                    Toast.makeText(getApplicationContext(), "ÉXITO" + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                                    //SharedPreferences
                                    SharedPreferences.Editor editor = ps.edit();
                                    editor.putString("user",email.getText().toString());
                                    editor.putString("pass",password.getText().toString());
                                    editor.commit();

                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    //Variable a enviar
                                    i.putExtra("email", u);
                                    startActivity(i);
                                    finish();

                                    tx.setText("");
                                    tx2.setText("");

                                } else {
                                    Toast.makeText(getApplicationContext(), "ERROR" + jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("email", email.getText().toString());
                            hashMap.put("password", password.getText().toString());

                            return hashMap;
                        }
                    };

                    requestQueue.add(request);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("¡Cuidado!");
        alertDialogBuilder.setMessage("¿Está seguro que desea cerrar Guárdame?");

        alertDialogBuilder.setPositiveButton("Cerrar Guárdame", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        alertDialogBuilder.setNeutralButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.show();

        // dont call **super**, if u want disable back button in current screen.
    }
}

