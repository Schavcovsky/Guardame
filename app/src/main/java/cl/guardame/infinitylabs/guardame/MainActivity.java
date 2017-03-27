package cl.guardame.infinitylabs.guardame;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cl.guardame.infinitylabs.guardame.Modelo.Nota;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView addnoteBtn;
    ListView listView;
    ArrayList<Nota> lista;
    String email;
    final Context context = this;
    String ni;
    SharedPreferences ps;
    String superemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        final SharedPreferences ps= getSharedPreferences("ElementoSesion", Context.MODE_PRIVATE);
        superemail = ps.getString("user","");

        Log.i("SharedPreferences", ps.getString("user",""));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //CustomFont
        addnoteBtn = (TextView)findViewById(R.id.addnoteBtn);

        Typeface thin_font = Typeface.createFromAsset(getAssets(),  "fonts/SF-UI-Display-Thin.otf");
        addnoteBtn.setTypeface(thin_font);
        ///

        //recibe intent de email
        //varibale global declarada
        email = getIntent().getStringExtra("email");

        GetData gd = new GetData();
        gd.execute(new String[] {"http://.../note_control.php"});

        //envia bundle
        listView = (ListView)findViewById(R.id.notesLv);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent in = new Intent(MainActivity.this, NoteActivity.class);
                Bundle b = new Bundle();

                //envia con "" mismo nombre
                b.putString("note_id", lista.get(i).getNote_id());
                b.putString("title", lista.get(i).getTitle());
                b.putString("content", lista.get(i).getNote_update_date());
                b.putString("note_update_date", lista.get(i).getContent());

                //ENVIA NOMBRE BUNDLE EN ""
                in.putExtra("nota",b);

                //Envía Email
                in.putExtra("email", superemail);
                Log.i("Correo Putextra", superemail);
                startActivity(in);
            }
        });

        Button addnoteBtn = (Button) findViewById(R.id.addnoteBtn);
        addnoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("email", superemail);
                Log.i("Correo Putextra", superemail);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            finish();

//            AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(context);
//            alertDialogBuilder.setTitle("¡Cuidado!");
//            alertDialogBuilder.setMessage("¿Está seguro que desea cerrar sesión?");
//
//            alertDialogBuilder.setPositiveButton("Cerrar sesión",new DialogInterface. OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//
//                    SharedPreferences settings = context.getSharedPreferences("ElementoSesion", Context.MODE_PRIVATE);
//                    settings.edit().clear().commit();
//
//                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(i);
//                    //dialog.cancel();
//                }
//            });
//
//            alertDialogBuilder.setNeutralButton("No",new DialogInterface. OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    dialog.cancel();
//                }
//            });
//            alertDialogBuilder.show();

            // dont call **super**, if u want disable back button in current screen.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_note) {
//            // Handle the camera action
//        } else if (id == R.id.nav_collections) {
//
//        } else if (id == R.id.nav_configuration) {
//
//        } else if (id == R.id.nav_about) {
//
//        } else
        if (id == R.id.nav_exit) {

            SharedPreferences settings = context.getSharedPreferences("ElementoSesion", Context.MODE_PRIVATE);
            settings.edit().clear().commit();

            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class GetData extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        String contenido = "";

        protected void onPostExecute(Boolean result) {
            if (result == true) {
                try {
                    lista = new ArrayList<Nota>();
                    JSONArray json = new JSONArray(contenido);
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jsonData = json.getJSONObject(i);
                        lista.add(new Nota (jsonData.getString("note_id"), jsonData.getString("title"),jsonData.getString("content"),jsonData.getString("note_update")));
                    }

                    //Carga el custom layout de notas
                    ArrayAdapter<String> adapter = new CustomLayout(MainActivity.this,R.layout.notes_layout, lista);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage() + " == " + e.getCause());
                }
            }
            dialog.dismiss();
        }

        protected void onPreExecute(){
            dialog.setMessage("Cargando datos de la nube");
            dialog.show();
        }

        protected Boolean doInBackground(String... urls) {
            InputStream inputStream = null;
            //envia variable
            String params = "email=" + superemail;
            for(String url1 : urls){
                try{
                    URL url = new URL(url1);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000 /* milisegundos */);
                    conn.setConnectTimeout(15000 /* milisegundos */);
                    // Método para enviar los datos
                    conn.setRequestMethod("POST");
                    // Si se requiere obtener un resultado de la página
                    // se coloca setDoInput(true);
                    conn.setDoInput(true); //recupera json
                    conn.setDoOutput(true); //envia parametro

                    ///ENVÍA PARAMETROS
                    OutputStream out = conn.getOutputStream();
                    Log.d("PARAMS", params);
                    out.write(params.getBytes());
                    out.flush();
                    out.close();
                    ///

                    // Recupera la página
                    conn.connect();
                    int response = conn.getResponseCode();
                    Log.d("SERVIDOR", "Server response: " + response);
                    inputStream = conn.getInputStream();
                    contenido = new Scanner(inputStream).useDelimiter("\\A").next();
                    Log.i("CONTENIDO",contenido); }catch(Exception ex){
                    Log.e("ERROR",ex.toString()); return false; }finally {
                    if (inputStream != null) {
                        try { inputStream.close(); }
                        catch (IOException e) { e.printStackTrace(); }
                    } }
            }
            return true; }

    }

    class CustomLayout extends ArrayAdapter {

        //nota es la clase
        public CustomLayout(Context context, int resource, List<Nota> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View item = convertView;

            if(item== null){
                item= getLayoutInflater().inflate(R.layout.notes_layout, null);
            }

            TextView title = (TextView) item.findViewById(R.id.titleTv);
            TextView content = (TextView) item.findViewById(R.id.contentTv);
            TextView date = (TextView) item.findViewById(R.id.dateTv);

            Nota elemento = (Nota) getItem(position);

            title.setText(elemento.getTitle());
            //content.setText(elemento.getContent());
            date.setText(elemento.getNote_update_date());

            return item;

        }
    }

}
