package com.example.FQW.config;

import org.hibernate.dialect.PostgreSQL10Dialect;

import java.sql.Types;

public class MyDriver extends PostgreSQL10Dialect {

    public MyDriver() {
        this.registerColumnType(Types.ARRAY, "bigint[]");
//        this.registerColumnType(Types.JAVA_OBJECT, "bigint[]");
    }
}
