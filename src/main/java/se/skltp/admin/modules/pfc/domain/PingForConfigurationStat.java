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
package se.skltp.admin.modules.pfc.domain;

import com.fasterxml.jackson.annotation.JsonRootName;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@JsonRootName("pingforconfigurationsstat")
public class PingForConfigurationStat implements Serializable {

    public enum PingStatus {
        Ok, Failure, Timeout, Unknown
    }

    private String name;
    private String url;
    private PingStatus status;
    private String lastErrorMessage;
    private Date lastErrorTime;
    private AtomicInteger okCounter;
    private AtomicInteger failureCounter;
    private AtomicInteger timeoutCounter;

    public PingForConfigurationStat(String name, String url) {
        this.name = name;
        this.url = url;
        status = PingStatus.Ok;
        lastErrorMessage = null;
        lastErrorTime = null;
        okCounter = new AtomicInteger(0);
        failureCounter = new AtomicInteger(0);
        timeoutCounter = new AtomicInteger(0);
    }

    public int incrementCounter(PingStatus status) {
        int newCounterValue = -1;
        switch (status) {
            case Ok:
                newCounterValue = okCounter.incrementAndGet();
                break;

            case Failure:
                newCounterValue = failureCounter.incrementAndGet();
                break;

            case Timeout:
                newCounterValue = timeoutCounter.incrementAndGet();
                break;
        }
        return newCounterValue;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public PingStatus getStatus() {
        return status;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public Date getLastErrorTime() {
        return lastErrorTime;
    }

    public AtomicInteger getOkCounter() {
        return okCounter;
    }

    public AtomicInteger getFailureCounter() {
        return failureCounter;
    }

    public AtomicInteger getTimeoutCounter() {
        return timeoutCounter;
    }
}