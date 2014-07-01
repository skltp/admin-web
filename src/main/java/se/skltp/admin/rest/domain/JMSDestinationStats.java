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
package se.skltp.admin.rest.domain;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("jmsdestinationstats")
public class JMSDestinationStats implements Serializable {

	private String destinationName;
	private Integer queueDepth;
	private Date timestampOldestMessage;
	private String brokerHost;
	
	// Getters / Setters
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	
	public Integer getQueueDepth() {
		return queueDepth;
	}
	
	public void setQueueDepth(Integer queueDepth) {
		this.queueDepth = queueDepth;
	}
	
	public Date getTimestampOldestMessage() {
		return timestampOldestMessage;
	}
	
	public void setTimestampOldestMessage(Date timestampOldestMessage) {
		this.timestampOldestMessage = timestampOldestMessage;
	}
	
	public String getBrokerHost() {
		return brokerHost;
	}
	public void setBrokerHost(String brokerHost) {
		this.brokerHost = brokerHost;
	}
	
}
