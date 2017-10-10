#!/usr/bin/python
# -*- encoding: iso-8859-1 -*-

"""
Python syslog client.

This code is placed in the public domain by the author.
Written by Christian Stigen Larsen.

This is especially neat for Windows users, who (I think) don't
get any syslog module in the default python installation.

See RFC3164 for more info -- http://tools.ietf.org/html/rfc3164

Note that if you intend to send messages to remote servers, their
syslogd must be started with -r to allow to receive UDP from
the network.
"""
import time
import sys
import socket
import re
import random

# I'm a python novice, so I don't know of better ways to define enums

FACILITY = {
    'kern': 0, 'user': 1, 'mail': 2, 'daemon': 3,
    'auth': 4, 'syslog': 5, 'lpr': 6, 'news': 7,
    'uucp': 8, 'cron': 9, 'authpriv': 10, 'ftp': 11,
    'local0': 16, 'local1': 17, 'local2': 18, 'local3': 19,
    'local4': 20, 'local5': 21, 'local6': 22, 'local7': 23,
}

LEVEL = {
    'emerg': 0, 'alert':1, 'crit': 2, 'err': 3,
    'warning': 4, 'notice': 5, 'info': 6, 'debug': 7
}

def syslog(sock, message, protocol="tcp", level=LEVEL['notice'], facility=FACILITY['daemon'],
    host='localhost', port=514):

    """
    Send syslog UDP packet to given host and port.
    """
    if protocol == "udp":
        #data = '<%d>%s' % (level + facility*8, message)
        data = '<165>1 2003-08-24T05:14:15.000003-07:00 192.0.2.1 myproc 8711 - - %% %s' % (message)
        sock.sendto(data, (host, port))
    else:
        #data = '<165>1 2003-08-24T05:14:15.000003-07:00 192.0.2.1 myproc 8710 - - %% %s' % (message)
        #final = str(len(data))+' '+data
        #print final
        #<30>Oct 12 12:49:06 host app[12345]: syslog msg
        #data = '<34>1 2003-10-11T22:14:15.003Z server1.com sudo - - %s' % (message)
        #data = '<165>1 2003-08-24T0%d:14:15.000003-07:00 192.0.2.1 %%ASA-6-302014 - - - %s' % ((x%6),message)
        print message
        hours = range(0, 24)
        x = random.choice(hours)
        #data = '<165>1 2003-08-24T0%d:14:15.000003-07:00 192.0.2.1 - - - - %s' % ((x%6),message)
        data = message
        #data = '<14>Feb 22 06:21:47 BAR-NG-VF500 BAR-NG-VF500/box_Firewall_Activity:  %s' % (message)
        
        #final = str(len(data))+' '+data
        sock.send(data)
    


def main():
    input_file = None
    if len(sys.argv) == 5:
        protocol = sys.argv[1]
        host = sys.argv[2]
        port = int(sys.argv[3])
        input_file = sys.argv[4]
    else:
        print "please provide host port and log file as input\n"
        print "python replay_syslogs.py <host> <port> <logfile_path>"
        exit(0)

    if protocol == "tcp":
        sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock.connect((host,port))
    else:
        sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    if input_file:
       with open(input_file, "r") as f:
        while True:
            line = f.readline()
            if not line:
                break
            res=re.match('.*?[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}[\s:]+', line)
            if res:
                line = line[len(res.group(0)):]
            syslog(sock, line, protocol=protocol, host=host, port=port)
    else:
        #syslog('Teardown TCP connection 106081 for outside:210.245.88.27/4350 to inside:192.168.1.21/3389 duration 0:00:04 bytes 106308 TCP Reset-O', host="127.0.0.1", port=50001)
        syslog('Info     BAR-NG-VF500 Remove: type=FWD|proto=UDP|srcIF=eth1|srcIP=192.168.70.6|srcPort=56144|srcMAC=08:00:27:da:d7:9c|dstIP=8.8.8.8|dstPort=53|dstService=domain|dstIF=eth0|rule=InternetAccess/<App>:RestrictBob|info=Balanced Session Idle Timeout|srcNAT=192.168.70.6|dstNAT=8.8.8.8|duration=20847|count=1|receivedBytes=164|sentBytes=62|receivedPackets=1|sentPackets=1|user=|protocol=|application=|target=|content=|urlcat=', host="127.0.0.1", port=50002)



if __name__ == '__main__':
    main()
