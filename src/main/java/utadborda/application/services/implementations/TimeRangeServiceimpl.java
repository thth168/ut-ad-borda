package utadborda.application.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.TimeRange;
import utadborda.application.services.DAO.TimeRangeRepo;
import utadborda.application.services.TimeRangeService;

import java.util.List;

@Service
public class TimeRangeServiceimpl implements TimeRangeService {
    private TimeRangeRepo timeRangeRepo;
    @Autowired
    public TimeRangeServiceimpl(
        TimeRangeRepo timeRangeRepo
    ){
        this.timeRangeRepo = timeRangeRepo;
    }
    @Override
    public TimeRange addTimeRange(TimeRange timeRange) {
        return timeRangeRepo.save(timeRange);
    }

    @Override
    public List<TimeRange> addTimeRanges(List<TimeRange> timeRange) {
        return timeRangeRepo.saveAll(timeRange);
    }
}
