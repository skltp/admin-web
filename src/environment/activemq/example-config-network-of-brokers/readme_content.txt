Contains example config for setting up ActiveMQ network-of-brokers for testing with multiple brokers.

NOTE: does not contain all recommended config for SKLTP production setup - only sets up enough to allow for testing the JMS-admin app.


Ports
======
node1: uses default ports:
  JMX: 1616
  TCP: 61616
  Console: 8161

node2: uses ports (to allow for both brokers to run on the same host):
  JMX: 1617
  TCP: 61617
  Console: 8162
