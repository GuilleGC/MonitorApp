package com.udl.monitorizacion;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        //Toast.makeText(getApplicationContext(), "Cargando Datos", Toast.LENGTH_LONG).show();
    }


    public void getCpuInfo() {
        try {
            Process proc = Runtime.getRuntime().exec("cat /proc/stat");
            InputStream is = proc.getInputStream();
            //TextView tv = (TextView)findViewById(R.id.tvcmd);
            //tv.setText(getStringFromInputStream(is));
            /*String cpu_info = getStringFromInputStream(is);
            String cpu_usage = cpu_info.split("BogoMIPS")[1].split("\n")[0];
            */
            Toast.makeText(getApplicationContext(), getStringFromInputStream(is), Toast.LENGTH_LONG).show();
            Log.w("CPU", "------ getCPUInfo ---" + getStringFromInputStream(is));
        }
        catch (IOException e) {
            Log.w("CPU", "------ getCpuInfo ERROR");
        }
    }

    public void getMemoryInfo() {
        try {
            Process proc = Runtime.getRuntime().exec("cat /proc/meminfo");
            InputStream is = proc.getInputStream();
            //TextView tv = (TextView)findViewById(R.id.tvcmd);
            //tv.setText(getStringFromInputStream(is));
            String mem_info = getStringFromInputStream(is);
            Toast.makeText(this, mem_info, Toast.LENGTH_LONG).show();
            Log.w("MEM", "------ getMemoryInfo ---" + getStringFromInputStream(is));

        } catch (IOException e) {
            Log.w("MEM", "------ getMemoryInfo ERROR");
        }
    }

    private static String getStringFromInputStream(InputStream is) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;

        try {
            while((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        }
        catch (IOException e) {
            Log.e("", "------ getStringFromInputStream " + e.getMessage());
        } finally {
            if(br != null) {
                try {
                    br.close();
                }
                catch (IOException e) {
                    Log.e("", "------ getStringFromInputStream " + e.getMessage());
                }
            }
        }

        return sb.toString();
    }

    @Override
    protected void onResume() {
        //getCpuInfo();
        //getMemoryInfo();
        super.onResume();
    }
}
