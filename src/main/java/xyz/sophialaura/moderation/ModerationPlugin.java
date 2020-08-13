package xyz.sophialaura.moderation;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.sophialaura.moderation.command.ReportCommand;
import xyz.sophialaura.moderation.dao.ReportDao;
import xyz.sophialaura.moderation.dao.ReportDaoImpl;
import xyz.sophialaura.moderation.models.PunishReason;
import xyz.sophialaura.moderation.models.PunishType;
import xyz.sophialaura.moderation.service.reason.PunishReasonService;
import xyz.sophialaura.moderation.service.reason.PunishReasonServiceImpl;
import xyz.sophialaura.moderation.service.report.ReportService;
import xyz.sophialaura.moderation.service.report.ReportServiceImpl;
import xyz.sophialaura.moderation.command.BMCommand;
import xyz.sophialaura.moderation.menu.api.MenuListener;
import xyz.sophialaura.moderation.sql.DatabaseConnection;
import xyz.sophialaura.moderation.sql.sqlite.SqliteConnection;
import xyz.sophialaura.moderation.staff.StaffMode;

import java.io.File;
import java.io.IOException;

public class ModerationPlugin extends JavaPlugin {

    private static ModerationPlugin instance;

    private DatabaseConnection databaseConnection;
    private ReportService reportService;
    private PunishReasonService punishReasonService;
    
    private ReportDao reportDao;

    private StaffMode staffMode;

    @Override
    public void onLoad() {
        instance = this;

        final File file = new File(getDataFolder(), "database.db");
        saveDefaultConfig();
        createFileOrIgnore(file);

        databaseConnection = new SqliteConnection(file);
        databaseConnection.setupConnection();
        databaseConnection.createTables();

        reportService = new ReportServiceImpl();
        punishReasonService = new PunishReasonServiceImpl();
        
        reportDao = new ReportDaoImpl(databaseConnection);

        staffMode = new StaffMode();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        punishReasonService.add(PunishReason.builder()
                .type(PunishType.BAN)
                .reason("Racism")
                .punishExpires(1000 * 60 * 60 * 24)
                .build());

        punishReasonService.add(PunishReason.builder()
                .type(PunishType.MUTE)
                .reason("Flood")
                .punishExpires(1000 * 60 * 60 * 24)
                .build());

        getCommand("bm").setExecutor(new BMCommand(this));
        getCommand("report").setExecutor(new ReportCommand());
    }

    @Override
    public void onDisable() {
        databaseConnection.shutdown();
    }

    public static ModerationPlugin getInstance() {
        return instance;
    }

    public ReportService getReportService() {
        return reportService;
    }

    public PunishReasonService getPunishReasonService() {
        return punishReasonService;
    }

    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }

    public ReportDao getReportDao() {
        return reportDao;
    }

    public StaffMode getStaffMode() {
        return staffMode;
    }

    private void createFileOrIgnore(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
