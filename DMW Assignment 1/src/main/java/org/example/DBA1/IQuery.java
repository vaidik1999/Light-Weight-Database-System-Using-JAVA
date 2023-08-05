package org.example.DBA1;

public interface IQuery {
    void runQuery(String query);

    void createQuery(String tableName, String columns);

    void selectQuery(String tableName, String fields, String conditions);

    void insertQuery(String tableName, String values);

    void updateQuery(String tableName, String values, String conditions);

    void deleteQuery(String tableName, String conditions);
}
