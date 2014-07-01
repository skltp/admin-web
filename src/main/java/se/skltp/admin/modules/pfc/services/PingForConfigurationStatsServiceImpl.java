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
package se.skltp.admin.modules.pfc.services;

import static se.skltp.admin.modules.pfc.domain.PingForConfigurationStat.PingStatus.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.skltp.admin.modules.pfc.domain.PingForConfigurationStat;
import se.skltp.admin.modules.pfc.domain.PingForConfigurationStats;

@Service
//@Profile("dev")
public class PingForConfigurationStatsServiceImpl implements PingForConfigurationStatsService {

private static Logger LOG = LoggerFactory.getLogger(PingForConfigurationStatsServiceImpl.class);

    @Autowired
    private PingForConfigurationStats pingForConfigurationStats;

    @Override
    public PingForConfigurationStats requestPingForConfigurationsStats() {
        LOG.warn("HardwiredPingForConfigurationsStats are being produced");

        for (PingForConfigurationStat pfcStat : pingForConfigurationStats.getList()) {
            pfcStat.incrementCounter(Ok);
        }

        LOG.warn("Returns {} stats", pingForConfigurationStats.getList().size());

        return pingForConfigurationStats;
    }
}
