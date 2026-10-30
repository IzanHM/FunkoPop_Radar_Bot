package com.funko;

public class Funko {

    //Atributos o características que queremos obtener de los funkos
    private String titulo;
    private double precio;
    private String urlCompra;
    private String urlImagen;

    /* Constructor
    cuando nuestro scrapper encuentre un funko
    llama al constructor e inicializamos o creamos el objeto
     */
    public Funko(String titulo, double precio, String urlCompra, String urlImagen) {
        this.titulo = titulo;
        this.precio = precio;
        this.urlCompra = urlCompra;
        this.urlImagen = urlImagen;
    }

    //  Getters and Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getUrlCompra() {
        return urlCompra;
    }

    public void setUrlCompra(String urlCompra) {
        this.urlCompra = urlCompra;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    //toString (ver el funko en la consola)
    @Override
    public String toString() {
        return "Funko{" +
                "titulo='" + titulo + '\'' +
                ", precio=" + precio + "€" +
                ", urlCompra='" + urlCompra + '\'' +
                ", urlImagen='" + urlImagen + '\'' +
                '}';
    }
}