package com.udl.monitorizacion;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**********
 *
 **********/
public class StatusWSAdapter extends ArrayAdapter<WebService> {
    private Activity context;
    private ArrayList<WebService> webServices;

    public StatusWSAdapter(Activity context, ArrayList<WebService> webServices) {
        super(context, R.layout.status_server_adapter, webServices);
        this.context = context;
        this.webServices = webServices;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View item = context.getLayoutInflater().inflate(R.layout.status_ws_adapter, parent, false);
        WebService ws = webServices.get(position);
        TextView txtViewWS = (TextView) item.findViewById(R.id.txtViewWS);
        TextView txtViewWSName = (TextView) item.findViewById(R.id.txtViewWSName);
        txtViewWSName.setText(ws.getName());
        if (ws.getStatus()) {

            txtViewWS.setBackgroundColor(Color.GREEN);
        } else {
            txtViewWS.setBackgroundColor(Color.RED);
        }

        return item;
    }

}
