//Servo
#include <Servo.h>

//Sensor de humedad
#include <DHT.h>
#include <DHT_U.h>
#include <Adafruit_Sensor.h>

//Balanza
#include <HX711_ADC.h>

//HC05
#include <SoftwareSerial.h>

SoftwareSerial BTserial(4,3); // RX | TX
char datoObtenido = ' ';

//DEFINE
#define DHTPIN 2
#define DHTTYPE DHT11
#define TSERVO 1000
#define TSERVOJUEGO 5
#define SENSORPIR 7
#define LEDPIN 8
#define RELEPIN 13
#define ECHOPIN 5 // receptor ultrasonico
#define TRIGGERPIN 6 // emisor ultrasonico
#define distanciaCerrarBolsa 10
#define valorServoArriba 90
#define valorServoAbajo 0
#define tiempDeJuego 15000
#define stabilisingTimeBalanza 2000
#define calibracionBalanza 213.00 // Factor de calibración para 1kg = 212.00

// identificacion residuos
#define humedadMenor 15
#define humedadMayor 20
#define pesoMenor 50
#define pesoMayor 100


DHT_Unified dht(DHTPIN, DHTTYPE);
uint32_t delayMS;

HX711_ADC LoadCell(A1, A0);

//Sensor ultrasónico
float distanciaBasura;
unsigned long tiempo;
unsigned long tiempoUltraSonido = 0;


//Servo
Servo servoMotor;

//Variables adicionales
bool bolsaAbierta = true;
bool seCambioBolsa = false; //Seteado desde la app
bool pedidoCerrarBolsa = false;
bool seSeteoPesoMaximo = false;
bool tachoLleno = false;
int anotacion = 0;
float pesoActual;
float pesoMaximo;
float humedad;

void setup() 
{
  Serial.begin(9600);

  //Pir
  pinMode(SENSORPIR, INPUT);

  //Servo
  servoMotor.attach(9);

  //Sensor ultrasónico
  pinMode(TRIGGERPIN, OUTPUT);
  pinMode(ECHOPIN, INPUT);

  //Relé
  pinMode(RELEPIN, OUTPUT);

  //Sensor humedad
  dht.begin();

  //Se configura la velocidad del puerto serie para poder imprimir en el puerto Serie
  //Se configura la velocidad de transferencia de datos entre el Bluethoot  HC05 y el de Android.
  BTserial.begin(9600); 
  
  //Balanza
  LoadCell.begin();
  LoadCell.start(stabilisingTimeBalanza);
  LoadCell.setCalFactor(calibracionBalanza); 
  
  //Led
  pinMode(LEDPIN, OUTPUT);
  
  Inicializar();
}

void loop() 
{
  if (bolsaAbierta)
  {
    AbrirCerrarTapa();
  }
  if(millis() > tiempoUltraSonido + TSERVO) // hacemos espera no bloqueante porque cuando se cierra la tapa, 
                                            // provoca que el ultrasonido detecte una distancia muy chica y se nos cierra la bolsa
  {
    tiempoUltraSonido = millis();
    distanciaBasura = ObtenerDistanciaBasura();
    Serial.println("Distancia: ");
    Serial.println(distanciaBasura);
    if (seSeteoPesoMaximo)
    {
      Serial.println("Se seteo el peso máximo: ");
      Serial.println(pesoMaximo);
      Serial.println("El peso máximo es: ");
      Serial.println(pesoActual);
    }
    if (pedidoCerrarBolsa)
    {
      Serial.println("Se pidió cerrar bolsa");
    }
    if (((distanciaBasura < distanciaCerrarBolsa) || (seSeteoPesoMaximo && pesoActual >= pesoMaximo) || pedidoCerrarBolsa) && bolsaAbierta) 
    {
      Serial.println("Se cierra la bolsa");
      CerrarBolsa();
    }
  }
  Pesar();
  ObtenerDatosAplicacion();
  if (seCambioBolsa)
  {
    ConfigurarPorCambioDeBolsa();
  }
}

void Inicializar()
{
  servoMotor.write(valorServoAbajo);
  digitalWrite(LEDPIN , HIGH);
}

void AbrirCerrarTapa()
{
  if(digitalRead(SENSORPIR) == HIGH)
  {
    servoMotor.write(valorServoArriba);
    Serial.println("Se abre la tapa");
    delay(5000); //Esperamos 5 segundos antes de cerrar la tapa para que el usuario arroje los residuos.
    servoMotor.write(valorServoAbajo);
    delay(1000); //Esperamos 1 segundo para que la tapa no intente cerrar y volver a abrir instantaneamente si detecta el PIR.
  }
}

float ObtenerDistanciaBasura()
{
  digitalWrite(TRIGGERPIN, HIGH);
  delayMicroseconds(10);
  digitalWrite(TRIGGERPIN, LOW);
  tiempo = (pulseIn(ECHOPIN, HIGH)/2); 
  return float(tiempo * 0.0343); // el sonido viaja a 343 metros por segundo
}

void CerrarBolsa()
{
  //Serial.println(distanciaBasura); //SACAR
  servoMotor.write(valorServoAbajo);
  digitalWrite(RELEPIN, HIGH);
  delay(5000); //Tiempo de cerrado de la bolsa.
  digitalWrite(RELEPIN, LOW);
  bolsaAbierta = false;
  pedidoCerrarBolsa  = false;
  servoMotor.write(valorServoArriba);
  digitalWrite(LEDPIN , LOW);
  if (distanciaBasura < distanciaCerrarBolsa)
  {
    tachoLleno = true;
  }
}

void ObtenerDatosAplicacion()
{
  if (BTserial.available())
  { 
    datoObtenido = BTserial.read();
    Serial.print(datoObtenido);
    switch(datoObtenido)
    {
      case 'c':
        seCambioBolsa = true;
      break;
      
      case 'j':
        IniciarModoJuego();
      break;
        
      case 'b':
        pedidoCerrarBolsa = true;
      break;
      
      case 'r':
        EnviarInformacionResiduo();
      break;

      case 'p':
        {
          float peso = ObtenerPeso();
          char pesoStr[8];
          dtostrf(peso, sizeof(pesoStr), 2, pesoStr); //Castea el float a char* con precisión de 2 decimales
          Serial.print("Peso actual: ");
          strcat(pesoStr, "%");
          Serial.println(pesoStr);
          BTserial.write(pesoStr);
        }
      break;

      case '1':
        {
          SetearPesoMaximo(100);
        }
      break;

      case '2':
        {
          SetearPesoMaximo(500);
        }
      break;

      case '3':
        {
          SetearPesoMaximo(1000);
        }
      break;
    }
  }
}

void IniciarModoJuego()
{ 
  int posicionServo = 0, cont = 0;
  int cantidad = 0;
  bool levantando = true;
  bool puedeAnotar;
  unsigned long tiempo_1 = 0;
  unsigned long tiempo_2 = 0;
  unsigned long tiempoComienzoJuego = millis();
  unsigned long tiempoActualJuego = millis();
  unsigned long tiempoTotalJuego = 0;
  while(tiempoTotalJuego < tiempDeJuego)
  {         
    if(millis() > tiempo_1 + TSERVOJUEGO && posicionServo < valorServoArriba && levantando) //Abre la tapa del tacho.
    { 
      tiempo_1 = millis();
      posicionServo++;
      servoMotor.write(posicionServo);
      digitalWrite(LEDPIN , HIGH);
      if (posicionServo == valorServoArriba){
        levantando = false;
      }
    }
  
    if(millis() > tiempo_1 + TSERVOJUEGO && posicionServo > valorServoAbajo && !levantando) //Cierra la tapa del tacho.
    { 
      tiempo_1 = millis();
      posicionServo--;
      servoMotor.write(posicionServo);
      digitalWrite(LEDPIN , LOW);
      if (posicionServo == valorServoAbajo)
      {
        levantando = true;
      }
    }
    tiempoActualJuego = millis();
    tiempoTotalJuego = tiempoActualJuego - tiempoComienzoJuego;
  } 
  Inicializar();
}

void ConfigurarPorCambioDeBolsa()
{
  servoMotor.write(valorServoAbajo);
  digitalWrite(LEDPIN , HIGH);
  bolsaAbierta = true; 
  seCambioBolsa = false;
  tachoLleno = false;
}

float ObtenerHumedad()
{
  delay(delayMS);
  sensors_event_t event;
  dht.temperature().getEvent(&event);
  return event.relative_humidity;
}

void Pesar()
{
  long tiempoBalanza;
  LoadCell.update();
  pesoActual = LoadCell.getData();
}

float ObtenerPeso()
{
  return pesoActual;
}

void SetearPesoMaximo(float peso)
{
  seSeteoPesoMaximo = true;
  pesoMaximo = peso;
}

void EnviarInformacionResiduo()
{
  humedad = ObtenerHumedad();
  Serial.println(humedad);
  if (tachoLleno && pesoActual > pesoMayor)
  {
    Serial.print("El contenido es mayormente basura");
    BTserial.write("El contenido es mayormente basura%");
  }
  else if (tachoLleno && pesoActual <= pesoMayor)
  {
    Serial.print("El contenido es mayormente reciclable");
    BTserial.write("El contenido es mayormente reciclable%");
  }
  else if (humedad <= humedadMenor && pesoActual <= pesoMenor)
  {
    Serial.print("El contenido es no degradable");
    BTserial.write("El contenido es no degradable%");
  }
  else if ((humedad > humedadMenor && humedad < humedadMayor) && (pesoActual > pesoMenor && pesoActual < pesoMayor))
  {
    Serial.print("El contenido es poco degradable");
    BTserial.write("El contenido es poco degradable%");
  }
  else if (humedad >= humedadMayor && pesoActual >= pesoMayor)
  {
    Serial.print("El contenido es altamente degradable");
    BTserial.write("El contenido es altamente degradable%");
  }
  else 
  {
    Serial.print("El contenido es poco degradable");
    BTserial.write("El contenido es poco degradable%");
  }
}
