package com.funko;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TelegramNotifier {

    //Datos de Telegram
    private static final String BOT_TOKEN = "TU_TELEGRAM_BOT_TOKEN";
    private static final String CHAT_ID = "TU_TELEGRAM_CHAT_ID";

    public static void  enviarNotificacionOferta(Funko funko) {
        String mensaje = "🔥 *¡OFERTA ENCONTRADA EN FUNKO POP!* 🔥\n\n  " +
                "📌 *Producto:* " + funko.getTitulo() + "\n" +
                "💰 *Nuevo Precio:* " + funko.getPrecio() + "\n" +
                "🛒 [¡Comprar ahora en Amazon!](" + funko.getUrlCompra() + ")";

        try {
            //Si el funko tiene imagen enviamos una foto con sendphoto
            if (funko.getUrlImagen() !=null && !funko.getUrlImagen().isEmpty()) {
                String urlTelegram = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendPhoto" + //direción base servidor Telegram
                        "?chat_id=" + CHAT_ID + //destinatario exacto
                        "&photo=" + URLEncoder.encode(funko.getUrlImagen(), StandardCharsets.UTF_8) + //fuente de la imagen
                        "&caption=" + URLEncoder.encode(mensaje, StandardCharsets.UTF_8) + //pie de foto
                        "&parse_mode=Markdown"; //indica a telegram como interpretar el texto

                Jsoup.connect(urlTelegram).ignoreContentType(true).get();
            } else {
                //Si no hay imagen texto normal
                String urlTelegram = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage" +
                        "?chat_id=" + CHAT_ID +
                        "&text=" +URLEncoder.encode(mensaje, StandardCharsets.UTF_8) + //texto sin foto
                        "&parse_mode=Markdown";

                Jsoup.connect(urlTelegram).ignoreContentType(true).get();
            }

            System.out.println("¡Notificación enviada a Telegram con éxito");

        } catch (IOException e) {
            System.out.println("Error al enviar el mensaje a Telegram: " + e.getMessage());
        }
    }
}
