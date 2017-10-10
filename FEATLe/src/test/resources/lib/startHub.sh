#!/bin/bash
#Initilaze hub and node
java -jar selenium-server-standalone-2.53.0.jar -role hub -port 4444 &
sleep 5
java -jar selenium-server-standalone-2.53.0.jar -port 5555 -role node -hub http://localhost:4444/grid/register -browser browserName=chrome,maxInstances=5 &
java -jar selenium-server-standalone-2.53.0.jar -port 5556 -role node -hub http://localhost:4444/grid/register -browser browserName=chrome,maxInstances=5 &
java -jar selenium-server-standalone-2.53.0.jar -port 5557 -role node -hub http://localhost:4444/grid/register -browser browserName=chrome,maxInstances=5 &
