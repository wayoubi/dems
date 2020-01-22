package ca.concordia.ginacody.comp6231.services;

/**
 * Hello world!
 */
public interface CabBookingService {
    Booking bookRide(String pickUpLocation) throws BookingException;
}