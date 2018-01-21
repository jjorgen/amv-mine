package org.nsu.dcis.amv.core.service;

import org.nsu.dcis.amv.core.entity.AspectMiningConfigurationEntity;
import org.nsu.dcis.amv.core.data.base.JdbcAspectMiningConfiguration;
import org.nsu.dcis.amv.core.util.enumeration.AspectMiningType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AspectMiningConfigurationService {

    @Autowired
    JdbcAspectMiningConfiguration jdbcAspectMiningConfiguration;

    public AspectMiningConfigurationEntity getConfiguration(AspectMiningType aspectMiningType) {

        AspectMiningConfigurationEntity aspectMiningConfigurationEntity = jdbcAspectMiningConfiguration.getConfiguration(aspectMiningType);

        return aspectMiningConfigurationEntity;
    }
}
