package com.funko;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("Inciando el buscador de Funkos...");

        //Inicializamos la BD
        DataBase.inicializarBaseDeDatos();

        System.out.println("----------------------------------------------------");

        //Objeto de la clase FunkoScraper
        FunkoScraper scraper = new FunkoScraper();

        //Ejecutamos el método de búsqueda de Funkos
        List<Funko> funkosEncontrados = scraper.searchFunkos();

        //Recorre la lista para ver que encontró y guardarlo automatimaticamente en la BD
        if (funkosEncontrados.isEmpty()) {
            System.out.println("No se ha encontrado ningún Funko en esta ejecución");
        } else {
            System.out.println("¡Se han encontrado " + funkosEncontrados.size() + " Funkos! Guardando todos en la Base de datos...");

            for (Funko f : funkosEncontrados) {
              //Procesamos con el radar inteligente
                DataBase.procesarFunkoConRadar(f);
            }
        }

        System.out.println("---------------------------------------");

        //Listamos todos los funkos
        DataBase.listarFunkos();

        System.out.println("¡Proceso finalizado con éxito");

    }
}