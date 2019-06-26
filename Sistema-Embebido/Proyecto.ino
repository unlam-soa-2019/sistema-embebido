#include <Servo.h>
#include <SoftwareSerial.h>
SoftwareSerial BTserial(4,3); // RX | TX

//Sensor de humedad
#include <DHT.h>
#include <DHT_U.h>
#include <Adafruit_Sensor.h> 
// Definimos el pin digital donde se conecta el sensor
#define DHTPIN 2
#define DHTTYPE DHT11

DHT_Unified dht(DHTPIN, DHTTYPE);
uint32_t delayMS;


char c = ' ';

const int rele = 13;
const int EchoPin = 5;
const int TriggerPin = 6;
float distancia;
long tiempo;
bool bolsaAbierta = true;
bool seCambioBolsa = false; //Seteado desde la app
bool cerrarBolsa = false;
byte sensorpir = 7;
int anotacion = 0;
 
Servo servoMotor;

void setup() {
  Serial.begin(9600);
  pinMode(sensorpir, INPUT);
  servoMotor.attach(9);
  pinMode(TriggerPin, OUTPUT);
  pinMode(EchoPin, INPUT);
  pinMode(rele,OUTPUT);

  //Se configura la velocidad del puerto serie para poder imprimir en el puerto Serie
  //Serial.begin(9600);
  Serial.println("Inicializando configuracion del HC-05...");
    
  //Se configura la velocidad de transferencia de datos entre el Bluethoot  HC05 y el de Android.
  BTserial.begin(9600); 
  Serial.println("Esperando Comandos AT...");
}

void loop() {

  digitalWrite(TriggerPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(TriggerPin, LOW);

  tiempo = (pulseIn(EchoPin, HIGH)/2); 
  distancia = float(tiempo * 0.0343);

  //Serial.print("Distancia: "); // imprime la distancia en el Monitor Serie
  //Serial.println(distancia);
   
  if (((distancia < 10) && bolsaAbierta) || cerrarBolsa) 
  {
    servoMotor.write(0);
    digitalWrite(rele, HIGH);
    delay(5000);
    digitalWrite(rele, LOW);
    bolsaAbierta = false;
    servoMotor.write(90);
    //delay(5000);          // Sacar cuando se implemente cerrar por app --> 'c'.
    //seCambioBolsa = true; // Sacar cuando se implemente cerrar por app --> 'c'.
  }

  if (bolsaAbierta)
  {
    servoMotor.write(0);
    if(digitalRead(sensorpir) == HIGH)
    {
      servoMotor.write(90);
      delay(1000);
    }
    else
    {
      servoMotor.write(0);
    }
  }

 //Si reciben datos del HC05 
  if (BTserial.available())
  { 
    //Se evalúa la opción enviada
    c = BTserial.read();
    /*Cerrar tapa luego de cambiar la bolsa*/
    if(c == 'c') 
    {
      seCambioBolsa = true;
    }
    /* Se activa modo juego*/
    if((c == 'j') && bolsaAbierta) 
    {
      for(int i=5;i>=0;i--) // Reemplazar por While (mientras no se mande por app una 'f'). Por ahora el juego dura 15 segundos
      {
        servoMotor.write(90);
        for(int j=3;j>=0;j--) // j = tiempo para embocar 
        {
          tiempo = (pulseIn(EchoPin, HIGH)/2); 
          distancia = float(tiempo * 0.0343);
          delay(1000);
          if (distancia < 40)
          {
            anotacion++; 
          }          
        }
        servoMotor.write(0); 
      }
      Serial.print(anotacion);  
      BTserial.write(anotacion); 
    }
	/*Se cierra la bolsa*/
	if(c == 'b')
	{
		cerrarBolsa = true;
	}
  }

  if(seCambioBolsa) //Se resetean los parámetros si se cambió la bolsa 
  {
    servoMotor.write(0);
    bolsaAbierta = true; 
    seCambioBolsa = false;
  }

//Si se ingresa datos por teclado en el monitor serie 
  if (Serial.available())
  {
    //se los lee y se los envia al HC05
    c =  Serial.read();
    //Serial.write(c);
    //Serial.println(c);
    BTserial.write(c); 
  }


//Humedad

  // Delay between measurements.
  delay(delayMS);
  // Get temperature event and print its value.
  sensors_event_t event;
  dht.temperature().getEvent(&event);
  if (isnan(event.temperature)) {
    //Serial.println(F("Error reading temperature!"));  //Comentario de error en temperatura, comentar por ahora para ver otros resultados
  }
  else {
    //Serial.print(F("Temperature: "));
    //Serial.print(event.temperature);
    //Serial.println(F("°C"));
  }
  // Get humidity event and print its value.
  dht.humidity().getEvent(&event);
  if (isnan(event.relative_humidity)) {
    //Serial.println(F("Error reading humidity!"));
  }
  else {
    //Serial.print(F("Humidity: "));
    //Serial.print(event.relative_humidity);
    //Serial.println(F("%"));
  }    
}
