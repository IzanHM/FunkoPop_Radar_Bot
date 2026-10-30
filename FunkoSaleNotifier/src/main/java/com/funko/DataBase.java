package com.funko;

import java.sql.*;

public class DataBase {

    //Definimos ruta de la BD local (archivo funkos.db en el proyecto)
    private static final String URL_DB = "jdbc:sqlite:funkos.db";

    //Inicializamos la BD y creamos la tabla si no existe
    public static void inicializarBaseDeDatos() {
        String sql = "CREATE TABLE IF NOT EXISTS funkos (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "titulo TEXT NOT NULL," +
                "precio REAL NOT NULL," +
                "url_compra TEXT NOT NULL,"+
                "url_imagen TEXT NOT NULL)";

        try (Connection conn = DriverManager.getConnection(URL_DB);
             Statement stmt = conn.createStatement()) { /* Usar Statement para ódenes estáticas o consultas
                                                           que no llevan variables externas ni interrogantes */

            stmt.execute(sql); //Enviamos la orden SQL a la BD para que ejecute la tabla
            System.out.println("Base de datos inicializada correctamente");

        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos: " + e.getMessage());
        }
    }

    //Método para procesar el Funko con el Radar de Ofertas
    public static void procesarFunkoConRadar(Funko funko) {
        //Buscamos si el funko ya está en la BD
        String sqlBuscar = "SELECT id, precio FROM funkos WHERE url_compra =?";
        String sqlInsertar = "INSERT INTO funkos (titulo, precio, url_compra, url_imagen) VALUES (?,?,?,?)";
        String sqlActualizar = "UPDATE funkos SET precio = ? WHERE url_compra =?";

        try (Connection conn = DriverManager.getConnection(URL_DB);
        PreparedStatement pstmtBuscar = conn.prepareStatement(sqlBuscar)) {

            pstmtBuscar.setString(1, funko.getUrlCompra());
            ResultSet rs = pstmtBuscar.executeQuery();

            if(rs.next()) {
                //El funko ya existe comparamos precios
                int id = rs.getInt("id");
                double precioAnterior = rs.getDouble("precio");
                double precioNuevo = funko.getPrecio();

                System.out.println("-> Funko ya registrado. Precio anterior: " + precioAnterior + "€ | Precio Actual: " + precioNuevo + "€");

                if (precioNuevo < precioAnterior) {
                    System.out.println("¡BAJADA DE PRECIO DETECTADA!");
                    //Disparamos la notificación a TG
                    TelegramNotifier.enviarNotificacionOferta(funko);
                } else {
                    System.out.println("El precio no bajado");
                }

                //Actualizamos el precio en la BD
                try (PreparedStatement pstmtActaulizar = conn.prepareStatement(sqlActualizar)) {
                    pstmtActaulizar.setDouble(1, precioNuevo);
                    pstmtActaulizar.setString(2, funko.getUrlCompra());
                    pstmtActaulizar.executeUpdate();
                }
            } else {
                //El funko NO existe aun, lo guardamos
                try (PreparedStatement pstmtInsertar = conn.prepareStatement(sqlInsertar)) {
                    pstmtInsertar.setString(1, funko.getTitulo());
                    pstmtInsertar.setDouble(2, funko.getPrecio());
                    pstmtInsertar.setString(3, funko.getUrlCompra());
                    pstmtInsertar.setString(4, funko.getUrlImagen());
                    pstmtInsertar.executeUpdate();
                    System.out.println("Nuevo funko añadido al radar: " + funko.getTitulo());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al procesar el Funko en la BD: " + e.getMessage());
        }
    }


    //Método para concultar y listar los Funkos guardados en la DB
    public static void listarFunkos() {
        String sql = "SELECT id, titulo, precio, url_compra, url_imagen FROM funkos";

        try (Connection conn = DriverManager.getConnection(URL_DB);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) { //Resultado de la consulta que hemos realizado

            System.out.println("--- LISTA DE FUNKOS EN LA DB ---");

            //Recorremos los resultados fila a fila
            while (rs.next()) {
                int id = rs.getInt("id");
                String titulo = rs.getString("titulo");
                double precio = rs.getDouble("precio");
                String urlCompra = rs.getString("url_compra");

                System.out.println("ID: " + id + " | " + titulo + "| Precio: " + precio + "€ | Enlace: " + urlCompra);
            }

            System.out.println("-----------------------------------------");
        } catch (SQLException e) {
            System.out.println("Error al consultar la base de datos: " + e.getMessage());
        }
    }
}
