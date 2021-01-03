package com.elisoft.recarga.notificaciones;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.Vibrator;
import android.util.Log;


import com.elisoft.recarga.MainActivity;
import com.elisoft.recarga.Servicio_recargar;
import com.elisoft.recarga.XXXX;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ROMAN on 24/11/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String id_recarga="0";

    private static final String TAG = "MyFirebaseMsgService";
    private Vibrator vibrator;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {

            //envio de ultima ubicacion del motista


             Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

        }
    }

    private void sendPushNotification(JSONObject json) {
        // opcionalmente podemos mostrar el json en log
       // Log.e(TAG, "Notification JSON " + json.toString());
      //  volumen();
        try {
            // obtener los datos de json
            JSONObject data = json.getJSONObject("data");

            // análisis de datos json
            id_recarga= data.getString("id_recarga");

            String title = data.getString("title");
            String message = data.getString("message");

            String numero = data.getString("numero");
            String monto = data.getString("monto");
            String empresa = data.getString("empresa");
            String tipo = data.getString("tipo");





            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
switch (tipo)
{
    case "1000":
        //usuario
        //se iniciar el servicio de obtencion de coordenadas deltaxi...

    try {



/*
        Intent servicio_contacto = new Intent(MyFirebaseMessagingService.this, Servicio_guardar_contacto.class);
        servicio_contacto.setAction(Constants.ACTION_RUN_ISERVICE);
        servicio_contacto.putExtra("nombre",ped2.getString("nombre_taxi", ""));
        servicio_contacto.putExtra("telefono",ped2.getString("celular", ""));
        startService(servicio_contacto);
*/
    }catch (Exception e)
    {
        e.printStackTrace();
    }




        SharedPreferences prefe = getSharedPreferences("recarga", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        editor.putString("id_recarga",String.valueOf(id_recarga));
        editor.commit();
        String codigo_tigo=prefe.getString("codigo_tigo","");

        String ci=prefe.getString("id_recarga", "");

        mNotificationManager.notificacion_con_activity(title, message, new Intent(getApplicationContext(),MainActivity.class));

        if(empresa.equals("TIGO"))
        {
            startService(new Intent(this, XXXX.class));
            Intent servicio_recarga=new Intent(this, Servicio_recargar.class);
            servicio_recarga.putExtra("operador","0");
            servicio_recarga.putExtra("numero",numero);
            servicio_recarga.putExtra("monto",monto);
            servicio_recarga.putExtra("codigo",codigo_tigo);
            servicio_recarga.putExtra("id_recarga",id_recarga);
            servicio_recarga.putExtra("empresa",empresa);
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
        }else if(empresa.equals("VIVA")){

        }else if(empresa.equals("ENTEL")){

            startService(new Intent(this, XXXX.class));
            Intent servicio_recarga=new Intent(this, Servicio_recargar.class);
            servicio_recarga.putExtra("operador","1");
            servicio_recarga.putExtra("numero",numero);
            servicio_recarga.putExtra("monto",monto);
            servicio_recarga.putExtra("codigo",codigo_tigo);
            servicio_recarga.putExtra("id_recarga",id_recarga);
            servicio_recarga.putExtra("empresa",empresa);
            startService(servicio_recarga);

            /*
            operador=1;
            String USSD = Uri.encode("*") + "133" + Uri.encode("*")+et_telefono.getText().toString().trim()+Uri.encode("*")+et_monto.getText().toString().trim()+Uri.encode("*")+"1"+Uri.encode("#");
            requestUSSD(USSD);
            */
        }



        break;

    default:
        // crear una intención para la notificación

        break;
}



        } catch (JSONException e) {
           Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
           Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


}