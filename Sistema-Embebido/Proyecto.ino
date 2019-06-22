#include <Servo.h>

const int rele = 13;
const int EchoPin = 5;
const int TriggerPin = 6;
float distancia;
long tiempo;
bool bolsaAbierta = true;
bool seCambioBolsa = false; //Seteado desde la app
byte sensorpir = 7;
 
Servo servoMotor;

void setup() {
  Serial.begin(9600);
  pinMode(sensorpir, INPUT);
  servoMotor.attach(9);
  pinMode(TriggerPin, OUTPUT);
  pinMode(EchoPin, INPUT);
  pinMode(rele,OUTPUT);
}

void loop() {

  digitalWrite(TriggerPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(TriggerPin, LOW);

  tiempo = (pulseIn(EchoPin, HIGH)/2); 
  distancia = float(tiempo * 0.0343);

  Serial.print("Distancia: "); // imprime la distancia en el Monitor Serie
  Serial.println(distancia);
   
  if ((distancia < 10) && bolsaAbierta) 
  {
    servoMotor.write(0);
    digitalWrite(rele, HIGH);
    delay(5000);
    digitalWrite(rele, LOW);
    bolsaAbierta = false;
    servoMotor.write(90);
    delay(5000);
    seCambioBolsa = true;//sacar
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

  if(seCambioBolsa)
  {
    servoMotor.write(0);
    bolsaAbierta = true;
    seCambioBolsa = false;
  }
}
