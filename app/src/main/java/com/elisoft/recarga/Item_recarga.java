package com.elisoft.recarga;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class Item_recarga extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<CRecargas> items;
    Bundle savedInstanceState;


    public Item_recarga(Activity activity, ArrayList<CRecargas> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<CRecargas> Pedidos) {
        for (int i = 0; i < Pedidos.size(); i++) {
            items.add(Pedidos.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_recarga, null);
        }

        CRecargas ped = items.get(position);

        TextView tv_nombre= (TextView) v.findViewById(R.id.tv_nombre);
        TextView tv_numero= (TextView) v.findViewById(R.id.tv_numero);
        TextView tv_monto= (TextView) v.findViewById(R.id.tv_monto);
        TextView tv_empresa= (TextView) v.findViewById(R.id.tv_empresa);
        TextView tv_estado= (TextView) v.findViewById(R.id.tv_estado);
        TextView tv_pagado= (TextView) v.findViewById(R.id.tv_pagado);
        TextView tv_mensaje_empresa= (TextView) v.findViewById(R.id.tv_mensaje_empresa);
        TextView tv_fecha= (TextView) v.findViewById(R.id.tv_fecha);

        LinearLayout ll_item=v.findViewById(R.id.ll_item);


        tv_nombre.setText(ped.getNombre_usuario());
        tv_numero.setText(ped.getNumero() );
        tv_monto.setText(ped.getNumero() );
        tv_empresa.setText(ped.getEmpresa() );
        tv_estado.setText(ped.getEstado() );
        tv_pagado.setText(ped.getPagado() );
        tv_mensaje_empresa.setText(ped.getMensaje_empresa());
        tv_fecha.setText(ped.getFecha());


        if(ped.getEstado().equals("SOLICITUD"))
        {
            try {
                ll_item.setBackgroundResource(R.color.colorSOLICITUD);
            }catch (Exception e)
            {
                Log.e("Estado",e.toString());
            }
        }else if(ped.getEstado().equals("RECARGADO"))
        {
            try {
                ll_item.setBackgroundResource(R.color.colorRECARGADO);
            }catch (Exception e)
            {
                Log.e("Estado",e.toString());
            }
        }else
        {
            try {
                ll_item.setBackgroundResource(R.color.colorERROR);
            }catch (Exception e)
            {
                Log.e("Estado",e.toString());
            }
        }

        return v;
    }


}



