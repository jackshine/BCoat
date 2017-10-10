/*
The MIT License (MIT)

Copyright (c) 2015 Victor Norgren

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>

void error(const char *msg) {
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[]) {
    int socketConnectionInQueue, socketConnection, portNumber;
    socklen_t socketLength;
    char buffer[256];
    struct sockaddr_in serv_addr, cli_addr;
    int n;
    
    if(argc < 2) {
        fprintf(stderr, "ERROR, no port provided\n");
        exit(1);
    }
    
    socketConnectionInQueue = socket(AF_INET, SOCK_STREAM, 0);
    
    if(socketConnectionInQueue < 0) {
        error("ERROR opening socket\n");
    }
    
    bzero((char *)&serv_addr, sizeof(serv_addr));
    portNumber = atoi(argv[1]);
    
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(portNumber);

    if(bind(socketConnectionInQueue, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
        error("ERROR on binding\n");
    }

    listen(socketConnectionInQueue, 5);

    socketLength = sizeof(cli_addr);
    socketConnection = accept(socketConnectionInQueue, (struct sockaddr *)&cli_addr, &socketLength);
    
    if(socketConnection < 0) {
        error("ERROR on accept\n");
    }

    bzero(buffer, 256);
    n = read(socketConnection, buffer, 255);
    if(n < 0) error("ERROR reading from socket\n");
    printf("Message: %s\n", buffer);
    n = write(socketConnection, "Message received.", 17);
    if(n < 0) error("ERROR writing to socket\n");
    close(socketConnection);
    close(socketConnectionInQueue);
    return 0;
}