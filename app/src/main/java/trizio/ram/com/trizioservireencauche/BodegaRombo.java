package trizio.ram.com.trizioservireencauche;

import android.app.Activity;
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
import java.util.List;


public class BodegaRombo extends Activity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bodega_rombo);

        final Bundle bundle = this.getIntent().getExtras();
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



        if ((procesOriginal.trim().equals("2") || proceso.equals("13")||proceso.equals("14")||proceso.equals("15")||proceso.equals("16")
                ||proceso.equals("17")) && (!procesOriginal.equals("12") || !procesOriginal.equals("16"))) {

            if (!procesOriginal.equals("2")) {

                String url1 = "http://" + ip + "/procesoSiguiente.php";

                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("sCodProceso", proceso));


                String resultServer1 = getHttpPost(url1, params1);
                System.out.println("---------------------------------resultserver----------------");
                System.out.println(" PRUEBA PROCESOS" + resultServer1);
                try {

                    JSONArray jArray1 = new JSONArray(resultServer1);
                    ArrayList<String> array1 = new ArrayList<String>();
                    for (int i = 0; i < jArray1.length(); i++) {
                        JSONObject json = jArray1.getJSONObject(i);
                        array1.add(json.getString("cod_proceso_siguiente"));
                        array1.add(json.getString("cod_proceso_anterior"));
                        proceso = array1.get(1);
                    }

                    System.out.println("PROCESO ANTERIOR " + array1.get(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            String url = "http://" + ip + "/consultarDetalle.php";


            List<NameValuePair> params = new ArrayList<NameValuePair>();

       /* if (proceso.equals("3")){
            params.add(new BasicNameValuePair("sTipoConsulta", "validacion"));
        }else{*/

            if (procesOriginal.trim().equals("14")){
                params.add(new BasicNameValuePair("sTipoConsulta", "romboValidar"));
            }else{
                params.add(new BasicNameValuePair("sTipoConsulta", "rombo"));
            }

            //}
            params.add(new BasicNameValuePair("sIp", ip));
            params.add(new BasicNameValuePair("sUser", user));
            params.add(new BasicNameValuePair("sPass", pass));
            params.add(new BasicNameValuePair("sBd", "openbravo"));
            params.add(new BasicNameValuePair("sCodProceso", proceso));
            params.add(new BasicNameValuePair("sCodEmpresa", empresa));
            params.add(new BasicNameValuePair("sCodSucursal", Sucursal));
            params.add(new BasicNameValuePair("sTipo", "mercar"));

            System.out.println("--------DETALLE------------");
            System.out.println(user);
            System.out.println(pass);
            System.out.println(proceso.trim());

            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);
            try {
                JSONArray jArray = new JSONArray(resultServer);
                final ArrayList<String> array = new ArrayList<>();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    array.add(json.getString("num_rombo"));
                    array.add(json.getString("cod_empresa"));
                    array.add(json.getString("cod_sucursal"));
                    array.add(json.getString("cuenta"));
                }

                final String[] rombos = new String[(array.size() / 4)];

                int j = 0;
                int count = 0;
                for (int i = 0; i < array.size(); i = i + 4) {
                    rombos[j] = array.get(i);
                    j++;
                }



                final GridView gridview = (GridView) findViewById(R.id.gridView7);// crear el
                // gridview a partir del elemento del xml gridview

                gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), rombos, "grid"));// con setAdapter se llena


                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {



                        if (procesOriginal.equals("2")) {
                            Intent intent = new Intent((Context) BodegaRombo.this, (Class) Cotizacion.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("user", user);
                            bundle2.putString("pass", pass);
                            bundle2.putString("sucursal", sucursal);
                            bundle2.putString("empresa", empresa);
                            bundle2.putString("ip", ip);
                            bundle2.putString("num_rombo", rombos[position]);
                            bundle2.putString("titulo", titulo + "/Cotizacion");
                            intent.putExtras(bundle2);
                            startActivity(intent);
                            finish();
                        }else {

                            if (procesOriginal.equals("15")) {
                                Intent intent = new Intent((Context) BodegaRombo.this, (Class) CambiarPromesa.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("ip", ip);
                                bundle2.putString("num_rombo", rombos[position]);
                                bundle2.putString("titulo", titulo + "/CambiarPromesa");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                finish();
                            } else {

                                if (procesOriginal.equals("16")) {
                                    Intent intent = new Intent((Context) BodegaRombo.this, (Class) Evento.class);
                                    Bundle bundle2 = new Bundle();
                                    bundle2.putString("user", user);
                                    bundle2.putString("pass", pass);
                                    bundle2.putString("sucursal", sucursal);
                                    bundle2.putString("empresa", empresa);
                                    bundle2.putString("ip", ip);
                                    bundle2.putString("num_rombo", rombos[position]);
                                    bundle2.putString("titulo", titulo + "/CambiarPromesa");
                                    bundle2.putString("proceso", "1");
                                    intent.putExtras(bundle2);
                                    startActivity(intent);
                                    finish();
                                } else {

                                    if (procesOriginal.equals("17")) {
                                        Intent intent = new Intent((Context) BodegaRombo.this, (Class) Evento.class);
                                        Bundle bundle2 = new Bundle();
                                        bundle2.putString("user", user);
                                        bundle2.putString("pass", pass);
                                        bundle2.putString("sucursal", sucursal);
                                        bundle2.putString("empresa", empresa);
                                        bundle2.putString("ip", ip);
                                        bundle2.putString("num_rombo", rombos[position]);
                                        bundle2.putString("titulo", titulo + "/CambiarPromesa");
                                        bundle2.putString("proceso", "2");
                                        intent.putExtras(bundle2);
                                        startActivity(intent);
                                        finish();
                                    } else {

                                        if (procesOriginal.equals("13") || procesOriginal.equals("14") || procesOriginal.equals("15")) {
                                            Intent intent = new Intent((Context) BodegaRombo.this, (Class) RecepcionValidacion.class);
                                            Bundle bundle2 = new Bundle();
                                            bundle2.putString("user", user);
                                            bundle2.putString("pass", pass);
                                            bundle2.putString("sucursal", sucursal);
                                            bundle2.putString("empresa", empresa);
                                            bundle2.putString("conexion", conexion);
                                            bundle2.putString("sucursal", sucursal);
                                            bundle2.putString("ip", ip);
                                            bundle2.putString("num_rombo", rombos[position]);
                                            bundle2.putString("titulo", titulo + "/Rec");
                                            bundle2.putString("proceso", procesOriginal);
                                            intent.putExtras(bundle2);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            if (procesOriginal.equals("16")) {


                                            } else {
                                                Intent intent = new Intent((Context) BodegaRombo.this, (Class) BodegaMercar2.class);
                                                Bundle bundle2 = new Bundle();
                                                System.out.println("PASOOOOOOOOOOOOOOOOOOOO");
                                                bundle2.putString("rombo", rombos[Integer.parseInt(String.valueOf(id))]);
                                                bundle2.putString("conexion", conexion);
                                                bundle2.putString("user", user);
                                                bundle2.putString("pass", pass);
                                                bundle2.putString("ip", ip);
                                                bundle2.putString("sucursal", sucursal);
                                                bundle2.putString("proceso", proceso);
                                                bundle2.putString("tipo", tipo);
                                                bundle2.putString("procesOriginal", procesOriginal);
                                                bundle2.putString("procesoAnterior", proceso);
                                                bundle2.putString("titulo", titulo + "/Rom" + rombos[Integer.parseInt(String.valueOf(id))]);
                                                intent.putExtras(bundle2);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                    }
                                }
                            }
                        }

                    }
                });

                String[] titulos = new String[]{"rombo"};

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
        }else{
            if (proceso.equals("16")) {

                String url1 = "http://" + ip + "/consultarGeneral.php";

                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("sCodUsuario", user));
                params1.add(new BasicNameValuePair("sCodEmpresa", empresa));
                params1.add(new BasicNameValuePair("sCodSucursal", sucursal));
                params1.add(new BasicNameValuePair("sParametro", "corregir"));

                String[] grid = new String[10] ;
                String resultServer1 = getHttpPost(url1, params1);
                System.out.println("---------------------------------resultserver----------------");
                System.out.println(resultServer1);
                try {

                    JSONArray jArray1 = new JSONArray(resultServer1);
                    ArrayList<String> array1 = new ArrayList<String>();
                    for (int i = 0; i < jArray1.length(); i++) {
                        JSONObject json = jArray1.getJSONObject(i);
                        array1.add(json.getString("num_rombo"));
                    }
                    grid = new String[array1.size()];

                    for (int i = 0;i<array1.size();i++) {
                        grid[i] = array1.get(i).trim();
                    }

                    System.out.println("PROCESO ANTERIOR " + array1.get(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                final GridView gridview = (GridView) findViewById(R.id.gridView7);// crear el
                // gridview a partir del elemento del xml gridview

                gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), grid, "grid"));// con setAdapter se llena


                final String[] finalGrid = grid;
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {


                        Intent intent = new Intent((Context) BodegaRombo.this, (Class) Corregir.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("user", user);
                        bundle2.putString("pass", pass);
                        bundle2.putString("sucursal", sucursal);
                        bundle2.putString("empresa", empresa);
                        bundle2.putString("conexion", conexion);
                        bundle2.putString("sucursal", sucursal);
                        bundle2.putString("ip", ip);
                        bundle2.putString("num_rombo", finalGrid[position]);
                        bundle2.putString("titulo", titulo + "/Rec");
                        bundle2.putString("proceso", procesOriginal);
                        intent.putExtras(bundle2);
                        startActivity(intent);
                        finish();
                    }
                });

                String[] titulos = new String[]{"rombo"};

                final GridView gridviewTitulos = (GridView) findViewById(R.id.gridView10);// crear el
                // gridview a partir del elemento del xml gridview

                gridviewTitulos.setAdapter(new CustomGridViewAdapter(getApplicationContext(), titulos, "grd"));

            /*

            String [] total = new String [] {Integer.toString(count)};

            final GridView gridviewTotal = (GridView) findViewById(R.id.gridView13);// crear el
            // gridview a partir del elemento del xml gridview

            gridviewTotal.setAdapter(new CustomGridViewAdapter(getApplicationContext(), total, "grd"));
*/
            }else{
                String url = "http://" + ip + "/consultarDetalle.php";


                List<NameValuePair> params = new ArrayList<NameValuePair>();

       /* if (proceso.equals("3")){
            params.add(new BasicNameValuePair("sTipoConsulta", "validacion"));
        }else{*/
                params.add(new BasicNameValuePair("sTipoConsulta", "rombo"));
                //}
                params.add(new BasicNameValuePair("sIp", ip));
                params.add(new BasicNameValuePair("sUser", user));
                params.add(new BasicNameValuePair("sPass", pass));
                params.add(new BasicNameValuePair("sBd", "openbravo"));
                params.add(new BasicNameValuePair("sCodProceso", proceso));
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
                        array.add(json.getString("cod_empresa"));
                        array.add(json.getString("cod_sucursal"));
                        array.add(json.getString("cuenta"));
                    }

                    final String[] rombos = new String[(array.size() / 4)];

                    int j = 0;
                    int count = 0;
                    for (int i = 0; i < array.size(); i = i + 4) {
                        rombos[j] = array.get(i);
                        j++;
                    }


                    final GridView gridview = (GridView) findViewById(R.id.gridView7);// crear el
                    // gridview a partir del elemento del xml gridview

                    gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), rombos, "grid"));// con setAdapter se llena


                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View v,
                                                int position, long id) {


                            if (procesOriginal.equals("13") || procesOriginal.equals("14") || procesOriginal.equals("15")) {
                                Intent intent = new Intent((Context) BodegaRombo.this, (Class) RecepcionValidacion.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("num_rombo", rombos[position]);
                                bundle2.putString("titulo", titulo + "/Rec");
                                bundle2.putString("proceso", procesOriginal);
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                finish();
                            } else {
                                if (procesOriginal.equals("16")) {


                                } else {
                                    Intent intent = new Intent((Context) BodegaRombo.this, (Class) BodegaMercar2.class);
                                    Bundle bundle2 = new Bundle();
                                    System.out.println("PASOOOOOOOOOOOOOOOOOOOO");
                                    bundle2.putString("rombo", rombos[Integer.parseInt(String.valueOf(id))]);
                                    bundle2.putString("conexion", conexion);
                                    bundle2.putString("user", user);
                                    bundle2.putString("pass", pass);
                                    bundle2.putString("ip", ip);
                                    bundle2.putString("sucursal", sucursal);
                                    bundle2.putString("proceso", proceso);
                                    bundle2.putString("tipo", tipo);
                                    bundle2.putString("procesOriginal", procesOriginal);
                                    bundle2.putString("procesoAnterior", proceso);
                                    bundle2.putString("titulo", titulo + "/Rom" + rombos[Integer.parseInt(String.valueOf(id))]);
                                    intent.putExtras(bundle2);
                                    startActivity(intent);
                                    finish();
                                }

                            }

                        }
                    });

                    String[] titulos = new String[]{"rombo"};

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
