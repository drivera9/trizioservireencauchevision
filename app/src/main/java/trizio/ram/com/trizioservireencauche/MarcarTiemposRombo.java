package trizio.ram.com.trizioservireencauche;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MarcarTiemposRombo extends Activity {

    String empresa = "";
    String sucursal = "";
    String conexion = "";
    String ip = "";
    String tipo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_tiempos);

        empresa = getIntent().getExtras().getString("empresa");
        sucursal = getIntent().getExtras().getString("sucursal");
        conexion = getIntent().getExtras().getString("conexion");
        ip = getIntent().getExtras().getString("ip");
        tipo = getIntent().getExtras().getString("tipo");

        String url = "http://" + ip + "/consultarDetalle.php";


        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sTipoConsulta", "tiempos"));
        params.add(new BasicNameValuePair("sIp", ip));
        params.add(new BasicNameValuePair("sUser", ""));
        params.add(new BasicNameValuePair("sPass", ""));
        params.add(new BasicNameValuePair("sBd", "openbravo"));
        params.add(new BasicNameValuePair("sCodEmpresa", empresa));
        params.add(new BasicNameValuePair("sCodSucursal", sucursal));
        params.add(new BasicNameValuePair("sCodProceso", ""));
        params.add(new BasicNameValuePair("sTipo", ""));

        System.out.println();
        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        try {
            JSONArray jArray = new JSONArray(resultServer);
            final ArrayList<String> array = new ArrayList<>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("num_rombo"));
            }

            final String[] rombos = new String[(array.size() )];

            for (int i = 0; i < array.size(); i++) {
                rombos[i] = array.get(i);
            }


            GridView gridview = (GridView) findViewById(R.id.gridContenidoTiempos);

            gridview.setAdapter(new CustomGridViewAdapter(getApplicationContext(), rombos, "grid"));// con setAdapter se llena


            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    Intent i = new Intent(MarcarTiemposRombo.this,MarcarTiemposDetalle.class);
                    i.putExtra("rombo",rombos[position]);
                    i.putExtra("empresa",empresa);
                    i.putExtra("sucursal",sucursal);
                    i.putExtra("ip",ip);
                    i.putExtra("tipo",tipo);
                    startActivity(i);
                }
            });

            String[] titulos = new String[]{"rombo"};

            GridView gridviewTitulos = (GridView) findViewById(R.id.gridTitulosTiempos);

            gridviewTitulos.setAdapter(new CustomGridViewAdapter(getApplicationContext(), titulos, "grd"));

        } catch (Exception e) {
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
}
