package xyz.sophialaura.moderation.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;

public interface DatabaseConnection {

    ExecutorService getExecutor();

    Connection getConnection();

    boolean isConnected();

    void setupConnection();

    default void createTables() {
        try (Statement statement = getConnection().createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS `punish` (`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " `unique_id` VARCHAR(36) NOT NULL, `ip_address` VARCHAR(36) NOT NULL, `author` VARCHAR(36) NOT NULL," +
                    " `ip_punish` TINYINT NOT NULL, `date` BIGINT(20) NOT NULL, `reason` VARCHAR(128) NOT NULL, `punish_type`" +
                    " TINYINT NOT NULL, `expires` BIGINT(20) NOT NULL)");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    default void execute(Runnable runnable) {
        getExecutor().execute(runnable);
    }

    default void shutdown() {
        getExecutor().shutdown();
    }

}
