package com.elisoft.recarga;

public class CRecargas {

   private int id;
   private String numero;
   private String monto;
   private String empresa;
   private String id_usuario;
   private String fecha;
   private String fecha_recarga;
   private String pagado;
   private String estado;
   private String mensaje_empresa;
   private String nombre_usuario;

    public CRecargas()
    {
        setId(0);
        setNumero("");
        setMonto("");
        setEmpresa("");
        setId_usuario("");
        setFecha("");
        setFecha_recarga("");
        setPagado("");
        setEstado("");
        setMensaje_empresa("");
        setNombre_usuario("");
    }

    public CRecargas(
            int id,
            String numero,
            String monto,
            String empresa,
            String id_usuario,
            String fecha,
            String fecha_recarga,
            String pagado,
            String estado,
            String mensaje_empresa,
            String nombre_usuario
    )
    {
        this.setId(id);
        this.setNumero(numero);
        this.setMonto(monto);
        this.setEmpresa(empresa);
        this.setId_usuario(id_usuario);
        this.setFecha(fecha);
        this.setFecha_recarga(fecha_recarga);
        this.setPagado(pagado);
        this.setEstado(estado);
        this.setMensaje_empresa(mensaje_empresa);
        this.setNombre_usuario(nombre_usuario);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(String id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getFecha_recarga() {
        return fecha_recarga;
    }

    public void setFecha_recarga(String fecha_recarga) {
        this.fecha_recarga = fecha_recarga;
    }

    public String getPagado() {
        return pagado;
    }

    public void setPagado(String pagado) {
        this.pagado = pagado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje_empresa() {
        return mensaje_empresa;
    }

    public void setMensaje_empresa(String mensaje_empresa) {
        this.mensaje_empresa = mensaje_empresa;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }
}
