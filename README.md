# Distributed-system-project_1
First distributed system project
For this project, I write a server program that will serve as a key value store.  It will be set up to allow a single client to communicate with the server and perform three basic operations: 
1) PUT(key, value)
2) GET (key)
3) DELETE(key) 
I set up this server to be single-threaded and it only has to respond to a single request at a time (e.g. it need not be multi-threaded – that will be part of Project #2). I use two distinct L4 communication protocols: UDP and TCP.  What this means is that my client and server programs must use sockets (no RPC….yet, that’s project #2) and be configurable such that I can dictate that client and server communicate using UDP for a given test run, but also be able to accomplish the same task using TCP. 

The client fulfills the following requirements:

The client takes the following command line arguments, in the order listed:
The hostname or IP address of the server (it must accept either).
The port number of the server.
The client should be robust to server failure by using a timeout mechanism to deal with an unresponsive server; if it does not receive a response to a particular request, I note it in a client log and send the remaining requests.
The client should read commands from a script that contains at least 50 distinct GET/PUT/DELETE transactions on key-value pairs. This script must have at least two different operations on at least 10 key-value pairs.   In other words, I do a GET and a PUT or a PUT and a DELETE or other valid combination of operations on 10 key value pairs.  
I design a simple protocol to communicate packet contents for the three request types along with data passed along as part of the requests (e.g. keys, values, etc.) The client must be robust to malformed or unrequested datagram packets. If it receives such a datagram packet, it should report it in a human-readable way (e.g., “received unsolicited response acknowledging unknown PUT/GET/DELETE with an invalid KEY” - something to that effect to denote an receiving an erroneous request) to my server log.   
Every line the client prints to the client log should be time-stamped with the current system time.
I have two two separate clients: 
One that communicates with the server over TCP
One that communicates with the server over UDP
 
The server must fulfills the following requirements:

The server takes the following command line arguments, in the order listed:
The port number it is to listen for datagram packets on.
The server should run forever (until forcibly killed by an external signal, such as a Control-C, a kill, or pressing the “Stop” button in Eclipse).
The server must display the requests received, and its responses, both in a human readable fashion; that is, it must explicitly print to the server log that it received a query from a particular InetAddress and port number for a specific word, and then print to the log its response to that query.
The server must be robust to malformed datagram packets. If it receives a malformed datagram packet, it should report it in a human-readable way (e.g., “received malformed request of length n from <address>:<port>”) to the server log. 
Every line the server prints to standard output or standard error must be time-stamped with the current system time (i.e., System.currentTimeMillis()).
I have two two separate clients: 
One that communicates with the server over TCP
One that communicates with the server over UDP
