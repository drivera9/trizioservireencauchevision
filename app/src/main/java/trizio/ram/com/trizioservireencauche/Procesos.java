package trizio.ram.com.trizioservireencauche;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Procesos extends Activity {
String dirIP = "";
    String titulo = "";
    String user = "";
    String pass = "";
    String proceso = "";
    String sucursal;
    String empresa;
    String ip = "";
    private LinearLayout layout;
    private View Progress;
    ArrayList<String> procesos = new ArrayList<>();
    ArrayList<String> nomProceso = new ArrayList<>();
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_procesos);
        Bundle bundle = this.getIntent().getExtras();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        layout = (LinearLayout) findViewById(R.id.LayoutRelativeProcesos);
        Progress = findViewById(R.id.progressbar);
        empresa = bundle.getString("empresa");
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        sucursal = bundle.getString("sucursal");
        procesos = bundle.getStringArrayList("procesos");
        nomProceso = bundle.getStringArrayList("nomProcesos");
        System.out.println("sucursal ------------>" + sucursal);
        final String conexion = bundle.getString("conexion");
        ip = bundle.getString("ip");

        for (int i = 0;i<procesos.size();i++){
            System.out.println("proceso - " + procesos.get(i));
        }

        titulo = "Sucursal " + sucursal;
        setTitle(titulo);


        if (conexion.equals("local")) {
            DataBaseHelper dataBaseHelper = new DataBaseHelper((Context) this);
            try {
                dataBaseHelper.createDataBase();
                dataBaseHelper.openDataBase();
            } catch (SQLException e) {
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList procesos = dataBaseHelper.getProcesos(user);

            dataBaseHelper.close();

            /*for (int i = 0; i < procesos.size(); i++) {
                if (procesos.get(i).equals("01")) {
                    recepcion.setVisibility(View.VISIBLE);
                    recepcionT.setVisibility(View.VISIBLE);
                }
                if (procesos.get(i).equals("02")) {
                    bodega.setVisibility(View.VISIBLE);
                    bodegaT.setVisibility(View.VISIBLE);
                }
                if (procesos.get(i).equals("06")) {
                    entrega.setVisibility(View.VISIBLE);
                    entregaT.setVisibility(View.VISIBLE);
                }
                if (procesos.get(i).equals("04")) {
                    sc.setVisibility(View.VISIBLE);
                    scT.setVisibility(View.VISIBLE);
                }
                if (procesos.get(i).equals("05")) {
                    mensajeria.setVisibility(View.VISIBLE);
                    mensajeriaT.setVisibility(View.VISIBLE);
                }

            }
            recepcion.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent((Context) Procesos.this, (Class) Recepcion.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("user", user);
                    bundle2.putString("pass", pass);
                    bundle2.putString("conexion", conexion);
                    bundle2.putString("proceso", "01");
                    intent.putExtras(bundle2);
                    Procesos.this.startActivity(intent);
                }

            });
            bodega.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent intent = new Intent((Context) Procesos.this, (Class) Recepcion.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("user", user);
                    bundle2.putString("pass", pass);
                    bundle2.putString("conexion", conexion);
                    bundle2.putString("proceso", "02");
                    intent.putExtras(bundle2);
                    Procesos.this.startActivity(intent);
                }

            });*/
        }else{


                    ArrayList<Lista_entrada> datos = new ArrayList<Lista_entrada>();



                    for (int i = 0;i<nomProceso.size();i++){
                        try {

                            if (procesos.get(i).trim().equals("1")) {
                                datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try {

                            if (procesos.get(i).trim().equals("2")) {
                                datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try {

                            if (procesos.get(i).trim().equals("3")) {
                                datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try {

                            if (procesos.get(i).trim().equals("4")) {
                                datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try {

                            if (procesos.get(i).trim().equals("5")) {
                                datos.add(new Lista_entrada(R.drawable.recepcion, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try {

                            if (procesos.get(i).trim().equals("6")) {
                                datos.add(new Lista_entrada(R.drawable.sc, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }


                        try{
                            if (procesos.get(i).trim().equals("7")) {

                                datos.add(new Lista_entrada(R.drawable.entrega, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("8")) {

                                datos.add(new Lista_entrada(R.drawable.mensajeria, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try {

                            if (procesos.get(i).trim().equals("10")) {
                                datos.add(new Lista_entrada(R.drawable.mano_de_obra, "   " + nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try{
                            if (procesos.get(i).trim().equals("11")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("12")) {

                                datos.add(new Lista_entrada(R.drawable.facturacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try{
                            if (procesos.get(i).trim().equals("13")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("14")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("15")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("16")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("17")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("18")) {
                                datos.add(new Lista_entrada(R.drawable.cambiarfecha, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("19")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("20")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("21")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }
                        try{
                            if (procesos.get(i).trim().equals("22")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try{
                            if (procesos.get(i).trim().equals("23")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try{
                            if (procesos.get(i).trim().equals("24")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try{
                            if (procesos.get(i).trim().equals("25")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try{
                            if (procesos.get(i).trim().equals("26")) {

                                datos.add(new Lista_entrada(R.drawable.validacion, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }

                        try{
                            if (procesos.get(i).trim().equals("99")) {

                                datos.add(new Lista_entrada(R.drawable.warning, nomProceso.get(i)));
                                continue;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            //Toast.makeText(getApplicationContext(),"Los procesos estan mal configurados!",Toast.LENGTH_LONG).show();
                        }


                    }

                    ListView lista = (ListView) findViewById(R.id.listView2);
                    lista.setAdapter(new Lista_adaptador(this, R.layout.entrada, datos){
                        @Override
                        public void onEntrada(Object entrada, View view) {
                            if (entrada != null) {
                                TextView texto_superior_entrada = (TextView) view.findViewById(R.id.textView_superior);
                                if (texto_superior_entrada != null)
                                    texto_superior_entrada.setText(((Lista_entrada) entrada).get_textoEncima());


                                ImageView imagen_entrada = (ImageView) view.findViewById(R.id.imageView_imagen);
                                if (imagen_entrada != null)
                                    imagen_entrada.setImageResource(((Lista_entrada) entrada).get_idImagen());
                            }
                        }
                    });

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                            Lista_entrada elegido = (Lista_entrada) pariente.getItemAtPosition(posicion);

                            mProgressDialog= new ProgressDialog(Procesos.this);
                            mProgressDialog.setIndeterminate(false);
                            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            mProgressDialog.setMessage("Espere...");

                            mProgressDialog.show();

                            if (elegido.get_textoEncima().trim().equals("RECEPCION")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) Recepcion.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Rec");
                                bundle2.putString("proceso", "1");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("TURNOS UBICACIONES")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) RecepcionUsuariosUbicaciones.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Rec");
                                bundle2.putString("proceso", "24");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("INICIAR PARAR PROCESO")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) IniciarPararProceso.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Rec");
                                bundle2.putString("proceso", "26");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("TURNOS USUARIOS")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) RecepcionUsuariosUbicaciones.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Rec");
                                bundle2.putString("proceso", "25");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }


                            if (elegido.get_textoEncima().trim().equals("LLAMAR COTIZACION")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "17");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Cotizacion");
                                bundle2.putString("proceso", "17");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("CAMBIAR PROMESA")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "15");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Cambiar Promesa");
                                bundle2.putString("proceso", "15");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("CAMBIAR TECNICO")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) Cambiar.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("parametro", "TECNICO");
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "15");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Cambiar Promesa");
                                bundle2.putString("proceso","21" );
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("CAMBIAR UBICACION")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) Cambiar.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("parametro", "UBICACION");
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "15");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Cambiar Promesa");
                                bundle2.putString("proceso", "22");
                                bundle2.putString("procesoActual", proceso);
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("MARCAR TIEMPOS")) {

                                //Intent intent = new Intent((Context) Procesos.this, (Class) MarcarTiemposOpciones.class);
                                Intent intent = new Intent((Context) Procesos.this, (Class) MarcarTiemposOpciones.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("parametro", "UBICACION");
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Marcar Tiempos");
                                bundle2.putString("proceso", "23");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("ASIGNAR OPERARIO DETALLE ORDEN")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) MarcarOperarioRombo.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("parametro", "UBICACION");
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Marcar Tiempos");
                                bundle2.putString("proceso", "24");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("CAMBIAR SEDE")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) CambiarSede.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Cambiar Sede");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("EVENTO")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) RomboEvento.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "16");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Evento recepcion");
                                bundle2.putString("proceso", "16");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("CAMBIAR FECHA")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) CambiarFecha.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "18");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Cambiar Fecha");
                                bundle2.putString("proceso", "18");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("TRASLADO")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "19");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Traslado");
                                bundle2.putString("proceso", "19");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("BODEGA")) {


                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "4");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("FACTURACION")) {


                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "12");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("MANO OBRA")) {


                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "10");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("DESPACHO")) {

                                Intent intent = new Intent((Context) Procesos.this, (Class) Despacho.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "7");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("TRANSITO")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "8");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("SC")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaGeneral.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/SC");
                                bundle2.putString("proceso", "6");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }


                            if (elegido.get_textoEncima().trim().equals("VALIDAR REC")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "13");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "13");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("VALIDAR ENT")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "15");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "15");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }

                            if (elegido.get_textoEncima().trim().equals("VALIDAR BOD")) {
                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaRombo.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("procesOriginal", "14");
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "14");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }



                            if (elegido.get_textoEncima().trim().equals("ENTREGAR")) {


                                Intent intent = new Intent((Context) Procesos.this, (Class) BodegaTerminar.class);
                                Bundle bundle2 = new Bundle();

                                bundle2.putString("user", user);
                                bundle2.putString("pass", pass);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("empresa", empresa);
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("titulo", titulo + "/Bodega");
                                bundle2.putString("proceso", "99");
                                bundle2.putString("tipo", "mercar");
                                intent.putExtras(bundle2);
                                startActivity(intent);
                                mProgressDialog.dismiss();
                            }


                        }
                    });
            }

    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        showProgress(show, this,Progress ,layout);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Context ctx,
                                    final View mProgressView, final View mFormView) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = ctx.getResources()
                    .getInteger(android.R.integer.config_shortAnimTime);

            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public void firmar(View v){

        Intent intent = new Intent(Procesos.this, Firma2.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("ip", ip);
        bundle2.putString("empresa", empresa.trim());
        intent.putExtras(bundle2);
        startActivity(intent);
    }

    public void foto(View v){

        Intent intent = new Intent(Procesos.this, Main2Activity.class);
        Bundle bundle2 = new Bundle();
        intent.putExtras(bundle2);
        startActivity(intent);
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
        getMenuInflater().inflate(R.menu.menu_procesos, menu);
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
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Saliendo...")
                .setMessage("Esta Seguro que desea salir?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DataBaseHelper dataBaseHelper = new DataBaseHelper(Procesos.this);
                        try {
                            dataBaseHelper.createDataBase();
                            dataBaseHelper.openDataBase();
                            dataBaseHelper.borrarMovimiento();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }

                        dataBaseHelper.close();
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }
}
