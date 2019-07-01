#include <Tapa.h>
#include <UltrasonidoSensor.h>

#define TSERVO 5
#define TUSONIDO 15
unsigned long tiempo_2 = 0;
int cantidad = 0;
bool puedeAnotar = true;
int servoPin = 9;
const byte trigger = 3;
const byte echo = 2;
UltrasonidoSensor us;
Tapa oTapa;

void setup() {
  Serial.begin(9600);
  oTapa = Tapa(servoPin);
  us = UltrasonidoSensor(echo, trigger);
  us.SetDistanciaMaxima(50);
}

void loop() {
  //Respuesta Bluetooth, c: Cerrar Tapa, j: Activar Modo Juego, b : Cerrar Bolsa
  loopDefault();
  loopGame();
}


void loopDefault(){
  
  
  }

void loopGame(){
  
    while(oTapa.ModoJuegoActivado('j')){
        oTapa.Abrir();
        oTapa.Cerrar();

        if(millis() > tiempo_2 + TUSONIDO && oTapa.PuedeLanzar(puedeAnotar)){
            tiempo_2 = millis();
            us.SetDistanciaActual();
            int distancia = us.GetDistanciaActual();
            if( distancia < us.GetDistanciaMaxima()){
                puedeAnotar = false;
                cantidad++;
                Serial.println(String(distancia) + "cm, Anotacion Nr: " + String(cantidad));
            }  
         }   
      }
}
