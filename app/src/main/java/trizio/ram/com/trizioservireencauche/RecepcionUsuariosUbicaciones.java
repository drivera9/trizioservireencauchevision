package trizio.ram.com.trizioservireencauche;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class RecepcionUsuariosUbicaciones extends AppCompatActivity {
    ArrayList<ArrayList> products;
    String productText = "";
    boolean invi = true;
    private static String accessToken;
    static final int REQUEST_CODE_PICK_ACCOUNT = 11;
    static final int REQUEST_ACCOUNT_AUTHORIZATION = 12;
    static final int REQUEST_PERMISSIONS = 13;
    private final String LOG_TAG = "MainActivity";
    int indice =0;
    Account mAccount;
    private final String ruta_fotos = "/storage/sdcard0/METRO/" ;
    private File file = new File(ruta_fotos);
    private Button boton;
    static String conexion = "";
    String tip_vehiculo = "";
    String tecnico = "";
    String encuentraPlaca = "";
    String encuentraNit = "";
    String nombreTabla = "";
    String ip = "" ;
    String placa = "";
    String vin = "";
    String Cedula = "";
    String nombre1 = "";
    String nombre2 = "";
    String apellido1 = "";
    String apellido2 = "";
    String nombre = "";
    String cod_placa = "";
    String num_rombo= "";
    String cod_ubicacion = "";
    String fec_proceso = "";
    String num_nit = "";
    String promesa = "";
    String solicitud = "";
    String sucursal;
    int request_code = 1;
    ArrayList atributos ;
    String titulo = "";
    String empresa;
    EditText texto;
    String[][] generalAux;
    String[][] general;
    String proceso = "";
    int date ;
    String fechaFoto;
    String dia ;
    String pago = "";
    String dirFoto;
    int longitud;
    String tiempoPromesa;
    String kilometraje = "";
    String sInformativo = "";
    String parametro = "";
    String pass;
    String user;
    String km = "";
    ProgressDialog mProgressDialog;
    String km_anterior = "";
    String razon = "";
    String descripcion = "";
    String soat = "";
    String tecno = "";
    String modelo = "";
    String cita = "";
    String horacita = "";
    String descripcionModelo = "";
    String motivo;
    String eventoCita = "";
    String eventoRazon = "";
    String fecha = "";
    String horaInicio = "";
    String tiempo = "";
    String horaFin = "";
    String usuario = "";
    static final int REQUEST_IMAGE_CAPTURE = 1;

    ArrayList<String> columnas = new ArrayList<>();
    ArrayList<String> datos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepcion);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mProgressDialog= new ProgressDialog(RecepcionUsuariosUbicaciones.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Subiendo imagen...");

        Bundle bundle = this.getIntent().getExtras();
        user = bundle.getString("user");
        pass = bundle.getString("pass");
        conexion = bundle.getString("conexion");
        titulo = bundle.getString("titulo");
        ip = bundle.getString("ip");
        proceso = bundle.getString("proceso");
        if (proceso.equals("11")){
            num_rombo = bundle.getString("num_rombo");
        }
        sucursal = bundle.getString("sucursal");
        empresa = bundle.getString("empresa");
        System.out.println("sucursal y empresa ------------>" + sucursal + empresa);
        String url1 = "http://"+  ip + "/delete.php";

        atributos = new ArrayList();
        fechaFoto = getCode();


        Button buscar = (Button) findViewById(R.id.button_buscar);
        Button anadir = (Button) findViewById(R.id.button_anadir);


        buscar.setVisibility(View.INVISIBLE);
        anadir.setVisibility(View.INVISIBLE);


        List<NameValuePair> params1 = new ArrayList<NameValuePair>();

        String resultServer1 = getHttpPost(url1, params1);

        JSONObject c2;


        setTitle(titulo);

        texto = (EditText) findViewById(R.id.editText_texto);
        TextView atributo = (TextView) findViewById(R.id.textView_atributo);

        if (atributo.getText().equals("ROMBO")) {
            texto.setInputType(InputType.TYPE_CLASS_NUMBER);
        }

        texto.requestFocus();

        if (conexion.equals("local")) {
           /* final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context) this);
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
            final String[][] general;
            general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01", 1);
            actualizar(general, indice,conexion);
            texto.requestFocus();



            final ImageButton button = (ImageButton) findViewById(R.id.imageButton8);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (indice == general.length - 1) {
                    } else {
                        indice++;
                        actualizar(general, indice,conexion);
                        texto.requestFocus();
                    }
                }
            });


            final ImageButton button2 = (ImageButton) findViewById(R.id.imageButton7);
            button2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (indice == 0) {
                    } else {
                        indice--;
                        actualizar(general, indice,conexion);
                        texto.requestFocus();
                    }
                }
            });

            final ImageButton button3 = (ImageButton) findViewById(R.id.imageButton6);
            button3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    indice = 0;
                    actualizar(general, indice,conexion);
                    texto.requestFocus();
                }
            });

            final ImageButton button4 = (ImageButton) findViewById(R.id.imageButton9);
            button4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    indice = general.length - 1;
                    actualizar(general, indice,conexion);
                    texto.requestFocus();
                }
            });*/
        }else{

            String url = "http://" + ip + "/consultarGeneral.php";

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sCodUser", pass));
            params.add(new BasicNameValuePair("sCodEmpresa", empresa));
            params.add(new BasicNameValuePair("sCodSucursal", sucursal));
            params.add(new BasicNameValuePair("sIndEstado", "A"));
            params.add(new BasicNameValuePair("sCodProceso", proceso));
            params.add(new BasicNameValuePair("sParametro", "consultarProceso"));

            String resultServer  = getHttpPost(url,params);
            try {
                JSONArray jArray = new JSONArray(resultServer);
                 generalAux = new String[jArray.length()][21];

                int j =0;
                longitud = jArray.length();
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);
                    generalAux[i][j] = json.getString("cod_empresa");
                    generalAux[i][j+1] = json.getString("cod_sucursal");
                    generalAux[i][j+2] = json.getString("cod_usuario");
                    generalAux[i][j+3] = json.getString("cod_proceso");
                    generalAux[i][j+4] = json.getString("cod_atributo");
                    generalAux[i][j+5] = json.getString("cod_valor");
                    generalAux[i][j+6] = json.getString("num_secuencia");
                    generalAux[i][j+7] = json.getString("num_orden");
                    generalAux[i][j+8] = json.getString("ind_requerido");
                    generalAux[i][j+9] = json.getString("ind_tipo");
                    generalAux[i][j+10] = json.getString("val_minimo");
                    generalAux[i][j+11] = json.getString("val_maximo");
                    generalAux[i][j+12] = json.getString("num_longitud");
                    generalAux[i][j+13] = json.getString("nom_ruta");
                    generalAux[i][j+14] = json.getString("val_defecto");
                    generalAux[i][j+15] = json.getString("cod_proceso_padre");
                    generalAux[i][j+16] = json.getString("ind_confirmacion");
                    generalAux[i][j+17] = json.getString("ind_estado");
                    generalAux[i][j+18] = json.getString("nom_columna");
                    generalAux[i][j+19] = json.getString("nom_tabla");
                    generalAux[i][j+20] = json.getString("idx_foto");
                }

                int contador = 0;

                for (int i = 0; i < generalAux.length; i++) {
                    for (j = 0; j < generalAux[i].length; j++) {
                     if (generalAux[i][j].equals("null")){
                         generalAux[i][j] = "";
                     }

                        //System.out.println("GENERAL->" + general[i][j]);
                    }
                    if (generalAux[i][17].equals("I")){
                        nombreTabla = generalAux[i][19].trim();
                        contador++;
                    }
                }

                System.out.println(longitud + " LONGITUD ");
                System.out.println(contador + " CONTADOR ");


                general = new String[longitud-contador][21];

                for (int i = 0; i < general.length; i++) {
                    for (j = 0; j < general[i].length; j++) {
                        general[i][j] = generalAux[i][j];
                        System.out.println(general[i][j].trim());
                    }
                }



                try {

                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);
                    ArrayList arrayAtributo = new ArrayList();
                    ArrayList arrayValor = new ArrayList();
                    try {
                        dataBaseHelper.createDataBase();
                        dataBaseHelper.openDataBase();
                        arrayValor = dataBaseHelper.consultarValor();
                        arrayAtributo = dataBaseHelper.consultarAtributo();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    dataBaseHelper.close();


                    for (int i = 0; i < arrayAtributo.size(); i++) {
                        System.out.println(" DATOS DE SQLITE ------------------------------------------------");
                        System.out.println(arrayAtributo.get(i) + " " + arrayValor.get(i));
                    }



                    for (int i = 0; i < arrayAtributo.size(); i++) {
                        if (arrayAtributo.get(i).equals("ROMBO")) {
                            num_rombo = arrayValor.get(i).toString().trim();
                        }
                        if (arrayAtributo.get(i).equals("PLACA")) {
                            cod_placa = arrayValor.get(i).toString().trim();
                        }
                        if (arrayAtributo.get(i).equals("UBICACION")) {
                            cod_ubicacion = arrayValor.get(i).toString().trim();
                        }
                        if (arrayAtributo.get(i).equals("TIPO VEHICULO")) {
                            tip_vehiculo = arrayValor.get(i).toString().trim();
                        }
                        if (arrayAtributo.get(i).equals("NIT")) {
                            num_nit = arrayValor.get(i).toString().trim();
                        }
                        if (arrayAtributo.get(i).equals("KM")) {
                            kilometraje = arrayValor.get(i).toString().trim();
                        }
                        if (arrayAtributo.get(i).equals("RAZON")) {
                            razon = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("SOAT")) {
                            soat = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("TECNICOMECANICA")) {
                            tecno = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("SOLICITUD")) {
                            solicitud = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("FECHA")) {
                            fecha = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("USUARIO")) {
                            usuario = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("TIEMPO")) {
                            tiempo = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("HORA INICIO")) {
                            horaInicio = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("HORA FIN")) {
                            horaFin = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("PRODUCTOS")) {
                            productText = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("TECNICO")) {
                            tecnico = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("PROMESA")) {
                            promesa = arrayValor.get(i).toString().trim();
                        }

                        if (arrayAtributo.get(i).equals("MODELO")) {
                            modelo = arrayValor.get(i).toString().trim();
                        }


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                actualizar(general, indice, conexion);

                final String cod_empresa = general[0][0];
                final String cod_sucursal = general[0][1];
                final String cod_proceso = general[0][3];
                final String cod_usuario = general[0][2];
                final String ind_estado = general[0][17];

                final Button buttonOk = (Button) findViewById(R.id.button_OK);
                buttonOk.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ok(cod_empresa,cod_sucursal,cod_proceso,cod_usuario,ind_estado,general);
                    }
                });

                final ImageButton button = (ImageButton) findViewById(R.id.imageButton8);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (indice == general.length - 1) {
                        } else {
                            indice++;
                            actualizar(general, indice, conexion);
                            texto.requestFocus();
                        }
                    }
                });


                final ImageButton button2 = (ImageButton) findViewById(R.id.imageButton7);
                button2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (indice == 0) {
                        } else {
                            indice--;
                            actualizar(general, indice,conexion);
                            texto.requestFocus();
                        }
                    }
                });

                final ImageButton button3 = (ImageButton) findViewById(R.id.imageButton6);
                button3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        indice = 0;
                        actualizar(general, indice,conexion);
                        texto.requestFocus();
                    }
                });

                final ImageButton button4 = (ImageButton) findViewById(R.id.imageButton9);
                button4.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        indice = general.length - 1;
                        actualizar(general, indice, conexion);
                        texto.requestFocus();
                    }
                });
            }catch (JSONException e ){
                e.printStackTrace();
            }
        }

        texto.requestFocus();

        String bodega = "";
        if (sucursal.trim().equals("51")){
            bodega = "1";
        }else{
            if (sucursal.trim().equals("33")){
                bodega = "2";
            }else{
                if (sucursal.trim().equals("10")){
                    bodega = "3";
                }else{
                    if (sucursal.trim().equals("99")){
                        bodega = "11";
                    }
                }
            }
        }

        String url = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sBodega", bodega));
        params.add(new BasicNameValuePair("sParametro", "romboDefault"));


        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);
        try {

            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                num_rombo = String.valueOf(Integer.parseInt(json.getString("rombo")) + 1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        soat = "1900-01-01";
        tecno = "1900-01-01";
        solicitud = "OK";
        pago = "CONTADO";
        razon = "1";
        descripcion = "CLIENTE";
        descripcionModelo = "AUT";
        modelo = "AUTO2017";
        kilometraje = "10";
        km_anterior = "9";
        tecnico = "DANIEL";
    }


    public void ampliar(View v){
        nombre = new String();

        nombre =  general[indice][5].trim();
        //File imgFile = new  File(ruta_fotos + getCode() + ".jpg");
        String strPath = dirFoto + nombre +  ".png";

        Bundle bundle = new Bundle();
        Intent intent = new Intent((Context) this, (Class) AmpliarImagen.class);
        bundle.putString("nombre", nombre);
        bundle.putString("path", strPath);
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    class ConsultarDatos extends AsyncTask<String, String, String> {

        String resultado;
        String user;
        @Override
        protected void onPreExecute(){
            mProgressDialog.show();

        }
        @Override
        protected String doInBackground(String... f_url) {
            //10.0.10.252
            String url = "http://" + ip + "/upload.php";
            System.out.println(url);
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,HttpVersion.HTTP_1_1);
            HttpPost httpPost = new HttpPost(url);



            nombre =   general[indice][5].trim();
            //File imgFile = new  File(ruta_fotos + getCode() + ".jpg");
            String imagen =  dirFoto + nombre +  ".png";
            System.out.println(imagen + " UPLOAD");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody fileBody = new FileBody(new File(imagen));
            builder.addPart("uploaded", fileBody);
            System.out.println(sucursal.trim() + "/" + getDia() + "/");
            builder.addTextBody("dia", "FOTOS_OPERATIVOS" + "/");

            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
            try{
                httpClient.execute(httpPost);
                httpClient.getConnectionManager().shutdown();
            }catch (ClientProtocolException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            mProgressDialog.dismiss();
        }
    }


    public void camara(View v) {
        try{
            ActivityCompat.requestPermissions(RecepcionUsuariosUbicaciones.this,
                    new String[]{"android.permission.GET_ACCOUNTS",
                            "android.permission.CAMERA"},
                    REQUEST_PERMISSIONS);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void visionApi(){
        //Si no existe crea la carpeta donde se guardaran las fotos
        //file.mkdirs();
        //String file = ruta_fotos + getCode() + ".jpg";
        TextView texto = (TextView) findViewById(R.id.textView_atributo);




        for (int i = 0; i < general.length; i++) {
            for (int j = 0; j < general[i].length; j++) {
                if (general[i][j].equals("null")){
                    general[i][j] = "";
                }
                System.out.println("GENERAL->" + general[i][j]);
            }
        }

        nombre = new String();
        nombre =   general[indice][5].trim();


        System.out.println("ESTE ES EL NOMBRE -> " + nombre + " general -> " +general[indice][4].trim());



        dirFoto =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)  + "/METRO/" + "FOTOS_OPERATIVOS" + "/";

        //String file = "/storage/E6E7-2A20/METRO/" + nombre;

        System.out.println(dirFoto);

        File fecha = new File(dirFoto);

        fecha.mkdirs();



        String file = dirFoto + nombre +  ".png";


        File mi_foto = new File(file);
        try {
            mi_foto.createNewFile();

            //
            Uri uri = Uri.fromFile(mi_foto);
            //Abre la camara para tomar la foto
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Guarda imagen
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            //Retorna a la actividad
            startActivityForResult(cameraIntent,  REQUEST_IMAGE_CAPTURE);
            ImageView myImage = (ImageView) findViewById(R.id.imageView);
            //File imgFile = new  File(ruta_fotos + getCode() + ".jpg");
            String strPath = dirFoto + nombre +  ".png";
            //String strPath = "/storage/E6E7-2A20/METRO/" + nombre;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;


            /*Bitmap bm = BitmapFactory.decodeFile(strPath,options);

            myImage.setImageBitmap(bm);
            myImage.invalidate();

            general[indice][13] = file;
            mostrarImagen();*/
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void uploadFoto(View v){
        new ConsultarDatos().execute("");
    }

    private String getCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String date = dateFormat.format(new Date());
        return date;
    }

    private String getDia() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String date = dateFormat.format(new Date());
        return date;
    }

    public void mostrarImagen(){
        ImageView myImage = (ImageView) findViewById(R.id.imageView);
        nombre =   general[indice][5].trim();
        //File imgFile = new  File(ruta_fotos + getCode() + ".jpg");
        String strPath = dirFoto + nombre +  ".png";
        System.out.println(strPath);
        //String strPath = "/storage/E6E7-2A20/METRO/" + nombre;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;




        Bitmap bm = BitmapFactory.decodeFile(strPath, options);


        myImage.setImageResource(R.drawable.camera);
        if (bm==null) {
            //myImage.setImageResource(R.drawable.camera);
        }else{
            //myImage.setImageBitmap(bm);
        }





        /*try {
            if (imgFile.exists()) {
                ImageView myImage = (ImageView) findViewById(R.id.imageView);
                myImage.setImageURI(Uri.fromFile(imgFile));

            }
        }catch (Exception e){
        }*/
    }



    public void siguiente(View v){


        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
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

        general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "1",1);
        if (indice==general.length-1){

        }else {
            indice++;
            actualizar(general, indice, conexion);
        }
    }





    public void cancelar(View v){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Saliendo...")
                .setMessage("Esta Seguro que desea dejar de recibir?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    public void guardar(View v){
        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
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
        final String[][] general;
        general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01",1);
       /* for (int j = 0;j<Integer.parseInt(resultado.get(0).toString());j++) {
            for (int i = 0; i < resultado.size()-18; i++) {
                general[j][i] = resultado.get(i).toString();
            }
        }*/

        final ArrayList resultado = new ArrayList();



        /*if (Integer.parseInt(resultado.get(0).toString()) > 0){
            int cont = 0;

        }*/

        final String cod_empresa = resultado.get(0).toString();
        final String cod_sucursal = resultado.get(1).toString();
        final String cod_usuario = resultado.get(2).toString();
        final String cod_proceso = resultado.get(3).toString();
        final String cod_atributo = resultado.get(4).toString();
        final String cod_valor = resultado.get(5).toString();
        final String num_orden = resultado.get(7).toString();

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
        //System.out.println(Integer.parseInt(resultado.get(6).toString()));
        dataBaseHelper.guardarProceso(cod_empresa, cod_sucursal, cod_usuario, cod_proceso, cod_atributo, texto.getText().toString(), num_orden);
        Toast.makeText(RecepcionUsuariosUbicaciones.this, (CharSequence) "se guardo correctamente!", Toast.LENGTH_SHORT).show();
        dataBaseHelper.close();
        texto.setText("");
    }

    public void atras(View v){

        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
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
        final String[][] general;
        general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01",1);
        if(indice == 0){

        }else{
            indice--;
            actualizar(general,indice,conexion);
        }
    }

    public void anteriorUltimo(View v){
        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
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
        final String[][] general;
        general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01",1);
        indice = general.length-1;
        actualizar(general, indice, conexion);
    }

    public void prueba (View v){
        TextView atributo = (TextView) findViewById(R.id.textView_atributo);
        atributo.setText("cambio");
    }

    public void anteriorPrimero(View v){
        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context)this);
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
        final String[][] general;
        general = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01",1);
        indice= 0;
        actualizar(general, indice, conexion);
    }


    public void actualizar(final String[][] general, final int indice, final String conexion){




        final TextView error = (TextView) findViewById(R.id.textView_error);
        //final TextView informativo = (TextView) findViewById(R.id.textView_informativo);
        //.setTextColor(Color.rgb(21,130,125));
        error.setText("");
        //informativo.setText("");

        final ArrayList resultado = new ArrayList();
        final TextView atributo = (TextView) findViewById(R.id.textView_atributo);
        final ImageView imagen = (ImageView) findViewById(R.id.imageView);

        final TextView atributo1 = (TextView) findViewById(R.id.textView_atributo);
            atributo1.setBackgroundResource(R.color.METRO_black);
            atributo1.setTextColor(Color.BLACK);


        texto.setText(general[indice][5].trim());

        Button buscar = (Button) findViewById(R.id.button_buscar);
        Button anadir = (Button) findViewById(R.id.button_anadir);

        if ( general[indice][15].trim().equals(general[indice][3].trim())) {
            buscar.setVisibility(View.INVISIBLE);
            anadir.setVisibility(View.INVISIBLE);
        }else {
            //imagen.setImageResource(R.drawable.add);
            Button borrartodo = (Button) findViewById(R.id.borrartodo);
            Button quitarespacios = (Button) findViewById(R.id.quitarespacios);
            borrartodo.setVisibility(View.INVISIBLE);
            quitarespacios.setVisibility(View.INVISIBLE);
            buscar.setVisibility(View.VISIBLE);
            anadir.setVisibility(View.VISIBLE);
        }

        if (general[indice][4].trim().equals("RAZON")) {
            error.setTextColor(Color.RED);
            error.setText(descripcion);
        }

        if (general[indice][4].trim().equals("SOAT")) {
            texto.setText(soat);
            general[indice][5] = soat;
        }

        if (general[indice][4].trim().equals("PLACA")) {
            texto.setText(cod_placa);
            general[indice][5] = cod_placa;
        }

        if (general[indice][4].trim().equals("PAGO")) {
            texto.setText(pago);
            general[indice][5] = pago;
        }

        if (general[indice][4].trim().equals("ROMBO")) {
            texto.setText(num_rombo);
            general[indice][5] = num_rombo;
        }

        if (general[indice][4].trim().equals("UBICACION")) {
            texto.setText(cod_ubicacion);
            general[indice][5] = cod_ubicacion;
        }

        if (general[indice][4].trim().equals("TIPO VEHICULO")) {
            texto.setText(tip_vehiculo);
            general[indice][5] = tip_vehiculo;
        }

        if (general[indice][4].trim().equals("SOLICITUD")) {
            texto.setText(solicitud);
            general[indice][5] = solicitud;
        }

        if (general[indice][4].trim().equals("FECHA")) {
            texto.setText(fecha);
            general[indice][5] = fecha;
        }

        if (general[indice][4].trim().equals("USUARIO")) {
            texto.setText(usuario);
            general[indice][5] = usuario;
        }

        if (general[indice][4].trim().equals("TIEMPO")) {
            texto.setText(tiempo);
            general[indice][5] = tiempo;
        }

        if (general[indice][4].trim().equals("HORA INICIO")) {
            texto.setText(horaInicio);
            general[indice][5] = horaInicio;
        }

        if (general[indice][4].trim().equals("HORA FIN")) {
            texto.setText(horaFin);
            general[indice][5] = horaFin;
        }

        if (general[indice][4].trim().equals("PRODUCTOS")) {
            texto.setText(productText);
            general[indice][5] = productText;
        }

        if (general[indice][4].trim().equals("TECNICO")) {
            texto.setText(tecnico);
            general[indice][5] = tecnico;
        }

        if (general[indice][4].trim().equals("PROMESA")) {
            texto.setText(promesa);
            general[indice][5] = promesa;
        }

        if (general[indice][4].trim().equals("RAZON")) {
            texto.setText(razon);
            general[indice][5] = razon;
        }



        if (general[indice][4].trim().equals("KM")) {
            texto.setText(kilometraje);
            general[indice][5] = kilometraje;
            error.setText("Km anterior : " + km_anterior.trim());
        }

        if (general[indice][4].trim().equals("NIT")) {
            texto.setText(num_nit);
            general[indice][5] = num_nit;
        }

        if (general[indice][4].trim().equals("PLACA") && encuentraPlaca.equals("false")) {
            error.setText("Placa no existe, se creara!");
        }

        if (general[indice][4].trim().equals("NIT") && encuentraNit.equals("false")) {
            error.setText("Nit no existe, se creara!");
        }

        if (general[indice][4].trim().equals("MODELO")) {
            if (modelo.equals("null")){
                modelo = "";
            }
            texto.setText(modelo);
            general[indice][5] = modelo;
        }

        if (general[indice][4].trim().equals("TECNICOMECANICA")) {
            texto.setText(tecno);
            general[indice][5] = tecno;
        }

        if (general[indice][4].trim().equals("ROMBO")) {
            if ( cita.trim().equals("") || cita.trim().equals("null")){
                atributo1.setBackgroundColor(Color.BLUE);
                atributo1.setTextColor(Color.WHITE);
            }else{
                error.setText("Hora cita : " + horacita.substring(0,5));
                atributo1.setBackgroundColor(Color.YELLOW);
                atributo1.setTextColor(Color.BLUE);
                eventoCita = "C";
            }
        }



        //for(int j = 0;j<general[0].length;j++){
        //    resultado.add(general[indice][j]);
        //}


       /* String url = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sCodUser", resultado.get(2).toString().trim()));
        params.add(new BasicNameValuePair("sCodEmpresa", resultado.get(0).toString().trim()));
        params.add(new BasicNameValuePair("sCodAtributo", resultado.get(4).toString().trim()));
        params.add(new BasicNameValuePair("sCodSucursal",sucursal));
        params.add(new BasicNameValuePair("sParametro", "actualizar"));

        final String resultServer  = getHttpPost(url, params);
        System.out.println("-----------ACTUALIZAR------------");
        System.out.println("coduser - " + resultado.get(2).toString());
        System.out.println("codempresa - " + resultado.get(0).toString());
        System.out.println("codatributo + " + resultado.get(4).toString());
        System.out.println(resultServer);
        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add((json.getString("cod_valor")));
            }
            texto.setText(array.get(0));
        }catch (JSONException e){
            e.printStackTrace();
        }*/

        //texto.setText(general[indice][5].trim());


        atributo.setText(general[indice][4].trim());
        //atributo.setText(resultado.get(7) + ". " + resultado.get(4).toString().trim());

        //if(general[indice][5].trim().equals("ROMBO")) imagen.setImageResource(R.drawable.rombo);
        //if(general[indice][5].trim().equals("PLACA")) imagen.setImageResource(R.drawable.placa6);
        //if(general[indice][5].trim().equals("UBICACION")) imagen.setImageResource(R.drawable.ubicacion);
        //if (resultado.get(13).equals("ROMBO.JPG")) imagen.setImageResource(R.drawable.rombo1);
        //if (resultado.get(13).equals("PLACA.JPG")) imagen.setImageResource(R.drawable.placa6);
        //if (resultado.get(13).equals("NIT.JPG")) imagen.setImageResource(R.drawable.cedula1);
        /*if (Integer.parseInt(resultado.get(0).toString()) > 0){
            int cont = 0;

        }*/

        mostrarImagen();

        texto.requestFocus();

        final String cod_empresa = general[indice][0];
        final String cod_sucursal =general[indice][1];
        final String cod_usuario = general[indice][2];
        final String[] cod_proceso = {general[indice][3]};
        final String[] cod_atributo = {general[indice][4]};
        final String cod_valor = general[indice][5];
        final String num_orden = general[indice][7];

        /*final String cod_empresa = resultado.get(0).toString();
        final String cod_sucursal = resultado.get(1).toString();
        final String cod_usuario = resultado.get(2).toString();
        final String cod_proceso = resultado.get(3).toString();
        final String cod_atributo = resultado.get(4).toString();
        final String cod_valor = resultado.get(5).toString();
        final String num_orden = resultado.get(7).toString();*/


        if (conexion.equals("local")) texto.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    /*if (texto.getText().length() == 0 && (cod_atributo.equals("ROMBO") || (cod_atributo.equals("UBICACION")) )) {
                        Intent intent = new Intent((Context) Recepcion.this, (Class) MostrarRombUbic.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("conexion", conexion);
                        Recepcion.this.startActivityForResult(intent,request_code);

                    } else {*/

                        final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context) RecepcionUsuariosUbicaciones.this);
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

                        if (resultado.get(9).equals("D")) {
                            String cedula = texto.getText().toString().replaceAll("\n", "");
                            String cedulafinal = "";

                                /*
                                Character  numAux = cedula.charAt(50);
                                String num = numAux.toString();
                                while (num.charAt(0)>='0' && num.charAt(0)<='9'){
                                    cedulafinal = cedulafinal + num.charAt(0);
                                }
                                /*for (int i = 50;i<58;i++){
                                    cedulafinal = cedulafinal+cedula.charAt(i);
                                }*/


                            Cedula = cedula.substring(47, 58);
                            nombre1 = cedula.substring(95, 116);
                            nombre2 = cedula.substring(117, 140);
                            apellido1 = cedula.substring(58, 71);
                            apellido2 = cedula.substring(72, 94);


                            if (Cedula.substring(1, 2).equals("00")) {
                                Cedula = cedula.substring(50, 58);
                            }
                            dataBaseHelper.updateNit(Cedula, nombre1, nombre2, apellido1, apellido2);
                            dataBaseHelper.close();
                            pasar(conexion, general);
                            return true;

                        } else {


                            //System.out.println(Integer.parseInt(resultado.get(6).toString()));
                            dataBaseHelper.guardarProceso(cod_empresa, cod_sucursal, cod_usuario, cod_proceso[0], cod_atributo[0], texto.getText().toString(), num_orden);
                            dataBaseHelper.close();
                            pasar(conexion, general);
                            return true;
                            //actualizar(general,indice);
                        }
                    //}
                }
                return false;
            }
        });

        else {



            final String finalIp = ip;

            final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
                public boolean onDoubleTap(MotionEvent e) {
                    if ((cod_atributo[0].trim().equals("ROMBO")
                            || cod_atributo[0].trim().equals("UBICACION") || cod_atributo[0].trim().equals("TECNICO")
                            || cod_atributo[0].trim().equals("NUMERO OT")) || cod_atributo[0].trim().equals("PROMESA")
                            || cod_atributo[0].trim().equals("RAZON2") || cod_atributo[0].trim().equals("RAZON")) {

                        Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarRombUbic.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("conexion", conexion);
                        bundle2.putString("ip", ip);
                        bundle2.putString("atributo", cod_atributo[0].trim());
                        bundle2.putString("empresa", cod_empresa);
                        bundle2.putString("sucursal", cod_sucursal);
                        intent.putExtras(bundle2);
                        RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                    }else{
                        if (general[indice][4].trim().equals("SOLICITUD")) {

                            Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) EditarSolicitud.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("conexion", conexion);
                            bundle2.putString("ip", ip);
                            bundle2.putString("atributo", texto.getText().toString().trim());
                            bundle2.putString("empresa", cod_empresa);
                            bundle2.putString("sucursal", sucursal);
                            bundle2.putString("parametro", parametro);
                            intent.putExtras(bundle2);
                            RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                            error.setText("");
                        }else {
                            if (general[indice][4].trim().equals("SOAT") || general[indice][4].trim().equals("TECNICOMECANICA")) {
                                Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) CalendarioSoat.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("parametro", general[indice][4].trim());
                                intent.putExtras(bundle2);
                                startActivityForResult(intent, request_code);
                            }
                        }//else{
                    }
                       /* if (general[indice][9].toString().equals("F")){
                            Intent intent = new Intent((Context) Recepcion.this, (Class) Calendario.class);
                            Recepcion.this.startActivityForResult(intent,request_code);
                        }
                    }*/

                        /*String url2 = "http://" + ip + "/consultarGeneral.php";

                        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                        params2.add(new BasicNameValuePair("sCodUser", resultado.get(2).toString()));
                        params2.add(new BasicNameValuePair("sCodEmpresa", resultado.get(0).toString()));
                        params2.add(new BasicNameValuePair("sCodAtributo", resultado.get(4).toString()));
                        params2.add(new BasicNameValuePair("sCodSucursal",sucursal));
                        params2.add(new BasicNameValuePair("sParametro", "actualizar"));


                        String resultServer2 = getHttpPost(url2, params2);
                        System.out.println("---------------------------------");
                        System.out.println(resultServer2);
                        ArrayList<String> array = new ArrayList<String>();
                        try {
                            JSONArray jArray = new JSONArray(resultServer2);

                            //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                array.add((json.getString("cod_valor")));
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        if (array.size()>0) {
                            String url4 = "http://" + ip + "/updateGeneral.php";

                            List<NameValuePair> params4 = new ArrayList<NameValuePair>();


                            params4.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                            params4.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                            params4.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                            params4.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                            params4.add(new BasicNameValuePair("sParametro", "update3"));

                            String resultServer4 = getHttpPost(url4, params4);

                            JSONObject c4;
                            try {

                                Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                pasar(conexion, general);
                            }catch(Exception e1){
                                Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {





                            final AlertDialog.Builder ad = new AlertDialog.Builder(Recepcion.this);

                            ad.setTitle("Error! ");
                            ad.setIcon(android.R.drawable.btn_star_big_on);
                            ad.setPositiveButton("Close", null);
                            InputStream isr = null;
                            String url = "http://" + finalIp + "/update.php";

                            List<NameValuePair> params = new ArrayList<NameValuePair>();


                            params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                            params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                            params.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                            params.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                            params.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                            params.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                            params.add(new BasicNameValuePair("sNumOrden", num_orden));

                            */

                            // params.add(new BasicNameValuePair("sNumOrden", num_orden));


                            /*if (resultado.get(4).equals("NIT")) {
                                String cedula = texto.getText().toString().replaceAll("\n", "");
                                String cedulafinal = "";

                                String url3 = "http://" + finalIp + ":8080/update2.php";
                                String resultServer3;
                                try {

                                    Cedula = cedula.substring(47, 58);
                                    nombre1 = cedula.substring(95, 116);
                                    nombre2 = cedula.substring(117, 140);
                                    apellido1 = cedula.substring(58, 71);
                                    apellido2 = cedula.substring(72, 94);
                                    if (Cedula.substring(1, 2).equals("00")) {
                                        Cedula = cedula.substring(50, 58);
                                    }


                                    List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                                    params3.add(new BasicNameValuePair("sNit", Cedula));
                                    params3.add(new BasicNameValuePair("sApellido1", apellido1));
                                    params3.add(new BasicNameValuePair("sApellido2", apellido2));
                                    params3.add(new BasicNameValuePair("sNombre1", nombre1));
                                    params3.add(new BasicNameValuePair("sNombre2", nombre2));
                                    resultServer3 = getHttpPost(url3, params3);
                                    System.out.println(resultServer3);

                                    String strStatusID = "0";
                                    String strError = "Unknow Status! 3";

                                    JSONObject c3;
                                    try {
                                        c3 = new JSONObject(resultServer3);
                                        strStatusID = c3.getString("StatusID");
                                        strError = c3.getString("Error");
                                    } catch (JSONException ex) {
                                        // TODO Auto-generated catch block
                                        ex.printStackTrace();
                                    }
                                    if (strStatusID.equals("0")) {
                                        ad.setMessage(strError);
                                        ad.show();
                                        return false;
                                    } else {
                                        Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                        pasar(conexion, general);
                                    }
                                } catch (Exception ec) {
                                    url3 = "http://" + finalIp + "/update.php";
                                    List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                                    params3.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                    params3.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                    params3.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                    params3.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                    params3.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                    params3.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                    params3.add(new BasicNameValuePair("sNumOrden", num_orden));
                                    resultServer3 = getHttpPost(url3, params3);
                                    String strStatusID = "0";
                                    String strError = "Unknow Status! 3";

                                    JSONObject c3;
                                    try {
                                        c3 = new JSONObject(resultServer3);
                                        strStatusID = c3.getString("StatusID");
                                        strError = c3.getString("Error");
                                    } catch (JSONException ex) {
                                        // TODO Auto-generated catch block
                                        ex.printStackTrace();
                                    }
                                    if (strStatusID.equals("0")) {
                                        ad.setMessage(strError);
                                        ad.show();
                                        return false;
                                    } else {
                                        Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                        pasar(conexion, general);
                                    }
                                }


                            } else {
                                if (resultado.get(4).equals("MATRICULA")) {
                                    String matricula = texto.getText().toString().replaceAll("\n", "");
                                    matricula = matricula.replaceAll(" ", "");
                                    System.out.println("LONGITUD : " + matricula.length());
                                    System.out.println(texto.getText().toString());
                                    String matriculafinal = "";


                                Character  numAux = cedula.charAt(50);
                                String num = numAux.toString();
                                while (num.charAt(0)>='0' && num.charAt(0)<='9'){
                                    cedulafinal = cedulafinal + num.charAt(0);
                                }

                                    try {
                                        placa = matricula.substring(55, 61);
                                        vin = matricula.substring(61, 78);

                                        texto.setText("");

                                        Intent intent = new Intent((Context) Recepcion.this, (Class) MostrarDatosMatricula.class);
                                        Bundle bundle2 = new Bundle();
                                        bundle2.putString("placa", placa);
                                        bundle2.putString("vin", vin);
                                        intent.putExtras(bundle2);
                                        Recepcion.this.startActivity(intent);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();

                                        ad.setTitle("Error! ");
                                        ad.setIcon(android.R.drawable.btn_star_big_on);
                                        ad.setPositiveButton("Close", null);
                                        String url5 = "http://" + finalIp + "/update.php";

                                        List<NameValuePair> params5 = new ArrayList<NameValuePair>();


                                        params5.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                        params5.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                        params5.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                        params5.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                        params5.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                        params5.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                        params5.add(new BasicNameValuePair("sNumOrden", num_orden));
                                        String resultServer = getHttpPost(url, params);
                                        System.out.println(resultServer);


                                        JSONObject c;
                                        try {
                                            Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                            pasar(conexion, general);
                                        }catch(Exception e1){
                                            Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                } else {
                                    String resultServer = getHttpPost(url, params);
                                    System.out.println(resultServer);

                                    String strStatusID = "0";
                                    String strError = "Unknow Status!";

                                    JSONObject c;
                                    try {

                                        Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();

                                        pasar(conexion, general);
                                    }catch(Exception e1){
                                        Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }*/


                            /*String file = "storage/sdcard0/METRO/" + nombre;
                            File mi_foto = new File(file);
                            String file2 = "storage/sdcard0/METRO/";
                            File mi_foto2 = new File(file2);
                            mi_foto.renameTo(mi_foto2);*/
                        //else{
                        //    Toast.makeText(getApplicationContext()," Este registro ya esta en la base de datos !" , Toast.LENGTH_SHORT).show();
                        //}
                        // }

                    return true;
                }
            });

            if ( !(general[indice][18].trim().equals(general[indice][3].trim()))) {




                Button BotonBuscar = (Button) findViewById(R.id.button_buscar);
                BotonBuscar.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if (general[indice][4].trim().equals("NIT")) {

                            Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarCliente.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("conexion", conexion);
                            bundle2.putString("ip", ip);
                            bundle2.putString("atributo", texto.getText().toString().trim());
                            bundle2.putString("empresa", cod_empresa);
                            bundle2.putString("sucursal", sucursal);
                            bundle2.putString("parametro", parametro);
                            intent.putExtras(bundle2);
                            RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                            error.setText("");
                        }else{

                            if ((cod_atributo[0].trim().equals("ROMBO")
                                    || cod_atributo[0].trim().equals("UBICACION") || cod_atributo[0].trim().equals("TECNICO")
                                    || cod_atributo[0].trim().equals("NUMERO OT")) || cod_atributo[0].trim().equals("PROMESA")
                                    || cod_atributo[0].trim().equals("RAZON2") || cod_atributo[0].trim().equals("RAZON")
                                    || cod_atributo[0].trim().equals("TIPO VEHICULO")
                                    || cod_atributo[0].trim().equals("PAGO")) {

                                Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarRombUbic.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("atributo", cod_atributo[0].trim());
                                bundle2.putString("empresa", cod_empresa);
                                bundle2.putString("sucursal", cod_sucursal);
                                intent.putExtras(bundle2);
                                RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                            }else{
                                if (general[indice][4].trim().equals("SOLICITUD")) {

                                    Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) EditarSolicitud.class);
                                    Bundle bundle2 = new Bundle();
                                    bundle2.putString("conexion", conexion);
                                    bundle2.putString("ip", ip);
                                    bundle2.putString("atributo", texto.getText().toString().trim());
                                    bundle2.putString("empresa", cod_empresa);
                                    bundle2.putString("sucursal", sucursal);
                                    bundle2.putString("parametro", parametro);
                                    intent.putExtras(bundle2);
                                    RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                                    error.setText("");
                                }else {
                                    if (general[indice][4].trim().equals("PRODUCTOS")) {

                                        Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MercarPedido.class);
                                        Bundle bundle2 = new Bundle();
                                        bundle2.putString("ip", ip);
                                        bundle2.putString("idmodano",modelo);
                                        intent.putExtras(bundle2);
                                        RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                                        error.setText("");
                                    }else {
                                        if (general[indice][4].trim().equals("HORA INICIO")) {

                                            Calendar mcurrentTime = Calendar.getInstance();
                                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                            int minute = mcurrentTime.get(Calendar.MINUTE);

                                            TimePickerDialog mTimePicker;
                                            mTimePicker = new TimePickerDialog(RecepcionUsuariosUbicaciones.this, new TimePickerDialog.OnTimeSetListener() {
                                                @Override
                                                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                                    texto.setText( selectedHour + ":" + selectedMinute);
                                                    horaInicio = texto.getText().toString();
                                                }
                                            }, hour, minute, true);
                                            mTimePicker.setTitle("Seleccione hora");
                                            mTimePicker.show();
                                        } else {
                                            if (general[indice][4].trim().equals("HORA FIN")) {

                                                Calendar mcurrentTime = Calendar.getInstance();
                                                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                                int minute = mcurrentTime.get(Calendar.MINUTE);

                                                TimePickerDialog mTimePicker;
                                                mTimePicker = new TimePickerDialog(RecepcionUsuariosUbicaciones.this, new TimePickerDialog.OnTimeSetListener() {
                                                    @Override
                                                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                                        texto.setText(selectedHour + ":" + selectedMinute);
                                                        horaFin = texto.getText().toString();
                                                    }
                                                }, hour, minute, true);
                                                mTimePicker.setTitle("Seleccione hora");
                                                mTimePicker.show();
                                            } else {
                                                if (general[indice][4].trim().equals("MODELO")) {
                                                    Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarModelo.class);
                                                    Bundle bundle2 = new Bundle();
                                                    bundle2.putString("placa", cod_placa);
                                                    bundle2.putString("ip", ip);
                                                    intent.putExtras(bundle2);
                                                    startActivityForResult(intent, request_code);
                                                } else {
                                                    if (general[indice][4].trim().equals("USUARIO")) {
                                                        Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarRombUbic.class);
                                                        Bundle bundle2 = new Bundle();
                                                        bundle2.putString("conexion", conexion);
                                                        bundle2.putString("ip", ip);
                                                        bundle2.putString("atributo", "TECNICO");
                                                        bundle2.putString("empresa", cod_empresa);
                                                        bundle2.putString("sucursal", cod_sucursal);
                                                        intent.putExtras(bundle2);
                                                        RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                                                    } else {
                                                        if (general[indice][4].trim().equals("FECHA") || general[indice][4].trim().equals("TIEMPO") || general[indice][4].trim().equals("TECNICOMECANICA")) {
                                                            Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) CalendarioSoat.class);
                                                            Bundle bundle2 = new Bundle();
                                                            bundle2.putString("parametro", general[indice][4].trim());
                                                            intent.putExtras(bundle2);
                                                            startActivityForResult(intent, request_code);
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
                });

                Button BotonAnadir = (Button) findViewById(R.id.button_anadir);
                BotonAnadir.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        proceso = general[indice][18].trim();
                        titulo = general[indice][4].trim();

                        cod_atributo[0] = general[indice][4].trim();

                        nombreTabla = generalAux[indice][19].trim();

                        System.out.println("TABLAAAAA " +  nombreTabla);

                        System.out.println(user + " " + pass + " " + proceso);
                        Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) RecepcionUsuariosUbicaciones.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("user", user);
                        bundle2.putString("pass", pass);
                        bundle2.putString("sucursal", sucursal);
                        bundle2.putString("empresa", empresa);
                        bundle2.putString("conexion", conexion);
                        bundle2.putString("sucursal", sucursal);
                        bundle2.putString("ip", ip);
                        bundle2.putString("titulo", titulo);
                        bundle2.putString("proceso", proceso);
                        bundle2.putString("nombreTabla",nombreTabla);
                        bundle2.putString("codAtributo",cod_atributo[0].trim());

                        intent.putExtras(bundle2);
                        startActivity(intent);
                    }
                });



            }

                texto.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector.onTouchEvent(event);
                    }
                });
                texto.setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {

                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {


                        /*if (texto.getText().toString().matches("") && (general[indice][5].equals("ROMBO") || general[indice][5].equals("UBICACION") || general[indice][5].equals("MECANICO"))) {
                            Intent intent = new Intent((Context) Recepcion.this, (Class) MostrarRombUbic.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("conexion", conexion);
                            bundle2.putString("ip", ip);
                            bundle2.putString("atributo", cod_atributo);
                            intent.putExtras(bundle2);
                            Recepcion.this.startActivityForResult(intent, request_code);
                            Toast.makeText(getApplicationContext(), resultado.get(4) + "  ATRIBUTO ", Toast.LENGTH_SHORT).show();

                        } else {*/
                            /*String url2 = "http://" + ip + "/consultarGeneral.php";

                            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                            params2.add(new BasicNameValuePair("sCodUser", general[indice][2].toString()));
                            params2.add(new BasicNameValuePair("sCodEmpresa", general[indice][0].toString()));
                            params2.add(new BasicNameValuePair("sCodAtributo", general[indice][4].toString()));
                            params2.add(new BasicNameValuePair("sCodSucursal",sucursal));
                            params2.add(new BasicNameValuePair("sParametro", "actualizar"));


                            String resultServer2 = getHttpPost(url2, params2);
                            System.out.println("---------------------------------");
                            System.out.println(resultServer2);
                            ArrayList<String> array = new ArrayList<String>();
                            try {
                                JSONArray jArray = new JSONArray(resultServer2);

                                //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json = jArray.getJSONObject(i);
                                    array.add((json.getString("cod_valor")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (array.size()>0) {
                                String url4 = "http://" + ip + "/updateGeneral.php";

                                List<NameValuePair> params4 = new ArrayList<NameValuePair>();


                                params4.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                params4.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                params4.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                params4.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                params4.add(new BasicNameValuePair("sParametro", "update3"));

                                String resultServer4 = getHttpPost(url4, params4);
                                System.out.println(resultServer4);


                                try {
                                    Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                    pasar(conexion, general);
                                }catch (Exception e){
                                    Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                }
                            } else {*/





                                /*final AlertDialog.Builder ad = new AlertDialog.Builder(Recepcion.this);

                                ad.setTitle("Error! ");
                                ad.setIcon(android.R.drawable.btn_star_big_on);
                                ad.setPositiveButton("Close", null);
                                InputStream isr = null;
                                String url = "http://" + finalIp + "/update.php";

                                List<NameValuePair> params = new ArrayList<NameValuePair>();


                                params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                params.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                params.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                params.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                params.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                params.add(new BasicNameValuePair("sNumOrden", num_orden));

                                String resultServer = getHttpPost(url, params);
                                System.out.println(resultServer);


                                JSONObject c;
                                try {
                                    Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                    pasar(conexion, general);
                                }catch(Exception e){
                                    Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                }*/

                            String atributo = texto.getText().toString().trim();
                            error.setTextColor(Color.RED);

                            String file = "storage/sdcard0/METRO/" + nombre;
                            File mi_foto = new File(file);
                            String file2 = "storage/sdcard0/METRO/";
                            File mi_foto2 = new File(file2);
                            mi_foto.renameTo(mi_foto2);

                            EditText texto1 = (EditText) findViewById(R.id.editText_texto);

                            if (general[indice][4].trim().equals("ROMBO")) {
                                error.setText("");
                                num_rombo = texto.getText().toString().trim();
                            }
                            if (general[indice][4].trim().equals("PLACA")) {
                                cod_placa = texto.getText().toString().trim();

                            }

                            if (general[indice][4].trim().equals("PAGO")) {
                                pago = texto.getText().toString().trim();
                            }
                            if (general[indice][4].trim().equals("PLACA") && encuentraPlaca.equals("false")) {
                                error.setText("Placa no existe, se creara!");
                            }
                            if (general[indice][4].trim().equals("UBICACION")) {
                                cod_ubicacion = texto.getText().toString().trim();
                            }
                            if (general[indice][4].trim().equals("TIPO VEHICULO")) {
                                tip_vehiculo = texto.getText().toString().trim();
                            }
                            if (general[indice][4].trim().equals("NIT")) {
                                num_nit = texto.getText().toString().trim();
                            }
                            if (general[indice][4].trim().equals("KM")) {
                                kilometraje = texto.getText().toString().trim();
                            }
                            if (general[indice][4].trim().equals("RAZON")) {
                                if (razon.equals("61") || razon.equals("62")){
                                    eventoRazon = "G";
                                }
                                razon = texto.getText().toString().trim();
                                error.setText(descripcion);
                            }

                            if (general[indice][4].trim().equals("SOAT")) {
                                soat = texto.getText().toString().trim();
                            }

                            if (general[indice][4].trim().equals("TECNICOMECANICA")) {
                                tecno = texto.getText().toString().trim();
                            }

                            if (general[indice][4].trim().equals("CEDULA")) {
                                num_nit = texto.getText().toString().trim();
                            }


                            if (general[indice][4].trim().equals("PLACA")) {

                                cita = new String();
                                horacita = new String();

                                String url4 = "http://" + ip + "/consultarGeneral.php";

                                List<NameValuePair> params4 = new ArrayList<NameValuePair>();

                                params4.add(new BasicNameValuePair("sParametro", "placanit"));
                                params4.add(new BasicNameValuePair("sPlaca", cod_placa));

                                String resultServer4 = getHttpPost(url4, params4);
                                System.out.println("---------------------------------resultserver----------------");
                                System.out.println(resultServer4);

                                try {

                                    JSONArray jArray = new JSONArray(resultServer4);
                                    ArrayList<String> array = new ArrayList<String>();
                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject json = jArray.getJSONObject(i);
                                        array.add(json.getString("nit"));
                                    }
                                    if (array.get(0).equals("null")){

                                    }else {
                                        num_nit = array.get(0);
                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                        try {

                                            guardarMovimiento("NIT", num_nit);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                        dataBaseHelper.close();
                                    }
                                } catch (JSONException e) {

                                    error.setText("");
                                    //informativo.setTextColor(Color.BLUE);
                                    e.printStackTrace();
                                }

                                String url3 = "http://" + ip + "/consultarGeneral.php";

                                List<NameValuePair> params3 = new ArrayList<NameValuePair>();

                                params3.add(new BasicNameValuePair("sParametro", "CITA"));
                                params3.add(new BasicNameValuePair("sPlaca", cod_placa));

                                String resultServer3 = getHttpPost(url3, params3);
                                System.out.println("---------------------------------resultserver----------------");
                                System.out.println(resultServer3);

                                try {

                                    JSONArray jArray = new JSONArray(resultServer3);
                                    ArrayList<String> array = new ArrayList<String>();
                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject json = jArray.getJSONObject(i);
                                        array.add(json.getString("placa"));
                                        array.add(json.getString("fecha"));
                                    }
                                    cita = array.get(0);
                                    horacita = array.get(1);
                                    eventoCita = "C";
                                } catch (JSONException e) {

                                    error.setText("");
                                    //informativo.setTextColor(Color.BLUE);
                                    e.printStackTrace();
                                }

                                String url1 = "http://" + ip + "/consultarGeneral.php";

                                List<NameValuePair> params1 = new ArrayList<NameValuePair>();

                                params1.add(new BasicNameValuePair("sParametro", "MODELO"));
                                params1.add(new BasicNameValuePair("sPlaca", cod_placa));

                                String resultServer1 = getHttpPost(url1, params1);
                                System.out.println("---------------------------------resultserver----------------");
                                System.out.println(resultServer1);
                                try {

                                    JSONArray jArray = new JSONArray(resultServer1);
                                    ArrayList<String> array = new ArrayList<String>();
                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject json = jArray.getJSONObject(i);
                                        array.add(json.getString("modelo"));
                                    }

                                    modelo = array.get(0);

                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                    try {

                                        guardarMovimiento("MODELO", modelo);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    dataBaseHelper.close();

                                } catch (JSONException e) {

                                    error.setText("");
                                    //informativo.setTextColor(Color.BLUE);
                                    e.printStackTrace();
                                }

                                String url = "http://" + ip + "/consultarGeneral.php";

                                List<NameValuePair> params = new ArrayList<NameValuePair>();

                                params.add(new BasicNameValuePair("sParametro", "placa"));
                                params.add(new BasicNameValuePair("sPlaca", cod_placa));

                                String resultServer = getHttpPost(url, params);
                                System.out.println("---------------------------------resultserver----------------");
                                System.out.println(resultServer);
                                String placa = "";
                                try {

                                    JSONArray jArray = new JSONArray(resultServer);
                                    ArrayList<String> array = new ArrayList<String>();
                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject json = jArray.getJSONObject(i);
                                        array.add(json.getString("placa"));
                                        placa = array.get(0);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                String url2 = "http://" + ip + "/consultarGeneral.php";

                                List<NameValuePair> params2 = new ArrayList<NameValuePair>();

                                params2.add(new BasicNameValuePair("sParametro", "fechas"));
                                params2.add(new BasicNameValuePair("sPlaca", cod_placa));

                                String resultServer2 = getHttpPost(url2, params2);
                                System.out.println("---------------------------------resultserver----------------");
                                System.out.println(resultServer2);
                                try {

                                    JSONArray jArray = new JSONArray(resultServer2);
                                    ArrayList<String> array = new ArrayList<String>();
                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject json = jArray.getJSONObject(i);
                                        array.add(json.getString("soat"));
                                        array.add(json.getString("tecno"));
                                    }


                                    soat = array.get(0);

                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                    try {

                                        guardarMovimiento("SOAT", soat);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    dataBaseHelper.close();

                                    tecno = array.get(1);


                                    try {

                                        guardarMovimiento("TECNICOMECANICA", tecno);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    dataBaseHelper.close();


                                } catch (JSONException e) {
                                    error.setText("");
                                    e.printStackTrace();
                                }

                                if (cod_placa.trim().equals(placa)) {
                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                    try {

                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    dataBaseHelper.close();
                                    general[indice][5] = texto.getText().toString().trim();
                                    pasar(conexion, general);
                                    error.setText("");
                                    encuentraPlaca = "true";

                                } else {
                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                    try {

                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                    //ArrayList arrayList = dataBaseHelper.getConsultas(string);
                                    dataBaseHelper.close();
                                    general[indice][5] = texto.getText().toString().trim();
                                    pasar(conexion, general);
                                    error.setText("");
                                    //error.setText("No existe la placa!");
                                    encuentraPlaca = "false";
                                }
                            } else {
                                if (general[indice][4].trim().equals("CEDULA")) {
                                    String cedula = texto.getText().toString().replaceAll("\n", "");


                                    try {
                                        Cedula = cedula.substring(47, 58);
                                        nombre1 = cedula.substring(95, 116);
                                        nombre2 = cedula.substring(117, 140);
                                        apellido1 = cedula.substring(58, 71);
                                        apellido2 = cedula.substring(72, 94);
                                        if (Cedula.substring(1, 2).equals("00")) {
                                            Cedula = cedula.substring(50, 58);
                                        }


                                        if (general[indice + 1][4].trim().equals("NOMBRES")) {
                                            general[indice + 1][5] = nombre1 + " " + nombre2 + " " + apellido1 + " " + apellido2;

                                        }

                                        texto.setText(Cedula);
                                        general[indice][5] = texto.getText().toString().trim();
                                        pasar(conexion, general);
                                        error.setText("");
                                    } catch (Exception e) {
                                        general[indice][5] = texto.getText().toString().trim();
                                        pasar(conexion, general);
                                        error.setText("");
                                        e.printStackTrace();
                                    }
                                } else {

                                    if (general[indice][4].trim().equals("NOMBRES")) {
                                        String cedula = texto.getText().toString().replaceAll("\n", "");


                                        try {
                                            Cedula = cedula.substring(47, 58);
                                            nombre1 = cedula.substring(95, 116);
                                            nombre2 = cedula.substring(117, 140);
                                            apellido1 = cedula.substring(58, 71);
                                            apellido2 = cedula.substring(72, 94);
                                            if (Cedula.substring(1, 3).equals("00")) {
                                                Cedula = cedula.substring(50, 58);
                                            }

                                            if (general[indice + 1][4].trim().equals("CEDULA")) {
                                                general[indice + 1][5] = Cedula;
                                            }

                                            for (int i = 0; i < general.length; i++) {
                                                if (general[i][4].trim().equals("NIT")) {
                                                    general[i][5] = Cedula;
                                                }
                                            }

                                            texto.setText(remove(nombre1) + " " + remove(nombre2) + " " + remove(apellido1) + " " + remove(apellido2));
                                            general[indice][5] = remove(nombre1) + " " + remove(nombre2) + " " + remove(apellido1) + " " + remove(apellido2);
                                            pasar(conexion, general);
                                            error.setText("");
                                        } catch (Exception e) {
                                            general[indice][5] = texto.getText().toString().trim();
                                            pasar(conexion, general);
                                            error.setText("");
                                            e.printStackTrace();
                                        }
                                    } else {


                                        if (general[indice][4].trim().equals("KM")) {

                                            String url = "http://" + ip + "/consultarGeneral.php";

                                            List<NameValuePair> params = new ArrayList<NameValuePair>();

                                            params.add(new BasicNameValuePair("sParametro", "km_anterior"));
                                            params.add(new BasicNameValuePair("sPlaca", cod_placa));

                                            String resultServer = getHttpPost(url, params);
                                            System.out.println("---------------------------------resultserver----------------");
                                            System.out.println(resultServer);
                                            try {

                                                JSONArray jArray = new JSONArray(resultServer);
                                                ArrayList<String> array = new ArrayList<String>();
                                                for (int i = 0; i < jArray.length(); i++) {
                                                    JSONObject json = jArray.getJSONObject(i);
                                                    array.add(json.getString("km_anterior"));
                                                    km_anterior = array.get(0);
                                                }

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                            if (km_anterior.trim().equals("null"))
                                                km_anterior = "1";

                                            if (Integer.parseInt(km_anterior.toString()) < Integer.parseInt(texto.getText().toString())) {

                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                try {

                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }
                                                //ArrayList arrayList = dataBaseHelper.getConsultas(string);
                                                dataBaseHelper.close();
                                                general[indice][5] = texto.getText().toString().trim();
                                                pasar(conexion, general);
                                                error.setText("");

                                            } else {
                                                //informativo.setTextColor(Color.BLUE);
                                                error.setText("Anterior : " + km_anterior + ", debe ser mayor!");


                                            }
                                        } else {

                                            if (general[indice][4].trim().equals("NIT")) {
                                                String url = "http://" + ip + "/consultarGeneral.php";

                                                List<NameValuePair> params = new ArrayList<NameValuePair>();

                                                params.add(new BasicNameValuePair("sParametro", "nit"));
                                                params.add(new BasicNameValuePair("sNit", num_nit));

                                                String resultServer = getHttpPost(url, params);
                                                System.out.println("---------------------------------resultserver----------------");
                                                System.out.println(resultServer);
                                                String nit = "";
                                                try {

                                                    JSONArray jArray = new JSONArray(resultServer);
                                                    ArrayList<String> array = new ArrayList<String>();
                                                    for (int i = 0; i < jArray.length(); i++) {
                                                        JSONObject json = jArray.getJSONObject(i);
                                                        array.add(json.getString("nit"));
                                                        nit = array.get(0);
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                if (num_nit.trim().equals(nit)) {
                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                    try {

                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    //ArrayList arrayList = dataBaseHelper.getConsultas(string);
                                                    dataBaseHelper.close();
                                                    general[indice][5] = texto.getText().toString().trim();
                                                    pasar(conexion, general);
                                                    error.setText("");
                                                    encuentraNit = "true";
                                                } else {
                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                    try {

                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    //ArrayList arrayList = dataBaseHelper.getConsultas(string);
                                                    dataBaseHelper.close();
                                                    encuentraNit = "false";
                                                    error.setText("");
                                                    //error.setText("No existe el nit!");
                                                    general[indice][5] = texto.getText().toString().trim();
                                                    //pasar(conexion, general);

                                                    Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarCliente.class);
                                                    Bundle bundle2 = new Bundle();
                                                    bundle2.putString("conexion", conexion);
                                                    bundle2.putString("ip", ip);
                                                    bundle2.putString("atributo", texto.getText().toString().trim());
                                                    bundle2.putString("empresa", cod_empresa);
                                                    bundle2.putString("sucursal", sucursal);
                                                    bundle2.putString("parametro", parametro);
                                                    intent.putExtras(bundle2);
                                                    RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                                                    error.setText("");

                                                }
                                            } else {
                                                if (general[indice][4].trim().equals("PLACA")) {
                                                    String url = "http://" + ip + "/consultarGeneral.php";

                                                    List<NameValuePair> params = new ArrayList<NameValuePair>();

                                                    params.add(new BasicNameValuePair("sParametro", "PLACA"));
                                                    params.add(new BasicNameValuePair("sPlaca", cod_placa));

                                                    String resultServer = getHttpPost(url, params);
                                                    System.out.println("---------------------------------resultserver----------------");
                                                    System.out.println(resultServer);
                                                    String nombre = "";
                                                    try {

                                                        JSONArray jArray = new JSONArray(resultServer);
                                                        ArrayList<String> array = new ArrayList<String>();
                                                        for (int i = 0; i < jArray.length(); i++) {
                                                            JSONObject json = jArray.getJSONObject(i);
                                                            array.add(json.getString("nombre"));
                                                            sInformativo = sInformativo + "\n" + array.get(0);
                                                        }

                                                    } catch (JSONException e) {

                                                        error.setText("");
                                                        error.setText("No existe la placa!");
                                                        //informativo.setTextColor(Color.BLUE);
                                                        e.printStackTrace();
                                                    }




                                                    if (sInformativo.equals("null") || sInformativo.equals("")) {

                                                        error.setText("No existe la placa!");
                                                    } else {
                                                        error.setText(sInformativo);
                                                        //informativo.setTextColor(Color.BLUE);
                                                        general[indice][5] = texto.getText().toString().trim();
                                                        pasar(conexion, general);
                                                        error.setText("");
                                                    }
                                                } else {
                                                    if (general[indice][4].trim().equals("RAZON")) {
                                                        final String[] grid2 = {"CLIENTE",
                                                                "CLIENTE NUEVO"
                                                                ,"RECOMENDADO"
                                                                ,"SOAT"
                                                                ,"RTM"
                                                                ,"CALL-CENTER"
                                                                ,"PREVIAJE"
                                                                };

                                                        try {

                                                            general[indice][5] = texto.getText().toString().trim();
                                                            if (grid2[Integer.parseInt(general[indice][5]) - 1].equals("")){
                                                                Toast.makeText(RecepcionUsuariosUbicaciones.this, "No existe la razon!", Toast.LENGTH_SHORT).show();
                                                                general[indice][5] = "";
                                                                error.setText("");
                                                            }else{
                                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                try {

                                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                } catch (SQLException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                dataBaseHelper.close();
                                                                error.setText(grid2[Integer.parseInt(general[indice][5]) - 1]);
                                                                pasar(conexion, general);
                                                                descripcion = grid2[Integer.parseInt(general[indice][5]) - 1];
                                                                if (razon.equals("61") || razon.equals("62")){
                                                                    eventoRazon = "G";
                                                                }
                                                                razon = general[indice][5];

                                                            }

                                                        }catch (Throwable e){
                                                            e.printStackTrace();
                                                            general[indice][5] = "";
                                                            error.setText("");
                                                            Toast.makeText(RecepcionUsuariosUbicaciones.this, "No existe la razon!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        if (general[indice][4].trim().equals("PROMESA")) {
                                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                            try {

                                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            } catch (SQLException e) {
                                                                e.printStackTrace();
                                                            }
                                                            dataBaseHelper.close();
                                                            promesa = texto.getText().toString().trim();
                                                            general[indice][5] = texto.getText().toString().trim();
                                                            pasar(conexion, general);
                                                            error.setText("");
                                                        } else {
                                                            if (general[indice][4].trim().equals("SOAT")) {
                                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                try {

                                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                } catch (SQLException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                dataBaseHelper.close();
                                                                soat = texto.getText().toString().trim();
                                                                general[indice][5] = texto.getText().toString().trim();
                                                                pasar(conexion, general);
                                                                error.setText("");
                                                            } else {
                                                                if (general[indice][4].trim().equals("TECNICOMECANICA")) {
                                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                    try {

                                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    } catch (SQLException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    dataBaseHelper.close();
                                                                    tecno = texto.getText().toString().trim();
                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                    pasar(conexion, general);
                                                                    error.setText("");
                                                                } else {
                                                                    if (general[indice][4].trim().equals("SOLICITUD")) {
                                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                        try {

                                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        } catch (SQLException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        dataBaseHelper.close();
                                                                        solicitud = texto.getText().toString().trim();
                                                                        general[indice][5] = texto.getText().toString().trim();
                                                                        pasar(conexion, general);
                                                                        error.setText("");
                                                                    } else {
                                                                        if (general[indice][4].trim().equals("FECHA")) {
                                                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                            try {

                                                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            } catch (SQLException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            dataBaseHelper.close();
                                                                            fecha = texto.getText().toString().trim();
                                                                            general[indice][5] = texto.getText().toString().trim();
                                                                            pasar(conexion, general);
                                                                            error.setText("");
                                                                        } else {
                                                                            if (general[indice][4].trim().equals("USUARIO")) {
                                                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                try {

                                                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (SQLException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                                dataBaseHelper.close();
                                                                                usuario = texto.getText().toString().trim();
                                                                                general[indice][5] = texto.getText().toString().trim();
                                                                                pasar(conexion, general);
                                                                                error.setText("");
                                                                            } else {
                                                                                if (general[indice][4].trim().equals("TIEMPO")) {
                                                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                    try {

                                                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                    } catch (IOException e) {
                                                                                        e.printStackTrace();
                                                                                    } catch (SQLException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    dataBaseHelper.close();
                                                                                    tiempo = texto.getText().toString().trim();
                                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                                    pasar(conexion, general);
                                                                                    error.setText("");
                                                                                } else {
                                                                                    if (general[indice][4].trim().equals("HORA FIN")) {
                                                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                        try {

                                                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                        } catch (IOException e) {
                                                                                            e.printStackTrace();
                                                                                        } catch (SQLException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                        dataBaseHelper.close();
                                                                                        horaFin = texto.getText().toString().trim();
                                                                                        general[indice][5] = texto.getText().toString().trim();
                                                                                        pasar(conexion, general);
                                                                                        error.setText("");
                                                                                    } else {
                                                                                        if (general[indice][4].trim().equals("HORA INICIO")) {
                                                                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                            try {

                                                                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                            } catch (IOException e) {
                                                                                                e.printStackTrace();
                                                                                            } catch (SQLException e) {
                                                                                                e.printStackTrace();
                                                                                            }
                                                                                            dataBaseHelper.close();
                                                                                            horaInicio = texto.getText().toString().trim();
                                                                                            general[indice][5] = texto.getText().toString().trim();
                                                                                            pasar(conexion, general);
                                                                                            error.setText("");
                                                                                        } else {
                                                                                            if (general[indice][4].trim().equals("PRODUCTOS")) {
                                                                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                try {

                                                                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                } catch (IOException e) {
                                                                                                    e.printStackTrace();
                                                                                                } catch (SQLException e) {
                                                                                                    e.printStackTrace();
                                                                                                }
                                                                                                dataBaseHelper.close();
                                                                                                productText = texto.getText().toString().trim();
                                                                                                general[indice][5] = texto.getText().toString().trim();
                                                                                                pasar(conexion, general);
                                                                                                error.setText("");
                                                                                            } else {
                                                                                                if (general[indice][4].trim().equals("PAGO")) {
                                                                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                    try {

                                                                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                    } catch (IOException e) {
                                                                                                        e.printStackTrace();
                                                                                                    } catch (SQLException e) {
                                                                                                        e.printStackTrace();
                                                                                                    }
                                                                                                    dataBaseHelper.close();
                                                                                                    pago = texto.getText().toString().trim();
                                                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                                                    pasar(conexion, general);
                                                                                                    error.setText("");
                                                                                                } else {
                                                                                                    if (general[indice][4].trim().equals("MODELO")) {
                                                                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                        try {

                                                                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                        } catch (IOException e) {
                                                                                                            e.printStackTrace();
                                                                                                        } catch (SQLException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }
                                                                                                        dataBaseHelper.close();
                                                                                                        modelo = texto.getText().toString().trim();
                                                                                                        general[indice][5] = texto.getText().toString().trim();
                                                                                                        pasar(conexion, general);
                                                                                                        error.setText("");

                                                                                                    } else {
                                                                                                        if (general[indice][4].trim().equals("UBICACION")) {
                                                                                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                            try {

                                                                                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                            } catch (IOException e) {
                                                                                                                e.printStackTrace();
                                                                                                            } catch (SQLException e) {
                                                                                                                e.printStackTrace();
                                                                                                            }
                                                                                                            dataBaseHelper.close();
                                                                                                            cod_ubicacion = texto.getText().toString().trim();
                                                                                                            general[indice][5] = texto.getText().toString().trim();
                                                                                                            pasar(conexion, general);
                                                                                                            error.setText("");

                                                                                                        } else {
                                                                                                            if (general[indice][4].trim().equals("TIPO VEHICULO")) {
                                                                                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                                try {

                                                                                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                                } catch (IOException e) {
                                                                                                                    e.printStackTrace();
                                                                                                                } catch (SQLException e) {
                                                                                                                    e.printStackTrace();
                                                                                                                }
                                                                                                                dataBaseHelper.close();
                                                                                                                tip_vehiculo = texto.getText().toString().trim();
                                                                                                                general[indice][5] = texto.getText().toString().trim();
                                                                                                                pasar(conexion, general);
                                                                                                                error.setText("");

                                                                                                            } else {

                                                                                                                if (general[indice][4].trim().equals("TECNICO")) {

                                                                                            /*String hayTecnico = "";
                                                                                            String url = "http://" + ip + "/consultarGeneral.php";

                                                                                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                                                            params.add(new BasicNameValuePair("sTecnico", texto1.getText().toString().trim()));
                                                                                            params.add(new BasicNameValuePair("sParametro", "tecnico"));


                                                                                            String resultServer = getHttpPost(url, params);
                                                                                            System.out.println(resultServer);
                                                                                            try {

                                                                                                JSONArray jArray = new JSONArray(resultServer);
                                                                                                ArrayList<String> array = new ArrayList<String>();
                                                                                                for (int i = 0; i < jArray.length(); i++) {
                                                                                                    JSONObject json = jArray.getJSONObject(i);
                                                                                                    hayTecnico = json.getString("cod_tecnico");
                                                                                                }*/

                                                                                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                                    try {

                                                                                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                                    } catch (IOException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    } catch (SQLException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    }
                                                                                                                    dataBaseHelper.close();
                                                                                                                    tecnico = texto.getText().toString().trim();
                                                                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                                                                    pasar(conexion, general);
                                                                                                                    error.setText("");

                                                                                            /*} catch (JSONException e) {
                                                                                                error.setText("No existe el tecnico!");
                                                                                                e.printStackTrace();
                                                                                            }*/


                                                                                                                } else {
                                                                                                                    if (general[indice][9].trim().equals("E")) {
                                                                                                                        if (isNumeric(atributo)) {
                                                                                                                            if (Integer.parseInt(atributo) >= Double.parseDouble(general[indice][10]) && Integer.parseInt(atributo) <= Double.parseDouble(general[indice][11])) {
                                                                                                                                if (general[indice][4].trim().equals("ROMBO")) {

                                                                                                                                    String url = "";
                                                                                                                                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                                                                                                    boolean ocupado = false;

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

                                                                                                                                    params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa.trim()));
                                                                                                                                    params.add(new BasicNameValuePair("sCodSucursal", bodega.trim()));
                                                                                                                                    url = "http://" + ip + "/consultarRecurso.php";
                                                                                                                                    params.add(new BasicNameValuePair("sParametro", "R"));
                                                                                                                                    String resultServer = getHttpPost(url, params);
                                                                                                                                    System.out.println("---------------------------------");
                                                                                                                                    System.out.println(resultServer);
                                                                                                                                    try {
                                                                                                                                        JSONArray jArray = new JSONArray(resultServer);
                                                                                                                                        ArrayList<String> array = new ArrayList<String>();
                                                                                                                                        for (int i = 0; i < jArray.length(); i++) {
                                                                                                                                            JSONObject json = jArray.getJSONObject(i);
                                                                                                                                            array.add((json.getString("rombo")));
                                                                                                                                            if (texto.getText().toString().equals(array.get(i).trim())) {
                                                                                                                                                ocupado = true;
                                                                                                                                                break;
                                                                                                                                            } else {
                                                                                                                                                ocupado = false;
                                                                                                                                            }
                                                                                                                                        }


                                                                                                                                    } catch (JSONException e) {
                                                                                                                                        e.printStackTrace();
                                                                                                                                    }


                                                                                                                                    if (ocupado) {
                                                                                                                                        error.setText("El rombo no esta disponible");
                                                                                                                                    } else {
                                                                                                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                                                        try {

                                                                                                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                                                        } catch (IOException e) {
                                                                                                                                            e.printStackTrace();
                                                                                                                                        } catch (SQLException e) {
                                                                                                                                            e.printStackTrace();
                                                                                                                                        }
                                                                                                                                        dataBaseHelper.close();
                                                                                                                                        general[indice][5] = texto.getText().toString().trim();
                                                                                                                                        pasar(conexion, general);
                                                                                                                                        error.setText("");
                                                                                                                                    }
                                                                                                                                } else {
                                                                                                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                                                    try {

                                                                                                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                                                    } catch (IOException e) {
                                                                                                                                        e.printStackTrace();
                                                                                                                                    } catch (SQLException e) {
                                                                                                                                        e.printStackTrace();
                                                                                                                                    }
                                                                                                                                    dataBaseHelper.close();
                                                                                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                                                                                    pasar(conexion, general);
                                                                                                                                    error.setText("");
                                                                                                                                }

                                                                                                                            } else {
                                                                                                                                error.setText("No esta en el rango!");
                                                                                                                                texto.setText("");
                                                                                                                                num_rombo = "";
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            error.setText("No es numero!");
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        if (general[indice][9].trim().equals("C")) {
                                                                                                                            if ((texto.getText().toString().trim().length() >= Double.parseDouble(general[indice][10])) && (texto.getText().toString().trim().length() <= Double.parseDouble(general[indice][11]))) {
                                                                                                                                general[indice][5] = texto.getText().toString().trim();
                                                                                                                                pasar(conexion, general);
                                                                                                                                error.setText("");

                                                                                                                            } else {
                                                                                                                                if (Double.parseDouble(general[indice][10]) == Double.parseDouble(general[indice][11]) && texto.getText().toString().trim().length() == Double.parseDouble(general[indice][10])) {
                                                                                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                                                                                    pasar(conexion, general);
                                                                                                                                    error.setText("");
                                                                                                                                } else {
                                                                                                                                    error.setText("No esta en el rango!");
                                                                                                                                }
                                                                                                                                error.setText("No esta en el rango!");
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            if (general[indice][9].trim().equals("F")) {
                                                                                                                                if (general[indice][18].trim().equals("PROMESA_ENTREGA")) {

                                                                                                                                    date = getHora();
                                                                                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                                                                                    pasar(conexion, general);
                                                                                                                                    error.setText("");
                                                                                                                                } else {
                                                                                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                                                                                    pasar(conexion, general);
                                                                                                                                    error.setText("");
                                                                                                                                }
                                                                                                                            } else {
                                                                                                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                                                try {

                                                                                                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                                                } catch (IOException e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                } catch (SQLException e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                }
                                                                                                                                dataBaseHelper.close();
                                                                                                                            }
                                                                                                                        }
                                                                                                                    }


                                                                                                                    // params.add(new BasicNameValuePair("sNumOrden", num_orden));


                                /*if (resultado.get(4).equals("NIT")) {
                                    String cedula = texto.getText().toString().replaceAll("\n", "");
                                    String cedulafinal = "";


                                    String url3 = "http://" + finalIp + ":8080/update2.php";
                                    String resultServer3;
                                    try {

                                        Cedula = cedula.substring(47, 58);
                                        nombre1 = cedula.substring(95, 116);
                                        nombre2 = cedula.substring(117, 140);
                                        apellido1 = cedula.substring(58, 71);
                                        apellido2 = cedula.substring(72, 94);
                                        if (Cedula.substring(1, 2).equals("00")) {
                                            Cedula = cedula.substring(50, 58);
                                        }


                                        List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                                        params3.add(new BasicNameValuePair("sNit", Cedula));
                                        params3.add(new BasicNameValuePair("sApellido1", apellido1));
                                        params3.add(new BasicNameValuePair("sApellido2", apellido2));
                                        params3.add(new BasicNameValuePair("sNombre1", nombre1));
                                        params3.add(new BasicNameValuePair("sNombre2", nombre2));
                                        resultServer3 = getHttpPost(url3, params3);
                                        System.out.println(resultServer3);

                                        String strStatusID = "0";
                                        String strError = "Unknow Status! 3";

                                        JSONObject c3;
                                        try {
                                            c3 = new JSONObject(resultServer3);
                                            strStatusID = c3.getString("StatusID");
                                            strError = c3.getString("Error");
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        if (strStatusID.equals("0")) {
                                            ad.setMessage(strError);
                                            ad.show();
                                            return false;
                                        } else {
                                            Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                            pasar(conexion, general);
                                        }
                                    } catch (Exception e) {
                                        url3 = "http://" + finalIp + "/update.php";
                                        List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                                        params3.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                        params3.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                        params3.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                        params3.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                        params3.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                        params3.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                        params3.add(new BasicNameValuePair("sNumOrden", num_orden));
                                        resultServer3 = getHttpPost(url3, params3);
                                        String strStatusID = "0";
                                        String strError = "Unknow Status! 3";

                                        JSONObject c3;
                                        try {
                                            c3 = new JSONObject(resultServer3);
                                            strStatusID = c3.getString("StatusID");
                                            strError = c3.getString("Error");
                                        } catch (JSONException ex) {
                                            // TODO Auto-generated catch block
                                            ex.printStackTrace();
                                        }
                                        if (strStatusID.equals("0")) {
                                            ad.setMessage(strError);
                                            ad.show();
                                            return false;
                                        } else {
                                            Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                            pasar(conexion, general);
                                        }
                                    }


                                } else {
                                    if (resultado.get(4).equals("MATRICULA")) {
                                        String matricula = texto.getText().toString().replaceAll("\n", "");
                                        matricula = matricula.replaceAll(" ", "");
                                        System.out.println("LONGITUD : " + matricula.length());
                                        System.out.println(texto.getText().toString());
                                        String matriculafinal = "";


                                        try {
                                            placa = matricula.substring(55, 61);
                                            vin = matricula.substring(61, 78);

                                            texto.setText("");

                                            Intent intent = new Intent((Context) Recepcion.this, (Class) MostrarDatosMatricula.class);
                                            Bundle bundle2 = new Bundle();
                                            bundle2.putString("placa", placa);
                                            bundle2.putString("vin", vin);
                                            intent.putExtras(bundle2);
                                            Recepcion.this.startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();

                                            ad.setTitle("Error! ");
                                            ad.setIcon(android.R.drawable.btn_star_big_on);
                                            ad.setPositiveButton("Close", null);
                                            String url5 = "http://" + finalIp + "/update.php";

                                            List<NameValuePair> params5 = new ArrayList<NameValuePair>();


                                            params5.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                            params5.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                            params5.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                            params5.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                            params5.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                            params5.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                            params5.add(new BasicNameValuePair("sNumOrden", num_orden));
                                            String resultServer = getHttpPost(url, params);
                                            System.out.println(resultServer);

                                            String strStatusID = "0";
                                            String strError = "Unknow Status!";

                                            JSONObject c;
                                            try {
                                                Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                                pasar(conexion, general);
                                            }catch(Exception e1){
                                                Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                    } else {
                                        String resultServer = getHttpPost(url, params);
                                        System.out.println(resultServer);


                                        JSONObject c;
                                        try {
                                            Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                            pasar(conexion, general);
                                        }catch(Exception e){
                                            Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }*/


                                                                                                                    //TALL_ENCABEZA_ORDEN
                                                /*)}
                                        }


                                    }
                                }
                            }*/


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
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    // }


                                }
                            }


                        }
                        return false;
                    }

                });
        }
    }

    public void ver (View v){
        Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarDatosNit.class);
        Bundle bundle2 = new Bundle();
        bundle2.putString("cedula", Cedula);
        bundle2.putString("nombre1", nombre1);
        bundle2.putString("nombre2", nombre2);
        bundle2.putString("apellido1",apellido1);
        bundle2.putString("apellido2", apellido2);
        intent.putExtras(bundle2);
        RecepcionUsuariosUbicaciones.this.startActivity(intent);
    }

    public void pasar(String conexion, final String[][] general){


        if (conexion.equals("local")) {
            final DataBaseHelper dataBaseHelper = new DataBaseHelper((Context) this);
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
            final String[][] general2;
            general2 = dataBaseHelper.consultarProceso("123", "METRO", sucursal, "01", 1);

            if (indice == general2.length - 1) {
                indice = 0;
                actualizar(general, indice, conexion);
                ok(empresa,sucursal,proceso,user,"A",general);
            } else {
                indice++;
                actualizar(general2, indice, conexion);
            }
        }else{
            if (indice == general.length - 1) {
                indice = 0;
                actualizar(general, indice, conexion);
                ok(empresa,sucursal,proceso,user,"A",general);
            } else {
                indice++;
                actualizar(general, indice, conexion);
            }
        }
    }

    public void ok(String cod_empresa, String cod_sucursal, String cod_proceso
            , String cod_usuario,
                   String ind_estado, String[][] general){



        //atributos = new ArrayList();


        atributos = new ArrayList<>();
        columnas = new ArrayList<>();
        datos = new ArrayList<>();

        if (cod_proceso.trim().equals("11")){
            atributos.add("ROMBO" + " :");
            atributos.add(num_rombo);

            for (int i = 0;i<(generalAux.length-general.length);i++) {
                columnas.add(generalAux[general.length + i][18]);
            }

            for (int i = 0;i<(generalAux.length-general.length);i++) {
                datos.add(generalAux[general.length + i][5]);
            }

        }

        for (int i = 0;i<general.length;i++) {
            if (general[i][4].trim().equals("SOAT")){
                soat = general[i][5].trim();
            }else{
                if (general[i][4].trim().equals("TECNICOMECANICA")){
                    tecno = general[i][5].trim();
                }
            }
            atributos.add(general[i][4].trim() + " :");
            atributos.add(general[i][5].trim());
        //}
            if(general[i][16].trim().equals("S")) {
                if (general[i][18].trim().equals("PROMESA_ENTREGA")) {
                    columnas.add(general[i][18].trim());
                    datos.add("GETDATE()");
                }else{
                    columnas.add(general[i][18].trim());
                    datos.add(general[i][5].trim());
                }
            }
        }


        for (int i = 0;i<(generalAux.length-general.length);i++) {
            columnas.add(generalAux[general.length + i][18].trim());
            System.out.println(generalAux[general.length + i][18] + "COLUMNA AUXXXXXX");
        }

        for (int i = 0;i<(generalAux.length-general.length);i++) {
            datos.add(generalAux[general.length + i][5].trim());
            System.out.println(generalAux[general.length + i][5] + "DATO AUXXXXXX");
        }

        /*String url1 = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params1 = new ArrayList<NameValuePair>();
        params1.add(new BasicNameValuePair("sParametro", "consecutivo"));


        String resultServer1  = getHttpPost(url1,params1);
        System.out.println("---------------------------------resultserver----------------");
        try {

            JSONArray jArray = new JSONArray(resultServer1);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("siguiente"));
                datos.add(array.get(0));
            }

        }catch (JSONException e ){
            e.printStackTrace();
        }*/


        //TALL_ENCABEZA_ORDEN_DATOS_POR_DEFECTO
        //columnas.add("NUMERO");
        //datos.add("251");

        String bodega = "";

        if (cod_sucursal.trim().equals("51")){
            bodega = "1";
        }else{
            if (cod_sucursal.trim().equals("33")){
                bodega = "2";
            }else{
                if (cod_sucursal.trim().equals("10")){
                    bodega = "3";
                }else{
                    if (cod_sucursal.trim().equals("99")){
                        bodega = "11";
                    }
                }
            }
        }


        if (cod_proceso.trim().equals("1")) {


            columnas.add("KILOMETRAJE");
            datos.add(kilometraje);

            columnas.add("KM_ANTERIOR");
            datos.add(km_anterior);

            columnas.add("BODEGA");
            datos.add(bodega);

            columnas.add("SERIE");
            datos.add(cod_placa);

            columnas.add("ROMBO_USADO");
            datos.add(num_rombo);

            columnas.add("ROMBO");
            datos.add(num_rombo);



       }else{
            if (cod_proceso.trim().equals("2")){
                columnas.add("nit_real");
                datos.add(num_nit);

            }
        }


        /*atributos.add("ROMBO :");
        atributos.add(num_rombo);
        atributos.add("PLACA : ");
        atributos.add(cod_placa);
        atributos.add("UBIC : ");
        atributos.add(cod_ubicacion);*/
        String faltanDatos = "false";

        for (int i =0;i<atributos.size();i=i+2){
            if (atributos.get(i+1).equals("")){
                faltanDatos ="true";
                break;
            }
        }

        if (proceso.equals("11")){

            Bundle bundle = new Bundle();
            Intent intent = new Intent((Context) this, (Class) GuardarValidacion.class);
            bundle.putString("cod_empresa", cod_empresa);
            bundle.putString("cod_sucursal", cod_sucursal);
            bundle.putString("cod_proceso", cod_proceso);
            bundle.putString("cod_placa", cod_placa);
            bundle.putString("num_rombo", num_rombo);
            bundle.putString("cod_usuario", cod_usuario);
            bundle.putString("cod_ubicacion", cod_ubicacion);
            bundle.putString("fec_proceso", fec_proceso);
            bundle.putString("num_nit", num_nit);
            bundle.putString("ind_estado", ind_estado);
            bundle.putStringArrayList("atributos", atributos);
            bundle.putStringArrayList("columnas", columnas);
            bundle.putStringArrayList("datos", datos);
            bundle.putString("ip", ip);
            bundle.putString("km", km);
            bundle.putString("faltanDatos", faltanDatos);
            intent.putExtras(bundle);
            this.startActivityForResult(intent, request_code);
        }else {


            Bundle bundle = new Bundle();
            Intent intent = new Intent((Context) this, (Class) GuardarUsuariosUbicaciones.class);
            bundle.putString("eventoRazon",eventoRazon);
            bundle.putString("eventoCita",eventoCita);
            bundle.putString("tip_vehiculo",tip_vehiculo);
            bundle.putString("descripcionModelo",descripcionModelo);
            bundle.putString("km",kilometraje);
            bundle.putString("encuentraNit",encuentraNit);
            bundle.putString("encuentraPlaca",encuentraPlaca);
            bundle.putString("solicitud",solicitud);
            bundle.putString("fecha",fecha);
            bundle.putString("usuario",usuario);
            bundle.putString("tiempo",tiempo);
            bundle.putString("horaInicio",horaInicio);
            bundle.putString("horaFin",horaFin);
            bundle.putSerializable("productos",products);
            bundle.putString("modelo",modelo);
            bundle.putString("promesa",promesa);
            bundle.putString("pago",pago);
            bundle.putString("tecno", tecno);
            bundle.putString("soat", soat);
            bundle.putString("cod_empresa", cod_empresa);
            bundle.putString("cod_sucursal", cod_sucursal);
            bundle.putString("cod_proceso", cod_proceso);
            bundle.putString("cod_placa", cod_placa);
            bundle.putString("num_rombo", num_rombo);
            bundle.putString("cod_usuario", cod_usuario);
            bundle.putString("cod_ubicacion", cod_ubicacion);
            bundle.putString("fec_proceso", fec_proceso);
            bundle.putString("num_nit", num_nit);
            bundle.putString("ind_estado", ind_estado);
            bundle.putStringArrayList("atributos", atributos);
            bundle.putStringArrayList("columnas", columnas);
            bundle.putStringArrayList("datos", datos);
            bundle.putString("ip", ip);
            bundle.putString("faltanDatos", faltanDatos);
            bundle.putString("nombre_tabla", nombreTabla);
            bundle.putString("razon", razon);
            intent.putExtras(bundle);
            this.startActivityForResult(intent, request_code);
        }
        //finish();
        /*for (int i = 0; i < general.length; i++) {
                general[i][5] = "";
        }*/

    }

    public void enter(View v){


        final TextView error = (TextView) findViewById(R.id.textView_error);
        //final TextView informativo = (TextView) findViewById(R.id.textView_informativo);
        //.setTextColor(Color.rgb(21,130,125));
        error.setText("");
        //informativo.setText("");

        final ArrayList resultado = new ArrayList();
        final TextView atributo = (TextView) findViewById(R.id.textView_atributo);
        final ImageView imagen = (ImageView) findViewById(R.id.imageView);

        final TextView atributo1 = (TextView) findViewById(R.id.textView_atributo);
        atributo1.setBackgroundColor(Color.WHITE);
        atributo1.setTextColor(Color.BLACK);

        Button buscar = (Button) findViewById(R.id.button_buscar);
        Button anadir = (Button) findViewById(R.id.button_anadir);




        //for(int j = 0;j<general[0].length;j++){
        //    resultado.add(general[indice][j]);
        //}


       /* String url = "http://" + ip + "/consultarGeneral.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sCodUser", resultado.get(2).toString().trim()));
        params.add(new BasicNameValuePair("sCodEmpresa", resultado.get(0).toString().trim()));
        params.add(new BasicNameValuePair("sCodAtributo", resultado.get(4).toString().trim()));
        params.add(new BasicNameValuePair("sCodSucursal",sucursal));
        params.add(new BasicNameValuePair("sParametro", "actualizar"));

        final String resultServer  = getHttpPost(url, params);
        System.out.println("-----------ACTUALIZAR------------");
        System.out.println("coduser - " + resultado.get(2).toString());
        System.out.println("codempresa - " + resultado.get(0).toString());
        System.out.println("codatributo + " + resultado.get(4).toString());
        System.out.println(resultServer);
        try {
            JSONArray jArray = new JSONArray(resultServer);
            ArrayList<String> array = new ArrayList<String>();
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add((json.getString("cod_valor")));
            }
            texto.setText(array.get(0));
        }catch (JSONException e){
            e.printStackTrace();
        }*/

        //texto.setText(general[indice][5].trim());


        atributo.setText(general[indice][4].trim());
        //atributo.setText(resultado.get(7) + ". " + resultado.get(4).toString().trim());

        if(general[indice][5].trim().equals("ROMBO")) imagen.setImageResource(R.drawable.rombo);
        if(general[indice][5].trim().equals("PLACA")) imagen.setImageResource(R.drawable.placa6);
        if(general[indice][5].trim().equals("UBICACION")) imagen.setImageResource(R.drawable.ubicacion);
        //if (resultado.get(13).equals("ROMBO.JPG")) imagen.setImageResource(R.drawable.rombo1);
        //if (resultado.get(13).equals("PLACA.JPG")) imagen.setImageResource(R.drawable.placa6);
        //if (resultado.get(13).equals("NIT.JPG")) imagen.setImageResource(R.drawable.cedula1);
        /*if (Integer.parseInt(resultado.get(0).toString()) > 0){
            int cont = 0;

        }*/

        mostrarImagen();

        texto.requestFocus();

        final String cod_empresa = general[indice][0];
        final String cod_sucursal =general[indice][1];
        final String cod_usuario = general[indice][2];
        final String[] cod_proceso = {general[indice][3]};
        final String[] cod_atributo = {general[indice][4]};
        final String cod_valor = general[indice][5];
        final String num_orden = general[indice][7];

        /*final String cod_empresa = resultado.get(0).toString();
        final String cod_sucursal = resultado.get(1).toString();
        final String cod_usuario = resultado.get(2).toString();
        final String cod_proceso = resultado.get(3).toString();
        final String cod_atributo = resultado.get(4).toString();
        final String cod_valor = resultado.get(5).toString();
        final String num_orden = resultado.get(7).toString();*/


        final String finalIp = ip;

        final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
            public boolean onDoubleTap(MotionEvent e) {
                if ((cod_atributo[0].trim().equals("ROMBO")
                        || cod_atributo[0].trim().equals("UBICACION") || cod_atributo[0].trim().equals("TECNICO")
                        || cod_atributo[0].trim().equals("NUMERO OT")) || cod_atributo[0].trim().equals("PROMESA")
                        || cod_atributo[0].trim().equals("RAZON2") || cod_atributo[0].trim().equals("RAZON")) {

                    Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarRombUbic.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("conexion", conexion);
                    bundle2.putString("ip", ip);
                    bundle2.putString("atributo", cod_atributo[0].trim());
                    bundle2.putString("empresa", cod_empresa);
                    bundle2.putString("sucursal", cod_sucursal);
                    intent.putExtras(bundle2);
                    RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                }else{
                    if (general[indice][4].trim().equals("SOAT") || general[indice][4].trim().equals("TECNICOMECANICA") ) {
                        Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) CalendarioSoat.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("parametro",general[indice][4].trim());
                        intent.putExtras(bundle2);
                        startActivityForResult(intent, request_code);
                    }//else{
                }
                       /* if (general[indice][9].toString().equals("F")){
                            Intent intent = new Intent((Context) Recepcion.this, (Class) Calendario.class);
                            Recepcion.this.startActivityForResult(intent,request_code);
                        }
                    }*/

                        /*String url2 = "http://" + ip + "/consultarGeneral.php";

                        List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                        params2.add(new BasicNameValuePair("sCodUser", resultado.get(2).toString()));
                        params2.add(new BasicNameValuePair("sCodEmpresa", resultado.get(0).toString()));
                        params2.add(new BasicNameValuePair("sCodAtributo", resultado.get(4).toString()));
                        params2.add(new BasicNameValuePair("sCodSucursal",sucursal));
                        params2.add(new BasicNameValuePair("sParametro", "actualizar"));


                        String resultServer2 = getHttpPost(url2, params2);
                        System.out.println("---------------------------------");
                        System.out.println(resultServer2);
                        ArrayList<String> array = new ArrayList<String>();
                        try {
                            JSONArray jArray = new JSONArray(resultServer2);

                            //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                array.add((json.getString("cod_valor")));
                            }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        if (array.size()>0) {
                            String url4 = "http://" + ip + "/updateGeneral.php";

                            List<NameValuePair> params4 = new ArrayList<NameValuePair>();


                            params4.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                            params4.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                            params4.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                            params4.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                            params4.add(new BasicNameValuePair("sParametro", "update3"));

                            String resultServer4 = getHttpPost(url4, params4);

                            JSONObject c4;
                            try {

                                Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                pasar(conexion, general);
                            }catch(Exception e1){
                                Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {





                            final AlertDialog.Builder ad = new AlertDialog.Builder(Recepcion.this);

                            ad.setTitle("Error! ");
                            ad.setIcon(android.R.drawable.btn_star_big_on);
                            ad.setPositiveButton("Close", null);
                            InputStream isr = null;
                            String url = "http://" + finalIp + "/update.php";

                            List<NameValuePair> params = new ArrayList<NameValuePair>();


                            params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                            params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                            params.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                            params.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                            params.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                            params.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                            params.add(new BasicNameValuePair("sNumOrden", num_orden));

                            */

                // params.add(new BasicNameValuePair("sNumOrden", num_orden));


                            /*if (resultado.get(4).equals("NIT")) {
                                String cedula = texto.getText().toString().replaceAll("\n", "");
                                String cedulafinal = "";

                                String url3 = "http://" + finalIp + ":8080/update2.php";
                                String resultServer3;
                                try {

                                    Cedula = cedula.substring(47, 58);
                                    nombre1 = cedula.substring(95, 116);
                                    nombre2 = cedula.substring(117, 140);
                                    apellido1 = cedula.substring(58, 71);
                                    apellido2 = cedula.substring(72, 94);
                                    if (Cedula.substring(1, 2).equals("00")) {
                                        Cedula = cedula.substring(50, 58);
                                    }


                                    List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                                    params3.add(new BasicNameValuePair("sNit", Cedula));
                                    params3.add(new BasicNameValuePair("sApellido1", apellido1));
                                    params3.add(new BasicNameValuePair("sApellido2", apellido2));
                                    params3.add(new BasicNameValuePair("sNombre1", nombre1));
                                    params3.add(new BasicNameValuePair("sNombre2", nombre2));
                                    resultServer3 = getHttpPost(url3, params3);
                                    System.out.println(resultServer3);

                                    String strStatusID = "0";
                                    String strError = "Unknow Status! 3";

                                    JSONObject c3;
                                    try {
                                        c3 = new JSONObject(resultServer3);
                                        strStatusID = c3.getString("StatusID");
                                        strError = c3.getString("Error");
                                    } catch (JSONException ex) {
                                        // TODO Auto-generated catch block
                                        ex.printStackTrace();
                                    }
                                    if (strStatusID.equals("0")) {
                                        ad.setMessage(strError);
                                        ad.show();
                                        return false;
                                    } else {
                                        Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                        pasar(conexion, general);
                                    }
                                } catch (Exception ec) {
                                    url3 = "http://" + finalIp + "/update.php";
                                    List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                                    params3.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                    params3.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                    params3.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                    params3.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                    params3.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                    params3.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                    params3.add(new BasicNameValuePair("sNumOrden", num_orden));
                                    resultServer3 = getHttpPost(url3, params3);
                                    String strStatusID = "0";
                                    String strError = "Unknow Status! 3";

                                    JSONObject c3;
                                    try {
                                        c3 = new JSONObject(resultServer3);
                                        strStatusID = c3.getString("StatusID");
                                        strError = c3.getString("Error");
                                    } catch (JSONException ex) {
                                        // TODO Auto-generated catch block
                                        ex.printStackTrace();
                                    }
                                    if (strStatusID.equals("0")) {
                                        ad.setMessage(strError);
                                        ad.show();
                                        return false;
                                    } else {
                                        Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                        pasar(conexion, general);
                                    }
                                }


                            } else {
                                if (resultado.get(4).equals("MATRICULA")) {
                                    String matricula = texto.getText().toString().replaceAll("\n", "");
                                    matricula = matricula.replaceAll(" ", "");
                                    System.out.println("LONGITUD : " + matricula.length());
                                    System.out.println(texto.getText().toString());
                                    String matriculafinal = "";


                                Character  numAux = cedula.charAt(50);
                                String num = numAux.toString();
                                while (num.charAt(0)>='0' && num.charAt(0)<='9'){
                                    cedulafinal = cedulafinal + num.charAt(0);
                                }

                                    try {
                                        placa = matricula.substring(55, 61);
                                        vin = matricula.substring(61, 78);

                                        texto.setText("");

                                        Intent intent = new Intent((Context) Recepcion.this, (Class) MostrarDatosMatricula.class);
                                        Bundle bundle2 = new Bundle();
                                        bundle2.putString("placa", placa);
                                        bundle2.putString("vin", vin);
                                        intent.putExtras(bundle2);
                                        Recepcion.this.startActivity(intent);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();

                                        ad.setTitle("Error! ");
                                        ad.setIcon(android.R.drawable.btn_star_big_on);
                                        ad.setPositiveButton("Close", null);
                                        String url5 = "http://" + finalIp + "/update.php";

                                        List<NameValuePair> params5 = new ArrayList<NameValuePair>();


                                        params5.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                        params5.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                        params5.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                        params5.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                        params5.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                        params5.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                        params5.add(new BasicNameValuePair("sNumOrden", num_orden));
                                        String resultServer = getHttpPost(url, params);
                                        System.out.println(resultServer);


                                        JSONObject c;
                                        try {
                                            Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                            pasar(conexion, general);
                                        }catch(Exception e1){
                                            Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                } else {
                                    String resultServer = getHttpPost(url, params);
                                    System.out.println(resultServer);

                                    String strStatusID = "0";
                                    String strError = "Unknow Status!";

                                    JSONObject c;
                                    try {

                                        Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();

                                        pasar(conexion, general);
                                    }catch(Exception e1){
                                        Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }*/


                            /*String file = "storage/sdcard0/METRO/" + nombre;
                            File mi_foto = new File(file);
                            String file2 = "storage/sdcard0/METRO/";
                            File mi_foto2 = new File(file2);
                            mi_foto.renameTo(mi_foto2);*/
                //else{
                //    Toast.makeText(getApplicationContext()," Este registro ya esta en la base de datos !" , Toast.LENGTH_SHORT).show();
                //}
                // }

                return true;
            }
        });

        if ( !(general[indice][18].trim().equals(general[indice][3].trim()))) {




            Button BotonBuscar = (Button) findViewById(R.id.button_buscar);
            BotonBuscar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    if (general[indice][4].trim().equals("NIT")) {

                        Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarCliente.class);
                        Bundle bundle2 = new Bundle();
                        bundle2.putString("conexion", conexion);
                        bundle2.putString("ip", ip);
                        bundle2.putString("atributo", texto.getText().toString().trim());
                        bundle2.putString("empresa", cod_empresa);
                        bundle2.putString("sucursal", sucursal);
                        bundle2.putString("parametro", parametro);
                        intent.putExtras(bundle2);
                        RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                        error.setText("");
                    }else{

                        if ((cod_atributo[0].trim().equals("ROMBO")
                                || cod_atributo[0].trim().equals("UBICACION") || cod_atributo[0].trim().equals("TECNICO")
                                || cod_atributo[0].trim().equals("NUMERO OT")) || cod_atributo[0].trim().equals("PROMESA")
                                || cod_atributo[0].trim().equals("RAZON2") || cod_atributo[0].trim().equals("RAZON")
                                || cod_atributo[0].trim().equals("TIPO VEHICULO")
                                || cod_atributo[0].trim().equals("PAGO")) {

                            Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarRombUbic.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("conexion", conexion);
                            bundle2.putString("ip", ip);
                            bundle2.putString("atributo", cod_atributo[0].trim());
                            bundle2.putString("empresa", cod_empresa);
                            bundle2.putString("sucursal", cod_sucursal);
                            intent.putExtras(bundle2);
                            RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                        }else{
                            if (general[indice][4].trim().equals("SOLICITUD")) {

                                Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) EditarSolicitud.class);
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("conexion", conexion);
                                bundle2.putString("ip", ip);
                                bundle2.putString("atributo", texto.getText().toString().trim());
                                bundle2.putString("empresa", cod_empresa);
                                bundle2.putString("sucursal", sucursal);
                                bundle2.putString("parametro", parametro);
                                intent.putExtras(bundle2);
                                RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                                error.setText("");
                            }else {
                                if (general[indice][4].trim().equals("PRODUCTOS")) {

                                    Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MercarPedido.class);
                                    Bundle bundle2 = new Bundle();
                                    bundle2.putString("ip", ip);
                                    bundle2.putString("empresa",num_nit);
                                    bundle2.putString("proyecto",cod_placa);
                                    intent.putExtras(bundle2);
                                    RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                                    error.setText("");
                                }else {
                                    if (general[indice][4].trim().equals("MODELO")) {
                                        Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarModelo.class);
                                        Bundle bundle2 = new Bundle();
                                        bundle2.putString("placa", cod_placa);
                                        bundle2.putString("ip", ip);
                                        intent.putExtras(bundle2);
                                        startActivityForResult(intent, request_code);
                                    } else {
                                        if (general[indice][4].trim().equals("FECHA")  || general[indice][4].trim().equals("TIEMPO") || general[indice][4].trim().equals("TECNICOMECANICA")) {
                                            Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) CalendarioSoat.class);
                                            Bundle bundle2 = new Bundle();
                                            bundle2.putString("parametro", general[indice][4].trim());
                                            intent.putExtras(bundle2);
                                            startActivityForResult(intent, request_code);

                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            });

            Button BotonAnadir = (Button) findViewById(R.id.button_anadir);
            BotonAnadir.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    proceso = general[indice][18].trim();
                    titulo = general[indice][4].trim();

                    cod_atributo[0] = general[indice][4].trim();

                    nombreTabla = generalAux[indice][19].trim();

                    System.out.println("TABLAAAAA " +  nombreTabla);

                    System.out.println(user + " " + pass + " " + proceso);
                    Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) RecepcionUsuariosUbicaciones.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("user", user);
                    bundle2.putString("pass", pass);
                    bundle2.putString("sucursal", sucursal);
                    bundle2.putString("empresa", empresa);
                    bundle2.putString("conexion", conexion);
                    bundle2.putString("sucursal", sucursal);
                    bundle2.putString("ip", ip);
                    bundle2.putString("titulo", titulo);
                    bundle2.putString("proceso", proceso);
                    bundle2.putString("nombreTabla",nombreTabla);
                    bundle2.putString("codAtributo",cod_atributo[0].trim());

                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
            });



        }

        texto.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });



                        /*if (texto.getText().toString().matches("") && (general[indice][5].equals("ROMBO") || general[indice][5].equals("UBICACION") || general[indice][5].equals("MECANICO"))) {
                            Intent intent = new Intent((Context) Recepcion.this, (Class) MostrarRombUbic.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putString("conexion", conexion);
                            bundle2.putString("ip", ip);
                            bundle2.putString("atributo", cod_atributo);
                            intent.putExtras(bundle2);
                            Recepcion.this.startActivityForResult(intent, request_code);
                            Toast.makeText(getApplicationContext(), resultado.get(4) + "  ATRIBUTO ", Toast.LENGTH_SHORT).show();

                        } else {*/
                            /*String url2 = "http://" + ip + "/consultarGeneral.php";

                            List<NameValuePair> params2 = new ArrayList<NameValuePair>();
                            params2.add(new BasicNameValuePair("sCodUser", general[indice][2].toString()));
                            params2.add(new BasicNameValuePair("sCodEmpresa", general[indice][0].toString()));
                            params2.add(new BasicNameValuePair("sCodAtributo", general[indice][4].toString()));
                            params2.add(new BasicNameValuePair("sCodSucursal",sucursal));
                            params2.add(new BasicNameValuePair("sParametro", "actualizar"));


                            String resultServer2 = getHttpPost(url2, params2);
                            System.out.println("---------------------------------");
                            System.out.println(resultServer2);
                            ArrayList<String> array = new ArrayList<String>();
                            try {
                                JSONArray jArray = new JSONArray(resultServer2);

                                //Toast.makeText(getApplicationContext()," proceso " + array.get(0),Toast.LENGTH_LONG).show();
                                for (int i = 0; i < jArray.length(); i++) {
                                    JSONObject json = jArray.getJSONObject(i);
                                    array.add((json.getString("cod_valor")));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (array.size()>0) {
                                String url4 = "http://" + ip + "/updateGeneral.php";

                                List<NameValuePair> params4 = new ArrayList<NameValuePair>();


                                params4.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                params4.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                params4.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                params4.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                params4.add(new BasicNameValuePair("sParametro", "update3"));

                                String resultServer4 = getHttpPost(url4, params4);
                                System.out.println(resultServer4);


                                try {
                                    Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                    pasar(conexion, general);
                                }catch (Exception e){
                                    Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                }
                            } else {*/





                                /*final AlertDialog.Builder ad = new AlertDialog.Builder(Recepcion.this);

                                ad.setTitle("Error! ");
                                ad.setIcon(android.R.drawable.btn_star_big_on);
                                ad.setPositiveButton("Close", null);
                                InputStream isr = null;
                                String url = "http://" + finalIp + "/update.php";

                                List<NameValuePair> params = new ArrayList<NameValuePair>();


                                params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa));
                                params.add(new BasicNameValuePair("sCodSucursal", cod_sucursal));
                                params.add(new BasicNameValuePair("sCodUsuario", cod_usuario));
                                params.add(new BasicNameValuePair("sCodProceso", cod_proceso));
                                params.add(new BasicNameValuePair("sCodAtributo", cod_atributo));
                                params.add(new BasicNameValuePair("sCodValor", texto.getText().toString()));
                                params.add(new BasicNameValuePair("sNumOrden", num_orden));

                                String resultServer = getHttpPost(url, params);
                                System.out.println(resultServer);


                                JSONObject c;
                                try {
                                    Toast.makeText(Recepcion.this, "Update Data Successfully", Toast.LENGTH_SHORT).show();
                                    pasar(conexion, general);
                                }catch(Exception e){
                                    Toast.makeText(Recepcion.this, "Dont Update Data Successfully", Toast.LENGTH_SHORT).show();
                                }*/


                    error.setTextColor(Color.RED);

                    String file = "storage/sdcard0/METRO/" + nombre;
                    File mi_foto = new File(file);
                    String file2 = "storage/sdcard0/METRO/";
                    File mi_foto2 = new File(file2);
                    mi_foto.renameTo(mi_foto2);

                    EditText texto1 = (EditText) findViewById(R.id.editText_texto);

                    if (general[indice][4].trim().equals("ROMBO")) {
                        error.setText("");
                        num_rombo = texto.getText().toString().trim();
                    }
                    if (general[indice][4].trim().equals("PLACA")) {
                        cod_placa = texto.getText().toString().trim();

                    }

                    if (general[indice][4].trim().equals("PAGO")) {
                        pago = texto.getText().toString().trim();

                    }
                    if (general[indice][4].trim().equals("PLACA") && encuentraPlaca.equals("false")) {
                        error.setText("Placa no existe, se creara!");
                    }
                    if (general[indice][4].trim().equals("UBICACION")) {
                        cod_ubicacion = texto.getText().toString().trim();
                    }
                    if (general[indice][4].trim().equals("TIPO VEHICULO")) {
                        cod_ubicacion = texto.getText().toString().trim();
                    }
                    if (general[indice][4].trim().equals("NIT")) {
                        num_nit = texto.getText().toString().trim();
                    }
                    if (general[indice][4].trim().equals("KM")) {
                        kilometraje = texto.getText().toString().trim();

                    }
                    if (general[indice][4].trim().equals("RAZON")) {
                        if (razon.equals("61") || razon.equals("62")){
                            eventoRazon = "G";
                        }
                        razon = texto.getText().toString().trim();
                        error.setText(descripcion);
                    }

                    if (general[indice][4].trim().equals("SOAT")) {
                        soat = texto.getText().toString().trim();
                    }

                    if (general[indice][4].trim().equals("TECNICOMECANICA")) {
                        tecno = texto.getText().toString().trim();
                    }

                    if (general[indice][4].trim().equals("CEDULA")) {
                        num_nit = texto.getText().toString().trim();
                    }


                    if (general[indice][4].trim().equals("PLACA")) {

                        cita = new String();
                        horacita = new String();

                        String url4 = "http://" + ip + "/consultarGeneral.php";

                        List<NameValuePair> params4 = new ArrayList<NameValuePair>();

                        params4.add(new BasicNameValuePair("sParametro", "placanit"));
                        params4.add(new BasicNameValuePair("sPlaca", cod_placa));

                        String resultServer4 = getHttpPost(url4, params4);
                        System.out.println("---------------------------------resultserver----------------");
                        System.out.println(resultServer4);

                        String url87 = "http://" + ip + "/consultarGeneral.php";

                        List<NameValuePair> params87 = new ArrayList<NameValuePair>();

                        params87.add(new BasicNameValuePair("sParametro", "km_anterior"));
                        params87.add(new BasicNameValuePair("sPlaca", cod_placa));

                        String resultServer87 = getHttpPost(url87, params87);
                        System.out.println("---------------------------------resultserver----------------");
                        System.out.println(resultServer87);
                        try {

                            JSONArray jArray = new JSONArray(resultServer87);
                            ArrayList<String> array = new ArrayList<String>();
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                array.add(json.getString("km_anterior"));
                                km_anterior = array.get(0);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {

                            JSONArray jArray = new JSONArray(resultServer4);
                            ArrayList<String> array = new ArrayList<String>();
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                array.add(json.getString("nit"));
                            }
                            if (array.get(0).equals("null")){

                            }else {
                                num_nit = array.get(0);
                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                try {

                                    guardarMovimiento("NIT", num_nit);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                dataBaseHelper.close();
                            }
                        } catch (JSONException e) {

                            error.setText("");
                            //informativo.setTextColor(Color.BLUE);
                            e.printStackTrace();
                        }

                        String url3 = "http://" + ip + "/consultarGeneral.php";

                        List<NameValuePair> params3 = new ArrayList<NameValuePair>();

                        params3.add(new BasicNameValuePair("sParametro", "CITA"));
                        params3.add(new BasicNameValuePair("sPlaca", cod_placa));

                        String resultServer3 = getHttpPost(url3, params3);
                        System.out.println("---------------------------------resultserver----------------");
                        System.out.println(resultServer3);

                        try {

                            JSONArray jArray = new JSONArray(resultServer3);
                            ArrayList<String> array = new ArrayList<String>();
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                array.add(json.getString("placa"));
                                array.add(json.getString("fecha"));
                            }
                            cita = array.get(0);
                            horacita = array.get(1);
                            eventoCita = "C";
                        } catch (JSONException e) {

                            error.setText("");
                            //informativo.setTextColor(Color.BLUE);
                            e.printStackTrace();
                        }

                        String url1 = "http://" + ip + "/consultarGeneral.php";

                        List<NameValuePair> params1 = new ArrayList<NameValuePair>();

                        params1.add(new BasicNameValuePair("sParametro", "MODELO"));
                        params1.add(new BasicNameValuePair("sPlaca", cod_placa));

                        String resultServer1 = getHttpPost(url1, params1);
                        System.out.println("---------------------------------resultserver----------------");
                        System.out.println(resultServer1);
                        try {

                            JSONArray jArray = new JSONArray(resultServer1);
                            ArrayList<String> array = new ArrayList<String>();
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                array.add(json.getString("modelo"));
                            }

                            modelo = array.get(0);

                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                            try {

                                guardarMovimiento("MODELO", modelo);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            dataBaseHelper.close();

                        } catch (JSONException e) {

                            error.setText("");
                            //informativo.setTextColor(Color.BLUE);
                            e.printStackTrace();
                        }

                        String url = "http://" + ip + "/consultarGeneral.php";

                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                        params.add(new BasicNameValuePair("sParametro", "placa"));
                        params.add(new BasicNameValuePair("sPlaca", cod_placa));

                        String resultServer = getHttpPost(url, params);
                        System.out.println("---------------------------------resultserver----------------");
                        System.out.println(resultServer);
                        String placa = "";
                        try {

                            JSONArray jArray = new JSONArray(resultServer);
                            ArrayList<String> array = new ArrayList<String>();
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                array.add(json.getString("placa"));
                                placa = array.get(0);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String url2 = "http://" + ip + "/consultarGeneral.php";

                        List<NameValuePair> params2 = new ArrayList<NameValuePair>();

                        params2.add(new BasicNameValuePair("sParametro", "fechas"));
                        params2.add(new BasicNameValuePair("sPlaca", cod_placa));

                        String resultServer2 = getHttpPost(url2, params2);
                        System.out.println("---------------------------------resultserver----------------");
                        System.out.println(resultServer2);
                        try {

                            JSONArray jArray = new JSONArray(resultServer2);
                            ArrayList<String> array = new ArrayList<String>();
                            for (int i = 0; i < jArray.length(); i++) {
                                JSONObject json = jArray.getJSONObject(i);
                                array.add(json.getString("soat"));
                                array.add(json.getString("tecno"));
                            }


                            soat = array.get(0);

                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                            try {

                                guardarMovimiento("SOAT", soat);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            dataBaseHelper.close();

                            tecno = array.get(1);


                            try {

                                guardarMovimiento("TECNICOMECANICA", tecno);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            dataBaseHelper.close();


                        } catch (JSONException e) {
                            error.setText("");
                            e.printStackTrace();
                        }

                        if (cod_placa.trim().equals(placa)) {
                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                            try {

                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            dataBaseHelper.close();
                            general[indice][5] = texto.getText().toString().trim();
                            pasar(conexion, general);
                            error.setText("");
                            encuentraPlaca = "true";

                        } else {
                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                            try {

                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            //ArrayList arrayList = dataBaseHelper.getConsultas(string);
                            dataBaseHelper.close();
                            general[indice][5] = texto.getText().toString().trim();
                            pasar(conexion, general);
                            error.setText("");
                            //error.setText("No existe la placa!");
                            encuentraPlaca = "false";
                        }
                    } else {
                        if (general[indice][4].trim().equals("CEDULA")) {
                            String cedula = texto.getText().toString().replaceAll("\n", "");


                            try {
                                Cedula = cedula.substring(47, 58);
                                nombre1 = cedula.substring(95, 116);
                                nombre2 = cedula.substring(117, 140);
                                apellido1 = cedula.substring(58, 71);
                                apellido2 = cedula.substring(72, 94);
                                if (Cedula.substring(1, 2).equals("00")) {
                                    Cedula = cedula.substring(50, 58);
                                }


                                if (general[indice + 1][4].trim().equals("NOMBRES")) {
                                    general[indice + 1][5] = nombre1 + " " + nombre2 + " " + apellido1 + " " + apellido2;

                                }

                                texto.setText(Cedula);
                                general[indice][5] = texto.getText().toString().trim();
                                pasar(conexion, general);
                                error.setText("");
                            } catch (Exception e) {
                                general[indice][5] = texto.getText().toString().trim();
                                pasar(conexion, general);
                                error.setText("");
                                e.printStackTrace();
                            }
                        } else {

                            if (general[indice][4].trim().equals("NOMBRES")) {
                                String cedula = texto.getText().toString().replaceAll("\n", "");


                                try {
                                    Cedula = cedula.substring(47, 58);
                                    nombre1 = cedula.substring(95, 116);
                                    nombre2 = cedula.substring(117, 140);
                                    apellido1 = cedula.substring(58, 71);
                                    apellido2 = cedula.substring(72, 94);
                                    if (Cedula.substring(1, 3).equals("00")) {
                                        Cedula = cedula.substring(50, 58);
                                    }

                                    if (general[indice + 1][4].trim().equals("CEDULA")) {
                                        general[indice + 1][5] = Cedula;
                                    }

                                    for (int i = 0; i < general.length; i++) {
                                        if (general[i][4].trim().equals("NIT")) {
                                            general[i][5] = Cedula;
                                        }
                                    }

                                    texto.setText(remove(nombre1) + " " + remove(nombre2) + " " + remove(apellido1) + " " + remove(apellido2));
                                    general[indice][5] = remove(nombre1) + " " + remove(nombre2) + " " + remove(apellido1) + " " + remove(apellido2);
                                    pasar(conexion, general);
                                    error.setText("");
                                } catch (Exception e) {
                                    general[indice][5] = texto.getText().toString().trim();
                                    pasar(conexion, general);
                                    error.setText("");
                                    e.printStackTrace();
                                }
                            } else {


                                if (general[indice][4].trim().equals("KM") ) {

                                    try {

                                        String url = "http://" + ip + "/consultarGeneral.php";

                                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                                        params.add(new BasicNameValuePair("sParametro", "km_anterior"));
                                        params.add(new BasicNameValuePair("sPlaca", cod_placa));

                                        String resultServer = getHttpPost(url, params);
                                        System.out.println("---------------------------------resultserver----------------");
                                        System.out.println(resultServer);
                                        try {

                                            JSONArray jArray = new JSONArray(resultServer);
                                            ArrayList<String> array = new ArrayList<String>();
                                            for (int i = 0; i < jArray.length(); i++) {
                                                JSONObject json = jArray.getJSONObject(i);
                                                array.add(json.getString("km_anterior"));
                                                km_anterior = array.get(0);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        if (km_anterior.trim().equals("null"))
                                            km_anterior = "1";

                                        if (Integer.parseInt(km_anterior.toString()) < Integer.parseInt(texto.getText().toString())) {

                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                            try {

                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                            //ArrayList arrayList = dataBaseHelper.getConsultas(string);
                                            dataBaseHelper.close();
                                            general[indice][5] = texto.getText().toString().trim();
                                            pasar(conexion, general);
                                            error.setText("");

                                        } else {
                                            //informativo.setTextColor(Color.BLUE);
                                            error.setText("Anterior : " + km_anterior + ", debe ser mayor!");


                                        }
                                    }catch (Exception e){
                                        error.setText("Ingrese un km valido");
                                        e.printStackTrace();
                                    }
                                } else {

                                    if (general[indice][4].trim().equals("NIT")) {
                                        String url = "http://" + ip + "/consultarGeneral.php";

                                        List<NameValuePair> params = new ArrayList<NameValuePair>();

                                        params.add(new BasicNameValuePair("sParametro", "nit"));
                                        params.add(new BasicNameValuePair("sNit", num_nit));

                                        String resultServer = getHttpPost(url, params);
                                        System.out.println("---------------------------------resultserver----------------");
                                        System.out.println(resultServer);
                                        String nit = "";
                                        try {

                                            JSONArray jArray = new JSONArray(resultServer);
                                            ArrayList<String> array = new ArrayList<String>();
                                            for (int i = 0; i < jArray.length(); i++) {
                                                JSONObject json = jArray.getJSONObject(i);
                                                array.add(json.getString("nit"));
                                                nit = array.get(0);
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (num_nit.trim().equals(nit)) {
                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                            try {

                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                            //ArrayList arrayList = dataBaseHelper.getConsultas(string);
                                            dataBaseHelper.close();
                                            general[indice][5] = texto.getText().toString().trim();
                                            pasar(conexion, general);
                                            error.setText("");
                                            encuentraNit = "true";
                                        } else {
                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                            try {

                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                            //ArrayList arrayList = dataBaseHelper.getConsultas(string);
                                            dataBaseHelper.close();
                                            encuentraNit = "false";
                                            error.setText("");
                                            //error.setText("No existe el nit!");
                                            general[indice][5] = texto.getText().toString().trim();
                                            //pasar(conexion, general);
                                            Intent intent = new Intent((Context) RecepcionUsuariosUbicaciones.this, (Class) MostrarCliente.class);
                                            Bundle bundle2 = new Bundle();
                                            bundle2.putString("conexion", conexion);
                                            bundle2.putString("ip", ip);
                                            bundle2.putString("atributo", texto.getText().toString().trim());
                                            bundle2.putString("empresa", cod_empresa);
                                            bundle2.putString("sucursal", sucursal);
                                            bundle2.putString("parametro", parametro);
                                            intent.putExtras(bundle2);
                                            RecepcionUsuariosUbicaciones.this.startActivityForResult(intent, request_code);
                                            error.setText("");

                                        }
                                    } else {
                                        if (general[indice][4].trim().equals("PLACA")) {
                                            String url = "http://" + ip + "/consultarGeneral.php";

                                            List<NameValuePair> params = new ArrayList<NameValuePair>();

                                            params.add(new BasicNameValuePair("sParametro", "PLACA"));
                                            params.add(new BasicNameValuePair("sPlaca", cod_placa));

                                            String resultServer = getHttpPost(url, params);
                                            System.out.println("---------------------------------resultserver----------------");
                                            System.out.println(resultServer);
                                            String nombre = "";
                                            try {

                                                JSONArray jArray = new JSONArray(resultServer);
                                                ArrayList<String> array = new ArrayList<String>();
                                                for (int i = 0; i < jArray.length(); i++) {
                                                    JSONObject json = jArray.getJSONObject(i);
                                                    array.add(json.getString("nombre"));
                                                    sInformativo = sInformativo + "\n" + array.get(0);
                                                }

                                            } catch (JSONException e) {

                                                error.setText("");
                                                error.setText("No existe la placa!");
                                                //informativo.setTextColor(Color.BLUE);
                                                e.printStackTrace();
                                            }




                                            if (sInformativo.equals("null") || sInformativo.equals("")) {

                                                error.setText("No existe la placa!");
                                            } else {
                                                error.setText(sInformativo);
                                                //informativo.setTextColor(Color.BLUE);
                                                general[indice][5] = texto.getText().toString().trim();
                                                pasar(conexion, general);
                                                error.setText("");
                                            }
                                        } else {
                                            if (atributo.getText().equals("RAZON")) {
                                                String[] grid2 = {"GENERAL"
                                                        ,"VENTA TALLERES MOSTRADOR"
                                                        ,"MAIL-CAMPAÑA"
                                                        ,"CALL CENTER-CAMPAÑA"
                                                        ,"CAMPAÑA DE REDES SOCIALES"
                                                        ,"AUTOSURA-CITA CARRO TALLER"
                                                        ,"AUTOSURA-CLIENTE"
                                                        ,"FONDOS Y FERIAS"
                                                        ,"CARRERAS CALI"
                                                        ,"CARRO TALLER"
                                                        ,"CONVENIOS INSTITUCIONALES"
                                                        ,"AUTOSURA-BONO"
                                                        ,"CLIENTE UNE MAS"
                                                        ,"SUCURSAL  AUTOSURA"
                                                        ,"CUPON CLIENTE UNE MAS"
                                                        ,"PROMOCIONES Y PUBLICACIONES EN REDES SOCIALES"
                                                        ,"CLIENTE DE GERENCIA"
                                                        ,"BONO DE CORTESIA"
                                                        ,"AGENDA UNIVERSITARIA"
                                                        ,"FLYER O VOLANTE"
                                                        ,"CONVENIO ALTO NIVEL"
                                                        ,"REVISTA AGENDA AUTOMOTRIZ"
                                                        ,"AVISO DIAGNOSTICO GRATIS"
                                                        ,"EXPOMOTRIZ"
                                                        ,"CITA REVISION PREVENTIVA"
                                                        ,"MAILING CAMPAÑA VACACIONES SIN PREOCUPACIONES"
                                                        ,"VOLANTE CAMPAÑA VACACIONES SIN PREOCUPACIONES"
                                                        ,"L A W , CAMPAÑA VACACIONES SIN PREOCUPACIONES"
                                                        ,"AVISOS FACHADA VACACIONES SIN PREOCUPACIONES"
                                                        ,"CIRCUITO SABANETA"
                                                        ,"RASPA Y GANA VACACIONE SIN PREOCUPACIONES"
                                                        ,"COMERCIAL BLU RADIO"
                                                        ,"CITAS POR PAGINA WEB INTERNET"
                                                        ,"EVENTO STA MARIA DE LOS ANGELES"
                                                        ,"BLU RADIO"
                                                        ,"RECORDATORIO DE KILOMETRAJE"
                                                        ,"LLAMADA DEL CLIENTE"
                                                        ,"REFERIDOS"
                                                        ,"EVENTO DIA DE CAMPO JARDIN BOTANICO",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "",
                                                        "GENERAL-FIME",
                                                        "CITA - FIME"};

                                                try {


                                                    general[indice][5] = texto.getText().toString().trim();
                                                    if (grid2[Integer.parseInt(general[indice][5]) - 1].equals("")){
                                                        Toast.makeText(RecepcionUsuariosUbicaciones.this, "No existe la razon!", Toast.LENGTH_SHORT).show();
                                                        general[indice][5] = "";
                                                        error.setText("");
                                                    }else{

                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                        try {

                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (SQLException e) {
                                                            e.printStackTrace();
                                                        }
                                                        dataBaseHelper.close();

                                                        error.setText(grid2[Integer.parseInt(general[indice][5]) - 1]);

                                                        descripcion = grid2[Integer.parseInt(general[indice][5]) - 1];
                                                        if (razon.equals("61") || razon.equals("62")){
                                                            eventoRazon = "G";
                                                        }
                                                        razon = general[indice][5];

                                                        pasar(conexion, general);

                                                    }

                                                }catch (Throwable e){
                                                    e.printStackTrace();
                                                    general[indice][5] = "";
                                                    error.setText("");
                                                    Toast.makeText(RecepcionUsuariosUbicaciones.this, "No existe la razon!", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                if (general[indice][4].trim().equals("PROMESA")) {
                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                    try {

                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    dataBaseHelper.close();
                                                    promesa = texto.getText().toString().trim();
                                                    general[indice][5] = texto.getText().toString().trim();
                                                    pasar(conexion, general);
                                                    error.setText("");
                                                } else {
                                                    if (general[indice][4].trim().equals("SOAT")) {
                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                        try {

                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (SQLException e) {
                                                            e.printStackTrace();
                                                        }
                                                        dataBaseHelper.close();
                                                        soat = texto.getText().toString().trim();
                                                        general[indice][5] = texto.getText().toString().trim();
                                                        pasar(conexion, general);
                                                        error.setText("");
                                                    } else {
                                                        if (general[indice][4].trim().equals("TECNICOMECANICA")) {
                                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                            try {

                                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            } catch (SQLException e) {
                                                                e.printStackTrace();
                                                            }
                                                            dataBaseHelper.close();
                                                            tecno = texto.getText().toString().trim();
                                                            general[indice][5] = texto.getText().toString().trim();
                                                            pasar(conexion, general);
                                                            error.setText("");
                                                        } else {
                                                            if (general[indice][4].trim().equals("SOLICITUD")) {
                                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                try {

                                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                } catch (SQLException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                dataBaseHelper.close();
                                                                solicitud = texto.getText().toString().trim();
                                                                general[indice][5] = texto.getText().toString().trim();
                                                                pasar(conexion, general);
                                                                error.setText("");
                                                            } else {
                                                                if (general[indice][4].trim().equals("FECHA")) {
                                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                    try {

                                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    } catch (SQLException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    dataBaseHelper.close();
                                                                    fecha = texto.getText().toString().trim();
                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                    pasar(conexion, general);
                                                                    error.setText("");
                                                                } else {
                                                                    if (general[indice][4].trim().equals("USUARIO")) {
                                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                        try {

                                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        } catch (SQLException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        dataBaseHelper.close();
                                                                        usuario = texto.getText().toString().trim();
                                                                        general[indice][5] = texto.getText().toString().trim();
                                                                        pasar(conexion, general);
                                                                        error.setText("");
                                                                    } else {
                                                                        if (general[indice][4].trim().equals("TIEMPO")) {
                                                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                            try {

                                                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            } catch (SQLException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            dataBaseHelper.close();
                                                                            tiempo = texto.getText().toString().trim();
                                                                            general[indice][5] = texto.getText().toString().trim();
                                                                            pasar(conexion, general);
                                                                            error.setText("");
                                                                        } else {
                                                                            if (general[indice][4].trim().equals("HORA FIN")) {
                                                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                try {

                                                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (SQLException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                                dataBaseHelper.close();
                                                                                horaFin = texto.getText().toString().trim();
                                                                                general[indice][5] = texto.getText().toString().trim();
                                                                                pasar(conexion, general);
                                                                                error.setText("");
                                                                            } else {
                                                                                if (general[indice][4].trim().equals("HORA INICIO")) {
                                                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                    try {

                                                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                    } catch (IOException e) {
                                                                                        e.printStackTrace();
                                                                                    } catch (SQLException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    dataBaseHelper.close();
                                                                                    fecha = texto.getText().toString().trim();
                                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                                    pasar(conexion, general);
                                                                                    error.setText("");
                                                                                } else {
                                                                                    if (general[indice][4].trim().equals("PRODUCTOS")) {
                                                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                        try {

                                                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                        } catch (IOException e) {
                                                                                            e.printStackTrace();
                                                                                        } catch (SQLException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                        dataBaseHelper.close();
                                                                                        productText = texto.getText().toString().trim();
                                                                                        general[indice][5] = texto.getText().toString().trim();
                                                                                        pasar(conexion, general);
                                                                                        error.setText("");
                                                                                    } else {
                                                                                        if (general[indice][4].trim().equals("PAGO")) {
                                                                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                            try {

                                                                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                            } catch (IOException e) {
                                                                                                e.printStackTrace();
                                                                                            } catch (SQLException e) {
                                                                                                e.printStackTrace();
                                                                                            }
                                                                                            dataBaseHelper.close();
                                                                                            pago = texto.getText().toString().trim();
                                                                                            general[indice][5] = texto.getText().toString().trim();
                                                                                            pasar(conexion, general);
                                                                                            error.setText("");
                                                                                        } else {
                                                                                            if (general[indice][4].trim().equals("MODELO")) {
                                                                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                try {

                                                                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                } catch (IOException e) {
                                                                                                    e.printStackTrace();
                                                                                                } catch (SQLException e) {
                                                                                                    e.printStackTrace();
                                                                                                }
                                                                                                dataBaseHelper.close();
                                                                                                modelo = texto.getText().toString().trim();
                                                                                                general[indice][5] = texto.getText().toString().trim();
                                                                                                pasar(conexion, general);
                                                                                                error.setText("");

                                                                                            } else {
                                                                                                if (general[indice][4].trim().equals("UBICACION")) {
                                                                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                    try {

                                                                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                    } catch (IOException e) {
                                                                                                        e.printStackTrace();
                                                                                                    } catch (SQLException e) {
                                                                                                        e.printStackTrace();
                                                                                                    }
                                                                                                    dataBaseHelper.close();
                                                                                                    cod_ubicacion = texto.getText().toString().trim();
                                                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                                                    pasar(conexion, general);
                                                                                                    error.setText("");

                                                                                                } else {
                                                                                                    if (general[indice][4].trim().equals("TIPO VEHICULO")) {
                                                                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                        try {

                                                                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                        } catch (IOException e) {
                                                                                                            e.printStackTrace();
                                                                                                        } catch (SQLException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }
                                                                                                        dataBaseHelper.close();
                                                                                                        tip_vehiculo = texto.getText().toString().trim();
                                                                                                        general[indice][5] = texto.getText().toString().trim();
                                                                                                        pasar(conexion, general);
                                                                                                        error.setText("");

                                                                                                    } else {
                                                                                                        if (general[indice][4].trim().equals("TECNICO")) {

                                                                                        /*String hayTecnico = "";
                                                                                        String url = "http://" + ip + "/consultarGeneral.php";

                                                                                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                                                        params.add(new BasicNameValuePair("sTecnico", texto1.getText().toString().trim()));
                                                                                        params.add(new BasicNameValuePair("sParametro", "tecnico"));


                                                                                        String resultServer = getHttpPost(url, params);
                                                                                        System.out.println(resultServer);
                                                                                        try {

                                                                                            JSONArray jArray = new JSONArray(resultServer);
                                                                                            ArrayList<String> array = new ArrayList<String>();
                                                                                            for (int i = 0; i < jArray.length(); i++) {
                                                                                                JSONObject json = jArray.getJSONObject(i);
                                                                                                hayTecnico = json.getString("cod_tecnico");
                                                                                            }



                                                                                        } catch (JSONException e) {
                                                                                            error.setText("No existe el tecnico!");
                                                                                            e.printStackTrace();
                                                                                        }*/

                                                                                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                            try {

                                                                                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                            } catch (IOException e) {
                                                                                                                e.printStackTrace();
                                                                                                            } catch (SQLException e) {
                                                                                                                e.printStackTrace();
                                                                                                            }
                                                                                                            dataBaseHelper.close();
                                                                                                            tecnico = texto.getText().toString().trim();
                                                                                                            general[indice][5] = texto.getText().toString().trim();
                                                                                                            pasar(conexion, general);
                                                                                                            error.setText("");


                                                                                                        } else {
                                                                                                            if (general[indice][9].trim().equals("E")) {
                                                                                                                if (isNumeric(texto.getText().toString().trim())) {
                                                                                                                    if (Integer.parseInt(texto.getText().toString().trim()) >= Double.parseDouble(general[indice][10]) && Integer.parseInt(texto.getText().toString().trim()) <= Double.parseDouble(general[indice][11])) {
                                                                                                                        if (general[indice][4].trim().equals("ROMBO")) {

                                                                                                                            String url = "";
                                                                                                                            List<NameValuePair> params = new ArrayList<NameValuePair>();
                                                                                                                            boolean ocupado = false;

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

                                                                                                                            params.add(new BasicNameValuePair("sCodEmpresa", cod_empresa.trim()));
                                                                                                                            params.add(new BasicNameValuePair("sCodSucursal", bodega.trim()));
                                                                                                                            url = "http://" + ip + "/consultarRecurso.php";
                                                                                                                            params.add(new BasicNameValuePair("sParametro", "R"));
                                                                                                                            String resultServer = getHttpPost(url, params);
                                                                                                                            System.out.println("---------------------------------");
                                                                                                                            System.out.println(resultServer);
                                                                                                                            try {
                                                                                                                                JSONArray jArray = new JSONArray(resultServer);
                                                                                                                                ArrayList<String> array = new ArrayList<String>();
                                                                                                                                for (int i = 0; i < jArray.length(); i++) {
                                                                                                                                    JSONObject json = jArray.getJSONObject(i);
                                                                                                                                    array.add((json.getString("rombo")));
                                                                                                                                    if (texto.getText().toString().equals(array.get(i).trim())) {
                                                                                                                                        ocupado = true;
                                                                                                                                        break;
                                                                                                                                    } else {
                                                                                                                                        ocupado = false;
                                                                                                                                    }
                                                                                                                                }


                                                                                                                            } catch (JSONException e) {
                                                                                                                                e.printStackTrace();
                                                                                                                            }


                                                                                                                            if (ocupado) {
                                                                                                                                error.setText("El rombo no esta disponible");
                                                                                                                            } else {
                                                                                                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                                                try {

                                                                                                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                                                } catch (IOException e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                } catch (SQLException e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                }
                                                                                                                                dataBaseHelper.close();
                                                                                                                                general[indice][5] = texto.getText().toString().trim();
                                                                                                                                pasar(conexion, general);
                                                                                                                                error.setText("");
                                                                                                                            }
                                                                                                                        } else {
                                                                                                                            DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                                            try {

                                                                                                                                guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                                            } catch (IOException e) {
                                                                                                                                e.printStackTrace();
                                                                                                                            } catch (SQLException e) {
                                                                                                                                e.printStackTrace();
                                                                                                                            }
                                                                                                                            dataBaseHelper.close();
                                                                                                                            general[indice][5] = texto.getText().toString().trim();
                                                                                                                            pasar(conexion, general);
                                                                                                                            error.setText("");
                                                                                                                        }

                                                                                                                    } else {
                                                                                                                        error.setText("No esta en el rango!");
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    error.setText("No es numero!");
                                                                                                                }
                                                                                                            } else {
                                                                                                                if (general[indice][9].trim().equals("C")) {
                                                                                                                    if ((texto.getText().toString().trim().length() >= Double.parseDouble(general[indice][10])) && (texto.getText().toString().trim().length() <= Double.parseDouble(general[indice][11]))) {
                                                                                                                        general[indice][5] = texto.getText().toString().trim();
                                                                                                                        pasar(conexion, general);
                                                                                                                        error.setText("");

                                                                                                                    } else {
                                                                                                                        if (Double.parseDouble(general[indice][10]) == Double.parseDouble(general[indice][11]) && texto.getText().toString().trim().length() == Double.parseDouble(general[indice][10])) {
                                                                                                                            general[indice][5] = texto.getText().toString().trim();
                                                                                                                            pasar(conexion, general);
                                                                                                                            error.setText("");
                                                                                                                        } else {
                                                                                                                            error.setText("No esta en el rango!");
                                                                                                                        }
                                                                                                                        error.setText("No esta en el rango!");
                                                                                                                    }
                                                                                                                } else {
                                                                                                                    if (general[indice][9].trim().equals("F")) {
                                                                                                                        if (general[indice][18].trim().equals("PROMESA_ENTREGA")) {

                                                                                                                            date = getHora();
                                                                                                                            general[indice][5] = texto.getText().toString().trim();
                                                                                                                            pasar(conexion, general);
                                                                                                                            error.setText("");
                                                                                                                        } else {
                                                                                                                            general[indice][5] = texto.getText().toString().trim();
                                                                                                                            pasar(conexion, general);
                                                                                                                            error.setText("");
                                                                                                                        }
                                                                                                                    } else {
                                                                                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                                                                        try {

                                                                                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                                                                        } catch (IOException e) {
                                                                                                                            e.printStackTrace();
                                                                                                                        } catch (SQLException e) {
                                                                                                                            e.printStackTrace();
                                                                                                                        }
                                                                                                                        dataBaseHelper.close();
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
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            // }


                        }
                    }

        if ( general[indice][15].trim().equals(general[indice][3].trim())) {
            buscar.setVisibility(View.INVISIBLE);
            anadir.setVisibility(View.INVISIBLE);
        }else {
            Button borrartodo = (Button) findViewById(R.id.borrartodo);
            Button quitarespacios = (Button) findViewById(R.id.quitarespacios);
            borrartodo.setVisibility(View.INVISIBLE);
            quitarespacios.setVisibility(View.INVISIBLE);
            buscar.setVisibility(View.VISIBLE);
            anadir.setVisibility(View.VISIBLE);
        }

        if (general[indice][4].trim().equals("RAZON")) {
            error.setTextColor(Color.RED);
            error.setText(descripcion);
        }

        if (general[indice][4].trim().equals("SOAT")) {
            texto.setText(soat);
            general[indice][5] = soat;
        }

        if (general[indice][4].trim().equals("PLACA")) {
            texto.setText(cod_placa);
            general[indice][5] = cod_placa;
        }

        if (general[indice][4].trim().equals("PAGO")) {
            texto.setText(pago);
            general[indice][5] = pago;
        }

        if (general[indice][4].trim().equals("ROMBO")) {
            texto.setText(num_rombo);
            general[indice][5] = num_rombo;
        }

        if (general[indice][4].trim().equals("UBICACION")) {
            texto.setText(cod_ubicacion);
            general[indice][5] = cod_ubicacion;
        }

        if (general[indice][4].trim().equals("TIPO VEHICULO")) {
            texto.setText(tip_vehiculo);
            general[indice][5] = tip_vehiculo;
        }

        if (general[indice][4].trim().equals("SOLICITUD")) {
            texto.setText(solicitud);
            general[indice][5] = solicitud;
        }

        if (general[indice][4].trim().equals("FECHA")) {
            texto.setText(fecha);
            general[indice][5] = fecha;
        }

        if (general[indice][4].trim().equals("USUARIO")) {
            texto.setText(usuario);
            general[indice][5] = usuario;
        }

        if (general[indice][4].trim().equals("TIEMPO")) {
            texto.setText(tiempo);
            general[indice][5] = tiempo;
        }

        if (general[indice][4].trim().equals("HORA FIN")) {
            texto.setText(horaFin);
            general[indice][5] = horaFin;
        }

        if (general[indice][4].trim().equals("HORA INICIO")) {
            texto.setText(horaInicio);
            general[indice][5] = horaInicio;
        }

        if (general[indice][4].trim().equals("PRODUCTOS")) {
            texto.setText(productText);
            general[indice][5] = productText;
        }

        if (general[indice][4].trim().equals("TECNICO")) {
            texto.setText(tecnico);
            general[indice][5] = tecnico;
        }

        if (general[indice][4].trim().equals("PROMESA")) {
            texto.setText(promesa);
            general[indice][5] = promesa;
        }

        if (general[indice][4].trim().equals("RAZON")) {
            texto.setText(razon);
            general[indice][5] = razon;
        }



        if (general[indice][4].trim().equals("KM")) {
            texto.setText(kilometraje);
            general[indice][5] = kilometraje;
        }

        if (general[indice][4].trim().equals("NIT")) {
            texto.setText(num_nit);
            general[indice][5] = num_nit;
        }

        if (general[indice][4].trim().equals("PLACA") && encuentraPlaca.equals("false")) {
            error.setText("Placa no existe, se creara!");
        }

        if (general[indice][4].trim().equals("NIT") && encuentraNit.equals("false")) {
            error.setText("Nit no existe, se creara!");
        }

        if (general[indice][4].trim().equals("MODELO")) {
            if (modelo.equals("null")){
                modelo = "";
            }
            texto.setText(modelo);
            general[indice][5] = modelo;
        }

        if (general[indice][4].trim().equals("TECNICOMECANICA")) {
            texto.setText(tecno);
            general[indice][5] = tecno;
        }

        if (general[indice][4].trim().equals("ROMBO")) {
            if ( cita.trim().equals("") || cita.trim().equals("null")){
                atributo1.setBackgroundColor(Color.BLUE);
                atributo1.setTextColor(Color.WHITE);
            }else{
                error.setText("Hora cita : " + horacita.substring(0,5));
                atributo1.setBackgroundColor(Color.YELLOW);
                atributo1.setTextColor(Color.BLUE);
                eventoCita = "C";
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

    private static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    public void refrescar(View v){
        mostrarImagen();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        try {
            if (resultCode == 2) {
                String parametro = data.getStringExtra("parametro");
                String valor = data.getStringExtra("valor");
                TextView error = (TextView) findViewById(R.id.textView_error);
                if (parametro.equals("razon")) {
                    descripcion = data.getStringExtra("descripcion");
                    error.setText(descripcion);
                    razon = valor.trim();
                    if (razon.equals("61") || razon.equals("62")) {
                        eventoRazon = "G";
                    }
                    texto.setText(razon);
                    general[indice][5] = texto.getText().toString().trim();
                    pasar(conexion, general);
                } else {
                    if (parametro.equals("vehiculo")) {
                        tip_vehiculo = valor.trim();
                        texto.setText(tip_vehiculo);
                        general[indice][5] = texto.getText().toString().trim();
                        pasar(conexion, general);
                    } else {
                        if (parametro.equals("PAGO")) {
                            pago = valor.trim();
                            texto.setText(pago);
                            general[indice][5] = texto.getText().toString().trim();
                            pasar(conexion, general);
                        } else {
                            if (parametro.equals("razon2")) {
                                km = data.getDataString();
                                texto.setText(data.getDataString());
                                general[indice][5] = texto.getText().toString().trim();
                                pasar(conexion, general);
                            } else {
                                if (parametro.equals("rombo")) {
                                    num_rombo = valor.trim();
                                    texto.setText(num_rombo);
                                    general[indice][5] = texto.getText().toString().trim();
                                    pasar(conexion, general);
                                } else {
                                    if (parametro.equals("ubicacion")) {
                                        cod_ubicacion = valor.trim();

                                        texto.setText(cod_ubicacion);
                                        general[indice][5] = texto.getText().toString().trim();
                                        pasar(conexion, general);
                                    } else {
                                        if (parametro.equals("tecnico")) {
                                            usuario = valor.trim();

                                            texto.setText(usuario);
                                            general[indice][5] = texto.getText().toString().trim();
                                            pasar(conexion, general);
                                        } else {

                                            if (parametro.equals("solicitud")) {
                                                solicitud = valor.trim();
                                                texto.setText(solicitud);
                                                general[indice][5] = texto.getText().toString().trim();
                                                pasar(conexion, general);
                                            } else {
                                                if (parametro.equals("solicitud")) {
                                                    solicitud = valor.trim();

                                                    texto.setText(solicitud);
                                                    general[indice][5] = texto.getText().toString().trim();
                                                    pasar(conexion, general);
                                                } else {
                                                    if (parametro.equals("guardar")) {
                                                        if (valor.equals("bien")) {
                                                            finish();
                                                        } else {

                                                        }
                                                    } else {
                                                        if (parametro.equals("fecha")) {
                                                            String par = data.getStringExtra("parametro2");
                                                            if (par.trim().equals("SOAT")) {


                                                                texto.setText(valor);
                                                                DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                try {

                                                                    guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                } catch (SQLException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                dataBaseHelper.close();
                                                                soat = valor;
                                                                general[indice][5] = texto.getText().toString().trim();
                                                                pasar(conexion, general);
                                                            } else {
                                                                if (par.trim().equals("FECHA")) {


                                                                    texto.setText(valor);
                                                                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                    try {

                                                                        guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    } catch (SQLException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    dataBaseHelper.close();
                                                                    fecha = valor;
                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                    pasar(conexion, general);
                                                                } else {
                                                                    if (par.trim().equals("TIEMPO")) {


                                                                        texto.setText(valor);
                                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                        try {

                                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        } catch (SQLException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        dataBaseHelper.close();
                                                                        tiempo = valor;
                                                                        general[indice][5] = texto.getText().toString().trim();
                                                                        pasar(conexion, general);
                                                                    } else {
                                                                        texto.setText(valor);
                                                                        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);

                                                                        try {

                                                                            guardarMovimiento(general[indice][4].trim().toString(), texto.getText().toString().trim());
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        } catch (SQLException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        dataBaseHelper.close();
                                                                        tecno = valor;
                                                                        general[indice][5] = texto.getText().toString().trim();
                                                                        pasar(conexion, general);
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            if (parametro.equals("cliente")) {
                                                                if (valor.equals("guardar")) {
                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                } else {
                                                                    general[indice][5] = texto.getText().toString().trim();
                                                                }
                                                            } else {
                                                                if (parametro.equals("guardarModelo")) {
                                                                    descripcionModelo = data.getStringExtra("desc");
                                                                    texto.setText(valor);
                                                                    modelo = texto.getText().toString().trim();
                                                                    general[indice][5] = texto.getText().toString().trim();
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
            }else{
                if (resultCode == 3) {

                    Intent intent = getIntent();
                    Bundle args = data.getBundleExtra("codes");
                    products = (ArrayList<ArrayList>) args.getSerializable("ARRAYLIST");
                    texto.setText("OK");
                    productText = "OK";
                }else{
                    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                        dirFoto = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/METRO/" + "FOTOS_OPERATIVOS" + "/";

                        File fecha = new File(dirFoto);

                        fecha.mkdirs();
                        String file = dirFoto + nombre + ".png";
                        File f = new File(file);

                        Uri uri = Uri.fromFile(f);
                        Bitmap bitmap;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            bitmap = crupAndScale(bitmap, 300); // if you mind scaling
                            ImageView myImage = (ImageView) findViewById(R.id.imageView);
                            myImage.setImageResource(android.R.color.transparent);
                            myImage.setImageBitmap(bitmap);
                            uploadImage(bitmap);
                        } catch (FileNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    } else {
                        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
                            if (resultCode == RESULT_OK) {
                                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                                AccountManager am = AccountManager.get(this);
                                Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                                for (Account account : accounts) {
                                    if (account.name.equals(email)) {
                                        mAccount = account;
                                        break;
                                    }
                                }
                                getAuthToken();
                            } else if (resultCode == RESULT_CANCELED) {
                                Toast.makeText(this, "No Account Selected", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        } else {
                            if (requestCode == REQUEST_ACCOUNT_AUTHORIZATION) {
                                if (resultCode == RESULT_OK) {
                                    Bundle extra = data.getExtras();
                                    onTokenReceived(extra.getString("authtoken"));
                                } else if (resultCode == RESULT_CANCELED) {
                                    Toast.makeText(this, "Authorization Failed", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        }
                    }
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
        }
    }

    public void guardarMovimiento(String atributo, String valor) throws IOException, SQLException {
        DataBaseHelper dataBaseHelper = new DataBaseHelper(RecepcionUsuariosUbicaciones.this);
        dataBaseHelper.createDataBase();
        dataBaseHelper.openDataBase();
        dataBaseHelper.guardarMovimiento(atributo,valor);
        dataBaseHelper.close();
    }

    public int getHora() {
        Calendar calendar = Calendar.getInstance();
        Calendar c = new GregorianCalendar();
        int mMinute = c.get(Calendar.MINUTE);
        return mMinute; // Devuelve el objeto Date con las nuevas horas a?adidas
    }

    public static String remove(String input) {
       String output = "";

        for (int i =0;i<input.length()-1;i++){
            if (Character.isLetter(input.charAt(i))){
                output += input.charAt(i);
            }
        }
        return output;
    }

    public void onDestroy() {
        super.onDestroy();

    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Saliendo...")
                .setMessage("Esta seguro que desea dejar de recibir?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

public void uploadImage(Bitmap b) {
    if (b != null) {
        try {
                /*Bitmap bitmap = resizeBitmap(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri));*/
            callCloudVision(b);
            //selectedImage.setImageBitmap(b);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    } else {
        Log.e(LOG_TAG, "Null image was returned.");
    }
}

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        //resultTextView.setText("Retrieving results from cloud");
        TextView error = (TextView) findViewById(R.id.textView_error);
        error.setTextColor(Color.RED);
        error.setText("Obteniendo datos..");
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder
                            (httpTransport, jsonFactory, credential);
                    Vision vision = builder.build();

                    List<Feature> featureList = new ArrayList<>();
                    Feature labelDetection = new Feature();
                    labelDetection.setType("LABEL_DETECTION");
                    labelDetection.setMaxResults(10);
                    featureList.add(labelDetection);

                    Feature textDetection = new Feature();
                    textDetection.setType("TEXT_DETECTION");
                    textDetection.setMaxResults(10);
                    featureList.add(textDetection);

                    Feature landmarkDetection = new Feature();
                    landmarkDetection.setType("LANDMARK_DETECTION");
                    landmarkDetection.setMaxResults(10);
                    featureList.add(landmarkDetection);

                    List<AnnotateImageRequest> imageList = new ArrayList<>();
                    AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
                    Image base64EncodedImage = getBase64EncodedJpeg(bitmap);
                    annotateImageRequest.setImage(base64EncodedImage);
                    annotateImageRequest.setFeatures(featureList);
                    imageList.add(annotateImageRequest);

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(imageList);

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);


                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);


                } catch (Exception e){
                    e.printStackTrace();
                }
                return "Cloud Vision API request failed.";
            }

            protected void onPostExecute(String result) {
                //resultTextView.setText(result);
                if (result.trim().equals("Error al traer los datos")){
                    TextView error = (TextView) findViewById(R.id.textView_error);
                    error.setTextColor(Color.RED);
                    error.setText(result);
                }else {
                    EditText texto = (EditText) findViewById(R.id.editText_texto);
                    texto.setText(cod_placa);
                    TextView error = (TextView) findViewById(R.id.textView_error);
                    error.setTextColor(Color.RED);
                    error.setText("");
                    //pasar(conexion, general);
                }
            }
        }.execute();
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        String e = "";
        StringBuilder message = new StringBuilder("Results:\n\n");
        message.append("Labels:\n");
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message.append(String.format(Locale.getDefault(), "%.3f: %s",
                        label.getScore(), label.getDescription()));
                message.append("\n");
            }
        } else {
            message.append("nothing\n");
        }

        message.append("Texts:\n");
        List<EntityAnnotation> texts = response.getResponses().get(0)
                .getTextAnnotations();
        if ((texts.get(1).getDescription().trim().length() == 3)){
            if ((texts.get(2).getDescription().trim().length() == 3) ) {
                cod_placa = texts.get(1).getDescription().trim() + texts.get(2).getDescription().trim();
            }else{
                e = "Error al traer los datos";
            }
        }else{
            e = "Error al traer los datos";
        }

        if (texts != null) {
            for (EntityAnnotation text : texts) {
                message.append(String.format(Locale.getDefault(), "%s: %s",
                        text.getLocale(), text.getDescription()));
                message.append("\n");
            }
        } else {
            message.append("nothing\n");
        }

        message.append("Landmarks:\n");
        List<EntityAnnotation> landmarks = response.getResponses().get(0)
                .getLandmarkAnnotations();
        if (landmarks != null) {
            for (EntityAnnotation landmark : landmarks) {
                message.append(String.format(Locale.getDefault(), "%.3f: %s",
                        landmark.getScore(), landmark.getDescription()));
                message.append("\n");
            }
        } else {
            message.append("nothing\n");
        }

        return e;
    }

    public Bitmap resizeBitmap(Bitmap bitmap) {

        int maxDimension = 1024;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    public Image getBase64EncodedJpeg(Bitmap bitmap) {
        Image image = new Image();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        image.encodeContent(imageBytes);
        return image;
    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    private void getAuthToken() {
        String SCOPE = "oauth2:https://www.googleapis.com/auth/cloud-platform";
        if (mAccount == null) {
            pickUserAccount();
        } else {
            new GetTokenTask(RecepcionUsuariosUbicaciones.this, mAccount, SCOPE, REQUEST_ACCOUNT_AUTHORIZATION)
                    .execute();
        }
    }

    public void onTokenReceived(String token){
        accessToken = token;
        visionApi();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAuthToken();
                } else {
                    Toast.makeText(RecepcionUsuariosUbicaciones.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    public static Bitmap crupAndScale (Bitmap source, int scale){
        int factor = source.getHeight() <= source.getWidth() ? source.getHeight(): source.getWidth();
        int longer = source.getHeight() >= source.getWidth() ? source.getHeight(): source.getWidth();
        int x = source.getHeight() >= source.getWidth() ?0:(longer-factor)/2;
        int y = source.getHeight() <= source.getWidth() ?0:(longer-factor)/2;
        source = Bitmap.createBitmap(source, x, y, factor, factor);
        source = Bitmap.createScaledBitmap(source, scale, scale, false);
        return source;
    }

    public void editar(View v){
        Button borrartodo = (Button) findViewById(R.id.borrartodo);
        Button quitarespacios = (Button) findViewById(R.id.quitarespacios);
        Button lupa = (Button) findViewById(R.id.button_buscar);
        Button plus = (Button) findViewById(R.id.button_anadir);


        if (borrartodo.getVisibility() == View.INVISIBLE){
            lupa.setVisibility(View.INVISIBLE);
            plus.setVisibility(View.INVISIBLE);
            borrartodo.setVisibility(View.VISIBLE);
            quitarespacios.setVisibility(View.VISIBLE);

        }else{
            if ( general[indice][15].trim().equals(general[indice][3].trim())) {
                lupa.setVisibility(View.INVISIBLE);
                plus.setVisibility(View.INVISIBLE);
            }else {
                lupa.setVisibility(View.VISIBLE);
                plus.setVisibility(View.VISIBLE);
            }
            borrartodo.setVisibility(View.INVISIBLE);
            quitarespacios.setVisibility(View.INVISIBLE);

        }

    }

    public void borrartodo(View v){
        EditText texto = (EditText) findViewById(R.id.editText_texto);
        texto.setText("");
    }

    public void quitarespacios(View v){
        EditText texto = (EditText) findViewById(R.id.editText_texto);
        texto.setText(texto.getText().toString().replace(" ",""));
    }
}