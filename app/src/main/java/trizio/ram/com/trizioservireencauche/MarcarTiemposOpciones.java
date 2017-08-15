package trizio.ram.com.trizioservireencauche;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MarcarTiemposOpciones extends Activity {
    String empresa = "";
    String sucursal = "";
    String conexion = "";
    String ip = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marcar_tiempos_opciones);

        empresa = getIntent().getExtras().getString("empresa");
        sucursal = getIntent().getExtras().getString("sucursal");
        conexion = getIntent().getExtras().getString("conexion");
        ip = getIntent().getExtras().getString("ip");

        ArrayList datos = new ArrayList();
        datos.add(new Lista_entrada(R.drawable.recoger, "Fecha inicial"));
        datos.add(new Lista_entrada(R.drawable.entregar, "Fecha final"));


        ListView lista = (ListView) findViewById(R.id.listView3);
        lista.setAdapter(new Lista_adaptador(this, R.layout.entrada, datos) {
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

                if (elegido.get_textoEncima().equals("Fecha inicial")) {

                    Intent i = new Intent(MarcarTiemposOpciones.this,MarcarTiemposRombo.class);
                    i.putExtra("tipo","inicial");
                    i.putExtra("empresa",empresa);
                    i.putExtra("sucursal",sucursal);
                    i.putExtra("conexion",conexion);
                    i.putExtra("ip",ip);
                    startActivity(i);
                }else{
                    Intent i = new Intent(MarcarTiemposOpciones.this,MarcarTiemposRombo.class);
                    i.putExtra("tipo","final");
                    i.putExtra("empresa",empresa);
                    i.putExtra("sucursal",sucursal);
                    i.putExtra("conexion",conexion);
                    i.putExtra("ip",ip);
                    startActivity(i);
                }


            }
        });
    }
}
