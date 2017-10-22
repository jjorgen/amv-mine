package org.nsu.dcis.core.data.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * This class initializes data base connectivity configuration.
 *
 * Modification Log:
 *
 * Date        Modifier             Description
 * ----------  -------------------  --------------------------------------------
 * 1/24/2016  J. Jorgensen         Initial version.
 */
public class BaseDao extends JdbcDaoSupport {

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }
}
