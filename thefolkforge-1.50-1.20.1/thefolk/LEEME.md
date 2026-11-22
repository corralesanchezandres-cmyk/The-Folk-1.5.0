# The Folk Forge 1.50 — mod de terror para Minecraft Java 1.20.1 (Forge)

El .jar se llama `thefolkforge-1.50-1.20.1.jar`. Para sacar una versión nueva cambia el número en DOS lugares:
`version = '...'` en `build.gradle` y `version="..."` en `src/main/resources/META-INF/mods.toml`.

## Criaturas
### The Folk (dweller de 3 bloques)
| Fase | Normal | ERROR / COMING | Comportamiento |
|---|---|---|---|
| 1 | 5 días | 2 días | Aparece lejos, a la vista, y solo te mira. Desaparece si te acercas a menos de 12 bloques. |
| 2 | 8 días | 4 días | Aparece a la vista y camina lento hacia ti. Al llegar a ~5 bloques se queda quieto y desaparece. |
| 3 | 5 días | 2 días | Aparece escondido (14-24 bloques), te persigue 1 minuto más rápido que tú, rompe bloques y golpea (3 corazones). |
| 4 | no existe | 2 días | Igual que la 3 pero con el DOBLE de velocidad. |

### The Thing (solo THE FOLK IS COMING)
Más rápida que The Folk. Aparece escondida, muestra "Im C0MING" en rojo, abre puertas, hace ruidos saturados,
hace 12 corazones de daño y desaparece a los 20 segundos.

### Man (solo THE FOLK IS COMING)
Cuerpo de jugador con ojos blancos brillantes. Te mira 2 segundos, muestra "CORRE" en rojo y corre muy rápido
hacia ti (hasta 30 s, 8 corazones de daño). Mientras exista y unos 3 minutos después, The Folk y The Thing aparecen
3 veces más seguido. Aparece cada 10-20 minutos.

## Eventos
- **Atrás** (The Thing, solo cuando el ciclo está en las fases 2 a 4): en un momento de sus 20 segundos, aparece el texto
  "atrás tuyo", suena una voz y The Thing se coloca 2 bloques detrás de ti y se queda quieto 1 segundo. Luego te
  persigue 1.5 veces más rápido. Pasa en el 70% de las apariciones.
- **Stalk** (The Folk, fases 1 y 2, modos ERROR y COMING): The Folk aparece pegado a una ventana de vidrio cerca de ti
  (o asomándose por una esquina), te mira ~4 s, silba, sale corriendo hacia la puerta más cercana a ti, la rompe y te
  persigue unos 15 segundos (solo rompe puertas, no paredes). Reemplaza al 40% de las apariciones de fase 1-2.

## FolkConfig (el engranaje)
Item `thefolk:folkconfig`. Clic derecho abre un menú con 3 botones:
- **Normal**: el mod funciona como siempre (sin fase 4, sin eventos, sin The Thing ni Man).
- **ERROR**: fases más rápidas + fase 4 + evento Stalk.
- **THE FOLK IS COMING**: todo lo de ERROR + The Thing + Man + evento Atrás.

El modo se guarda en el mundo. Consíguelo en el creativo ("Herramientas y utilidades") o con `/give @s thefolk:folkconfig`.

## Idiomas
`/folklang` abre un panel para elegir **Español** o **English**. Funciona sin trucos activados.
También: `/folklang es` y `/folklang en`. Cambia los textos en pantalla ("atrás tuyo" / "behind you",
"CORRE" / "RUN") y qué voz suena. Los mensajes de los comandos de prueba siguen en español.

## Sonidos propios
Los silbidos y las voces usan sonidos de vanilla por defecto. Para poner los tuyos:
1. Guarda tus archivos `.ogg` (mono) en `src/main/resources/assets/thefolk/sounds/`.
2. En `src/main/resources/assets/thefolk/sounds.json` cambia la línea `"name"` de cada sonido por
   `"thefolk:nombre_del_archivo"` (sin `.ogg`) y borra `"type": "event"` de esa línea.
   Sonidos: `whistle` (silbido), `voice.behind_es` y `voice.behind_en` (voz del evento Atrás).

## Comandos de prueba (necesitan permisos de operador / trucos activados)
- `/thefolk spawn 1|2|3|4` — invoca a The Folk en esa fase
- `/thefolk thing` — invoca a The Thing (con el evento Atrás)
- `/thefolk stalk` — evento Stalk (mejor dentro de una casa con ventanas de vidrio; si no hay, aparece a la vista)
- `/thefolk man` — invoca a Man
- `/thefolk mode normal|error|coming` — cambia el modo sin usar el item
- `/thefolk status` — muestra modo, día del mundo, día del ciclo y fase
- `/time add 24000` suma un día para saltar de fase

Las criaturas son invulnerables (solo creativo o /kill las eliminan). Aparecen solas únicamente en el Overworld,
fuera de Pacífico y no con jugadores en creativo/espectador (con los comandos sí funcionan).

## Ajustes
Bloque CONFIGURACIÓN al inicio de:
- `FolkDirector.java`: días por fase, cada cuánto aparece cada cosa, distancias, probabilidad de eventos, aumento de Man
- `TheFolkEntity.java`: velocidades, daño, tiempos del evento Stalk
- `TheThingEntity.java`: velocidad, daño, ruidos, tiempos del evento Atrás
- `TheManEntity.java`: velocidad, daño, tiempo mirando/corriendo

## Compilar e instalar
Sube el contenido de esta carpeta a tu repositorio (reemplaza `src`; el workflow de GitHub Actions se queda igual)
y descarga el .jar desde Actions → Artifacts. Ponlo en `%appdata%\.minecraft\mods` y abre TLauncher con Forge 1.20.1.
