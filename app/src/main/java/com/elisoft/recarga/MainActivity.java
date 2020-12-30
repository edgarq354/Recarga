package com.elisoft.recarga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    EditText et_telefono,et_codigo,et_monto;
    private final static int MY_PERMISSIONS_REQUEST_CALL_PHONE = 123;
    private TelephonyManager telephonyManager;

    Button bt_guardar,bt_recargas;

    String tigo="";
    String viva="";
    String entel="";
    int operador=0;

    RadioButton rb_tigo;
    RadioButton rb_viva;
    RadioButton rb_entel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_telefono=findViewById(R.id.et_telefono);
        et_codigo=findViewById(R.id.et_codigo);
        et_monto=findViewById(R.id.et_monto);
        bt_guardar=findViewById(R.id.bt_guardar);
        bt_recargas=findViewById(R.id.bt_recargas);

        rb_tigo=findViewById(R.id.rb_tigo);
        rb_viva=findViewById(R.id.rb_viva);
        rb_entel=findViewById(R.id.rb_entel);


        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        bt_guardar.setOnClickListener(this);
        bt_recargas.setOnClickListener(this);

        verificar_todos_los_permisos();

        SharedPreferences prefe = getSharedPreferences("recarga", Context.MODE_PRIVATE);
        et_codigo.setText(prefe.getString("codigo_tigo",""));

    }



/*

    private void requestUSSD(String USSD){

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //API >= 26
            TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            manager.sendUssdRequest(USSD, new TelephonyManager.UssdResponseCallback() {
                @Override
                public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                    super.onReceiveUssdResponse(telephonyManager, request, response);
                    Toast.makeText(getApplicationContext(), "onReceiveUssdResponse()" + response, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                    super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
                    Toast.makeText(getApplicationContext(), "onReceiveUssdResponseFailed()" + request, Toast.LENGTH_LONG).show();
                }
            }, new Handler());
        }else{      //API < 26
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +USSD));
            startActivity(callIntent);
        }

    }
*/
    //Detecta si los permisos fueron concedidos (android 6.0+)
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        int per=0;


        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISO CONCEDIDO!
                } else {
                    Toast.makeText(getApplicationContext(), "No se tienen permisos CALL_PHONE!", Toast.LENGTH_LONG).show();
                }
                return;


            }

            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 ) {
                    for (int i=0;i<grantResults.length;i++){
                        if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            per++;
                        }
                    }

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    finish();
                }

                if(per<grantResults.length){
                    finish();
                }else
                {

                }
                return;
            }
        }


    }
    private void dailNumber(String USSD) {
        //startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + USSD)));
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        //callIntent.putExtra("simSlot", 0);
       // callIntent.putExtra("com.android.phone.extra.slot", operador);
        callIntent.putExtra("simSlot", operador); //For sim 1
        callIntent.setData(Uri.parse("tel:" + USSD));
        startActivity(callIntent);
    }


    private void requestUSSD(String USSD){

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CALL_PHONE);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //API >= 26

           telephonyManager.sendUssdRequest("*105#", new TelephonyManager.UssdResponseCallback() {
                @Override
                public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                    super.onReceiveUssdResponse(telephonyManager, request, response);

                    Log.d("Received response","okay"+response.toString());
                  //  ((TextView)findViewById(R.id.response)).setText(response);
                }

                @Override
                public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                    super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
                    Log.e("ERROR ","can't receive response"+failureCode);
                }
            },new Handler(Looper.getMainLooper()){
                @Override
                public void handleMessage(Message msg) {
                    Log.e("ERROR","error");
                }
            });
            /*
            TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            manager.sendUssdRequest(USSD, new TelephonyManager.UssdResponseCallback() {
                @Override
                public void onReceiveUssdResponse(TelephonyManager telephonyManager, String request, CharSequence response) {
                    super.onReceiveUssdResponse(telephonyManager, request, response);
                    Toast.makeText(getApplicationContext(), "onReceiveUssdResponse()" + response, Toast.LENGTH_LONG).show();
                }
                @Override
                public void onReceiveUssdResponseFailed(TelephonyManager telephonyManager, String request, int failureCode) {
                    super.onReceiveUssdResponseFailed(telephonyManager, request, failureCode);
                    Toast.makeText(getApplicationContext(), "onReceiveUssdResponseFailed()" + request, Toast.LENGTH_LONG).show();
                }
            }, new Handler());
            */

/*
            try {
                Log.e("ussd","trying to send ussd request");
                telephonyManager.sendUssdRequest("*123#",
                        callback,
                        handler);
            }catch (Exception e){


                String msg= e.getMessage();
                Log.e("DEBUG",e.toString());
                e.printStackTrace();
            }
*/
        }else{      //API < 26
            TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = mngr.getLine1Number();
            startService(new Intent(this, XXXX.class));
            dailNumber(USSD);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_guardar:
               recargar_crerdito();

                break;
            case R.id.bt_recargas:
                startActivity(new Intent(this,Lista_Recargas.class));
                break;
        }


    }

    private void recargar_crerdito() {
        if(rb_tigo.isChecked())
        {
            SharedPreferences prefe = getSharedPreferences("recarga", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=prefe.edit();
            editor.putString("codigo_tigo",et_codigo.getText().toString().trim());
            editor.commit();

            startService(new Intent(this, XXXX.class));
            Intent servicio_recarga=new Intent(this, Servicio_recargar.class);
            servicio_recarga.putExtra("operador","0");
            servicio_recarga.putExtra("numero",et_telefono.getText().toString().trim());
            servicio_recarga.putExtra("monto",et_monto.getText().toString().trim());
            servicio_recarga.putExtra("codigo",et_codigo.getText().toString().trim());
            servicio_recarga.putExtra("id_recarga","0");
            servicio_recarga.putExtra("empresa","TIGO");
            startService(servicio_recarga);
/*
            operador=Integer.parseInt(intent.getStringExtra("operador"));
            numero=intent.getStringExtra("numero");
            monto=intent.getStringExtra("monto");
            codigo=intent.getStringExtra("codigo");
            id_recarga=intent.getStringExtra("id_recarga");
            empresa=intent.getStringExtra("empresa");

            operador=0;
            String USSD = Uri.encode("*") + "555" + Uri.encode("#") +"3"+ Uri.encode("#") +"2"+ Uri.encode("#") +et_monto.getText().toString().trim()+Uri.encode("#")+et_telefono.getText().toString().trim()+Uri.encode("#")+et_codigo.getText().toString().trim()+Uri.encode("#");
            requestUSSD(USSD);
            */
        }else if(rb_viva.isChecked()){

        }else if(rb_entel.isChecked()){

            startService(new Intent(this, XXXX.class));
            Intent servicio_recarga=new Intent(this, Servicio_recargar.class);
            servicio_recarga.putExtra("operador","1");
            servicio_recarga.putExtra("numero",et_telefono.getText().toString().trim());
            servicio_recarga.putExtra("monto",et_monto.getText().toString().trim());
            servicio_recarga.putExtra("codigo",et_codigo.getText().toString().trim());
            servicio_recarga.putExtra("id_recarga","0");
            servicio_recarga.putExtra("empresa","ENTEL");
            startService(servicio_recarga);

            /*
            operador=1;
            String USSD = Uri.encode("*") + "133" + Uri.encode("*")+et_telefono.getText().toString().trim()+Uri.encode("*")+et_monto.getText().toString().trim()+Uri.encode("*")+"1"+Uri.encode("#");
            requestUSSD(USSD);
            */
        }
    }


    public void verificar_todos_los_permisos()
    {

        String[] SMS_PERMISSIONS1 = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE };


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            SMS_PERMISSIONS1 = new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CALL_PHONE };
        }



        ActivityCompat.requestPermissions(MainActivity.this,
                SMS_PERMISSIONS1,
                1);


    }




}
