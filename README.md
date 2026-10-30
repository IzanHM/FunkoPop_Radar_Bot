# 🎯 Funko Pop Radar & Price Drop Notifier

Un sistema autónomo de monitoreo de precios y descubrimiento de productos escalable, desarrollado en **Java**. El sistema realiza scraping dinámico en múltiples categorías de coleccionables, persiste el historial de precios en **SQLite** para evitar duplicados y envía alertas en tiempo real a través de la **API de Telegram** cuando detecta bajadas de precio.

---

## 🚀 Características Principales

* **🔍 Escaneo Multi-Categoría Autónomo:** Recorre más de 10 universos (Marvel, Anime, Disney, Star Wars, etc.) extrayendo decenas de productos sin necesidad de introducir enlaces manuales.
* **🛡️ Antibloqueo & Politeness:** Implementa pausas dinámicas de cortesía (`Thread.sleep`), rotación de cabeceras HTTP (`User-Agent`, `Accept-Language`) y exclusión estricta de anuncios patrocinados (`sspa`, `slredirect`).
* **📊 Persistencia con SQLite:** Almacenamiento local del catálogo y trazabilidad de precios históricos.
* **📱 Alertas de Telegram en Tiempo Real:** Notificaciones instantáneas al teléfono con el título del producto, nuevo precio, enlace directo y foto.
* **⚡ Automatización en Segundo Plano:** Empaquetado como un ejecutable `.jar` ligero y programado mediante scripts de lotes (`.bat`) y el Programador de Tareas de Windows.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 21
* **Scraping & HTML Parsing:** [Jsoup](https://jsoup.org/)
* **Base de Datos:** SQLite / JDBC
* **Notificaciones:** Telegram Bot API (`HttpURLConnection` / JSON)
* **Gestor de Dependencias:** Maven
* **Automatización:** Windows Task Scheduler & Batch Scripts

---

## ⚙️ Arquitectura del Sistema

```text
[ Windows Task Scheduler ]
          │
          ▼
 [ ejecutar_radar.bat ] ──► [ FunkoScraper.java ] (Jsoup)
                                    │
                                    ▼ (Extracción de datos)
                            [ DataBase.java ] (SQLite)
                                    │
                      ┌─────────────┴─────────────┐
                      ▼                           ▼
            ¿Producto Nuevo?             ¿Bajada de Precio?
                      │                           │
                      └─────────────┬─────────────┘
                                    ▼
                          [ TelegramBot.java ]
                                    │
                                    ▼
                         📱 Mensaje en Telegram
```

---

## 🔧 Configuración y Puesta en Marcha

### Prerrequisitos
* Java JDK 17 o superior.
* Maven instalado.

### Instalación

1. Clona el repositorio:
   ```bash
   git clone [https://github.com/TU_USUARIO/funko-pop-radar-bot.git](https://github.com/TU_USUARIO/funko-pop-radar-bot.git)
   cd funko-pop-radar-bot
   ```

2. Configura las credenciales de tu Bot de Telegram en `TelegramBot.java`:
   ```java
   private static final String BOT_TOKEN = "TU_BOT_TOKEN";
   private static final String CHAT_ID = "TU_CHAT_ID";
   ```

3. Compila y empaqueta el archivo ejecutable `.jar`:
   ```bash
   mvn clean package
   ```

4. Ejecuta el sistema:
   ```bash
   java -jar target/FunkoSaleNotifier-1.0-SNAPSHOT.jar
   ```

---

## 👤 Autor

Creado con pasión por el coleccionismo y la automatización.
* **GitHub:** [@TU_USUARIO](https://github.com/TU_USUARIO)

