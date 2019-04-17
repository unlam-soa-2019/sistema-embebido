# SmartTrashCan

Descripción general: SmartTrashCan consiste en un contenedor de residuos inteligente. El mismo contará con las siguientes funcionalidades:

Abre y cierra la tapa del contenedor al aproximarse al mismo para que se puedan arrojar los residuos.
Contará con un juego de tres luces:
-Luz verde: El contenedor puede operar sin inconvenientes.
-Luz roja: Avisa que el contenedor está lleno de residuos.
-Luz amarilla: Avisa que los residuos están degradados.

## Sensores:

**1. Infrarrojo:**
- Modelo: Hc-sr501
- Descripción: El sensor será utilizado para detectar la presencia de personas que se encuentren próximas a SmartTrashCan para poder abrir la tapa del mismo sin necesidad de interactuar con él.

**2. Humedad:**
- Modelo: Dht11 Arduino
- Descripción: El sensor será utilizado para detectar el nivel de humedad y de esta forma saber qué tan degradado se encuentra el contenido dentro de SmartTrashCan, pudiendo dar aviso al usuario a través de alguna aplicación que sus deshechos no están en condiciones favorables.

**3. Ultrasónico:**
- Modelo: Hc-sr04
- Descripción: El sensor será utilizado para verificar el nivel de basura dentro de SmartTrashCan, a partir de la distancia que existe entre la tapa y el contenido del mismo. 

## Actuadores:
1. Para abrir y cerrar la tapa. Tomando la decisión a partir del sensor infrarrojo.
2. Para las luces. Pensamos en poner luces (verde/roja) que indique si el estado de humedad o si el tacho está lleno o no.
3. Cerrado de bolsa automático. 
