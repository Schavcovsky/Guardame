package cl.guardame.infinitylabs.guardame;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class AddNoteActivity extends AppCompatActivity {

    String titleF, contentF, emailF;
    Button addnoteBtn;

    String t, c, e;

    /*Login*/
    private Button login;
    private RequestQueue requestQueue;
    private static final String URL = "http://.../add_note.php";
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        final EditText title = (EditText) findViewById(R.id.titleEt);
        final EditText content = (EditText) findViewById(R.id.contentEt);

        requestQueue = Volley.newRequestQueue(this);

        //recibe intent de email
        //varibale global declarada
        e = getIntent().getStringExtra("email");

        addnoteBtn = (Button) findViewById(R.id.addnoteBtn);
        addnoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("INFO", "Enter pressed");

                t = title.getText().toString();
                c = content.getText().toString();

                Log.i("TÍTULO: ", t);
                Log.i("CONTENIDO: ", c);

                request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.names().get(0).equals("success")) {
                                Toast.makeText(getApplicationContext(), "ÉXITO" + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                                Intent i = new Intent(AddNoteActivity.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                //VOLVER A ENVIAR MAIL PARA QUE PUEDA LEER LAS NOTAS
                                i.putExtra("email", e);
                                startActivity(i);
                                finish();

                                title.setText("");
                                content.setText("");

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
                        hashMap.put("title", title.getText().toString());
                        hashMap.put("content", content.getText().toString());
                        hashMap.put("email", e);

                        return hashMap;
                    }
                };

                requestQueue.add(request);
            }
        });

    }

}
