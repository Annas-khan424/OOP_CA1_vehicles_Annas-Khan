package org.example;

import java.util.Comparator;

public class VehicleComparator implements Comparator<Vehicle>
{
    public int compare(Vehicle vehicle1, Vehicle vehicle2) {
        return vehicle2.getRegistration().compareTo(vehicle1.getRegistration());
    }
}

