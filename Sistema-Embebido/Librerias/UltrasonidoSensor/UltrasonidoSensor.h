#ifndef UltrasonidoSensor_h
#define UltrasonidoSensor_h

#include "Arduino.h"

class UltrasonidoSensor{
	
	public:
		UltrasonidoSensor();
		UltrasonidoSensor(int EchoPin, int TriggerPin);
		void SetDistanciaActual();
		int GetDistanciaActual();
		void SetDistanciaMaxima(int DistanciaMaxima);
		int GetDistanciaMaxima();

	private:
		int EchoPin;
		int TriggerPin;
		int DistanciaActual;
		int DistanciaMaxima;
		int CalcularDistancia();
};

#endif


