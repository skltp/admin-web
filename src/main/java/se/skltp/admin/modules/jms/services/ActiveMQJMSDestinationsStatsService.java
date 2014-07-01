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
package se.skltp.admin.modules.jms.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import se.skltp.admin.modules.jms.services.support.ActiveMQMonitorSupport;
import se.skltp.admin.modules.jms.domain.JMSDestinationStats;
import se.skltp.admin.modules.jms.domain.JMSDestinationsStats;

@Service
@Profile("default")
public class ActiveMQJMSDestinationsStatsService implements JMSDestinationsStatsService {

	private static Logger LOG = LoggerFactory.getLogger(ActiveMQJMSDestinationsStatsService.class);
	
	@Autowired
	private ActiveMQMonitorSupport activeMQMonitorSupport;
		
	@Override
	public JMSDestinationsStats requestJMSDestinationsStats() {
		List<JMSDestinationStats> jmsDestinationStats = new ArrayList<JMSDestinationStats>();
		activeMQMonitorSupport.pollAmqBrokerForStats(jmsDestinationStats);
		return new JMSDestinationsStats(jmsDestinationStats);
	}

}
