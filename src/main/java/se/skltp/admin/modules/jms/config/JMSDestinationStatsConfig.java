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
package se.skltp.admin.modules.jms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import se.skltp.admin.modules.jms.services.support.ActiveMQMonitorSupport;

@Configuration
public class JMSDestinationStatsConfig {
	private static Logger LOG = LoggerFactory.getLogger(JMSDestinationStatsConfig.class);

	@Value("${brokerUsername}")
	private String brokerUsername;

	@Value("${brokerPassword}")
	private String brokerPassword;

	@Value("${brokerJmxUsername}")
	private String brokerJmxUsername;

	@Value("${brokerJmxPassword}")
	private String brokerJmxPassword;

	@Value("${brokerHost}")
	private String brokerHost;

	@Value("${brokerPort}")
	private String brokerPort;

	@Value("${brokerJmxPort}")
	private String brokerJmxPort;
	
	@Value("${brokerName}")
	private String brokerName;

	@Value("${queuePatterns}")
	private String queuePatterns;	

	@Bean
	public ActiveMQMonitorSupport activeMQMonitorSupport() {
		ActiveMQMonitorSupport amqSupport =  new ActiveMQMonitorSupport(brokerHost, brokerJmxPort, brokerPort, brokerName, queuePatterns);
		amqSupport.setBrokerUsername(brokerUsername);
		amqSupport.setBrokerPassword(brokerPassword);
		return amqSupport;
	}
}
