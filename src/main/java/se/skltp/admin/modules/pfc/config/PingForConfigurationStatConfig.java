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
package se.skltp.admin.modules.pfc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import se.skltp.admin.modules.pfc.domain.PingForConfigurationStat;
import se.skltp.admin.modules.pfc.domain.PingForConfigurationStats;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by magnus on 30/06/14.
 */
@Configuration
public class PingForConfigurationStatConfig {

    @Value("${pfc.pingIntervalSecs}")
    private int pingIntervalSecs;

    @Value("${pfc.timeoutSecs}")
    private int timeoutSecs;

    @Bean
    public PingForConfigurationStats loadPingForConfigurationsStats() {
        List<PingForConfigurationStat> pingForConfigurationStats = new ArrayList<PingForConfigurationStat>();
        pingForConfigurationStats.add(new PingForConfigurationStat("Producer-1", "http://localhost:8080/ping1:"));
        pingForConfigurationStats.add(new PingForConfigurationStat("Producer-2", "http://localhost:8080/ping2:"));
        pingForConfigurationStats.add(new PingForConfigurationStat("Producer-3", "http://localhost:8080/ping3:"));

        return new PingForConfigurationStats(pingIntervalSecs, timeoutSecs, pingForConfigurationStats);
    }
}
