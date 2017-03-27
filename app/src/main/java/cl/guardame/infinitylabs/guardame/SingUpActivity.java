package cl.guardame.infinitylabs.guardame;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingUpActivity extends AppCompatActivity {

    Button singupBtn;
    String n, u, p, pc;
    private RequestQueue requestQueue;
    private static final String URL = "http://.../create_user.php";
    private StringRequest request;

    TextView name;
    TextView user;
    TextView pass;
    TextView passC;
    CheckBox tycCb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        //CustomFont

        //TextView
        TextView tx5 = (TextView) findViewById(R.id.nameTv);
        TextView tx = (TextView) findViewById(R.id.userTv);
        TextView tx2 = (TextView) findViewById(R.id.pwTv);
        TextView tx7 = (TextView) findViewById(R.id.pwCTV);

        Typeface thin_font = Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Display-Thin.otf");
        tx5.setTypeface(thin_font);
        tx.setTypeface(thin_font);
        tx2.setTypeface(thin_font);
        tx7.setTypeface(thin_font);

        //Button
        Button tx3 = (Button) findViewById(R.id.loginBtn);
        Button tx4 = (Button) findViewById(R.id.backBtn);

        Typeface regular_font = Typeface.createFromAsset(getAssets(), "fonts/SF-UI-Display-Regular.otf");
        tx3.setTypeface(regular_font);
        tx4.setTypeface(regular_font);


        //End CustomFont
        tx4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(SingUpActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        //

        //FontColor
        EditText nameEt = (EditText)findViewById(R.id.nameTv);
        nameEt.setTextColor(0xAAFFFFFF);

        EditText emailEt = (EditText)findViewById(R.id.userTv);
        emailEt.setTextColor(0xAAFFFFFF);

        EditText passEt = (EditText)findViewById(R.id.pwTv);
        passEt.setTextColor(0xAAFFFFFF);

        EditText passCEt = (EditText)findViewById(R.id.pwCTV);
        passCEt.setTextColor(0xAAFFFFFF);
        //FontColor

        requestQueue = Volley.newRequestQueue(this);

        name = (TextView) findViewById(R.id.nameTv);
        user = (TextView) findViewById(R.id.userTv);
        pass = (TextView) findViewById(R.id.pwTv);
        passC = (TextView) findViewById(R.id.pwCTV);

        singupBtn = (Button) findViewById(R.id.loginBtn);
        singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("INFO", "Enter pressed");

                n = name.getText().toString();
                u = user.getText().toString();
                p = pass.getText().toString();
                pc = passC.getText().toString();

                Log.i("NAME", n);
                Log.i("USER", u);
                Log.i("PASS", p);
                Log.i("PASS C", pc);

                final String nameF = name.getText().toString();
                final String passF = pass.getText().toString();
                final String email = user.getText().toString();

                if (!isNameEmpty(nameF)) {
                    name.setError("Mínimo 4 caracteres");
                } else

                  if (!isValidEmail(email)) {
                      user.setError("Correo inválido");
                }

                else if (!isValidPassword(passF)) {
                      pass.setError("Contraseña inválida");
                }

                else if (!p.equals(pc)) {
                    passC.setError("Las contraseñas no coiciden");
                } else {

                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                Toast.makeText(getApplicationContext(), "ÉXITO" + jsonObject.getString("success"), Toast.LENGTH_LONG).show();

                                Intent i = new Intent(SingUpActivity.this, LoginActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();

                                name.setText("");
                                user.setText("");
                                pass.setText("");
                                passC.setText("");

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
                        hashMap.put("email", user.getText().toString());
                        hashMap.put("name", name.getText().toString());
                        hashMap.put("password", pass.getText().toString());

                        return hashMap;
                    }
                };
                requestQueue.add(request);
            }
            }
        });
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }

    // validating password with retype password
    private boolean isNameEmpty(String name) {

        final TextView nameF = (TextView) findViewById(R.id.nameTv);

        if (nameF.getText().toString().length() >= 4) {
            return true;
        }
        return false;
    }

}
