#ifndef Tapa_h
#define Tapa_h

#include "Arduino.h"
#include "Servo.h"

class Tapa{
	
	public:
		Tapa();
		Tapa(int ServoPin);
		void Abrir();
		void Cerrar();
		bool ModoJuegoActivado(char message);
		int GetPosicion();
		bool PuedeLanzar(bool x);
		

	private:
		Servo oServo;
		int ServoPin; //Pin por el que lee arduino
		int Posicion = 0; //Tendra la posicion de la helice del servo con respecto a MinGrado y MaxGrado
		unsigned long Tiempo = 5; //Para controlar la velocidad a la que gira el Servo
		unsigned long TiempoInicial = 0;
		//IMPORTANTE: 1000 representa 1 segundo
		//IMPORTANTE: Los Serial.print(...) que se encuentren mientras rota, provocaran que se reduzca la velocidad de movimiento
		int Cantidad = 0; //MODIFICAR A FUTURO
		int Cont = 0; //MODIFICAR A FUTURO
		bool Estado = true; //true: tacho de basura abriendose, false: tacho de basura cerrandose
		int MinGrado = 0;
		int MaxGrado = 90;
		bool Lanzar = true;
		bool Abriendose = true;

};

#endif


