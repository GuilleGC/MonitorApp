package com.udl.monitorizacion;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**********
 *
 **********/
public class StatusServerAdapter extends ArrayAdapter<Server> {

    private Activity context;
    private ArrayList<Server> servers;

    public StatusServerAdapter(Activity context, ArrayList<Server> servers) {
        super(context, R.layout.status_server_adapter, servers);
        this.context = context;
        this.servers = servers;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View item = context.getLayoutInflater().inflate(R.layout.status_server_adapter, null);
        Server s = servers.get(position);
        TextView txtViewServer = (TextView) item.findViewById(R.id.txtViewServerStatus);
        TextView txtViewCpu = (TextView) item.findViewById(R.id.txtViewCpu);
        TextView txtViewRam = (TextView) item.findViewById(R.id.txtViewRam);
        txtViewServer.append(s.getServer());
        txtViewCpu.append(s.getCpu());
        txtViewRam.append(s.getRam());
        return item;
    }

}
