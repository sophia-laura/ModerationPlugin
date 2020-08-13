package xyz.sophialaura.moderation.sql;

public interface Crud<T> {

    void createTables();

    void createOrUpdate(T t);

    void delete(T t);

}
