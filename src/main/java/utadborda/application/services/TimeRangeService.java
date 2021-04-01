package utadborda.application.services;

import utadborda.application.Entities.TimeRange;

import java.util.List;

public interface TimeRangeService {
    TimeRange addTimeRange(TimeRange timeRange);
    List<TimeRange> addTimeRanges(List<TimeRange> timeRange);
}
