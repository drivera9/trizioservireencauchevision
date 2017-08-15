package trizio.ram.com.trizioservireencauche;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
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
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MostrarRombUbic extends Activity {
    String cod_empresa = "";
    String cod_sucursal = "";
    String[] procesos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_romb_ubic);
        Bundle bundle = this.getIntent().getExtras();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);



        final GridView gridview = (GridView) findViewById(R.id.gridView4);

        final String conexion = bundle.getString("conexion");
        final String ip = bundle.getString("ip");
        final String atributo = bundle.getString("atributo");
        cod_empresa = bundle.getString("empresa");
        cod_sucursal = bundle.getString("sucursal");

        if (conexion.equals("local")) {
            DataBaseHelper dataBaseHelper = new DataBaseHelper((Context) this);
            try {
                dataBaseHelper.createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dataBaseHelper.openDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ArrayList aux = dataBaseHelper.getRombos("METRO", "10");
            final String[] procesos = new String[aux.size()];
            for (int i = 0; i < aux.size(); i++) {
                procesos[i] = aux.get(i).toString();
            }
            // crear el
            // gridview a partir del elemento del xml gridview

            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), procesos, "grid"));// con setAdapter se llena


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    Intent data = new Intent();
                    data.setData(Uri.parse(procesos[position]));
                    setResult(2, data);
                    finish();

                }
            });

            //System.out.println(procesos.get(0).toString() +  " - user : " +  user);

            dataBaseHelper.close();
        }else {
            String url = "";
            System.out.println(atributo +  "<-ATRIBUTOOOO");

            if ((atributo.trim().equals("PROMESA"))){
                procesos = new String[10];

                double horas = 1;
                int minutos = 60;

                for (int i =0;i<procesos.length;i=i+2){
                    procesos[i] = horas + "";
                    procesos[i+1] = minutos + "";
                    horas = horas + 0.5;
                    minutos = minutos + 30;
                }


                gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), procesos, "grid"));// con setAdapter se llena


                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View v,
                                            int position, long id) {

                        Intent data = new Intent();
                        data.setData(Uri.parse(procesos[position].trim()));
                        setResult(2, data);
                        finish();
                    }
                });

            }else {
                if (atributo.trim().equals("CL")) {
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    url = "http://" + ip + "/consultarRecurso.php";
                    params.add(new BasicNameValuePair("sParametro", "CL"));
                    params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa.trim()));
                    params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal.trim()));
                    String resultServer = getHttpPost(url, params);
                    System.out.println("---------------------------------");
                    System.out.println(resultServer);
                    try {
                        JSONArray jArray = new JSONArray(resultServer);
                        ArrayList<String> array = new ArrayList<String>();
                        //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);
                            array.add((json.getString("nit")));
                        }
                        procesos = new String[array.size()];
                        for (int i = 0; i < array.size(); i++) {
                            procesos[i] = array.get(i);
                        }


                        gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), procesos, "grid"));// con setAdapter se llena


                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            public void onItemClick(AdapterView<?> parent, View v,
                                                    int position, long id) {
                                Intent data = new Intent();
                                data.setData(Uri.parse(procesos[position].trim()));
                                setResult(2, data);
                                finish();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (atributo.trim().equals("PAGO")) {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        url = "http://" + ip + "/consultarRecurso.php";
                        params.add(new BasicNameValuePair("sParametro", "PAGO"));
                        params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa.trim()));
                        params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal.trim()));
                        String resultServer = getHttpPost(url, params);
                        System.out.println("---------------------------------");
                        System.out.println(resultServer);
                        try {
                            JSONArray jArray = new JSONArray(resultServer);
                            ArrayList<String> array = new ArrayList<String>();
                            //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                array.add((json.getString("val_recurso")));
                            }
                            procesos = new String[array.size()];
                            for (int i = 0; i < array.size(); i++) {
                                procesos[i] = array.get(i);
                            }


                            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), procesos, "grid"));// con setAdapter se llena


                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                public void onItemClick(AdapterView<?> parent, View v,
                                                        int position, long id) {
                                    Intent data = new Intent();
                                    data.putExtra("parametro", "PAGO");
                                    data.putExtra("valor", procesos[position].trim());
                                    //data.setDataAndType(Uri.parse(procesos[position+1].trim()),procesos[position].trim().toString());

                                    setResult(2, data);
                                    finish();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (atributo.trim().equals("TECNICO")) {

                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                            url = "http://" + ip + "/consultarGeneral.php";
                            params.add(new BasicNameValuePair("sParametro", "operarios"));

                            String resultServer = getHttpPost(url, params);
                            System.out.println(resultServer);
                            try {
                                JSONArray jArray = new JSONArray(resultServer);
                                final ArrayList<String> array = new ArrayList<String>();
                                procesos = new String[jArray.length()];
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json = jArray.getJSONObject(i);
                                    procesos[i] = json.getString("nombres");
                                    array.add(json.getString("nit"));
                                }

                                gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), procesos, "grid"));// con setAdapter se llena


                                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    public void onItemClick(AdapterView<?> parent, View v,
                                                            int position, long id) {
                                        Intent data = new Intent();
                                        data.putExtra("parametro", "tecnico");
                                        data.putExtra("valor", array.get(position).trim());
                                        //data.setDataAndType(Uri.parse(procesos[position+1].trim()),procesos[position].trim().toString());

                                        setResult(2, data);
                                        finish();
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (atributo.trim().equals("UBICACION")) {

                                DataBaseHelper dataBaseHelper = new DataBaseHelper(MostrarRombUbic.this);
                                ArrayList arryUbic = new ArrayList();
                                try {
                                    dataBaseHelper.createDataBase();
                                    dataBaseHelper.openDataBase();
                                    arryUbic = dataBaseHelper.consultarUbicaciones();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                                procesos = new String[arryUbic.size()];
                                for (int i = 0; i < arryUbic.size(); i++) {
                                    procesos[i] = arryUbic.get(i).toString().trim();
                                }

                                gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), procesos, "grid"));// con setAdapter se llena


                                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    public void onItemClick(AdapterView<?> parent, View v,
                                                            int position, long id) {
                                        Intent data = new Intent();
                                        data.putExtra("parametro", "ubicacion");
                                        data.putExtra("valor", procesos[position].trim());
                                        //data.setDataAndType(Uri.parse(procesos[position+1].trim()),procesos[position].trim().toString());

                                        setResult(2, data);
                                        finish();
                                    }
                                });

                            } else {
                                if (atributo.trim().equals("TECNICO")) {

                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(MostrarRombUbic.this);
                                    ArrayList arrayTecnicos = new ArrayList();
                                    try {
                                        dataBaseHelper.createDataBase();
                                        dataBaseHelper.openDataBase();
                                        arrayTecnicos = dataBaseHelper.consultarTecnicos();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    } catch (Throwable e) {
                                        e.printStackTrace();
                                    }
                                    procesos = new String[arrayTecnicos.size()];
                                    for (int i = 0; i < arrayTecnicos.size(); i++) {
                                        procesos[i] = arrayTecnicos.get(i).toString().trim();
                                    }

                                    gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), procesos, "grid"));// con setAdapter se llena


                                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        public void onItemClick(AdapterView<?> parent, View v,
                                                                int position, long id) {
                                            Intent data = new Intent();
                                            data.putExtra("parametro", "tecnico");
                                            data.putExtra("valor", procesos[position].trim());
                                            //data.setDataAndType(Uri.parse(procesos[position+1].trim()),procesos[position].trim().toString());

                                            setResult(2, data);
                                            finish();
                                        }
                                    });

                                } else {
                                    if (atributo.trim().equals("RAZON2")) {
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        url = "http://" + ip + "/consultarRecurso.php";
                                        params.add(new BasicNameValuePair("sParametro", "RAZON"));
                                        params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa.trim()));
                                        params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal.trim()));
                                        String resultServer = getHttpPost(url, params);
                                        System.out.println("---------------------------------");
                                        System.out.println(resultServer);


                                        try {
                                            JSONArray jArray = new JSONArray(resultServer);
                                            ArrayList<String> array = new ArrayList<String>();
                                            //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                                            int j = 0;
                                            for (int i = 0; i < jArray.length(); i++) {
                                                JSONObject json = jArray.getJSONObject(i);
                                                array.add((json.getString("razon")));
                                                array.add((json.getString("descripcion")));
                                                //procesos[j] = array.get(j);
                                                //procesos[j+1] = array.get(j+1);
                                                //grid[i] = array.get(j+1);
                                                //j=j+2;
                                            }

                                            procesos = new String[array.size()];
                                            final String[] grid = new String[array.size() / 2];
                                            int k = 0;
                                            for (int i = 0; i < array.size(); i = i + 2) {
                                                procesos[k] = array.get(i);
                                                System.out.println(procesos[k]);
                                                k = k + 1;
                                            }
                                            k = 0;
                                            for (int i = 0; i < array.size() / 2; i++) {
                                                grid[i] = array.get(k + 1);
                                                k = k + 2;
                                            }


                                            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), grid, "grid"));// con setAdapter se llena


                                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                public void onItemClick(AdapterView<?> parent, View v,
                                                                        int position, long id) {

                                                    if (grid[position].length() > 2) {

                                                        Intent data = new Intent();
                                                        data.putExtra("parametro", "razon2");
                                                        data.putExtra("valor", procesos[position].trim());
                                                        data.putExtra("km", grid[position].trim());
                                                        //data.setDataAndType(Uri.parse(procesos[position+1].trim()),procesos[position].trim().toString());

                                                        setResult(2, data);
                                                        finish();
                                                    }
                                                }
                                            });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        if (atributo.trim().equals("RAZON")) {
                             /* List<NameValuePair> params = new ArrayList<NameValuePair>();
                                url = "http://" + ip + "/consultarRecurso.php";
                                params.add(new BasicNameValuePair("sParametro", "K"));
                                params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa.trim()));
                                params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal.trim()));
                                String resultServer = getHttpPost(url, params);
                                System.out.println("---------------------------------");
                                System.out.println(resultServer);


                            try {
                                JSONArray jArray = new JSONArray(resultServer);
                                ArrayList<String> array = new ArrayList<String>();
                                //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                                int j = 0;
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json = jArray.getJSONObject(i);
                                    //array.add((json.getString("razon")));
                                    array.add((json.getString("descripcion")));
                                    //procesos[j] = array.get(j);
                                    //procesos[j+1] = array.get(j+1);
                                    //grid[i] = array.get(j+1);
                                    //j=j+2;
                                }

                                procesos = new String[array.size()];
                                final String[] grid = new String[array.size()/2];
                                int k = 0;
                                for (int i = 0;i<array.size();i=i+2){
                                    procesos[k] = array.get(i);
                                    System.out.println(procesos[k]);
                                    k=k+1;
                                }
                                k=0;
                                for (int i = 0;i<array.size()/2;i++){
                                    grid[i] = array.get(k+1);
                                    k=k+2;
                                }

                                String[] procesos2 = new String[array.size()];
                                for (int i = 0;i<array.size();i=i+2){
                                    procesos2[k] = array.get(i);
                                    System.out.println(procesos2[k]);
                                    k=k+1;
                                }*/

                                            final String[] grid2 = {"CLIENTE",
                                                    "CLIENTE NUEVO"
                                                    , "RECOMENDADO"
                                                    , "SOAT"
                                                    , "RTM"
                                                    , "CALL-CENTER"
                                                    , "PREVIAJE"
                                            };

                                            final String[] procesos2 = new String[grid2.length];
                                            for (int i = 0; i < grid2.length; i++) {
                                                procesos2[i] = i + 1 + "";
                                                System.out.println(procesos2[i]);
                                            }

                                            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), grid2, "grid"));// con setAdapter se llena


                                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                public void onItemClick(AdapterView<?> parent, View v,
                                                                        int position, long id) {

                                                    if (grid2[position].length() > 2) {

                                                        Intent data = new Intent();
                                                        data.putExtra("parametro", "razon");
                                                        data.putExtra("valor", procesos2[position].trim());
                                                        data.putExtra("descripcion", grid2[position]);
                                                        //data.setDataAndType(Uri.parse(procesos[position+1].trim()),procesos[position].trim().toString());

                                                        data.putExtras(data);
                                                        setResult(2, data);
                                                        finish();
                                                    }
                                                }
                                            });
                                            //} catch (JSONException e) {
                                            //  e.printStackTrace();
                                            //}
                                        } else {
                                            if (atributo.trim().equals("TIPO VEHICULO")) {

                                                final String[] grid2 = {"A"
                                                        , "C"
                                                        , "K"
                                                };

                                                final String[] procesos2 = new String[grid2.length];
                                                for (int i = 0; i < grid2.length; i++) {
                                                    procesos2[i] = i + 1 + "";
                                                    System.out.println(procesos2[i]);
                                                }

                                                gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), grid2, "grid"));// con setAdapter se llena


                                                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                    public void onItemClick(AdapterView<?> parent, View v,
                                                                            int position, long id) {

                                                        Intent data = new Intent();
                                                        data.putExtra("parametro", "vehiculo");
                                                        data.putExtra("valor", grid2[position].trim());
                                                        //data.setDataAndType(Uri.parse(procesos[position+1].trim()),procesos[position].trim().toString());

                                                        data.putExtras(data);
                                                        setResult(2, data);
                                                        finish();

                                                    }
                                                });
                                                //} catch (JSONException e) {
                                                //  e.printStackTrace();
                                                //}
                                            } else {
                                                if (atributo.trim().equals("PRUEBA")) {

                                                    DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                                                    url = "http://" + ip + "/consultarRecurso.php";
                                                    HttpPost httppost = new HttpPost(url);
                                                } else {

                                                    if (atributo.trim().equals("ROMBO")) {

                                                        String bodega = "";
                                                        if (cod_sucursal.trim().equals("51")) {
                                                            bodega = "1";
                                                        } else {
                                                            if (cod_sucursal.trim().equals("33")) {
                                                                bodega = "2";
                                                            } else {
                                                                if (cod_sucursal.trim().equals("10")) {
                                                                    bodega = "3";
                                                                } else {
                                                                    if (cod_sucursal.trim().equals("99")) {
                                                                        bodega = "11";
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        url = "http://" + ip + "/consultarRecurso.php";
                                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                        params.add(new BasicNameValuePair("sParametro", "R"));
                                                        params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa.trim()));
                                                        params.add(new BasicNameValuePair("sCodSucursal", bodega.trim()));

                                                        String resultServer = getHttpPost(url, params);
                                                        System.out.println("---------------------------------");
                                                        System.out.println(resultServer);
                                                        try {
                                                            JSONArray jArray = new JSONArray(resultServer);
                                                            ArrayList<String> array = new ArrayList<String>();
                                                            //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                                                            for (int i = 0; i < jArray.length(); i++) {
                                                                JSONObject json = jArray.getJSONObject(i);
                                                                array.add((json.getString("rombo")));
                                                            }
                                                            procesos = new String[array.size()];
                                                            for (int i = 0; i < array.size(); i++) {
                                                                procesos[i] = array.get(i);
                                                            }


                                                            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), procesos, "grid"));// con setAdapter se llena


                                                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                                public void onItemClick(AdapterView<?> parent, View v,
                                                                                        int position, long id) {

                                                                    Intent data = new Intent();
                                                                    data.putExtra("parametro", "rombo");
                                                                    data.putExtra("valor", procesos[position].trim());
                                                                    //data.setDataAndType(Uri.parse(procesos[position+1].trim()),procesos[position].trim().toString());

                                                                    data.putExtras(data);
                                                                    setResult(2, data);
                                                                    finish();
                                                                }
                                                            });
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    } else {


                                                        List<NameValuePair> params = new ArrayList<NameValuePair>();


                                                        params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa.trim()));
                                                        params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal.trim()));
                                                        if (atributo.trim().equals("ROMBO")) {
                                                            url = "http://" + ip + "/consultarRecurso.php";
                                                            params.add(new BasicNameValuePair("sParametro", "R"));
                                                        } else {
                                                            if (atributo.trim().equals("MECANICO")) {
                                                                url = "http://" + ip + "/consultarRecurso.php";
                                                                params.add(new BasicNameValuePair("sParametro", "M"));
                                                            } else {
                                                                if (atributo.trim().equals("RAZON2")) {
                                                                    url = "http://" + ip + "/consultarRecurso.php";
                                                                    params.add(new BasicNameValuePair("sParametro", "K"));
                                                                } else {
                                                                    if (atributo.trim().equals("UBICACION")) {
                                                                        url = "http://" + ip + "/consultarRecurso.php";
                                                                        params.add(new BasicNameValuePair("sParametro", "U"));
                                                                    } else {
                                                                        if (atributo.trim().equals("SOLICITUD")) {
                                                                            url = "http://" + ip + "/consultarRecurso.php";
                                                                            params.add(new BasicNameValuePair("sParametro", "S"));
                                                                        } else {
                                                                        /*if (atributo.trim().equals("TECNICO")) {
                                                                            url = "http://" + ip + "/consultarRecurso.php";
                                                                            params.add(new BasicNameValuePair("sParametro", "T"));
                                                                        } else {*/
                                                                            url = "http://" + ip + "/consultarRecurso.php";
                                                                            params.add(new BasicNameValuePair("sParametro", "U"));
                                                                            //}
                                                                        }
                                                                    }
                                                                }

                                                            }
                                                        }

                                                        String resultServer = getHttpPost(url, params);
                                                        System.out.println("---------------------------------");
                                                        System.out.println(resultServer);
                                                        try {
                                                            JSONArray jArray = new JSONArray(resultServer);
                                                            ArrayList<String> array = new ArrayList<String>();
                                                            //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                                                            for (int i = 0; i < jArray.length(); i++) {
                                                                JSONObject json = jArray.getJSONObject(i);
                                                                array.add((json.getString("val_recurso")));
                                                            }
                                                            procesos = new String[array.size()];
                                                            for (int i = 0; i < array.size(); i++) {
                                                                procesos[i] = array.get(i);
                                                            }


                                                            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), procesos, "grid"));// con setAdapter se llena


                                                            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                                                public void onItemClick(AdapterView<?> parent, View v,
                                                                                        int position, long id) {

                                                                    Intent data = new Intent();
                                                                    if (atributo.trim().equals("UBICACION")) {
                                                                        data.putExtra("parametro", "ubicacion");
                                                                    } else {
                                                                        if (atributo.trim().equals("TECNICO")) {
                                                                            data.putExtra("parametro", "tecnico");
                                                                        } else {
                                                                            if (atributo.trim().equals("SOLICITUD")) {
                                                                                data.putExtra("parametro", "solicitud");
                                                                            } else {
                                                                                data.putExtra("parametro", "rombo");
                                                                            }
                                                                        }
                                                                    }
                                                                    data.putExtra("valor", procesos[position].trim());
                                                                    //data.setDataAndType(Uri.parse(procesos[position+1].trim()),procesos[position].trim().toString());

                                                                    data.putExtras(data);
                                                                    setResult(2, data);
                                                                    finish();
                                                                }
                                                            });
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
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
        getMenuInflater().inflate(R.menu.menu_mostrar_romb_ubic, menu);
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
        Intent data = new Intent();
        data.setData(Uri.parse("regresar"));
        setResult(2, data);
        finish();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
