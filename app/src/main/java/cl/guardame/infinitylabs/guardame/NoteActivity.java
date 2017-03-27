package cl.guardame.infinitylabs.guardame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class NoteActivity extends AppCompatActivity {

    String title;
    String content;
    String note_update_date;

    final Context context = this;

    //Delete
    String note_id;
    /*Login*/
    private RequestQueue requestQueue;
    private static final String URL = "http://.../delete_note.php";
    private StringRequest request;
    ///

    String e;
    String u;

    //Modify
    private static final String URL2 = "http://.../modify_note.php";
    ///

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        //Instanciar los elementos
        final TextView tit = (TextView)findViewById(R.id.titleTv);
        TextView dat = (TextView)findViewById(R.id.dateTv);
        final TextView con = (TextView)findViewById(R.id.contentTv);

        Button delete = (Button)findViewById(R.id.noteDeleteBtn);

        //Rescate de Intent y Bundle de info
        Intent intent = getIntent();
        //RECIBE NOMBRE BUNDLE EN ""
        Bundle extras = intent.getExtras().getBundle("nota");

        //recibe con "" mismo nombre

        //borrar
        note_id = extras.getString("note_id");
        //end borrar

        title = extras.getString("title");
        note_update_date = extras.getString("content");
        content = extras.getString("note_update_date");

        tit.setText(title);
        dat.setText(note_update_date);
        con.setText(content);

        requestQueue = Volley.newRequestQueue(this);

        //recibe intent de email
        //varibale global declarada
        e = getIntent().getStringExtra("email");

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.i("Note Id", note_id);

                AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("¡Cuidado!");
                alertDialogBuilder.setMessage("¿Está seguro que desea eliminar el Apunte?");

                alertDialogBuilder.setPositiveButton("Eliminar Nota",new DialogInterface. OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.names().get(0).equals("success")) {
                                        Toast.makeText(getApplicationContext(), "ÉXITO" + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(NoteActivity.this, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        //Variable a enviar
                                        i.putExtra("email", e);
                                        startActivity(i);
                                        finish();

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
                                hashMap.put("note_id", note_id);
                                return hashMap;
                            }
                        };

                        requestQueue.add(request);

                        Intent i = new Intent(NoteActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //Variable a enviar
                        i.putExtra("email", e);
                        startActivity(i);
                        finish();
                    }
                });

                alertDialogBuilder.setNeutralButton("No",new DialogInterface. OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilder.show();


            }
        });

        Button modifyBtn = (Button)findViewById(R.id.modifyBtn);
        modifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("¡Cuidado!");
                alertDialogBuilder.setMessage("¿Está seguro que desea modificar el apunte?");

                alertDialogBuilder.setPositiveButton("Modificar",new DialogInterface. OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        request = new StringRequest(Request.Method.POST, URL2, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.names().get(0).equals("success")) {
                                        Toast.makeText(getApplicationContext(), "ÉXITO" + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(NoteActivity.this, MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        //VOLVER A ENVIAR MAIL PARA QUE PUEDA LEER LAS NOTAS
                                        i.putExtra("email", e);
                                        startActivity(i);
                                        finish();

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
                                hashMap.put("note_id", note_id);
                                hashMap.put("title", tit.getText().toString());
                                hashMap.put("content", con.getText().toString());
                                hashMap.put("email", e);

                                return hashMap;
                            }
                        };

                        requestQueue.add(request);

                        Intent i = new Intent(NoteActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        //Variable a enviar
                        i.putExtra("email", e);
                        startActivity(i);
                        finish();
                    }
                });

                alertDialogBuilder.setNeutralButton("No",new DialogInterface. OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilder.show();


            }
        });


    }

}
