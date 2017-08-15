package trizio.ram.com.trizioservireencauche;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class EditarSolicitud extends Activity {
    String cod_empresa = "";
    String cod_sucursal = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_solicitud);
        Bundle bundle = this.getIntent().getExtras();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);


        final String conexion = bundle.getString("conexion");
        final String ip = bundle.getString("ip");
        final String atributo = bundle.getString("atributo");
        cod_empresa = bundle.getString("empresa");
        cod_sucursal = bundle.getString("sucursal");
    }

    public void aceptar(View v){
        EditText solicitud = (EditText) findViewById(R.id.editSolicitud);
        Intent data = new Intent();
        data.putExtra("parametro", "solicitud");
        data.putExtra("valor", solicitud.getText().toString().trim());
        //data.setDataAndType(Uri.parse(procesos[position+1].trim()),procesos[position].trim().toString());

        setResult(2, data);
        finish();

    }
}
