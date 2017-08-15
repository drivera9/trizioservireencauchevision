package trizio.ram.com.trizioservireencauche;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;


public class Cambiar extends Activity {
    String conexion = "";
    String user = "";
    String pass = "";
    String ip = "";
    String sucursal = "";
    String titulo = "";
    String proceso = "";
    String tipo = "";
    String procesOriginal = "";
    String empresa;
    String Sucursal;
    ProgressDialog mProgressDialog;
    String parametro = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar);

        final Bundle bundle = this.getIntent().getExtras();
        parametro  = bundle.getString("parametro");
        user = bundle.getString("user");
        empresa = bundle.getString("empresa");
        Sucursal = bundle.getString("sucursal");
        procesOriginal = bundle.getString("procesOriginal");
        pass = bundle.getString("pass");
        proceso = bundle.getString("proceso");
        System.out.println("PROCESOOOoOoOoOoo" + proceso);
        conexion = bundle.getString("conexion");
        ip = bundle.getString("ip");
        tipo = bundle.getString("tipo");
        sucursal = bundle.getString("sucursal");
        titulo = bundle.getString("titulo");



        mProgressDialog= new ProgressDialog(Cambiar.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Espere...");


            String url = "http://" + ip + "/consultarDetalle.php";


            final List<NameValuePair> params = new ArrayList<NameValuePair>();

            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());


            params.add(new BasicNameValuePair("sTipoConsulta", "llamar"));
            params.add(new BasicNameValuePair("sAno", String.valueOf(calendar.get(Calendar.YEAR))));
            params.add(new BasicNameValuePair("sMes", String.valueOf(calendar.get(Calendar.MONTH)+1)));
            params.add(new BasicNameValuePair("sDia", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
            params.add(new BasicNameValuePair("sIp", ip));
            params.add(new BasicNameValuePair("sUser", user));
            params.add(new BasicNameValuePair("sPass", pass));
            params.add(new BasicNameValuePair("sBd", "openbravo"));
            params.add(new BasicNameValuePair("sCodProceso", "1"));
            params.add(new BasicNameValuePair("sCodEmpresa", empresa));
            params.add(new BasicNameValuePair("sCodSucursal", Sucursal));
            params.add(new BasicNameValuePair("sTipo", "mercar"));

            System.out.println("--------DETALLE------------");
            System.out.println(user);
            System.out.println(pass);
            System.out.println(proceso.trim());

            /** Get result from Server (Return the JSON Code)
             * StatusID = ? [0=Failed,1=Complete]
             * Error	= ?	[On case error return custom error message]
             *
             * Eg Save Failed = {"StatusID":"0","Error":"Email Exists!"}
             * Eg Save Complete = {"StatusID":"1","Error":""}
             */

            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);
            try {
                JSONArray jArray = new JSONArray(resultServer);
                final ArrayList<String> array = new ArrayList<>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    array.add(json.getString("num_rombo"));
                    array.add(json.getString("cod_placa"));

                }

                final String[] rombos = new String[(array.size())];

                int j = 0;
                for (int i = 0; i < array.size(); i=i+2) {
                    rombos[i] = array.get(i);
                    rombos[i+1] = array.get(i+1);
                    j = j+2;
                }



                final GridView gridview = (GridView) findViewById(R.id.gridView7);// crear el
                // gridview a partir del elemento del xml gridview

                gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), rombos, "grid"));// con setAdapter se llena


                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View v,
                                            final int position, long id) {



                        if (position%2 == 0) {
                            Intent intent = new Intent((Context) Cambiar.this, (Class) CambiarDetalle.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("parametro", parametro);
                            bundle2.putString("user", user);
                            bundle2.putString("pass", pass);
                            bundle2.putString("sucursal", sucursal);
                            bundle2.putString("empresa", empresa);
                            bundle2.putString("ip", ip);
                            bundle2.putString("num_rombo", rombos[position]);
                            bundle2.putString("titulo", titulo + "/Cotizacion");
                            bundle2.putString("nombreTabla", "");
                            bundle2.putString("codAtributo", "");
                            bundle2.putString("conexion", conexion);
                            bundle2.putString("placa", rombos[position+1]);
                            bundle2.putString("ip", ip);
                            bundle2.putString("titulo", titulo + "/Rec");
                            bundle2.putString("proceso", proceso);
                            intent.putExtras(bundle2);
                            startActivity(intent);
                            finish();

                        }

                    }
                });

                String[] titulos = new String[]{"rombo","placa"};

                final GridView gridviewTitulos = (GridView) findViewById(R.id.gridView10);// crear el
                // gridview a partir del elemento del xml gridview

                gridviewTitulos.setAdapter(new CustomGridViewAdapter(getApplicationContext(), titulos, "grd"));

            /*

            String [] total = new String [] {Integer.toString(count)};

            final GridView gridviewTotal = (GridView) findViewById(R.id.gridView13);// crear el
            // gridview a partir del elemento del xml gridview

            gridviewTotal.setAdapter(new CustomGridViewAdapter(getApplicationContext(), total, "grd"));
*/


            } catch (JSONException e) {
                e.printStackTrace();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bodega_rombo, menu);
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

    public void cancelar(View v){
        finish();
    }
}
