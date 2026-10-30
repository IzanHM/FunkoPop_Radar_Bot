//Logica para conectarnos a Amazon

package com.funko;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunkoScraper {

    //Lista de Categorías para abarcar más productos
    private static final List<String> PALABRAS_CLAVE = List.of(
            "Funko Pop Marvel",
            "Funko Pop Disney",
            "Funko Pop Kimetsu",
            "Funko Pop Attack on Titan",
            "Funko Pop Pokemon",
            "Funko Pop My Hero Academia",
            "Funko Pop One Piece",
            "Funko Pop Frieren",
            "Funko Pop Pirates of the Caribbean",
            "Funko Pop How to Train Your Dragon",
            "Funko Pop Harry Potter",
            "Funko Pop The Lord of the Rings",
            "Funko Pop Terror"
    );

    public List<Funko> searchFunkos() {
        List<Funko> listaFunkos = new ArrayList<>();
        Set<String> urlsProcesadas = new HashSet<>(); //Evita duplicados globales entre distintas categorias

        System.out.println("Iniciando escaneo multi-categoría en Amazon...");

        //Bucle parar recorrer palabras clave de forma autónoma
        for (String keyword : PALABRAS_CLAVE) {
            System.out.println("\n Buscando categoría " + keyword);

            //Construimos la URL dinámicamente
            String urlCat = "https://www.amazon.es/s?k=" + keyword.replace(" ", "+");

            try {
                //Nos conectamos a la pagina con cabaceras seguras
                Document docCatalogo = Jsoup.connect(urlCat)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0")
                        .header("Accept-Language", "es-ES,es;q=0.9")
                        .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,iamge/avif,image/webp,image/apng,*/*;q=0.8")
                        .header("Referer", "https://www.amazon.es/")
                        .timeout(10000)
                        .get();

                //Extraemos los enlaces de productos de la página de resultados
                Elements enlacesProductos = docCatalogo.select("div.s-main-slot a[href*='/dp/']");

                System.out.println("-> Enlaces encontrados para '" + keyword + "': " + enlacesProductos.size());

                //Evitamos procesar la misma url 2 veces
                int contadorCategoria = 0;

                for (Element link : enlacesProductos) {
                    if (contadorCategoria >= 45) break;

                    String href = link.absUrl("href"); //Obtenemos URL completa de cada producto
                    if (href.isEmpty()) continue;

                    //FILTRO DE SEGURIDAD: Ignoramos anuncios de Amazon
                    if (href.contains("sponsored-ads") || href.contains("slredirect") || href.contains("sspa")) {
                        continue;
                    }

                    //Limpiamos la URL de parámetros innecesarios
                    if (href.contains("/dp/")) {
                        int indexDp = href.indexOf("/dp/");
                        int indexSlash = href.indexOf("/", indexDp + 4);
                        if (indexSlash != -1) {
                            href = href.substring(0, indexSlash); //Nos quedamos la URL limpia
                        }
                    } else {
                        continue;
                    }

                    //Si ya hemos procesado esta URL en esta ejecución la saltamos
                    if (urlsProcesadas.contains(href)) {
                        continue;
                    }

                    urlsProcesadas.add(href);

                    System.out.println("-> Entrando al producto:" + href);

                    //Entramos a cada producto extrayendo sus datos
                    try {
                        Thread.sleep(2000); //Pausa para proteger la IP
                        Document docProducto = Jsoup.connect(href)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36 OPR/108.0.0.0")
                                .header("Accept-Language", "es-ES,es;q=0.9")
                                .timeout(10000)
                                .get();

                        //Extraemos Titulo
                        Element tituloElement = docProducto.selectFirst("#productTitle");
                        String titulo = (tituloElement != null) ? tituloElement.text().trim() : "Funko sin título";

                        //Extraemos el precio uniendo la parte entera y los céntimos
                        double precio = 0.0;
                        Element precioElement = docProducto.selectFirst(".a-price .a-offscreen");
                        if (precioElement != null) {
                            String textoPrecio = precioElement.text();
                            String limpio = textoPrecio.replace("€", "").replace("$", "").trim();
                            if (limpio.contains(",")) { //Reemplazamos la coma por un punto
                                limpio = limpio.replace(".", "").replace(",", ".");
                            }
                            precio = Double.parseDouble(limpio);
                        }

                        //Extraemos la imagen en alta resolución
                        Element imgElement = docProducto.selectFirst("#landingImage");
                        String urlImagen = "";
                        if (imgElement != null) {
                            urlImagen = imgElement.attr("data-old-hires");
                            if (urlImagen.isEmpty()) {
                                urlImagen = imgElement.attr("src");
                            }
                        }

                        //Creamos el objeto y lo añadimos a la lista
                        Funko funko = new Funko(titulo, precio, href, urlImagen);
                        listaFunkos.add(funko);

                        System.out.println("Extraído con éxito: " + titulo + " (" + precio + "€)");
                        contadorCategoria++;

                    } catch (IOException e) {
                        System.out.println("No se pudo extraer este producto individual: " + e.getMessage());
                    }
                }

                //Pausa extra entre cambio de categoría
                Thread.sleep(3000);

            } catch (Exception e) {
                System.out.println("Error al buscar la categoría " + keyword + ": " + e.getMessage());
            }
        }

        return listaFunkos;
    }
}

