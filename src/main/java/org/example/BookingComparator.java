package org.example;

import java.util.Comparator;

public class BookingComparator implements Comparator<Booking> {

    public int compare(Booking booking1,Booking booking2){
        return booking2.getBookingDateTime().compareTo(booking1.getBookingDateTime());
    }
}


