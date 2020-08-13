package xyz.sophialaura.moderation.service.report;

import com.google.common.collect.ImmutableList;
import xyz.sophialaura.moderation.models.Report;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ReportServiceImpl implements ReportService {

    private final Set<Report> reports;

    public ReportServiceImpl() {
        reports = new HashSet<>();
    }

    @Override
    public ImmutableList<Report> findAll() {
        return ImmutableList.copyOf(reports);
    }

    @Override
    public ImmutableList<Report> findAllByTarget(UUID target) {
        return ImmutableList.copyOf(reports.stream().filter(report -> report.getTarget().equals(target)).collect(Collectors.toSet()));
    }

    @Override
    public void add(Report report) {
        this.reports.add(report);
    }

    @Override
    public void remove(Report report) {
        this.reports.remove(report);
    }

    @Override
    public void removeIf(Predicate<Report> predicate) {
        this.reports.removeIf(predicate);
    }

    @Override
    public void purgeAll() {
        this.reports.clear();
    }
}
