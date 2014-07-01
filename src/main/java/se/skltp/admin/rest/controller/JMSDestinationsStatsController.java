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
package se.skltp.admin.rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import se.skltp.admin.core.services.JMSDestinationsStatsService;
import se.skltp.admin.rest.domain.JMSDestinationsStats;

@RestController
@RequestMapping(method = RequestMethod.GET, value = "/api/jmsdestinationsstats")
public class JMSDestinationsStatsController {
	private static Logger LOG = LoggerFactory.getLogger(JMSDestinationsStatsController.class);
	
	@Autowired
	private JMSDestinationsStatsService jmsDestinationsStatsService;
	
	@RequestMapping
	@ResponseStatus(HttpStatus.OK)
	public JMSDestinationsStats getAllJMSDestinationsStats() {
		return jmsDestinationsStatsService.requestJMSDestinationsStats();
	}
}
