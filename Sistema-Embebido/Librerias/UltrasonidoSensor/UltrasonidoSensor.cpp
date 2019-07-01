#include "Arduino.h"
#include "UltrasonidoSensor.h"

UltrasonidoSensor::UltrasonidoSensor(){}

UltrasonidoSensor::UltrasonidoSensor(int EchoPin, int TriggerPin){
	this->EchoPin = EchoPin;
	this->TriggerPin = TriggerPin;
	pinMode(this->TriggerPin, HIGH);
  	pinMode(this->EchoPin, INPUT);
  	digitalWrite(this->TriggerPin, LOW);
}

int UltrasonidoSensor::CalcularDistancia(){
	digitalWrite(this->TriggerPin, HIGH);
  	delayMicroseconds(10);
  	digitalWrite(this->TriggerPin, LOW);
  	return pulseIn(this->EchoPin, HIGH) / 58;
}

void UltrasonidoSensor::SetDistanciaActual(){
	this->DistanciaActual = CalcularDistancia();
}

int UltrasonidoSensor::GetDistanciaActual(){
	return this->DistanciaActual;
}

void UltrasonidoSensor::SetDistanciaMaxima(int DistanciaMaxima){
	this->DistanciaMaxima = DistanciaMaxima;
}

int UltrasonidoSensor::GetDistanciaMaxima(){
	return this->DistanciaMaxima;
}