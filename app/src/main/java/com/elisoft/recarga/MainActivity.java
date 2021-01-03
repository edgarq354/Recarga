package com.elisoft.recarga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
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

import com.elisoft.recarga.notificaciones.SharedPrefManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    EditText et_telefono,et_codigo,et_monto,et_token;
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
    private int requestCode;
    private String[] permissions;
    private int[] grantResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_telefono=findViewById(R.id.et_telefono);
        et_codigo=findViewById(R.id.et_codigo);
        et_monto=findViewById(R.id.et_monto);
        et_token=findViewById(R.id.et_token);
        bt_guardar=findViewById(R.id.bt_guardar);
        bt_recargas=findViewById(R.id.bt_recargas);

        rb_tigo=findViewById(R.id.rb_tigo);
        rb_viva=findViewById(R.id.rb_viva);
        rb_entel=findViewById(R.id.rb_entel);


        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        bt_guardar.setOnClickListener(this);
        bt_recargas.setOnClickListener(this);





        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("Firebase token", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // guardaremos el token en las preferencias compartidas más tarde
                        SharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);


                        if (token != null || token == "") {
                            et_token.setText(token);

                        } else {
                            mensaje_error("No se a podido generar el Token. porfavor active sus datos de Red e instale Google Pay Service");
                        }

                    }
                });


        SharedPreferences prefe = getSharedPreferences("recarga", Context.MODE_PRIVATE);
        et_codigo.setText(prefe.getString("codigo_tigo",""));



        //VERIFICAR PERMISO DE LLAMADA
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            final String[] CAMERA_PERMISSIONS = { Manifest.permission.INTERNET,
                    Manifest.permission.CALL_PHONE };

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                //YA LO CANCELE Y VOUELVO A PERDIR EL PERMISO.

                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Atención!");
                dialogo1.setMessage("Debes otorgar permisos de acceso a llamada.");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Solicitar permiso", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        dialogo1.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this,
                                CAMERA_PERMISSIONS,
                                MY_PERMISSIONS_REQUEST_CALL_PHONE);

                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        dialogo1.cancel();

                    }
                });
                dialogo1.show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        CAMERA_PERMISSIONS,
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }


    }



    public void mensaje_error(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
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
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // PERMISO CONCEDIDO!
                } else {
                    Toast.makeText(getApplicationContext(), "No se tienen permisos CALL_PHONE!", Toast.LENGTH_LONG).show();
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
                SharedPreferences prefe = getSharedPreferences("recarga", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=prefe.edit();
                editor.putString("codigo_tigo",et_codigo.getText().toString().trim());
                editor.commit();

                if(rb_tigo.isChecked())
                {
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







                break;
            case R.id.bt_recargas:
                startActivity(new Intent(this, Lista_Recargas.class));
                break;
        }

    }




}
