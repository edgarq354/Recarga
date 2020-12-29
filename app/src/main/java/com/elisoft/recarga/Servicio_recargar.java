package com.elisoft.recarga;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class Servicio_recargar  extends IntentService {
    Suceso suceso;
    String numero="";
    String codigo="";
    String monto="";
    String id_recarga="";
    String empresa="";
    int operador=0;
    private static final String TAG = Servicio_recargar.class.getSimpleName();
    RequestQueue queue=null;



    public Servicio_recargar() {
        super("Servicio_recargar");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();

            operador=Integer.parseInt(intent.getStringExtra("operador"));
            numero=intent.getStringExtra("numero");
            monto=intent.getStringExtra("monto");
            codigo=intent.getStringExtra("codigo");
            id_recarga=intent.getStringExtra("id_recarga");
            empresa=intent.getStringExtra("empresa");


            handleActionRun();
            if (Constants.ACTION_RUN_ISERVICE.equals(action)) {


            }
        }
    }

    /**
     * Maneja la acción de ejecución del servicio
     */
    private void handleActionRun() {



        if(empresa.equals("TIGO"))
        {
           // operador=0;
            String USSD = Uri.encode("*") + "555" + Uri.encode("#") +"3"+ Uri.encode("#") +"2"+ Uri.encode("#") +monto+Uri.encode("#")+numero+Uri.encode("#")+codigo+Uri.encode("#");
            dailNumber(USSD);
        }else if(empresa.equals("VIVA")){

        }else if(empresa.equals("ENTEL")){
           // operador=1;
            String USSD = Uri.encode("*") + "133" + Uri.encode("*")+numero+Uri.encode("*")+monto+Uri.encode("*")+"1"+Uri.encode("#");
            dailNumber(USSD);
        }
        //  Thread.sleep(1000);
        // Quitar de primer plano
        //  stopForeground(true);
        // stopService(new Intent(this,Servicio_recargar.class));
        // si nuestro estado esta en 2 o mayor .. quiere decir que no nuestro pedido se finalizo o sino se cancelo... sin nninguna carrera...


    }

    private void servicio_monto_total_por_id_pedido(String sid_pedido2) {

        try {

            JSONObject jsonParam= new JSONObject();
            jsonParam.put("id_pedido", sid_pedido2);

            String url=getString(R.string.servidor) + "frmPedido.php?opcion=monto_total_por_id_pedido";
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
                Log.e("volley","Setting a new request queue");
            }


            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {
                            try {
                                suceso= new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));

                                if (suceso.getSuceso().equals("1")) {
                                  //  monto_total=respuestaJSON.getString("monto_total");


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }
            ){
                public Map<String,String> getHeaders() throws AuthFailureError {
                    Map<String,String> parametros= new HashMap<>();
                    parametros.put("content-type","application/json; charset=utf-8");
                    parametros.put("Authorization","apikey 849442df8f0536d66de700a73ebca-us17");
                    parametros.put("Accept", "application/json");

                    return  parametros;
                }
            };


            // TIEMPO DE ESPERA
            myRequest.setRetryPolicy(new DefaultRetryPolicy(6000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myRequest);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {




        SharedPreferences pedido=getSharedPreferences("ultimo_pedido",MODE_PRIVATE);
        SharedPreferences.Editor edit=pedido.edit();
        edit.putString("id_pedido","");
        edit.commit();

        if (queue != null) {
            queue.stop();
        }





        // Emisión para avisar que se terminó el servicio
        //Intent localIntent = new Intent(Constants.ACTION_PROGRESS_EXIT);
       // LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }




    //VERIFICAR SI ESTA CON CONEXION WIFI
    protected Boolean conectadoWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                return info.isConnected();
            }
        }
        return false;
    }
    //VERIFICAR SI ESTA CON CONEXION DE DATOS
    protected Boolean conectadoRedMovil(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                return info.isConnected();
            }
        }
        return false;
    }

    protected Boolean estaConectado(){
        if(conectadoWifi()){
            return true;
        }else{
            return conectadoRedMovil();
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
}

