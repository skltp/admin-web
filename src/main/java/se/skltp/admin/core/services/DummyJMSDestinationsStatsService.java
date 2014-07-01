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
package se.skltp.admin.core.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import se.skltp.admin.rest.domain.JMSDestinationStats;
import se.skltp.admin.rest.domain.JMSDestinationsStats;

@Service
@Profile("dev")
public class DummyJMSDestinationsStatsService implements JMSDestinationsStatsService {

private static Logger LOG = LoggerFactory.getLogger(DummyJMSDestinationsStatsService.class);
	
	@Override
	public JMSDestinationsStats requestJMSDestinationsStats() {
		LOG.warn("Hardwired jms-destinations are being produced");
		
		List<JMSDestinationStats> jmsDestinationStats = new ArrayList<JMSDestinationStats>();
		JMSDestinationStats jmsDestination = new JMSDestinationStats();
		jmsDestination.setDestinationName("Queue-1");
		jmsDestination.setQueueDepth(10);
		jmsDestination.setTimestampOldestMessage(new Date());
		jmsDestinationStats.add(jmsDestination);
		
		jmsDestination = new JMSDestinationStats();
		jmsDestination.setDestinationName("Queue-2");
		jmsDestination.setQueueDepth(200);
		jmsDestination.setTimestampOldestMessage(new Date());
		jmsDestinationStats.add(jmsDestination);
		
		jmsDestination = new JMSDestinationStats();
		jmsDestination.setDestinationName("Queue-3");
		jmsDestination.setQueueDepth(59);
		jmsDestination.setTimestampOldestMessage(new Date());
		jmsDestinationStats.add(jmsDestination);
		
		jmsDestination = new JMSDestinationStats();
		jmsDestination.setDestinationName("Queue-4");
		jmsDestination.setQueueDepth(1229);
		jmsDestination.setTimestampOldestMessage(new Date());
		jmsDestinationStats.add(jmsDestination);
		
		jmsDestination = new JMSDestinationStats();
		jmsDestination.setDestinationName("Queue-5");
		jmsDestination.setQueueDepth(0);
		jmsDestination.setTimestampOldestMessage(new Date());
		jmsDestinationStats.add(jmsDestination);
		
		return new JMSDestinationsStats(jmsDestinationStats);
	}
}
