package ca.concordia.ginacody.comp6231.server;



import ca.concordia.ginacody.comp6231.services.Booking;
import ca.concordia.ginacody.comp6231.services.BookingException;
import ca.concordia.ginacody.comp6231.services.CabBookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.Math.random;
import static java.util.UUID.randomUUID;

public class CabBookingServiceImpl implements CabBookingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CabBookingServiceImpl.class);


    @Override
    public Booking bookRide(String pickUpLocation) throws BookingException {
        LOGGER.info("WooHoo!!!");
        if (random() < 0.3) throw new BookingException("Cab unavailable");
        return new Booking(randomUUID().toString());
    }
}
