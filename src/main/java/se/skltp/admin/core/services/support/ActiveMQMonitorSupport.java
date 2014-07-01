/**
 * Copyright (c) 2014 Center for eHalsa i samverkan (CeHis).
 * 								<http://cehis.se/>
 *
 * This file is part of SKLTP.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package se.skltp.admin.core.services.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.jms.InvalidSelectorException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.QueueConnection;
import javax.jms.Session;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.skltp.admin.rest.domain.JMSDestinationStats;

public class ActiveMQMonitorSupport {
	private Logger log = LoggerFactory.getLogger(ActiveMQMonitorSupport.class);
	private String jmxServiceUrl;
	private String brokerUrl;

	/**
	 * For testing only.
	 * 
	 * @param args
	 */
	// public static void main(String[] args) {
	public static void main_REMOVED_FOR_SPRING_BOOT_TO_FIND_A_SINGLE_MAIN_CLASS(
			String[] args) {
		try {
			ActiveMQMonitorSupport amqBrowser = new ActiveMQMonitorSupport(
					"localhost", 1616, 61616);
			List<JMSDestinationStats> jmsDestinationStats = new ArrayList<JMSDestinationStats>();
			amqBrowser.pollAmqBrokerForStats(jmsDestinationStats);

			for (JMSDestinationStats stat : jmsDestinationStats) {
				System.out.println("Queue name: " + stat.getDestinationName()
						+ ", Queue depth: " + stat.getQueueDepth()
						+ ", Oldest message timestamp: "
						+ stat.getTimestampOldestMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ActiveMQMonitorSupport(String amqHost, int amqJmxPort,
			int amqBrokerPort) {
		this.jmxServiceUrl = "service:jmx:rmi:///jndi/rmi://" + amqHost + ":"
				+ amqJmxPort + "/jmxrmi";
		this.brokerUrl = "tcp://" + amqHost + ":" + amqBrokerPort;
	}

	public void pollAmqBrokerForStats(
			List<JMSDestinationStats> jmsDestinationStats) {
		pollAmqBrokerUsingJmx(jmsDestinationStats);
		populateTimestampForOldestMessageOnQueues(jmsDestinationStats);

		// sort list by queue name
		Comparator<JMSDestinationStats> c = new Comparator<JMSDestinationStats>() {
			@Override
			public int compare(JMSDestinationStats stat1,
					JMSDestinationStats stat2) {
				return stat1.getDestinationName().compareTo(
						stat2.getDestinationName());
			}
		};
		Collections.sort(jmsDestinationStats, c);
	}

	protected void pollAmqBrokerUsingJmx(
			List<JMSDestinationStats> jmsDestinationStats) {
		JMXConnector jmxc = null;
		try {
			JMXServiceURL url = new JMXServiceURL(jmxServiceUrl);
			jmxc = JMXConnectorFactory.connect(url);
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

			// System.out.println("--- DOMAINS ---");
			// for (String s : mbsc.getDomains()) {
			// System.out.println(s);
			// }

			pollAmqBrokerUsingJmx(mbsc, jmsDestinationStats);
		} catch (Exception e) {
			log.error("could not connect using JMX", e);
			throw new RuntimeException(e);
		} finally {
			if (jmxc != null) {
				try {
					jmxc.close();
				} catch (Exception e) {
					// do nothing, close quietly
				}
			}
		}
	}

	protected void pollAmqBrokerUsingJmx(MBeanServerConnection mbsc,
			List<JMSDestinationStats> jmsDestinationsStats)
			throws MalformedObjectNameException, InvalidSelectorException {

		// Note: JMX changes from AMQ 5.6 to 5.9:
		// https://issues.apache.org/jira/browse/AMQ-4237

		// #5.6
		// ObjectName mBeanName = new
		// ObjectName("org.apache.activemq:BrokerName=localhost,Type=Broker");
		// #5.9
		ObjectName mBeanName = new ObjectName(
				"org.apache.activemq:brokerName=localhost,type=Broker");
		org.apache.activemq.broker.jmx.BrokerViewMBean mbeanProxy = JMX
				.newMBeanProxy(mbsc, mBeanName,
						org.apache.activemq.broker.jmx.BrokerViewMBean.class,
						false);

		ObjectName[] queues = mbeanProxy.getQueues();
		for (ObjectName q : queues) {
			org.apache.activemq.broker.jmx.QueueViewMBean qbeanProxy = JMX
					.newMBeanProxy(
							mbsc,
							q,
							org.apache.activemq.broker.jmx.QueueViewMBean.class,
							false);

			JMSDestinationStats destStat = new JMSDestinationStats();
			jmsDestinationsStats.add(destStat);

			destStat.setDestinationName(qbeanProxy.getName());
			destStat.setQueueDepth((int) qbeanProxy.getQueueSize());

			// DOES NOT WORK REMOTELY!
			// throws: java.io.NotSerializableException:
			// org.apache.activemq.command.ActiveMQTextMessage
			// List messages = qbeanProxy.browseMessages();
			// for (Object msg : messages) {
			// System.err.println("list: " + msg.getClass());
			// }
		}
	}

	protected void populateTimestampForOldestMessageOnQueues(
			List<JMSDestinationStats> jmsDestinationsStats) {
		QueueConnection conn = null;
		Session session = null;
		try {
			ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(
					brokerUrl);
			conn = cf.createQueueConnection();
			conn.start();
			session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);

			for (JMSDestinationStats jmsDestinationStats : jmsDestinationsStats) {
				QueueBrowser browser = null;
				try {
					browser = session.createBrowser(session
							.createQueue(jmsDestinationStats
									.getDestinationName()));
					Enumeration msgs = browser.getEnumeration();
					if (msgs.hasMoreElements()) {
						// only check the first (oldest) message
						Message msg = (Message) msgs.nextElement();
						jmsDestinationStats.setTimestampOldestMessage(new Date(
								msg.getJMSTimestamp()));
					}
				} finally {
					if (browser != null) {
						try {
							browser.close();
						} catch (Exception e) {
							// close quietly
						}
					}
				}
			}
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
					// close quietly
				}
			}
			if (conn != null) {
				try {
					conn.stop();
					conn.close();
				} catch (Exception e) {
					// close quietly
				}
			}
		}
	}

}
