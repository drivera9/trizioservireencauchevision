package trizio.ram.com.trizioservireencauche;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MarcarOperarioDetalle extends Activity {
    String empresa = "";
    String sucursal = "";
    String conexion = "";
    String ip = "";
    String rombo = "";
    String tipo = "";
    ArrayAdapter<String> operarios ;
    ArrayAdapter<String> nitOperarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_operario_detalle);
        empresa = getIntent().getExtras().getString("empresa");
        sucursal = getIntent().getExtras().getString("sucursal");
        conexion = getIntent().getExtras().getString("conexion");
        ip = getIntent().getExtras().getString("ip");
        rombo = getIntent().getExtras().getString("rombo");

        TextView titulo = (TextView) findViewById(R.id.tituloTipo) ;
        titulo.setText("Cambiar Operario");

        operarios  = new ArrayAdapter<String>(MarcarOperarioDetalle.this, android.R.layout.select_dialog_item);
        nitOperarios = new ArrayAdapter<String>(MarcarOperarioDetalle.this, android.R.layout.select_dialog_item);

        String url = "http://" + ip + "/consultarGeneral.php";


        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "detalleOrdenOperario"));
        params.add(new BasicNameValuePair("sCodSucursal", sucursal));
        params.add(new BasicNameValuePair("sCodEmpresa", empresa));
        params.add(new BasicNameValuePair("sRombo", rombo));


        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

        ArrayList datos = new ArrayList<Lista_entrada_tall_detalle>();
        try {
            JSONArray jArray = new JSONArray(resultServer);
            final ArrayList<String> array = new ArrayList<>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                datos.add( new Lista_entrada_tall_detalle(R.drawable.recoger,json.getString("operacion").trim(),
                        json.getString("numero").trim()));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        url = "http://" + ip + "/consultarGeneral.php";

        params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "operarios"));

        resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

        try {
            JSONArray jArray = new JSONArray(resultServer);
            final ArrayList<String> array = new ArrayList<>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                operarios.add(json.getString("nombres").trim());
                nitOperarios.add(json.getString("nit").trim());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


        ListView lista = (ListView) findViewById(R.id.productosDetalle);
        lista.setAdapter(new Lista_adaptador(this, R.layout.entrada, datos) {
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior);
                    if (texto_superior_entrada != null)
                        texto_superior_entrada.setText(((Lista_entrada_tall_detalle) entrada).get_textoEncima());


                    ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen);
                    if (imagen_entrada != null)
                        imagen_entrada.setImageResource(((Lista_entrada_tall_detalle) entrada).get_idImagen());
                }
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                final Lista_entrada_tall_detalle elegido = (Lista_entrada_tall_detalle) pariente.getItemAtPosition(posicion);

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MarcarOperarioDetalle.this);
                builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Seleccione operario");

                /*final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MarcarOperarioDetalle.this, android.R.layout.select_dialog_singlechoice);
                arrayAdapter.add("Hardik");
                arrayAdapter.add("Archit");
                arrayAdapter.add("Jignesh");
                arrayAdapter.add("Umang");
                arrayAdapter.add("Gatti");*/

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(operarios, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String strName = nitOperarios.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(MarcarOperarioDetalle.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("El operario seleccionado es : " + operarios.getItem(which));
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                String url = "http://" + ip + "/guardarMovimiento.php";


                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("sParametro", "detalleOrdenOperario"));
                                params.add(new BasicNameValuePair("sCodigo", elegido.get_textoEncima().trim()));
                                params.add(new BasicNameValuePair("sNumero", elegido.getId().trim()));
                                params.add(new BasicNameValuePair("sOperario", strName.trim()));

                                String resultServer = getHttpPost(url, params);
                                System.out.println(resultServer);
                                Toast.makeText(getApplicationContext(), "Haz actualizado correctamente", Toast.LENGTH_SHORT).show();
                                actualizarLista();
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();

                /*AlertDialog.Builder builder1 = new AlertDialog.Builder(MarcarOperarioDetalle.this);
                builder1.setMessage("DESEA ACTUALIZAR LA FECHA?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "SI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String url = "http://" + ip + "/guardarMovimiento.php";


                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("sParametro", "detalleOrden"));
                                params.add(new BasicNameValuePair("sCodigo", elegido.get_textoEncima().trim()));
                                params.add(new BasicNameValuePair("sNumero", elegido.getId().trim()));
                                if (tipo.trim().equals("inicial")){
                                    params.add(new BasicNameValuePair("sTipo", "inicial"));
                                }else{
                                    params.add(new BasicNameValuePair("sTipo", "final"));
                                }


                                String resultServer = getHttpPost(url, params);
                                System.out.println(resultServer);
                                Toast.makeText(getApplicationContext(), "Haz actualizado correctamente la fecha", Toast.LENGTH_SHORT).show();
                                actualizarLista();
                            }
                        });

                builder1.setNegativeButton(
                        "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();*/

            }
        });
    }

    public void actualizarLista(){
        String url = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "detalleOrdenOperario"));
        params.add(new BasicNameValuePair("sCodSucursal", sucursal));
        params.add(new BasicNameValuePair("sCodEmpresa", empresa));
        params.add(new BasicNameValuePair("sRombo", rombo));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

        ArrayList datos = new ArrayList<Lista_entrada_tall_detalle>();
        try {
            JSONArray jArray = new JSONArray(resultServer);
            final ArrayList<String> array = new ArrayList<>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                datos.add( new Lista_entrada_tall_detalle(R.drawable.recoger,json.getString("operacion").trim(),
                        json.getString("numero").trim()));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }


        ListView lista = (ListView) findViewById(R.id.productosDetalle);
        lista.setAdapter(new Lista_adaptador(this, R.layout.entrada, datos) {
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {
                    TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior);
                    if (texto_superior_entrada != null)
                        texto_superior_entrada.setText(((Lista_entrada_tall_detalle) entrada).get_textoEncima());


                    ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen);
                    if (imagen_entrada != null)
                        imagen_entrada.setImageResource(((Lista_entrada_tall_detalle) entrada).get_idImagen());
                }
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                final Lista_entrada_tall_detalle elegido = (Lista_entrada_tall_detalle) pariente.getItemAtPosition(posicion);

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MarcarOperarioDetalle.this);
                builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("SELECCIONE OPERARIO");



                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(operarios, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String strName = nitOperarios.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(MarcarOperarioDetalle.this);
                        builderInner.setMessage(strName);
                        builderInner.setTitle("El operario seleccionado es : " + operarios.getItem(which));
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                String url = "http://" + ip + "/guardarMovimiento.php";


                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("sParametro", "detalleOrdenOperario"));
                                params.add(new BasicNameValuePair("sCodigo", elegido.get_textoEncima().trim()));
                                params.add(new BasicNameValuePair("sNumero", elegido.getId().trim()));
                                params.add(new BasicNameValuePair("sOperario", strName.trim()));

                                String resultServer = getHttpPost(url, params);
                                System.out.println(resultServer);
                                Toast.makeText(getApplicationContext(), "Haz actualizado correctamente ", Toast.LENGTH_SHORT).show();
                                actualizarLista();
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();

                /*AlertDialog.Builder builder1 = new AlertDialog.Builder(MarcarOperarioDetalle.this);
                builder1.setMessage("DESEA ACTUALIZAR LA FECHA?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "SI",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String url = "http://" + ip + "/guardarMovimiento.php";


                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("sParametro", "detalleOrden"));
                                params.add(new BasicNameValuePair("sCodigo", elegido.get_textoEncima().trim()));
                                params.add(new BasicNameValuePair("sNumero", elegido.getId().trim()));
                                if (tipo.trim().equals("inicial")){
                                    params.add(new BasicNameValuePair("sTipo", "inicial"));
                                }else{
                                    params.add(new BasicNameValuePair("sTipo", "final"));
                                }
                                String resultServer = getHttpPost(url, params);
                                System.out.println(resultServer);
                                Toast.makeText(getApplicationContext(), "Haz actualizado correctamente la fecha", Toast.LENGTH_SHORT).show();
                                actualizarLista();
                            }
                        });

                builder1.setNegativeButton(
                        "NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();*/

            }
        });
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
