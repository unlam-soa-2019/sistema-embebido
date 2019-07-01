#include "Arduino.h"
#include "Tapa.h"

Tapa::Tapa(){}

Tapa::Tapa(int ServoPin){
	this->oServo.attach(ServoPin);
}

void Tapa::Abrir(){
  if(millis() > this->TiempoInicial + this->Tiempo && this->Posicion < this->MaxGrado && this->Abriendose){ //Abre el tacho de basura
    this->TiempoInicial = millis();
    this->Posicion++;
    this->oServo.write(this->Posicion);
    if (this->Posicion == this->MaxGrado){
      this->Abriendose = false;
    }
  }
}

void Tapa::Cerrar(){
	if(millis() > this->TiempoInicial + this->Tiempo && this->Posicion > this->MinGrado && !this->Abriendose){ //Cierra el tacho de basura
    this->TiempoInicial = millis();
    this->Posicion--;
    this->oServo.write(this->Posicion);
    if (this->Posicion == this->MinGrado){
      this->Abriendose = true;
    }
  }
}

bool Tapa::ModoJuegoActivado(char message){
  return true;
}

int Tapa::GetPosicion(){
  return this->Posicion;
}

bool Tapa::PuedeLanzar(bool lanzar){
  bool puedeLanzar = lanzar;
  if(this->Posicion == 0){
    this->Cont++;
    if(this->Cont == 1){
      puedeLanzar = true;
    }
  }
  else{
    this->Cont = 0;
  }
  return puedeLanzar;
}