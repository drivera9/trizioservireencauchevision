package trizio.ram.com.trizioservireencauche;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class IniciarPararProceso extends Activity {

    String user = "";
    String pass = "";
    String ip = "";
    String sucursal = "";
    String empresa = "";
    String proceso = "";
    Timer timerR;
    Timer timerA;
    Timer timerV;
    Timer timerColor;
    String orden = "";
    String placa = "";
    String rombo = "";
    String operacionEvento = "";
    String eventoElegido = "";
    String descEventoElegido = "";
    String titulo = "";
    Spinner spDet;
    Spinner sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_parar_proceso);

        user = getIntent().getExtras().getString("user");
        pass = getIntent().getExtras().getString("pass");
        ip = getIntent().getExtras().getString("ip");
        sucursal = getIntent().getExtras().getString("sucursal");
        empresa = getIntent().getExtras().getString("empresa");
        proceso = getIntent().getExtras().getString("proceso");

        String url = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "rombosPorTecnico"));
        params.add(new BasicNameValuePair("sSucursal", sucursal.trim()));
        params.add(new BasicNameValuePair("sEmpresa", empresa.trim()));
        params.add(new BasicNameValuePair("sProceso", proceso.trim()));
        params.add(new BasicNameValuePair("sUsuario", user.trim()));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        ArrayList<String> array = new ArrayList<String>();

        final ArrayList<String> ubicaciones = new ArrayList<String>();
        final ArrayList<String> rombos = new ArrayList<String>();
        final ArrayList<String> placas = new ArrayList<String>();
        final ArrayList<String> tecnicos = new ArrayList<String>();
        final ArrayList<String> ordenes = new ArrayList<String>();
        try {

            JSONArray jArray = new JSONArray(resultServer);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("cod_ubicacion"));
                ubicaciones.add(json.getString("cod_ubicacion").trim());
                array.add(json.getString("num_rombo"));
                rombos.add(json.getString("num_rombo").trim());
                array.add(json.getString("cod_placa"));
                placas.add(json.getString("cod_placa").trim());
                array.add(json.getString("cod_tecnico"));
                tecnicos.add(json.getString("cod_tecnico".trim()));
                ordenes.add(json.getString("numero".trim()));
                titulo = json.getString("nombres".trim());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView t = (TextView) findViewById(R.id.titulo);
        t.setText(titulo);

        final String[] datos = new String[(array.size()/4)];
        int j = 0;
        for (int i = 0;i<array.size();i=i+4){
            datos[j] = array.get(i).trim() + " - " + array.get(i+1).trim() + " - " + array.get(i+2).trim()  ;
            j++;
        }

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(this,
                        R.layout.spinner_item, datos);

        sp = (Spinner)findViewById(R.id.spinnerPlacas);

        adaptador.setDropDownViewResource(
                R.layout.spinner_item_drop);

        sp.setAdapter(adaptador);

        sp.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {

                        TextView rojo = (TextView) findViewById(R.id.textRojo);
                        TextView amarillo = (TextView) findViewById(R.id.textAmarillo);
                        TextView verde = (TextView) findViewById(R.id.textVerde);

                        rojo.setBackgroundResource(R.drawable.circle_text_view_rojo);
                        amarillo.setBackgroundResource(R.drawable.circle_text_view_amarillo);
                        verde.setBackgroundResource(R.drawable.circle_text_view_verde);

                        TextView labelVerde = (TextView) findViewById(R.id.labelVerde);
                        TextView labelAmarillo = (TextView) findViewById(R.id.labelAmarillo);
                        TextView labelRojo = (TextView) findViewById(R.id.labelRojo);

                        labelVerde.setText("");
                        labelAmarillo.setText("");
                        labelRojo.setText("");
                        rojo.setText("");
                        amarillo.setText("");
                        verde.setText("");

                        String url = "http://" + ip + "/consultarGeneral.php";

                        orden = ordenes.get(position).trim();

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("sParametro", "detallePorOrden"));
                        params.add(new BasicNameValuePair("sOrden", ordenes.get(position).trim()));
                        params.add(new BasicNameValuePair("sUsuario", user.trim()));

                        String resultServer = getHttpPost(url, params);
                        System.out.println(resultServer);
                        ArrayList<String> array = new ArrayList<String>();
                        try {

                            JSONArray jArray = new JSONArray(resultServer);

                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                array.add(json.getString("operacion"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String[] datos = new String[array.size()];
                        for (int i = 0;i<array.size();i++){
                            datos[i] = array.get(i).trim();
                        }

                        if (datos.length == 0 ){
                            verde.setClickable(false);
                            amarillo.setClickable(false);
                            rojo.setClickable(false);
                            Button eventoVerde = (Button) findViewById(R.id.botonEventoVerde);
                            Button eventoAmarillo = (Button) findViewById(R.id.botonEventoAmarillo);
                            Button eventoRojo = (Button) findViewById(R.id.botonEventoRojo);

                            eventoVerde.setClickable(false);
                            eventoAmarillo.setClickable(false);
                            eventoRojo.setClickable(false);
                        }else{
                            verde.setClickable(true);
                            amarillo.setClickable(false);
                            rojo.setClickable(false);
                            Button eventoVerde = (Button) findViewById(R.id.botonEventoVerde);
                            Button eventoAmarillo = (Button) findViewById(R.id.botonEventoAmarillo);
                            Button eventoRojo = (Button) findViewById(R.id.botonEventoRojo);

                            eventoVerde.setClickable(true);
                            eventoAmarillo.setClickable(false);
                            eventoRojo.setClickable(false);
                        }
                        ArrayAdapter<String> adaptador =
                                new ArrayAdapter<String>(getApplicationContext(),
                                        R.layout.spinner_item, datos);

                        spDet = (Spinner)findViewById(R.id.spinnerDetalle);

                        adaptador.setDropDownViewResource(
                                R.layout.spinner_item_drop);

                        spDet.setAdapter(adaptador);

                        spDet.setOnItemSelectedListener(
                                new AdapterView.OnItemSelectedListener() {
                                    public void onItemSelected(AdapterView<?> parent,
                                                               android.view.View v, int position, long id) {



                                        if (timerR != null) {
                                            timerR.cancel();
                                            timerR.purge();
                                            timerR = null;
                                        }

                                        if (timerV != null) {
                                            timerV.cancel();
                                            timerV.purge();
                                            timerV = null;
                                        }

                                        if (timerA != null) {
                                            timerA.cancel();
                                            timerA.purge();
                                            timerA = null;
                                        }

                                        TextView rojo = (TextView) findViewById(R.id.textRojo);
                                        TextView amarillo = (TextView) findViewById(R.id.textAmarillo);
                                        TextView verde = (TextView) findViewById(R.id.textVerde);

                                        rojo.setBackgroundResource(R.drawable.circle_text_view_rojo);
                                        amarillo.setBackgroundResource(R.drawable.circle_text_view_amarillo);
                                        verde.setBackgroundResource(R.drawable.circle_text_view_verde);

                                        TextView labelVerde = (TextView) findViewById(R.id.labelVerde);
                                        TextView labelAmarillo = (TextView) findViewById(R.id.labelAmarillo);
                                        TextView labelRojo = (TextView) findViewById(R.id.labelRojo);

                                        labelVerde.setText("");
                                        labelAmarillo.setText("");
                                        labelRojo.setText("");
                                        rojo.setText("");
                                        amarillo.setText("");
                                        verde.setText("");
                                    }

                                    public void onNothingSelected(AdapterView<?> parent) {
                                        EditText u = (EditText) findViewById(R.id.editUbic) ;
                                        u.setText("");

                                        EditText r = (EditText) findViewById(R.id.editRombo) ;
                                        r.setText("");

                                        EditText p = (EditText) findViewById(R.id.editPlaca) ;
                                        p.setText("");

                                        EditText t = (EditText) findViewById(R.id.editTecnico) ;
                                        t.setText("");
                                    }
                                });

                        EditText u = (EditText) findViewById(R.id.editUbic) ;
                        u.setText(ubicaciones.get(position).trim());

                        EditText r = (EditText) findViewById(R.id.editRombo) ;
                        r.setText(rombos.get(position).trim());

                        EditText p = (EditText) findViewById(R.id.editPlaca) ;
                        p.setText(placas.get(position).trim());

                        EditText t = (EditText) findViewById(R.id.editTecnico) ;
                        t.setText(tecnicos.get(position).trim());
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        EditText u = (EditText) findViewById(R.id.editUbic) ;
                        u.setText("");

                        EditText r = (EditText) findViewById(R.id.editRombo) ;
                        r.setText("");

                        EditText p = (EditText) findViewById(R.id.editPlaca) ;
                        p.setText("");

                        EditText t = (EditText) findViewById(R.id.editTecnico) ;
                        t.setText("");
                    }
                });

        TextView rojo = (TextView) findViewById(R.id.textRojo);
        rojo.setClickable(false);

        TextView amarillo = (TextView) findViewById(R.id.textAmarillo);
        amarillo.setClickable(false);
    }

    public void guardarEventoVerde(View v){

        String url = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "nombreEventos"));
        params.add(new BasicNameValuePair("sTipo", "I"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        final ArrayAdapter<String> eventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);
        final ArrayAdapter<String> codEventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);

        try {

            JSONArray jArray = new JSONArray(resultServer);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                eventos.add(json.getString("nom_evento").trim());
                codEventos.add(json.getString("cod_evento").trim());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(IniciarPararProceso.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Seleccione evento");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setCancelable(false);
        builderSingle.setAdapter(eventos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int elegido = which;
                AlertDialog.Builder builderInner = new AlertDialog.Builder(IniciarPararProceso.this);
                builderInner.setMessage("");
                builderInner.setCancelable(false);
                builderInner.setTitle("EVENTO : " + eventos.getItem(which));
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {

                        Spinner spinner = (Spinner)findViewById(R.id.spinnerDetalle);
                        String operacion = spinner.getSelectedItem().toString();

                        EditText r = (EditText) findViewById(R.id.editRombo) ;

                        EditText p = (EditText) findViewById(R.id.editPlaca) ;

                        String url = "http://" + ip + "/guardarMovimiento.php";

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("sParametro", "eventoDetalleOrden"));
                        params.add(new BasicNameValuePair("sOperacion",operacion.trim()));
                        params.add(new BasicNameValuePair("sNumero", orden));
                        params.add(new BasicNameValuePair("sRombo", r.getText().toString().trim()));
                        params.add(new BasicNameValuePair("sPlaca", p.getText().toString().trim()));
                        params.add(new BasicNameValuePair("sEvento", codEventos.getItem(elegido)));
                        params.add(new BasicNameValuePair("sDescEvento", eventos.getItem(elegido)));

                        String resultServer = getHttpPost(url, params);
                        System.out.println(resultServer);

                        Button evento = (Button) findViewById(R.id.botonEventoVerde);
                        evento.setText(eventos.getItem(elegido));
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    public void guardarEventoAmarillo(View v){
        String url = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "nombreEventos"));
        params.add(new BasicNameValuePair("sTipo", "P"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        final ArrayAdapter<String> eventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);
        final ArrayAdapter<String> codEventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);

        try {

            JSONArray jArray = new JSONArray(resultServer);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                eventos.add(json.getString("nom_evento").trim());
                codEventos.add(json.getString("cod_evento").trim());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(IniciarPararProceso.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Seleccione evento");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setCancelable(false);
        builderSingle.setAdapter(eventos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int elegido = which;
                AlertDialog.Builder builderInner = new AlertDialog.Builder(IniciarPararProceso.this);
                builderInner.setMessage("");
                builderInner.setCancelable(false);
                builderInner.setTitle("EVENTO : " + eventos.getItem(which));
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {

                        Spinner spinner = (Spinner)findViewById(R.id.spinnerDetalle);
                        String operacion = spinner.getSelectedItem().toString();

                        EditText r = (EditText) findViewById(R.id.editRombo) ;

                        EditText p = (EditText) findViewById(R.id.editPlaca) ;

                        String url = "http://" + ip + "/guardarMovimiento.php";

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("sParametro", "eventoDetalleOrden"));
                        params.add(new BasicNameValuePair("sOperacion",operacion.trim()));
                        params.add(new BasicNameValuePair("sNumero", orden));
                        params.add(new BasicNameValuePair("sRombo", r.getText().toString().trim()));
                        params.add(new BasicNameValuePair("sPlaca", p.getText().toString().trim()));
                        params.add(new BasicNameValuePair("sEvento", codEventos.getItem(elegido)));
                        params.add(new BasicNameValuePair("sDescEvento", eventos.getItem(elegido)));

                        String resultServer = getHttpPost(url, params);
                        System.out.println(resultServer);

                        Button evento = (Button) findViewById(R.id.botonEventoAmarillo);
                        evento.setText(eventos.getItem(elegido));
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    public void guardarEventoRojo(View v){
        String url = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "nombreEventos"));
        params.add(new BasicNameValuePair("sTipo", "T"));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        final ArrayAdapter<String> eventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);
        final ArrayAdapter<String> codEventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);

        try {

            JSONArray jArray = new JSONArray(resultServer);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                eventos.add(json.getString("nom_evento").trim());
                codEventos.add(json.getString("cod_evento").trim());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(IniciarPararProceso.this);
        builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Seleccione evento");

        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setCancelable(false);
        builderSingle.setAdapter(eventos, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final int elegido = which;
                AlertDialog.Builder builderInner = new AlertDialog.Builder(IniciarPararProceso.this);
                builderInner.setMessage("");
                builderInner.setCancelable(false);
                builderInner.setTitle("EVENTO : " + eventos.getItem(which));
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {

                        Spinner spinner = (Spinner)findViewById(R.id.spinnerDetalle);
                        String operacion = spinner.getSelectedItem().toString();

                        EditText r = (EditText) findViewById(R.id.editRombo) ;

                        EditText p = (EditText) findViewById(R.id.editPlaca) ;

                        String url = "http://" + ip + "/guardarMovimiento.php";

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("sParametro", "eventoDetalleOrden"));
                        params.add(new BasicNameValuePair("sOperacion",operacion.trim()));
                        params.add(new BasicNameValuePair("sNumero", orden));
                        params.add(new BasicNameValuePair("sRombo", r.getText().toString().trim()));
                        params.add(new BasicNameValuePair("sPlaca", p.getText().toString().trim()));
                        params.add(new BasicNameValuePair("sEvento", codEventos.getItem(elegido)));
                        params.add(new BasicNameValuePair("sDescEvento", eventos.getItem(elegido)));

                        String resultServer = getHttpPost(url, params);
                        System.out.println(resultServer);

                        Button evento = (Button) findViewById(R.id.botonEventoVerde);
                        evento.setText(eventos.getItem(elegido));
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builderSingle.show();
    }

    public void actualizarRojo(View v){

        try {

            TextView rojo = (TextView) findViewById(R.id.textRojo);
            rojo.setBackgroundResource(R.drawable.circle_text_view_rojo_press);

            TextView verde = (TextView) findViewById(R.id.textVerde);
            verde.setBackgroundResource(R.drawable.circle_text_view_verde);

            TextView amarillo = (TextView) findViewById(R.id.textAmarillo);
            amarillo.setBackgroundResource(R.drawable.circle_text_view_amarillo);

            Spinner spinner = (Spinner) findViewById(R.id.spinnerDetalle);
            String text = spinner.getSelectedItem().toString();

            String url = "http://" + ip + "/guardarMovimiento.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sParametro", "detalleRojo"));
            params.add(new BasicNameValuePair("sOrden", orden));
            params.add(new BasicNameValuePair("sOperacion", text.trim()));

            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);

            if (timerA != null) {
                timerA.cancel();
                timerA.purge();
                timerA = null;
            }

            if (timerColor != null) {
                timerColor.cancel();
                timerColor.purge();
                timerColor = null;
            }

            url = "http://" + ip + "/consultarGeneral.php";

            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sParametro", "nombreEventosPorOperacion"));
            params.add(new BasicNameValuePair("sOperacionEvento", operacionEvento));
            params.add(new BasicNameValuePair("sTipo", "P"));

            resultServer = getHttpPost(url, params);
            System.out.println(resultServer);
            final ArrayAdapter<String> eventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);
            final ArrayAdapter<String> codEventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);

            try {

                JSONArray jArray = new JSONArray(resultServer);

                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    eventos.add(json.getString("nom_evento").trim());
                    codEventos.add(json.getString("cod_evento").trim());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            TextView labelRojo =(TextView) findViewById(R.id.labelRojo);
            labelRojo.setText("");

            TextView labelVerde =(TextView) findViewById(R.id.labelVerde);
            labelVerde.setText("En proceso...");

            TextView labelAmarillo = (TextView) findViewById(R.id.labelAmarillo);
            labelAmarillo.setText("");

            amarillo = (TextView) findViewById(R.id.textAmarillo);
            amarillo.setBackgroundResource(R.drawable.circle_text_view_amarillo);

            verde = (TextView) findViewById(R.id.textVerde);
            verde.setBackgroundResource(R.drawable.circle_text_view_verde);

            spinner = (Spinner)findViewById(R.id.spinnerDetalle);
            String operacion = spinner.getSelectedItem().toString();

            EditText r = (EditText) findViewById(R.id.editRombo) ;

            EditText p = (EditText) findViewById(R.id.editPlaca) ;

            url = "http://" + ip + "/guardarMovimiento.php";

            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sParametro", "eventoDetalleOrdenActualizar"));
            params.add(new BasicNameValuePair("sOperacion",operacion.trim()));
            params.add(new BasicNameValuePair("sNumero", orden));
            params.add(new BasicNameValuePair("sRombo", r.getText().toString().trim()));
            params.add(new BasicNameValuePair("sPlaca", p.getText().toString().trim()));
            params.add(new BasicNameValuePair("sEvento", eventoElegido));
            params.add(new BasicNameValuePair("sDescEvento", descEventoElegido));

            resultServer = getHttpPost(url, params);
            System.out.println(resultServer);

            if (timerR != null) {
                timerR.cancel();
                timerR.purge();
                timerR = null;
            }

            if (timerV != null) {
                timerV.cancel();
                timerV.purge();
                timerV = null;
            }

            if (timerA != null) {
                timerA.cancel();
                timerA.purge();
                timerA = null;
            }

            if (timerColor != null) {
                timerColor.cancel();
                timerColor.purge();
                timerColor = null;
            }

            timerR = new Timer();
            timerR.schedule(new TimerTask() {
                int i = 0;

                @Override
                public void run() {
                    TextView rojo = (TextView) findViewById(R.id.textRojo);
                    TextView verde = (TextView) findViewById(R.id.textVerde);
                    TextView amarillo = (TextView) findViewById(R.id.textAmarillo);
                    amarillo.setClickable(false);
                    verde.setClickable(false);

                    Spinner spinner = (Spinner) findViewById(R.id.spinnerDetalle);
                    String text = spinner.getSelectedItem().toString();

                    String url = "http://" + ip + "/consultarGeneral.php";
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("sParametro", "detalleTiemposRojo"));
                    params.add(new BasicNameValuePair("sOrden", orden));
                    params.add(new BasicNameValuePair("sOperacion", text.trim()));

                    String resultServer = getHttpPost(url, params);
                    System.out.println(resultServer);
                    String fecha = "";
                    try {

                        JSONArray jArray = new JSONArray(resultServer);

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);
                            fecha = json.getString("fecha_fin").trim();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = simpleDateFormat.parse(fecha);
                        Calendar c = Calendar.getInstance();
                        String year = String.valueOf(c.get(Calendar.YEAR));
                        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
                        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
                        String min = String.valueOf(c.get(Calendar.MINUTE));
                        String sec = String.valueOf(c.get(Calendar.SECOND));
                        date2 = simpleDateFormat.parse(year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long difference = date2.getTime() - date1.getTime();
                    int days = (int) (difference / (1000*60*60*24));
                    int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);


                    setText(rojo, String.valueOf(min));
                    i++;
                }
            }, 0, 60000);

            labelRojo =(TextView) findViewById(R.id.labelRojo);
            labelRojo.setText("Terminado...");

            labelVerde =(TextView) findViewById(R.id.labelVerde);
            labelVerde.setText("");

            labelAmarillo =(TextView) findViewById(R.id.labelAmarillo);
            labelAmarillo.setText("");

            if (timerV != null) {
                timerV.cancel();
                timerV.purge();
                timerV = null;
            }

            if (timerA != null) {
                timerA.cancel();
                timerA.purge();
                timerA = null;
            }

            if (timerR != null) {
                timerR.cancel();
                timerR.purge();
                timerR = null;
            }

            if (timerColor != null) {
                timerColor.cancel();
                timerColor.purge();
                timerColor = null;
            }

            Toast.makeText(getApplicationContext(), "Haz terminado correctamente!", Toast.LENGTH_SHORT).show();
            finish();
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No haz actualizado correctamente la fecha", Toast.LENGTH_SHORT).show();
        }
    }

    public void actualizarVerde(View v){

        try {
            spDet.setEnabled(false);
            sp.setEnabled(false);
            TextView amarillo = (TextView) findViewById(R.id.textAmarillo);
            amarillo.setClickable(true);

            TextView rojo = (TextView) findViewById(R.id.textRojo);
            rojo.setClickable(true);

            TextView verde = (TextView) findViewById(R.id.textVerde);
            verde.setBackgroundResource(R.drawable.circle_text_view_verde_press);

            Spinner spinner = (Spinner) findViewById(R.id.spinnerDetalle);
            String text = spinner.getSelectedItem().toString();

            String url = "http://" + ip + "/guardarMovimiento.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sParametro", "detalleVerde"));
            params.add(new BasicNameValuePair("sOrden", orden));
            params.add(new BasicNameValuePair("sOperacion", text.trim()));

            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);


            if (timerV != null) {
                timerV.cancel();
                timerV.purge();
                timerV = null;
            }

            timerV = new Timer();
            timerV.schedule(new TimerTask() {
                int i = 0;

                @Override
                public void run() {
                    TextView verde = (TextView) findViewById(R.id.textVerde);
                    Spinner spinner = (Spinner) findViewById(R.id.spinnerDetalle);
                    String text = spinner.getSelectedItem().toString();

                    String url = "http://" + ip + "/consultarGeneral.php";
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("sParametro", "detalleTiemposVerde"));
                    params.add(new BasicNameValuePair("sOrden", orden));
                    params.add(new BasicNameValuePair("sOperacion", text.trim()));

                    String resultServer = getHttpPost(url, params);
                    System.out.println(resultServer);
                    String fecha = "";
                    try {

                        JSONArray jArray = new JSONArray(resultServer);

                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject json = jArray.getJSONObject(i);
                            fecha = json.getString("fecha_ini").trim();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = simpleDateFormat.parse(fecha);
                        Calendar c = Calendar.getInstance();
                        String year = String.valueOf(c.get(Calendar.YEAR));
                        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
                        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
                        String min = String.valueOf(c.get(Calendar.MINUTE));
                        String sec = String.valueOf(c.get(Calendar.SECOND));
                        date2 = simpleDateFormat.parse(year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long difference = date2.getTime() - date1.getTime();
                    int days = (int) (difference / (1000*60*60*24));
                    int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                    int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);


                    setText(verde, String.valueOf(min));
                    i++;
                }
            }, 0, 60000);

            TextView labelRojo =(TextView) findViewById(R.id.labelRojo);
            labelRojo.setText("");

            TextView labelVerde =(TextView) findViewById(R.id.labelVerde);
            labelVerde.setText("En proceso...");

            TextView labelAmarillo =(TextView) findViewById(R.id.labelAmarillo);
            labelAmarillo.setText("");
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No haz actualizado correctamente la fecha", Toast.LENGTH_SHORT).show();
        }
    }

    public void actualizarAmarillo(View v){

        try {
            final TextView labelAmarillo = (TextView) findViewById(R.id.labelAmarillo);

            if (!labelAmarillo.getText().toString().trim().equals("En pausa...")){

                String url = "http://" + ip + "/consultarGeneral.php";

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sParametro", "nombreEventos"));
                params.add(new BasicNameValuePair("sTipo", "P"));

                String resultServer = getHttpPost(url, params);
                System.out.println(resultServer);
                final ArrayAdapter<String> eventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);
                final ArrayAdapter<String> codEventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);

                try {

                    JSONArray jArray = new JSONArray(resultServer);

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        eventos.add(json.getString("nom_evento").trim());
                        codEventos.add(json.getString("cod_evento").trim());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final AlertDialog.Builder builderSingle = new AlertDialog.Builder(IniciarPararProceso.this);
                builderSingle.setIcon(R.drawable.ic_launcher);
                builderSingle.setTitle("Seleccione evento para iniciar");

                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setCancelable(false);
                builderSingle.setAdapter(eventos, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final int elegido = which;
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(IniciarPararProceso.this);
                        builderInner.setCancelable(false);
                        builderInner.setMessage("");
                        builderInner.setTitle("EVENTO : " + eventos.getItem(which));
                        builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {

                                TextView amarillo = (TextView) findViewById(R.id.textAmarillo);
                                amarillo.setBackgroundResource(R.drawable.circle_text_view_amarillo_press);

                                TextView verde = (TextView) findViewById(R.id.textVerde);
                                verde.setBackgroundResource(R.drawable.circle_text_view_verde);

                                TextView rojo = (TextView) findViewById(R.id.textRojo);
                                rojo.setBackgroundResource(R.drawable.circle_text_view_rojo);

                                TextView labelRojo =(TextView) findViewById(R.id.labelRojo);
                                labelRojo.setText("");

                                TextView labelVerde =(TextView) findViewById(R.id.labelVerde);
                                labelVerde.setText("");

                                labelAmarillo.setText("En pausa...");

                                Spinner spinner = (Spinner)findViewById(R.id.spinnerDetalle);
                                String operacion = spinner.getSelectedItem().toString();

                                EditText r = (EditText) findViewById(R.id.editRombo) ;

                                EditText p = (EditText) findViewById(R.id.editPlaca) ;

                                operacionEvento = codEventos.getItem(elegido);

                                String url = "http://" + ip + "/guardarMovimiento.php";

                                List<NameValuePair> params = new ArrayList<NameValuePair>();
                                params.add(new BasicNameValuePair("sParametro", "eventoDetalleOrden"));
                                params.add(new BasicNameValuePair("sOperacion",operacion.trim()));
                                params.add(new BasicNameValuePair("sNumero", orden));
                                params.add(new BasicNameValuePair("sRombo", r.getText().toString().trim()));
                                params.add(new BasicNameValuePair("sPlaca", p.getText().toString().trim()));
                                params.add(new BasicNameValuePair("sEvento", codEventos.getItem(elegido)));
                                params.add(new BasicNameValuePair("sDescEvento", eventos.getItem(elegido)));

                                eventoElegido = codEventos.getItem(elegido).trim();
                                descEventoElegido = eventos.getItem(elegido).trim();

                                String resultServer = getHttpPost(url, params);
                                System.out.println(resultServer);

                                Button evento = (Button) findViewById(R.id.botonEventoAmarillo);
                                evento.setText(eventos.getItem(elegido));

                                if (timerV != null) {
                                    timerV.cancel();
                                    timerV.purge();
                                    timerV = null;
                                }

                                if (timerR != null) {
                                    timerR.cancel();
                                    timerR.purge();
                                    timerR = null;
                                }

                                timerColor = new Timer();
                                timerColor.schedule(new TimerTask() {
                                    boolean pressed = true;

                                    @Override
                                    public void run() {
                                        if (pressed){
                                            TextView amarillo = (TextView) findViewById(R.id.textAmarillo);
                                            setBackground(amarillo,R.drawable.circle_text_view_amarillo);
                                            pressed = false;
                                        }else{
                                            TextView amarillo = (TextView) findViewById(R.id.textAmarillo);
                                            setBackground(amarillo,R.drawable.circle_text_view_amarillo_press);
                                            pressed = true;
                                        }

                                    }
                                }, 0, 1000);

                                timerA = new Timer();
                                timerA.schedule(new TimerTask() {
                                    int i = 0;

                                    @Override
                                    public void run() {
                                        TextView amarillo = (TextView) findViewById(R.id.textAmarillo);
                                        Spinner spinner = (Spinner) findViewById(R.id.spinnerDetalle);
                                        String text = spinner.getSelectedItem().toString();

                                        String url = "http://" + ip + "/consultarGeneral.php";
                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                        params.add(new BasicNameValuePair("sParametro", "detalleTiemposAmarillo"));
                                        params.add(new BasicNameValuePair("sOrden", orden));
                                        params.add(new BasicNameValuePair("sOperacion", text.trim()));

                                        String resultServer = getHttpPost(url, params);
                                        System.out.println(resultServer);
                                        String fecha = "";
                                        try {

                                            JSONArray jArray = new JSONArray(resultServer);

                                            for (int i = 0; i < jArray.length(); i++) {
                                                JSONObject json = jArray.getJSONObject(i);
                                                fecha = json.getString("Fec_ini").trim();
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        Date date1 = null;
                                        Date date2 = null;
                                        try {
                                            date1 = simpleDateFormat.parse(fecha);
                                            Calendar c = Calendar.getInstance();
                                            String year = String.valueOf(c.get(Calendar.YEAR));
                                            String month = String.valueOf(c.get(Calendar.MONTH) + 1);
                                            String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                                            String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
                                            String min = String.valueOf(c.get(Calendar.MINUTE));
                                            String sec = String.valueOf(c.get(Calendar.SECOND));
                                            date2 = simpleDateFormat.parse(year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        long difference = date2.getTime() - date1.getTime();
                                        int days = (int) (difference / (1000*60*60*24));
                                        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                                        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);


                                        setText(amarillo, String.valueOf(min));
                                        i++;
                                    }
                                }, 0, 60000);

                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.show();


            }else{

                if (timerA != null) {
                    timerA.cancel();
                    timerA.purge();
                    timerA = null;
                }

                if (timerColor != null) {
                    timerColor.cancel();
                    timerColor.purge();
                    timerColor = null;
                }

                String url = "http://" + ip + "/consultarGeneral.php";

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sParametro", "nombreEventosPorOperacion"));
                params.add(new BasicNameValuePair("sOperacionEvento", operacionEvento));
                params.add(new BasicNameValuePair("sTipo", "P"));

                String resultServer = getHttpPost(url, params);
                System.out.println(resultServer);
                final ArrayAdapter<String> eventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);
                final ArrayAdapter<String> codEventos = new ArrayAdapter<String>(IniciarPararProceso.this, android.R.layout.select_dialog_item);

                try {

                    JSONArray jArray = new JSONArray(resultServer);

                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject json = jArray.getJSONObject(i);
                        eventos.add(json.getString("nom_evento").trim());
                        codEventos.add(json.getString("cod_evento").trim());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                TextView labelRojo =(TextView) findViewById(R.id.labelRojo);
                labelRojo.setText("");

                TextView labelVerde =(TextView) findViewById(R.id.labelVerde);
                labelVerde.setText("En proceso...");

                labelAmarillo.setText("");

                TextView amarillo = (TextView) findViewById(R.id.textAmarillo);
                amarillo.setBackgroundResource(R.drawable.circle_text_view_amarillo);

                TextView verde = (TextView) findViewById(R.id.textVerde);
                verde.setBackgroundResource(R.drawable.circle_text_view_verde_press);

                Spinner spinner = (Spinner)findViewById(R.id.spinnerDetalle);
                String operacion = spinner.getSelectedItem().toString();

                EditText r = (EditText) findViewById(R.id.editRombo) ;

                EditText p = (EditText) findViewById(R.id.editPlaca) ;

                url = "http://" + ip + "/guardarMovimiento.php";

                params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sParametro", "eventoDetalleOrdenActualizar"));
                params.add(new BasicNameValuePair("sOperacion",operacion.trim()));
                params.add(new BasicNameValuePair("sNumero", orden));
                params.add(new BasicNameValuePair("sRombo", r.getText().toString().trim()));
                params.add(new BasicNameValuePair("sPlaca", p.getText().toString().trim()));
                params.add(new BasicNameValuePair("sEvento", eventoElegido));
                params.add(new BasicNameValuePair("sDescEvento", descEventoElegido));

                resultServer = getHttpPost(url, params);
                System.out.println(resultServer);

                Button evento = (Button) findViewById(R.id.botonEventoAmarillo);
                evento.setText(descEventoElegido);

                timerV = new Timer();
                timerV.schedule(new TimerTask() {
                    int i = 0;

                    @Override
                    public void run() {
                        TextView verde = (TextView) findViewById(R.id.textVerde);
                        Spinner spinner = (Spinner) findViewById(R.id.spinnerDetalle);
                        String text = spinner.getSelectedItem().toString();

                        String url = "http://" + ip + "/consultarGeneral.php";
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("sParametro", "detalleTiemposVerde"));
                        params.add(new BasicNameValuePair("sOrden", orden));
                        params.add(new BasicNameValuePair("sOperacion", text.trim()));

                        String resultServer = getHttpPost(url, params);
                        System.out.println(resultServer);
                        String fecha = "";
                        try {

                            JSONArray jArray = new JSONArray(resultServer);

                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                fecha = json.getString("fecha_ini").trim();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        Date date1 = null;
                        Date date2 = null;
                        try {
                            date1 = simpleDateFormat.parse(fecha);
                            Calendar c = Calendar.getInstance();
                            String year = String.valueOf(c.get(Calendar.YEAR));
                            String month = String.valueOf(c.get(Calendar.MONTH) + 1);
                            String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
                            String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
                            String min = String.valueOf(c.get(Calendar.MINUTE));
                            String sec = String.valueOf(c.get(Calendar.SECOND));
                            date2 = simpleDateFormat.parse(year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + sec);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long difference = date2.getTime() - date1.getTime();
                        int days = (int) (difference / (1000*60*60*24));
                        int hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
                        int min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);


                        setText(verde, String.valueOf(min));
                        i++;
                    }
                }, 0, 60000);


            }


        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No haz actualizado correctamente el evento", Toast.LENGTH_SHORT).show();
        }
    }

    private void setText(final TextView text, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }

    private void setBackground(final TextView text, final int value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setBackgroundResource(value);
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

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Seguro deseas salir?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "SI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (timerV != null) {
                            timerV.cancel();
                            timerV.purge();
                            timerV = null;
                        }

                        if (timerA != null) {
                            timerA.cancel();
                            timerA.purge();
                            timerA = null;
                        }

                        if (timerR != null) {
                            timerR.cancel();
                            timerR.purge();
                            timerR = null;
                        }

                        if (timerColor != null) {
                            timerColor.cancel();
                            timerColor.purge();
                            timerColor = null;
                        }
                        finish();
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
        alert11.show();
    }

}
