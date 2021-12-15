package org.example;
public class Car extends Vehicle {

    private int seats;   // measured in litres.  For Cars and Trucks

    public Car(String type, String make, String model, double milesPerKwH,
               String registration, double costPerMile,
               int year, int month, int day,
               int mileage, double latitude, double longitude,
               int seats) {
        // call superclass constructor to initialize the fields defined in Vehicle
        super(type, make, model, milesPerKwH,
                registration, costPerMile,
                year, month, day,
                mileage, latitude, longitude);

        this.seats = seats;
    }

    // Constructor version to be used to recreate a Car that was read from file.
    // It will have already been allocated an id.
    //
    public Car(int id, String type, String make, String model, double milesPerKwH,
               String registration, double costPerMile,
               int year, int month, int day,
               int mileage, double latitude, double longitude,
               int seats) {
        // call superclass constructor to initialize the fields defined in Vehicle
        super(id, type, make, model, milesPerKwH,
                registration, costPerMile,
                year, month, day,
                mileage, latitude, longitude);

        this.seats = seats;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "Car{" +
                "no of seats =" + seats +
                "} " + super.toString();
    }
}
