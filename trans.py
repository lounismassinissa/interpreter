import Sensor

while 1:
	Sensor.send(2,"1")
	Sensor.delay(2000)
	Sensor.send(2,"0")
	Sensor.delay(2000)
