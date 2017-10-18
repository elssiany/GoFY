# importamos la libreria de pyrebase previamente instalada por consola
import pyrebase
# importamos la libreria de GPIO para usar los pines de la Raspi
import RPi.GPIO as GPIO
# importamos la libreria time para hacer retardos recordar que es en segundos
from time import sleep
import os


config = {
  "apiKey": "AIzaSyBwmhqTzDC7QlpPRr63tazjhsBsTuner3U",
  "authDomain": "gofy-c1730.firebaseapp.com",
  "databaseURL": "https://gofy-c1730.firebaseio.com",
  "storageBucket": "gofy-c1730.appspot.com"
}

firebase = pyrebase.initialize_app(config)

db = firebase.database()

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)#evito los mensajes de warning

# declaramos los pines de salida o entrada
# aqui esta con BCM es decir usando el numero del GPIO lo puedes cambiar a Board pra usar numero de pin fisico
GPIO.setup(17, GPIO.OUT)#pin de salida para el led
GPIO.setup(27, GPIO.IN)#pin de lectura para la IR
GPIO.setup(22, GPIO.OUT)#pin de salida para la alarma

idProduct = "-Kw24TH46pgvGBWBr8lQ"



# Return CPU temperature as a character string
def getCPUtemperature():
    res = os.popen('vcgencmd measure_temp').readline()
    return (res.replace("temp=", "").replace("'C\n", ""))


# Return RAM information (unit=kb) in a list
# Index 0: total RAM
# Index 1: used RAM
# Index 2: free RAM
def getRAMinfo():
    p = os.popen('free')
    i = 0
    while 1:
        i = i + 1
        line = p.readline()
        if i == 2:
            return (line.split()[1:4])


# Return % of CPU used by user as a character string
def getCPUuse():
    return(str(os.popen("top -n1 | awk '/Cpu\(s\):/ {print $2}'").readline().strip()))

# Return information about disk space as a list (unit included)
# Index 0: total disk space
# Index 1: used disk space
# Index 2: remaining disk space
# Index 3: percentage of disk used
def getDiskSpace():
    p = os.popen("df -h /")
    i = 0
    while 1:
        i = i + 1
        line = p.readline()
        if i == 2:
            return (line.split()[1:5])



##Date time formatting
dateString = '%d/%m/%Y %H:%M:%S'



# mientras el codigo se este ejecutando
# mientras no se suspenda el codigo
while True:



    #Informacion del sistema

    # CPU INFO
    CPU_temp = getCPUtemperature()
    CPU_usage = getCPUuse()
    #firebase.make_put_request("/PI/CPU", "/temperature", CPU_temp)

    db.child("active-systems").child(idProduct).child("system-information").child("valueCPUTemp").set(CPU_temp)

    db.child("active-systems").child(idProduct).child("system-information").child("valueCPU").set(CPU_usage)


    # RAM INFO
    RAM_stats = getRAMinfo()
    RAM_total = round(int(RAM_stats[0]) / 1000, 1)
    RAM_used = round(int(RAM_stats[1]) / 1000, 1)
    RAM_free = round(int(RAM_stats[2]) / 1000, 1)
    #firebase.make_put_request("/PI/RAM", "/free", str(RAM_free) + "")
    #firebase.make_put_request("/PI/RAM", "/used", str(RAM_used) + "")
    #firebase.make_put_request("/PI/RAM", "/total", str(RAM_total) + "")

    db.child("active-systems").child(idProduct).child("system-information").child("valueRAM").set(RAM_used)

    # DISK INFO
    DISK_stats = getDiskSpace()
    DISK_total = DISK_stats[0]
    DISK_free = DISK_stats[1]
    DISK_perc = DISK_stats[3]
    DISK_used = float(DISK_total[:-1]) - float(DISK_free[:-1])
    #firebase.make_put_request("/PI/DISK", "/total", str(DISK_total[:-1]))
    #firebase.make_put_request("/PI/DISK", "/free", str(DISK_free[:-1]))
    #firebase.make_put_request("/PI/DISK", "/used", str(DISK_used))
    #firebase.make_put_request("/PI/DISK", "/percentage", str(DISK_perc))

    db.child("active-systems").child(idProduct).child("system-information").child("valueDisk").set(DISK_used)



    #  el get del arbol smarthome-kevin de firebase
    # lectura del dato de firebase
    modeScan = db.child("active-systems").child(idProduct).child("system-information").child("modeScan").get()

    # lectura del dato de firebase
    askPermission = db.child("active-systems").child(idProduct).child("system-information").child("askPermission").get()

    alarm = db.child("active-systems").child(idProduct).child("system-information").child("alarm").get()

    if(alarm == True):
        GPIO.output(22, alarm.val())
        # es como un delay pero en segundos, es 4 segundos
        sleep(4)
        db.child("active-systems").child(idProduct).child("system-information").child("alarm").set(False)


    if(askPermission.val()=="scan-sensors"):
        if(modeScan.val()==True):
            db.child("active-systems").child(idProduct).child("system-information").child("modeScan").set(True)
        else:
            db.child("active-systems").child(idProduct).child("system-information").child("modeScan").set(False)



    #leemos la lectura digital del pi del IR es el GPIO 27
    lecturaIR = GPIO.input(27)


    if(lecturaIR == True):
        print("Se envia TRUE a firebase")
        # se envia a firebase
        if(modeScan.val()==True):
            db.child("report-scan-sensors").child(idProduct).push().set("DM")
        print("TRUE ENVIADO")

    if(lecturaIR == False):
        print("Se envia FALSE a firebase")
        print("FALSE ENVIADO")


    # es como un delay pero en segundos, es 2 segundos
    sleep(2)