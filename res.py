import Sensor

while 1:
	Sensor.attendre()
	p = Sensor.receive()
	if(p == "1"):
		Sensor.mark()
	else:
		Sensor.unmark()

