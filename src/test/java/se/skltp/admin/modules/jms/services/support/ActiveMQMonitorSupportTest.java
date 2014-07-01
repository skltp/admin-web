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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import se.skltp.admin.modules.jms.domain.JMSDestinationStats;

public class ActiveMQMonitorSupportTest {
	ActiveMQMonitorSupport amqSupport;
	String queuePatterns = "Q.A, Q.B, Q.C";

	@Before
	public void setup() {
		amqSupport = new ActiveMQMonitorSupport("localhost, 127.0.0.1",
				"1616, 1617", "61616, 61617", "localhost, localhost",
				queuePatterns);
	}

	@Test
	public void testFilterStatsUsingQueuePatterns() {
		List<JMSDestinationStats> stats = new ArrayList<JMSDestinationStats>();
		JMSDestinationStats stat = new JMSDestinationStats();
		stat.setDestinationName("Q.A");
		stats.add(stat);
		stat = new JMSDestinationStats();
		stat.setDestinationName("DLQ.Q.A");
		stats.add(stat);
		stat = new JMSDestinationStats();
		stat.setDestinationName("Q.A.DLQ");
		stats.add(stat);
		stat = new JMSDestinationStats();
		stat.setDestinationName("Q.C");
		stats.add(stat);
		stat = new JMSDestinationStats();
		stat.setDestinationName("Q.X");
		stats.add(stat);

		amqSupport.filterStatsUsingQueuePatterns(stats);

		assertEquals(4, stats.size());
		assertEquals("Q.A", stats.get(0).getDestinationName());
		assertEquals("DLQ.Q.A", stats.get(1).getDestinationName());
		assertEquals("Q.A.DLQ", stats.get(2).getDestinationName());
		assertEquals("Q.C", stats.get(3).getDestinationName());
	}
}
