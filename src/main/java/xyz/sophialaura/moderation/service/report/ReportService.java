package xyz.sophialaura.moderation.service.report;

import com.google.common.collect.ImmutableList;
import xyz.sophialaura.moderation.models.Report;

import java.util.UUID;
import java.util.function.Predicate;

public interface ReportService {

    ImmutableList<Report> findAll();

    ImmutableList<Report> findAllByTarget(UUID target);

    void add(Report report);

    void remove(Report report);

    void removeIf(Predicate<Report> predicate);

    void purgeAll();

}
