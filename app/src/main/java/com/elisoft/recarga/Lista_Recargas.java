package com.elisoft.recarga;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Lista_Recargas extends AppCompatActivity {



    //variables de solicitudes
    private ProgressDialog pDialog;
    ArrayList<CRecargas> cRecargas=new ArrayList<CRecargas>();

    CRecargas recargar_ultimo=null;

    ListView lv_lista;
    // fin de variables de solicitudes

    AlertDialog.Builder dialogo1 ;
    Suceso suceso;


    MediaPlayer mp;



    Date fecha_conexion;

    RequestQueue queue=null;





    @Override
    protected void onStart() {

        servicio_volley_lista();

        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista__recargas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        lv_lista=(ListView)findViewById(R.id.lv_lista);



//AUDIO

//AUDIO
       // setSonido(0);


        lv_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CRecargas hi=new CRecargas();
                hi=cRecargas.get(i);

              //  abrir_solicitud(hi);

            }
        });



        servicio_volley_lista();
        fecha_conexion = new Date();



        /*
        long diferencia=0;
        Date fecha_actual=new Date();
        try {


            //obtienes la diferencia de las fechas
            diferencia = Math.abs(fecha_actual.getTime() - fecha_conexion.getTime());
        }catch (Exception ee)
        {
            diferencia=11000;
        }

        if(diferencia>10000)
        {
            fecha_conexion=new Date();
            servicio_volley_lista();
        }

*/
    }

    private void servicio_volley_lista() {

        try {
            String v_url= getString(R.string.servidor)+"frmRecarga.php?opcion=lista_de_recarga";


            JSONObject jsonParam= new JSONObject();
            jsonParam.put("ci", "ci");
            if (queue == null) {
                queue = Volley.newRequestQueue(this);
                Log.e("volley","Setting a new request queue");
            }


            JsonObjectRequest myRequest= new JsonObjectRequest(
                    Request.Method.POST,
                    v_url,
                    jsonParam,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject respuestaJSON) {


                            try {
                                suceso=new Suceso(respuestaJSON.getString("suceso"),respuestaJSON.getString("mensaje"));
                                recargar_ultimo=null;

                                if (suceso.getSuceso().equals("1")) {
                                    JSONArray usu=respuestaJSON.getJSONArray("lista");
                                    cRecargas=new ArrayList<CRecargas>();


                                    for (int i=0;i<usu.length();i++)
                                    {
                                        String id=usu.getJSONObject(i).getString("id");
                                        String numero=usu.getJSONObject(i).getString("numero");
                                        String monto=usu.getJSONObject(i).getString("monto");
                                        String empresa=usu.getJSONObject(i).getString("empresa");
                                        String id_usuario=usu.getJSONObject(i).getString("id_usuario");
                                        String fecha=usu.getJSONObject(i).getString("fecha");
                                        String fecha_recarga=usu.getJSONObject(i).getString("fecha_recarga");
                                        String pagado=usu.getJSONObject(i).getString("pagado");
                                        String estado=usu.getJSONObject(i).getString("estado");
                                        String mensaje_empresa=usu.getJSONObject(i).getString("mensaje_empresa");
                                        String nombre_usuario=usu.getJSONObject(i).getString("nombre_usuario");


                                        cRecargas.add(new CRecargas(
                                                Integer.parseInt(id),
                                                numero,
                                                monto,
                                                empresa,
                                                id_usuario,
                                                fecha,
                                                fecha_recarga,
                                                pagado,
                                                estado,
                                                mensaje_empresa,
                                                nombre_usuario));

                                        if(estado.equals("SOLICITUD"))
                                        {
                                        recargar_ultimo=new CRecargas(
                                                Integer.parseInt(id),
                                                numero,
                                                monto,
                                                empresa,
                                                id_usuario,
                                                fecha,
                                                fecha_recarga,
                                                pagado,
                                                estado,
                                                mensaje_empresa,
                                                nombre_usuario);
                                        }
                                    }

                                    Item_recarga adaptador = new Item_recarga(Lista_Recargas.this,cRecargas);
                                    lv_lista.setAdapter(adaptador);

                                } else  {
                                    cRecargas.clear();
                                    Item_recarga adaptador = new Item_recarga(Lista_Recargas.this,cRecargas);
                                    lv_lista.setAdapter(adaptador);
                                }

                                //...final de final....................






                                if(recargar_ultimo!=null)
                                {
                                    Intent servicio_recarga=new Intent(Lista_Recargas.this, Servicio_recargar_actualizado.class);
                                    servicio_recarga.putExtra("id_recarga",String.valueOf(recargar_ultimo.getId()));
                                    servicio_recarga.putExtra("mensaje_empresa","");
                                    servicio_recarga.putExtra("estado","PROCESO");
                                    servicio_recarga.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startService(servicio_recarga);

                                    recargar_credito(recargar_ultimo);
                                    SystemClock.sleep(20000);
                                }else{
                                    SystemClock.sleep(20000);
                                }
                                servicio_volley_lista();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                mensaje_error("Falla en tu conexión a Internet.");
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    mensaje_error("Falla en tu conexión a Internet.");
                }
            }
            ){
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> parametros= new HashMap<>();
                    parametros.put("content-type","application/json; charset=utf-8");
                    parametros.put("Authorization","apikey 849442df8f0536d66de700a73ebca-us17");
                    parametros.put("Accept", "application/json");

                    return  parametros;
                }
            };


            myRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(myRequest);

        } catch (Exception e) {

        }


    }

    private void recargar_credito(CRecargas recargar_ultimo) {

        SharedPreferences prefe = getSharedPreferences("recarga", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=prefe.edit();
        editor.putString("id_recarga",String.valueOf(recargar_ultimo.getId()));
        editor.commit();
        String codigo_tigo=prefe.getString("codigo_tigo","");

        String ci=prefe.getString("id_recarga", "");
            if(recargar_ultimo.getEmpresa().equals("TIGO"))
            {
                startService(new Intent(this, XXXX.class));
                Intent servicio_recarga=new Intent(this, Servicio_recargar.class);
                servicio_recarga.putExtra("operador","0");
                servicio_recarga.putExtra("numero",recargar_ultimo.getNumero().toString().trim());
                servicio_recarga.putExtra("monto",recargar_ultimo.getMonto().toString().trim());
                servicio_recarga.putExtra("codigo",codigo_tigo);
                servicio_recarga.putExtra("id_recarga",String.valueOf(recargar_ultimo.getId()));
                servicio_recarga.putExtra("empresa",recargar_ultimo.getEmpresa());
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
            }else if(recargar_ultimo.getEmpresa().equals("VIVA")){

            }else if(recargar_ultimo.getEmpresa().equals("ENTEL")){

                startService(new Intent(this, XXXX.class));
                Intent servicio_recarga=new Intent(this, Servicio_recargar.class);
                servicio_recarga.putExtra("operador","1");
                servicio_recarga.putExtra("numero",recargar_ultimo.getNumero().toString().trim());
                servicio_recarga.putExtra("monto",recargar_ultimo.getMonto().toString().trim());
                servicio_recarga.putExtra("codigo",codigo_tigo);
                servicio_recarga.putExtra("id_recarga",String.valueOf(recargar_ultimo.getId()));
                servicio_recarga.putExtra("empresa",recargar_ultimo.getEmpresa());
                startService(servicio_recarga);

            /*
            operador=1;
            String USSD = Uri.encode("*") + "133" + Uri.encode("*")+et_telefono.getText().toString().trim()+Uri.encode("*")+et_monto.getText().toString().trim()+Uri.encode("*")+"1"+Uri.encode("#");
            requestUSSD(USSD);
            */
            }
    }

    public void mensaje_error_final(String mensaje)
    {try {
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                finish();
            }
        });
        builder.create();
        builder.show();
    }catch (Exception e)
    {
        Log.e("mensaje error final",e.toString());
    }
    }

    //FIN DE SERVICIO DE COORDENADAS
    public void mensaje_error(String mensaje)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Importante");
        builder.setMessage(mensaje);
        builder.setPositiveButton("OK", null);
        builder.create();
        builder.show();
    }








}