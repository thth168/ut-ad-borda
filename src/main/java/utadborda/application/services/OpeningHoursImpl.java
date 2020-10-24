package utadborda.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.OpeningHours;
import utadborda.application.services.DAO.OpeningHoursRepo;

@Service
public class OpeningHoursImpl implements OpeningHoursService {
    private OpeningHoursRepo openingHoursRepo;
    @Autowired
    public OpeningHoursImpl(
        OpeningHoursRepo openingHoursRepo
    ){
        this.openingHoursRepo = openingHoursRepo;
    }

    @Override
    public OpeningHours addOpeningHours(OpeningHours openingHours) {
        return openingHoursRepo.save(openingHours);
    }
}
