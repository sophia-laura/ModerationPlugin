package xyz.sophialaura.moderation.dao;

import com.google.common.collect.ImmutableList;
import xyz.sophialaura.moderation.models.Report;
import xyz.sophialaura.moderation.sql.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReportDaoImpl implements ReportDao {

    private final DatabaseConnection databaseConnection;

    public ReportDaoImpl(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public void createTables() {
        try (Statement statement = getConnection().createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS `reports` (`author` VARCHAR(36) NOT NULL, `target` " +
                    "VARCHAR(36) NOT NULL, `date` BIGINT(20) NOT NULL, `reason` VARCHAR(128) NOT NULL)");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public ImmutableList<Report> findAllByTarget(UUID uniqueId) {
        List<Report> reports = new ArrayList<>();

        try (PreparedStatement stmt = getConnection().prepareStatement("SELECT * FROM `reports` WHERE `target`=?")) {
            stmt.setString(1, uniqueId.toString());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reports.add(Report.builder()
                        .author(UUID.fromString(rs.getString("author")))
                        .target(uniqueId)
                        .date(rs.getLong("date"))
                        .reason(rs.getString("reason"))
                        .build());
            }
            rs.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return ImmutableList.copyOf(reports);
    }

    @Override
    public void clearAllByTarget(UUID uniqueId) {
        databaseConnection.execute(() -> {
            try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM `reports` WHERE `target`=?")) {
                stmt.setString(1, uniqueId.toString());
                stmt.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    @Override
    public void clearAll() {
        databaseConnection.execute(() -> {
            try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM `reports`")) {
                stmt.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    @Override
    public void clearExpired() {
        databaseConnection.execute(() -> {
            try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM `reports`")) {
                stmt.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    @Override
    public void createOrUpdate(Report report) {
        databaseConnection.execute(() -> {
            try (PreparedStatement stmt = getConnection().prepareStatement("INSERT INTO `reports` (`author`, `target`, " +
                    "`date`, `reason`) VALUES (?, ?, ?, ?)")) {
                stmt.setString(1, report.getAuthor().toString());
                stmt.setString(2, report.getTarget().toString());
                stmt.setLong(3, report.getDate());
                stmt.setString(4, report.getReason());
                stmt.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    @Override
    public void delete(Report report) {
        databaseConnection.execute(() -> {
            try (PreparedStatement stmt = getConnection().prepareStatement("DELETE FROM `reports` WHERE `date`=?")) {
                stmt.setLong(1, report.getDate());
                stmt.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    private Connection getConnection() {
        return databaseConnection.getConnection();
    }

}
