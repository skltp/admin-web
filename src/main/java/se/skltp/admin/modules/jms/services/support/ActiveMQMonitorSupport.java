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
package se.skltp.admin.modules.jms.services.support;

import java.util.*;

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

import se.skltp.admin.modules.jms.domain.JMSDestinationStats;

public class ActiveMQMonitorSupport {
	private Logger log = LoggerFactory.getLogger(ActiveMQMonitorSupport.class);
	private String[] jmxServiceUrls;
	private String[] jmxBrokerNames;
	private String[] brokerUrls;
	private String[] brokerPresentationName;
	private String[] queuePatterns;
	private boolean useBrokerCredentials;
	private String brokerUsername;
	private String brokerPassword;
	private boolean useBrokerJmxCredentials;
	private String brokerJmxUsername;
	private String brokerJmxPassword;

	/**
	 * For testing only.
	 * 
	 * @param args
	 */
	// public static void main(String[] args) {
	public static void main_REMOVED_FOR_SPRING_BOOT_TO_FIND_A_SINGLE_MAIN_CLASS(
			String[] args) {
		try {
			String brokerHosts = "localhost, 127.0.0.1";
			String brokerJmxPorts = "1616, 1617";
			String brokerPorts = "61616, 61617";
			// String brokerNames = "localhost, localhost";
			String brokerNames = "nob580_1, nob580_2";
			String queuePatterns = "queue.1,queue.2";

			ActiveMQMonitorSupport amqBrowser = new ActiveMQMonitorSupport(
					brokerHosts, brokerJmxPorts, brokerPorts, brokerNames,
					queuePatterns);

			List<JMSDestinationStats> jmsDestinationStats = new ArrayList<JMSDestinationStats>();
			amqBrowser.pollAmqBrokerForStats(jmsDestinationStats);

			for (JMSDestinationStats stat : jmsDestinationStats) {
				System.out.println("Queue name: " + stat.getDestinationName()
						+ ", Queue depth: " + stat.getQueueDepth()
						+ ", Oldest message timestamp: "
						+ stat.getTimestampOldestMessage() + ", Broker: "
						+ stat.getBrokerHost());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ActiveMQMonitorSupport(String amqHost, String amqJmxPort,
			String amqBrokerPort, String amqBrokerName, String queuePatterns) {

		// this.jmxServiceUrls = "service:jmx:rmi:///jndi/rmi://" + amqHost +
		// ":" + amqJmxPort + "/jmxrmi";
		// this.brokerUrls = "tcp://" + amqHost + ":" + amqBrokerPort;

		String[] amqHosts = parseConfigEntry(amqHost);
		String[] amqJmxPorts = parseConfigEntry(amqJmxPort);
		String[] amqBrokerPorts = parseConfigEntry(amqBrokerPort);
		String[] amqBrokerNames = parseConfigEntry(amqBrokerName);
		this.queuePatterns = parseConfigEntry(queuePatterns);

		if (!(amqHosts.length == amqJmxPorts.length
				&& amqHosts.length == amqBrokerPorts.length && amqHosts.length == amqBrokerNames.length)) {
			throw new IllegalArgumentException(
					"Configuration exception: number of hosts and ports do not match");
		}

		jmxServiceUrls = new String[amqHosts.length];
		jmxBrokerNames = new String[amqHosts.length];
		brokerUrls = new String[amqHosts.length];
		brokerPresentationName = new String[amqHosts.length];
		for (int i = 0; i < amqHosts.length; i++) {
			jmxServiceUrls[i] = "service:jmx:rmi:///jndi/rmi://" + amqHosts[i]
					+ ":" + amqJmxPorts[i] + "/jmxrmi";
			// valid for AMQ > 5.8, JMX names changed in AMQ 5.8 - see release
			// notes
			jmxBrokerNames[i] = "org.apache.activemq:brokerName="
					+ amqBrokerNames[i] + ",type=Broker";
			brokerUrls[i] = "tcp://" + amqHosts[i] + ":" + amqBrokerPorts[i];
			brokerPresentationName[i] = amqHosts[i] + ":" + amqBrokerPorts[i];
		}
	}

	public void setBrokerUsername(String brokerUsername) {
		this.brokerUsername = brokerUsername;
		if (brokerUsername != null && brokerUsername.trim().length() > 0) {
			useBrokerCredentials = true;
		}
	}

	public void setBrokerPassword(String brokerPassword) {
		this.brokerPassword = brokerPassword;
	}

	public void setBrokerJmxUsername(String brokerJmxUsername) {
		this.brokerJmxUsername = brokerJmxUsername;
		if (brokerJmxUsername != null && brokerJmxUsername.trim().length() > 0) {
			useBrokerJmxCredentials = true;
		}
	}

	public void setBrokerJmxPassword(String brokerJmxPassword) {
		this.brokerJmxPassword = brokerJmxPassword;
	}

	/**
	 * Parse comma-separated entries.
	 * 
	 * @return
	 */
	private String[] parseConfigEntry(String configEntry) {
		String[] parsedEntries = configEntry.split(",");
		// remove any surrounding whitespace
		for (int i = 0; i < parsedEntries.length; i++) {
			parsedEntries[i] = parsedEntries[i].trim();
		}
		return parsedEntries;
	}

	public void pollAmqBrokerForStats(
			List<JMSDestinationStats> jmsDestinationStats) {
		for (int i = 0; i < jmxServiceUrls.length; i++) {
			try {
				List<JMSDestinationStats> statsForBroker = pollAmqBrokerUsingJmx(
						jmxServiceUrls[i], jmxBrokerNames[i],
						brokerPresentationName[i]);
				filterStatsUsingQueuePatterns(statsForBroker);
				populateTimestampForOldestMessageOnQueues(statsForBroker,
						brokerUrls[i]);
				jmsDestinationStats.addAll(statsForBroker);
			} catch (Exception e) {
				// set flag in result
				JMSDestinationStats stat = new JMSDestinationStats();
				stat.setBrokerHost(brokerPresentationName[i]);
				stat.setDestinationName("# ERROR: " + e.getMessage());
				stat.setQueueDepth(-1);
				jmsDestinationStats.add(stat);
			}
		}

		sortList(jmsDestinationStats);
	}

	void filterStatsUsingQueuePatterns(List<JMSDestinationStats> stats) {
		for (Iterator iter = stats.iterator(); iter.hasNext();) {
			JMSDestinationStats stat = (JMSDestinationStats) iter.next();
			boolean foundPatternMatch = false;
			for (String queuePattern : queuePatterns) {
				if (stat.getDestinationName().contains(queuePattern)) {
					foundPatternMatch = true;
					break;
				}
			}
			if (!foundPatternMatch) {
				iter.remove();
			}
		}
	}

	private void sortList(List<JMSDestinationStats> jmsDestinationStats) {
		// sort list by: oldest message, queue depth, queue name, broker
		Comparator<JMSDestinationStats> c = new Comparator<JMSDestinationStats>() {
			@Override
			public int compare(JMSDestinationStats stat1,
					JMSDestinationStats stat2) {
				int result = 0;
				// oldest message
				if (result == 0) {
					long t1 = stat1.getTimestampOldestMessage() != null ? stat1
							.getTimestampOldestMessage().getTime() : 0;
					long t2 = stat2.getTimestampOldestMessage() != null ? stat2
							.getTimestampOldestMessage().getTime() : 0;
					result = Long.compare(t2, t1);
				}
				// queue depth
				if (result == 0) {
					result = Integer.compare(stat2.getQueueDepth(),
							stat1.getQueueDepth());
				}
				// queue name
				if (result == 0) {
					result = stat1.getDestinationName().compareTo(
							stat2.getDestinationName());
				}
				// broker name
				if (result == 0) {
					result = stat1.getBrokerHost().compareTo(
							stat2.getBrokerHost());
				}
				return result;
			}
		};
		Collections.sort(jmsDestinationStats, c);
	}

	protected List<JMSDestinationStats> pollAmqBrokerUsingJmx(
			String jmxServiceUrl, String jmxBrokerName,
			String brokerPresentationName) {
		JMXConnector jmxc = null;
		HashMap<String, Object> environment = null;
		try {
			JMXServiceURL url = new JMXServiceURL(jmxServiceUrl);
			if (useBrokerJmxCredentials) {
				environment = new HashMap<>();
				environment.put(JMXConnector.CREDENTIALS, new String[]{brokerJmxUsername, brokerJmxPassword});
			}
			jmxc = JMXConnectorFactory.connect(url, environment);
			MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

			// System.out.println("--- DOMAINS ---");
			// for (String s : mbsc.getDomains()) {
			// System.out.println(s);
			// }

			return pollAmqBrokerUsingJmx(mbsc, jmxBrokerName,
					brokerPresentationName);
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

	protected List<JMSDestinationStats> pollAmqBrokerUsingJmx(
			MBeanServerConnection mbsc, String jmxBrokerName,
			String brokerPresentationName) throws MalformedObjectNameException,
			InvalidSelectorException {

		// Note: JMX changes from AMQ 5.6 to 5.8:
		// https://issues.apache.org/jira/browse/AMQ-4237

		// #5.6
		// ObjectName mBeanName = new
		// ObjectName("org.apache.activemq:BrokerName=localhost,Type=Broker");
		// #5.8
		ObjectName mBeanName = new ObjectName(jmxBrokerName);
		org.apache.activemq.broker.jmx.BrokerViewMBean mbeanProxy = JMX
				.newMBeanProxy(mbsc, mBeanName,
						org.apache.activemq.broker.jmx.BrokerViewMBean.class,
						false);

		List<JMSDestinationStats> jmsDestinationsStats = new ArrayList<JMSDestinationStats>();
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
			destStat.setConsumerCount((int) qbeanProxy.getConsumerCount());
			destStat.setBrokerHost(brokerPresentationName);

			// DOES NOT WORK REMOTELY!
			// throws: java.io.NotSerializableException:
			// org.apache.activemq.command.ActiveMQTextMessage
			// List messages = qbeanProxy.browseMessages();
			// for (Object msg : messages) {
			// System.err.println("list: " + msg.getClass());
			// }
		}
		return jmsDestinationsStats;
	}

	protected void populateTimestampForOldestMessageOnQueues(
			List<JMSDestinationStats> jmsDestinationsStats, String brokerUrl) {
		QueueConnection conn = null;
		Session session = null;
		try {
			ActiveMQConnectionFactory cf = null;
			if (useBrokerCredentials) {
				cf = new ActiveMQConnectionFactory(brokerUsername, brokerPassword, brokerUrl);
			}
			else {
				cf = new ActiveMQConnectionFactory(brokerUrl);
			}
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
