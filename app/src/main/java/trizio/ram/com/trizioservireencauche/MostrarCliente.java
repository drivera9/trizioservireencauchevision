package trizio.ram.com.trizioservireencauche;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MostrarCliente extends Activity {

    String cod_empresa = "";
    String cod_sucursal = "";
    String conexion = "";
    String ip = "";
    String atributo = "";

    boolean estaNit = false;
    String url = "";

    ProgressDialog mProgressDialog;
    String nombres = "";
    String telefono1 = "";
    String mail = "";
    String direccion = "";
    String celular = "";
    String razonComercial = "";
    String contacto1 = "";
    String contacto2 = "";
    boolean nit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_cliente);

        mProgressDialog= new ProgressDialog(MostrarCliente.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Consultando datos...");

        Bundle bundle = this.getIntent().getExtras();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        conexion = bundle.getString("conexion");
        ip = bundle.getString("ip");
        atributo = bundle.getString("atributo");
        cod_empresa = bundle.getString("empresa");
        cod_sucursal = bundle.getString("sucursal");

        TextView TextNit = (TextView) findViewById(R.id.textView_nit);
        TextNit.setText(atributo);

        final GridView gridview = (GridView) findViewById(R.id.gridView4);
        verCliente();

    }

    public void verCliente(){
        new GuardarMovimiento().execute("");
    }

    class GuardarMovimiento extends AsyncTask<String, String, String> {

        String resultado;
        String user;
        @Override
        protected void onPreExecute(){


            mProgressDialog.show();


        }
        @Override
        protected String doInBackground(String... f_url) {



            ArrayList<Boolean> atributosBool = new ArrayList<>();


            List<NameValuePair> params = new ArrayList<NameValuePair>();

            url = "http://" + ip + "/consultarGeneral.php";
            params.add(new BasicNameValuePair("sParametro", "cliente"));
            params.add(new BasicNameValuePair("sNit",atributo ));
            String resultServer = getHttpPost(url, params);
            System.out.println("---------------------------------");
            System.out.println(resultServer);
            try {
                JSONArray jArray = new JSONArray(resultServer);
                ArrayList<String> array = new ArrayList<String>();
                //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    nombres = (json.getString("nombres"));
                }



            } catch (JSONException e) {
                e.printStackTrace();

                List<NameValuePair> params2 = new ArrayList<NameValuePair>();

                url = "http://" + ip + "/consultarGeneral.php";
                params2.add(new BasicNameValuePair("sParametro", "clienteNuevo"));
                params2.add(new BasicNameValuePair("sNit",atributo ));
                String resultServer2 = getHttpPost(url, params2);
                System.out.println("---------------------------------");
                System.out.println(resultServer2);
                try {
                    JSONArray jArray = new JSONArray(resultServer2);
                    ArrayList<String> array = new ArrayList<String>();
                    //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        nombres = (json.getString("nombres"));
                    }

                } catch (JSONException e2) {
                    e2.printStackTrace();
                }


                nit = false;
            }

            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
            String url2 = "http://" + ip + "/consultarGeneral.php";
            params2.add(new BasicNameValuePair("sParametro", "cliente"));
            params2.add(new BasicNameValuePair("sNit", atributo));
            String resultServer2 = getHttpPost(url2, params2);
            System.out.println("---------------------------------");
            System.out.println(resultServer2);
            try {
                JSONArray jArray2 = new JSONArray(resultServer2);
                ArrayList<String> array = new ArrayList<String>();
                //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                for (int i = 0; i < jArray2.length(); i++) {
                    JSONObject json2 = jArray2.getJSONObject(i);
                    mail = (json2.getString("mail"));
                    telefono1 = (json2.getString("telefono_1"));
                    celular =  (json2.getString("celular"));
                    razonComercial = (json2.getString("razon_comercial"));
                    contacto1 = (json2.getString("contacto_1"));
                    contacto2 = (json2.getString("contacto_2"));
                    direccion = (json2.getString("direccion"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }






            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {

            if (!nit){
                TextView TextNit = (TextView) findViewById(R.id.textView_nit);
                TextNit.setText("No existe el nit");
            }
            EditText EditNombres = (EditText) findViewById(R.id.editText_nombres);
            EditNombres.setText(nombres);

            EditText EditRazon = (EditText) findViewById(R.id.editText_empresa);
            EditRazon.setText(razonComercial);

            EditText EditTel = (EditText) findViewById(R.id.editText_telefono1);
            EditTel.setText(telefono1);

            EditText EditCel = (EditText) findViewById(R.id.editText_telefonoMovil);
            EditCel.setText(celular);

            EditText EditEmail = (EditText) findViewById(R.id.editText_email);
            EditEmail.setText(mail);

            EditText EditDir = (EditText) findViewById(R.id.editText_direccion);
            EditDir.setText(direccion);

            EditText EditContacto = (EditText) findViewById(R.id.editText_contacto1);
            EditContacto.setText(contacto1);

            EditText EditContacto2 = (EditText) findViewById(R.id.editText_contacto2);
            EditContacto2.setText(contacto2);


            TextView textNit = (TextView) findViewById(R.id.textView_nit);
            if (textNit.getText().toString().trim().equals("No existe el nit") || EditNombres.getText().toString().trim().equals("null")){
                estaNit = false;
            }else{
                estaNit = true;
            }

            mProgressDialog.dismiss();

        }
    }

    public void regresar(View v){
        Intent data = new Intent();
        data.putExtra("parametro", "cliente");
        data.putExtra("valor", "no");
        setResult(2, data);
        finish();
    }

    public void guardar(View v){


        EditText EditNombres = (EditText) findViewById(R.id.editText_nombres);
        nombres = EditNombres.getText().toString().trim();

        EditText EditRazon = (EditText) findViewById(R.id.editText_empresa);
        razonComercial = EditRazon.getText().toString().trim();

        EditText EditTel = (EditText) findViewById(R.id.editText_telefono1);
        telefono1 = EditTel.getText().toString().trim();

        EditText EditCel = (EditText) findViewById(R.id.editText_telefonoMovil);
        celular = EditCel.getText().toString().trim();

        EditText EditEmail = (EditText) findViewById(R.id.editText_email);
        mail = EditEmail.getText().toString().trim();

        EditText EditDir = (EditText) findViewById(R.id.editText_direccion);
        direccion = EditDir.getText().toString().trim();

        EditText EditContacto = (EditText) findViewById(R.id.editText_contacto1);
        contacto1 = EditContacto.getText().toString().trim();

        EditText EditContacto2 = (EditText) findViewById(R.id.editText_contacto2);
        contacto2 = EditContacto2.getText().toString().trim();

        if (!estaNit){
            if (EditNombres.getText().toString().trim().equals("") ||
                    EditRazon.getText().toString().trim().equals("") ||
                    EditCel.getText().toString().trim().equals("") ||
                    EditTel.getText().toString().trim().equals("") ||
                    EditEmail.getText().toString().trim().equals("") ||
                    EditDir.getText().toString().trim().equals("") ){
                Toast.makeText(getApplicationContext(), "Hacen falta datos !", Toast.LENGTH_SHORT).show();
            }else {
                String url = "http://" + ip + "/guardarMovimiento.php";

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sNombres", nombres));
                params.add(new BasicNameValuePair("sEmpresa", razonComercial));
                params.add(new BasicNameValuePair("sTelefono", telefono1));
                params.add(new BasicNameValuePair("sCelular", celular));
                params.add(new BasicNameValuePair("sEmail", mail));
                params.add(new BasicNameValuePair("sDireccion", direccion));
                params.add(new BasicNameValuePair("sNit", atributo));
                params.add(new BasicNameValuePair("sContacto1", contacto1));
                params.add(new BasicNameValuePair("sContacto2", contacto2));
                params.add(new BasicNameValuePair("sParametro", "guardarTerceroNuevo"));


                String resultServer = getHttpPost(url, params);
                System.out.println(resultServer);


                try {

                    Intent data = new Intent();
                    data.putExtra("parametro", "cliente");
                    data.putExtra("valor", "guardar");
                    setResult(2, data);
                    finish();
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "No se guardo el detalle", Toast.LENGTH_LONG).show();
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
            }
        }else {

            String url = "http://" + ip + "/guardarMovimiento.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sNombres", nombres));
            params.add(new BasicNameValuePair("sEmpresa", razonComercial));
            params.add(new BasicNameValuePair("sTelefono", telefono1));
            params.add(new BasicNameValuePair("sCelular", celular));
            params.add(new BasicNameValuePair("sEmail", mail));
            params.add(new BasicNameValuePair("sDireccion", direccion));
            params.add(new BasicNameValuePair("sNit", atributo));
            params.add(new BasicNameValuePair("sContacto1", contacto1));
            params.add(new BasicNameValuePair("sContacto2", contacto2));
            params.add(new BasicNameValuePair("sParametro", "3"));


            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);


            try {

                Intent data = new Intent();
                data.putExtra("parametro", "cliente");
                data.putExtra("valor", "guardar");
                setResult(2, data);
                finish();
            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(), "No se guardo el detalle", Toast.LENGTH_LONG).show();
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }
        }
    }

    public String getHttpPost(String url, List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}
