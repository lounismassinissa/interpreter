from  blinkt  import set_pixel, set_all, show, clear
from time import sleep

while True:
	sleep(0.05)
	fo = open("leds.conf", "r")
	line = fo.readline()
	c = int(line)
	# Close opend file
	fo.close()
	clear()
	if c==1 :
		set_all(255, 255, 255, 0.1)
	else :
		set_all(10,10,10,0.1)
	show()
