package xyz.sophialaura.moderation.dao;

import com.google.common.collect.ImmutableList;
import xyz.sophialaura.moderation.models.Report;
import xyz.sophialaura.moderation.sql.Crud;

import java.util.UUID;

public interface ReportDao extends Crud<Report> {

    ImmutableList<Report> findAllByTarget(UUID uniqueId);

    void clearAllByTarget(UUID uniqueId);

    void clearAll();

    void clearExpired();

}
