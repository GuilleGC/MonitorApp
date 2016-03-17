package com.udl.monitorizacion;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**********
 *
 **********/
public class MonitorActivity extends Activity {

    ListView listView;
    GridView gridView;
    StatusServerAdapter statusServerAdapter;
    StatusWSAdapter statusWSAdapter;
    ArrayList<Server> listServers;
    ArrayList<WebService> listWebServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monitor_layout);
        listServers = new ArrayList<Server>();
        listWebServices = new ArrayList<WebService>();


        get_data();
        listView = (ListView) findViewById(R.id.listViewEstado);
        gridView = (GridView) findViewById(R.id.gridViewWS);

        statusServerAdapter = new StatusServerAdapter(this, listServers);
        statusWSAdapter = new StatusWSAdapter(this, listWebServices);

        listView.setAdapter(statusServerAdapter);
        gridView.setAdapter(statusWSAdapter);


    }

    public void get_data() {
        //Conexion para obtener datos
        listServers.add(new Server("MasterServer", "20%", "46%"));
        listServers.add(new Server("SlaveServer", "80%", "76%"));

        listWebServices.add(new WebService("Web Compras", true));
        listWebServices.add(new WebService("Web Mantenimiento", false));
        listWebServices.add(new WebService("Web Mantenimiento2", false));
        listWebServices.add(new WebService("Web Pedidos", true));

        Toast.makeText(this, "Cargando Datos", Toast.LENGTH_LONG);
    }
}
