package org.example;
import java.util.Comparator;

public class PassengerNameComparator implements Comparator<Passenger>
{
    public int compare(Passenger passenger1, Passenger passenger2) {
        return passenger1.getName().compareTo(passenger2.getName());
    }
}
