package org.nsu.dcis.amv.core.data.base;

import org.nsu.dcis.amv.core.entity.AspectMiningConfigurationEntity;
import org.nsu.dcis.amv.core.util.enumeration.AspectMiningType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class JdbcAspectMiningConfiguration extends BaseDao {

    private static final String SELECT_ASPECT_MINING_CONFIGURATION =
        "SELECT SEQ_ID, NAME " +
          "FROM ASPECT_MINING_CONFIGURATION " +
         "WHERE SEQ_ID = ?";

    public AspectMiningConfigurationEntity getConfiguration(AspectMiningType aspectMiningType) {
        Integer seqId = 2;
        AspectMiningConfigurationEntity entity = getJdbcTemplate().queryForObject(SELECT_ASPECT_MINING_CONFIGURATION,
                new Object[]{aspectMiningType.getSeqId()}, new RowMapper<AspectMiningConfigurationEntity>() {
            @Override
            public AspectMiningConfigurationEntity mapRow(ResultSet resultSet, int i) throws SQLException {
                AspectMiningConfigurationEntity entity = new AspectMiningConfigurationEntity();
                entity.setSeqId(resultSet.getInt("SEQ_ID"));
                entity.setName(resultSet.getString("NAME"));
                return entity;
            }
        });
        return entity;
    }
}
