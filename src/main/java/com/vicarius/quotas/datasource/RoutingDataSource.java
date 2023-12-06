package com.vicarius.quotas.datasource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.time.LocalTime;
import java.time.ZoneId;

public class RoutingDataSource extends AbstractRoutingDataSource {


    @Override
    protected Object determineCurrentLookupKey() {
        LocalTime now = LocalTime.now(ZoneId.of("UTC"));
        if (now.isAfter(LocalTime.of(9, 0)) && now.isBefore(LocalTime.of(17, 0))) {
            return "mysqlDataSource";
        } else {
            return "elasticDataSource";
        }
    }
}
