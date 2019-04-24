# SmartTrashCan

Descripción general: SmartTrashCan consiste en un contenedor de residuos inteligente. El mismo contará con las siguientes funcionalidades:

Abre y cierra la tapa del contenedor al aproximarse al mismo para que se puedan arrojar los residuos.
Contará con un juego de tres luces:
 - Luz verde: El contenedor puede operar sin inconvenientes.
 - Luz roja: Avisa que el contenedor está lleno de residuos.
 - Luz amarilla: Avisa que los residuos están degradados.

Sumado a esto, SmartTrashCan ayudará con la detección de elementos reciclables / no reciclables y de esta manera colaborar con el medio ambiente.

## Sensores:

**1. Infrarrojo:**
- Modelo: Hc-sr501
- Link: https://www.monarcaelectronica.com.ar/producto/sensor-movimiento-hc-sr501-pir-infrarrojo-arduino-mona-2/
- Descripción: El sensor será utilizado para detectar la presencia de personas que se encuentren próximas a SmartTrashCan para poder abrir la tapa del mismo sin necesidad de interactuar con él.

**2. Humedad y temperatura:**
- Modelo: Dht11 Arduino
- Link: https://tienda.patagoniatec.com/sensores-para-arduino/humedad-y-temperatura/dht11-sensor-de-temperatura-y-humedad-dht-11-arduino-ptec/
- Descripción: El sensor será utilizado para detectar el nivel de humedad y temperatura de los residuos arrojados y de esta forma saber qué tan degradado se encuentra el contenido dentro de SmartTrashCan, pudiendo dar aviso al usuario a través de alguna aplicación que sus desechos no están en condiciones favorables; o si los mismos podrían ser en realidad elementos reciclables (ejemplo: papel).

**3. Ultrasónico:**
- Modelo: Hc-sr04
- Link: https://www.monarcaelectronica.com.ar/producto/sensor-ultrasonico-hc-sr04-arduino-mona/
- Descripción: El sensor será utilizado para verificar el nivel de basura dentro de SmartTrashCan, a partir de la distancia que existe entre la tapa y el contenido del mismo. 

**4. Peso:**
- Modelo
- Link: https://www.monarcaelectronica.com.ar/producto/celda-carga-sensor-peso-10kg-interfaz-hx711-mona/
- Descripción: El sensor será utilizado para controlar el peso de la basura dentro de SmartTrashCan.


## Actuadores:
1. Para abrir y cerrar la tapa. Tomando la decisión a partir del sensor infrarrojo.
2. Para las luces. Pensamos en poner luces (verde/roja) que indique si el estado de humedad o si el tacho está lleno o no.
3. Cerrado de bolsa automático. 


## Lógica:

- A partir de la detección de magnitudes como el peso, la humedad y el tamaño del elemento arrojado al tacho se podría proveer información útil al usuario mediante la app mobile (android). Por ejemplo: si arrojo un elemento de gran tamaño, poco peso y baja humedad podría ser una botella de plástico, la cual es el un elemento reciclable.

