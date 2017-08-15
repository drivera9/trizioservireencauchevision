package trizio.ram.com.trizioservireencauche;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
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

public class MercarPedido extends Activity {
    MyCustomAdapter dataAdapter = null;
    ProgressDialog mProgressDialog;
    String ip = "";
    String proyecto = "";
    String empresa = "";
    int request_code = 1;
    String idmodano = "";
    ArrayList<ArrayList> products;
    String elegido = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mercar_pedido);

        ip = getIntent().getExtras().getString("ip");
        idmodano = getIntent().getExtras().getString("idmodano");

        mProgressDialog= new ProgressDialog(MercarPedido.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Consultando datos...");

        final String[] datos =
                new String[]{"SELECCIONE OPCION..","PLANTILLAS","GRUPOS","TODO"};

        ArrayAdapter<String> adaptador =
                new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.spinner_item, datos);
        adaptador.setDropDownViewResource(
                R.layout.spinner_item_drop);

        Spinner spinner = (Spinner)findViewById(R.id.spinnerSelect);

        spinner.setAdapter(adaptador);

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               android.view.View v, int position, long id) {
                        if (parent.getItemAtPosition(position).toString().trim().equals("GRUPOS")){
                            elegido = "GRUPOS";
                            new ConsultarDatosGrupo().execute("");
                        }else{
                            if (parent.getItemAtPosition(position).toString().trim().equals("TODO")){
                                elegido = "TODO";
                                ArrayList<ItemCheck> countryList = new ArrayList<ItemCheck>();
                                ItemCheck country;

                                //create an ArrayAdaptar from the String Array
                                dataAdapter = new MyCustomAdapter(getApplicationContext(),
                                        R.layout.item_check, countryList);
                                ListView listView = (ListView) findViewById(R.id.listChequeo);
                                // Assign adapter to ListView
                                listView.setAdapter(dataAdapter);


                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {
                                        // When clicked, show a toast with the TextView text
                                        ItemCheck country = (ItemCheck) parent.getItemAtPosition(position);

                                    }
                                });
                            }else{
                                if (parent.getItemAtPosition(position).toString().trim().equals("PLANTILLAS")){
                                    elegido = "PLANTILLAS";
                                    new ConsultarDatos().execute("");
                                }
                            }
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

    }

    class ConsultarDatosGrupo extends AsyncTask<String, String, String> {

        ArrayList<ItemCheck> countryList = new ArrayList<ItemCheck>();
        ItemCheck country;
        @Override
        protected void onPreExecute(){
            mProgressDialog.show();

        }
        @Override
        protected String doInBackground(String... f_url) {

            String url3 = "http://" + ip + "/consultarGeneral.php";

            List<NameValuePair> params3 = new ArrayList<NameValuePair>();
            params3.add(new BasicNameValuePair("sParametro", "consultarGrupos"));
            String resultServer3 = getHttpPost(url3, params3);
            System.out.println(resultServer3);

            try {

                JSONArray jArray2 = new JSONArray(resultServer3);

                for (int j = 0; j < jArray2.length(); j++) {
                    JSONObject json2 = jArray2.getJSONObject(j);
                    final String[] datos =
                            new String[]{"Elem1","Elem2","Elem3","Elem4","Elem5"};
                    country = new ItemCheck(json2.getString("subgrupo").trim(),json2.getString("descripcion").trim(),false,"0","0",datos);
                    countryList.add(country);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            mProgressDialog.dismiss();
            //create an ArrayAdaptar from the String Array
            dataAdapter = new MyCustomAdapter(getApplicationContext(),
                    R.layout.item_check, countryList);
            ListView listView = (ListView) findViewById(R.id.listChequeo);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // When clicked, show a toast with the TextView text
                    ItemCheck country = (ItemCheck) parent.getItemAtPosition(position);

                }
            });

            checkButtonClick();
        }
    }

    class ConsultarDatos extends AsyncTask<String, String, String> {

        ArrayList<ItemCheck> countryList = new ArrayList<ItemCheck>();
        ItemCheck country;
        @Override
        protected void onPreExecute(){
            mProgressDialog.show();

        }
        @Override
        protected String doInBackground(String... f_url) {

            String url3 = "http://" + ip + "/consultarGeneral.php";

            List<NameValuePair> params3 = new ArrayList<NameValuePair>();
            params3.add(new BasicNameValuePair("sParametro", "consultarPlantillas"));
            params3.add(new BasicNameValuePair("sIdModAno", idmodano));
            String resultServer3 = getHttpPost(url3, params3);
            System.out.println(resultServer3);

            try {

                JSONArray jArray2 = new JSONArray(resultServer3);

                for (int j = 0; j < jArray2.length(); j++) {
                    JSONObject json2 = jArray2.getJSONObject(j);
                    final String[] datos =
                            new String[]{"Elem1","Elem2","Elem3","Elem4","Elem5"};
                    country = new ItemCheck(json2.getString("id").trim(),json2.getString("nombre").trim(),false,"0","0",datos);
                    countryList.add(country);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            url3 = "http://" + ip + "/consultarGeneral.php";

            params3 = new ArrayList<NameValuePair>();
            params3.add(new BasicNameValuePair("sParametro", "consultarPlantillasNueva"));
            resultServer3 = getHttpPost(url3, params3);
            System.out.println(resultServer3);

            try {

                JSONArray jArray2 = new JSONArray(resultServer3);

                for (int j = 0; j < jArray2.length(); j++) {
                    JSONObject json2 = jArray2.getJSONObject(j);
                    final String[] datos =
                            new String[]{"Elem1","Elem2","Elem3","Elem4","Elem5"};
                    country = new ItemCheck(json2.getString("id").trim(),json2.getString("nombre").trim(),false,"0","0",datos);
                    countryList.add(country);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            mProgressDialog.dismiss();
            //create an ArrayAdaptar from the String Array
            dataAdapter = new MyCustomAdapter(getApplicationContext(),
                    R.layout.item_check, countryList);
            ListView listView = (ListView) findViewById(R.id.listChequeo);
            // Assign adapter to ListView
            listView.setAdapter(dataAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // When clicked, show a toast with the TextView text
                    ItemCheck country = (ItemCheck) parent.getItemAtPosition(position);

                }
            });

            checkButtonClick();
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



    private class MyCustomAdapter extends ArrayAdapter<ItemCheck> {

        private ArrayList<ItemCheck> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<ItemCheck> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<ItemCheck>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
            EditText cant;
            EditText val;
            Spinner prices;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.item_check, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                holder.cant = (EditText) convertView.findViewById(R.id.editCant);
                holder.val = (EditText) convertView.findViewById(R.id.editValor);
                holder.prices = (Spinner) convertView.findViewById(R.id.spinnerPrices);

                holder.cant.setVisibility(View.GONE);
                holder.val.setVisibility(View.GONE);
                holder.prices.setVisibility(View.GONE);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        ItemCheck country = (ItemCheck) cb.getTag();

                        country.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ItemCheck country = countryList.get(position);
            holder.code.setText(" (" + country.getCode() + ")");
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);

            return convertView;

        }

    }

    private void checkButtonClick() {


        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<ItemCheck> countryList = dataAdapter.countryList;
                for(int i=0;i<countryList.size();i++){
                    ItemCheck country = countryList.get(i);
                    if(country.isSelected()){
                        responseText.append("\n" + country.getName());
                    }
                }



            }
        });

    }

    public void aceptar(View v){

        Intent i = new Intent(MercarPedido.this,MercarPedidoDetalleNuevo.class);
        ArrayList<String> codes = new ArrayList<>();
        ArrayList<ItemCheck> countryList = dataAdapter.countryList;
        for(int j=0;j<countryList.size();j++){
            ItemCheck country = countryList.get(j);
            if(country.isSelected()){
                codes.add(country.getCode());
            }
        }

        i.putStringArrayListExtra("codes",codes);
        i.putExtra("ip",ip);
        i.putExtra("parametro",elegido);
        startActivityForResult(i,request_code);
    }

    public void cancelar(View v){
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        try {

            if (requestCode == 1) {
                Intent intent = new Intent();
                Bundle args = data.getBundleExtra("codes");
                //products = (ArrayList<ArrayList>) args.getSerializable("codes");
                intent.putExtra("codes",args);
                setResult(3, intent);
                finish();


            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

