Required ActiveMQ konfiguration
================================
Ref: https://skl-tp.atlassian.net/wiki/display/SKLTP/Installationsmanual+ActiveMQ

1. Enable JMX for ActiveMQ-broker (in activemq.xml and wrapper.conf)
2. If ActiveMQ-broker is behind a firewall
  a) open firewall for broker TCP-port (default is 61616)
  b) open firewall for JMX-port (default is 1616 in wrapper.conf)
  c) konfigure RMI-port (in wrapper.conf) to be the same port as the JMX-port
     (to reduce the number of required firewall holes) as below.
     Note: RMI-port will otherwise be dynamically allocated - which won't work
       when using firewalls in between client and broker.
---
Note: change the "n" in "wrapper.java.additional.n" below to follow the sequence
in your wrapper.conf - otherwise the config won't work.

# Firewall tunnelling of JMX
#-----------------------------------------------------------------------------
# Note: by default (Java 7) two ports are needed, the com.sun.management.jmxremote.port
#   and a second port which is dynamically allocated by the server.
#   To avoid dynamic allocation of the second port, add property
#   com.sun.management.jmxremote.rmi.port below and set it to the same port as
#   the first port for convenience (fewer holes in the firewall).
#   This is available in Java 7 > update 4 according to blog:
#     "Tunneling JMX in the 7u4 JDK", http://hirt.se/blog/?p=289
#   but the property is missing from the official Java docs for both Java 7 and 8:
#     http://docs.oracle.com/javase/7/docs/technotes/guides/management/agent.html
#-----------------------------------------------------------------------------
wrapper.java.additional.n=-Dcom.sun.management.jmxremote.rmi.port=1616
---