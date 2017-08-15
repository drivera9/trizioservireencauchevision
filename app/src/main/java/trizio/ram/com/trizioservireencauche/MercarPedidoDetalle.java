package trizio.ram.com.trizioservireencauche;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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

public class MercarPedidoDetalle extends Activity {
    MyCustomAdapter dataAdapter = null;
    ProgressDialog mProgressDialog;
    ArrayList<String> codes;
    MyCustomAdapter.ViewHolder holder = null;
    String ip = "";
    String[] cantUpdates ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mercar_pedido_detalle);
        ip = getIntent().getExtras().getString("ip");
        codes = getIntent().getExtras().getStringArrayList("codes");

        mProgressDialog= new ProgressDialog(MercarPedidoDetalle.this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Consultando datos...");

        new ConsultarDatos().execute("");
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


            for (int i = 0;i<codes.size();i++){
                String url3 = "http://" + ip + "/consultarGeneral.php";

                List<NameValuePair> params3 = new ArrayList<NameValuePair>();
                params3.add(new BasicNameValuePair("sParametro", "consultarPlantillasLin"));
                params3.add(new BasicNameValuePair("sId", codes.get(i)));
                String resultServer3 = getHttpPost(url3, params3);
                System.out.println(resultServer3);
                String[] prices = new String[0];
                try {

                    JSONArray jArray2 = new JSONArray(resultServer3);

                    for (int j = 0; j < jArray2.length(); j++) {
                        JSONObject json2 = jArray2.getJSONObject(j);

                        String url4 = "http://" + ip + "/consultarGeneral.php";

                        List<NameValuePair> params4 = new ArrayList<NameValuePair>();
                        params4.add(new BasicNameValuePair("sParametro", "consultarPlantillasLinPrices"));
                        params4.add(new BasicNameValuePair("sProductoId", json2.getString("Producto_Id").trim()));
                        String resultServer4 = getHttpPost(url4, params4);
                        System.out.println(resultServer4);

                        try {

                            JSONArray jArray4 = new JSONArray(resultServer4);
                            prices = new String[10];
                            for (int k = 0; k < jArray4.length(); k++) {
                                JSONObject json = jArray4.getJSONObject(k);

                                prices[k] = (json.getString("precio_1").trim());
                                prices[k+1] = (json.getString("precio_2").trim());
                                prices[k+2] = (json.getString("precio_3").trim());
                                prices[k+3] = (json.getString("precio_4").trim());
                                prices[k+4] = (json.getString("precio_5").trim());
                                prices[k+5] = (json.getString("precio_6").trim());
                                prices[k+6] = (json.getString("precio_7").trim());
                                prices[k+7] = (json.getString("precio_8").trim());
                                prices[k+8] = (json.getString("precio_9").trim());
                                prices[k+9] = (json.getString("precio_10").trim());

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        country = new ItemCheck(json2.getString("Producto_Id").trim(),json2.getString("descripcion").trim(),true,json2.getString("Cantidad").trim(),
                                json2.getString("precio").trim(),prices);
                        countryList.add(country);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

            cantUpdates = new String[listView.getCount()];

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
        public View getView(final int position, View convertView, ViewGroup parent) {


            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.item_check, null);

                holder = new MyCustomAdapter.ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                holder.cant = (EditText) convertView.findViewById(R.id.editCant);

                /*final ItemCheck row = countryList.get(position);
                if (holder.cant != null)
                    holder.cant.setText(row.getCant());
                holder.cant.addTextChangedListener(new EditTextWatcher(row));*/

                holder.val = (EditText) convertView.findViewById(R.id.editValor);

                /*final ItemCheck row2 = countryList.get(position);
                if (holder.val != null)
                    holder.val.setText(row2.getVal());
                holder.val.addTextChangedListener(new EditTextWatcherVal(row2));*/



                holder.prices = (Spinner) convertView.findViewById(R.id.spinnerPrices);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        ItemCheck country = (ItemCheck) cb.getTag();

                        country.setSelected(cb.isChecked());
                    }
                });
            } else {
                holder = (MyCustomAdapter.ViewHolder) convertView.getTag();
            }

            final ItemCheck country = countryList.get(position);
            holder.code.setText(" (" + country.getCode() + ")");
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isSelected());
            holder.name.setTag(country);

            ArrayAdapter<String> adaptador =
                    new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.spinner_item, country.getPrices());
            adaptador.setDropDownViewResource(
                    R.layout.spinner_item_drop);
            holder.prices.setAdapter(adaptador);

            holder.prices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView adapter, View v, int i, long lng) {

                    //country.setVal(adapter.getItemAtPosition(i).toString());
                    //holder.val.setText(country.getVal());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parentView)
                {

                }
            });


            holder.cant.setText(country.getCant());

            holder.cant.getBackground().mutate().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);

            holder.val.setText(country.getVal());

            holder.val.getBackground().mutate().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);



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

        ArrayList<ArrayList> codes = new ArrayList<>();
        ArrayList<ItemCheck> countryList = dataAdapter.countryList;


        ArrayList<String> products = new ArrayList<>();



        for(int j=0;j<countryList.size();j++){
            ItemCheck country = countryList.get(j);
            products = new ArrayList<>();
            products.add(country.getCode());
            products.add(country.getCant());
            products.add(country.getVal());
            products.add(String.valueOf(country.getListaPrice()));
            if(country.isSelected()){
                codes.add(products);
            }
        }

        Intent intent = new Intent();
        Bundle args = new Bundle();
        args.putSerializable("ARRAYLIST",codes);
        intent.putExtra("codes",args);
        setResult(3, intent);
        finish();

    }

    public void cancelar(View v){
        finish();
    }
}
