package org.example;


import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

//
public class BookingManager {
    private final ArrayList<Booking> bookingList;
    private final PassengerStore passengerStore;
    private final VehicleManager vehicleManager;
    Email email = new Email();
    private IdGenerator idGenerator;

    // Constructor
    public BookingManager(String filename, PassengerStore passengerStore, VehicleManager vehicleManager) {
        this.bookingList = new ArrayList<>();
        loadBookingDataFromFile(filename);
        this.passengerStore = passengerStore;
        this.vehicleManager = vehicleManager;

    }

    //TODO implement functionality as per specification



    public void displayInForm(Booking b) {

        System.out.println("-----------------------------------------------------");
        System.out.println("Booking ID: " + b.getBookingId());
        System.out.println("Passenger ID: " + b.getPassengerId());
        System.out.println("Vehicle ID : " + b.getVehicleId());
        System.out.println("Booking Date and Time: " + b.getBookingDateTime());
        System.out.println("Start Location: " + b.getStartLocation());
        System.out.println("End Location: " + b.getEndLocation());
        System.out.println("Total Cost: " + b.getCost());
        System.out.println("-----------------------------------------------------");

    }
    public void displayAllForm() {
        ArrayList<Booking> booking = bookingList;
        for (Booking b : booking) {
            displayInForm(b);
        }
    }


    public void addNewBooking() {

        Scanner kb = new Scanner(System.in);
        System.out.println("These are Vehicles Ids available in the list");
        vehicleManager.displayAllVehicleId();
        System.out.println("These are Passengers Ids available in the list");
        passengerStore.displayAllPassengerId();
        try {
            FileWriter writer = new FileWriter("Booking.txt", true);

            System.out.println("Enter Vehicle ID from above list");
            int vehicleId = kb.nextInt();

            System.out.println("Enter Passenger ID from above list");
            int passengerId = kb.nextInt();

            System.out.println("Enter Booking Year");
            int year = kb.nextInt();

            System.out.println("Enter Booking Month");
            int month = kb.nextInt();

            System.out.println("Enter Booking Day");
            int day = kb.nextInt();

            System.out.println("Enter Booking Hour");
            int hour = kb.nextInt();

            System.out.println("Enter Booking Minute");
            int minute = kb.nextInt();

            System.out.println("Enter Start Latitude");
            double latStart = kb.nextDouble();

            System.out.println("Enter Start Longitude");
            double longStart = kb.nextDouble();

            System.out.println("Enter End Latitude");
            double latEnd = kb.nextDouble();

            System.out.println("Enter End Longitude");
            double longEnd = kb.nextDouble();

            System.out.println("Enter Cost");
            double cost = kb.nextDouble();

            if (passengerStore.findPassengerById(passengerId) == null) {
                System.out.println("Passenger " + passengerId + " was not found");
            }

            else if (vehicleManager.findVehicleById(vehicleId) == null) {
                System.out.println("Vehicle " + vehicleId + " was not found");
            }

            else {

                boolean found = addBooking(passengerId, vehicleId, year, month, day, hour, minute, latStart, longStart, latEnd, longEnd, cost);
                if (!found) {
                    System.out.println("Booking was added");
                } else {
                    System.out.println("Booking already exists");
                }
//                writer.write("\n" + addNewBooking().inTXT());
//                writer.close();
            }

        }catch (IOException e) {
            System.out.println("Exception Thrown. " + e);
        }


    }
    public boolean addBooking(int passengerId, int vehicleId, int year, int month, int day, int hour, int minute,
                              double startLatitude, double startLongitude,
                              double endLatitude, double endLongitude, double cost) {


        if (passengerStore.findPassengerById(passengerId) != null &&
                vehicleManager.findVehicleById(vehicleId) != null) {

            bookingList.add(new Booking(passengerId, vehicleId, year, month, day, hour, minute,
                    startLatitude, startLongitude,
                    endLatitude, endLongitude, cost));


            System.out.println(email.sendReminderBookingMessage(passengerId, vehicleId, year, month, day, hour, minute,
                    startLatitude, startLongitude,
                    endLatitude, endLongitude, cost));

        } else {
            System.out.println("Cannot find passenger or vehicle on record!");
        }
        return false;
    }



    public void removeBooking(Booking b) {

        System.out.println("Booking " + b.getBookingId() + " removed");
        bookingList.remove(b);
    }


    public Booking findBookingById(int bookingId) {

        for (Booking b : bookingList) {
            if (b.getBookingId() == bookingId) {
                return b;
            }
        }
        return null;
    }

    public ArrayList findBookingByVehicleID(int vehicleId) {
        ArrayList list = new ArrayList();
        for (Booking b : bookingList) {
            if (b.getVehicleId() == vehicleId) {
                list.add(b);
            }
        }

        return list;
    }

    public ArrayList findBookingByPassengerID(int passengerID) {
        ArrayList list = new ArrayList();
        for (Booking b : bookingList) {
            if (b.getPassengerId() == passengerID) {
                list.add(b);
            }
        }

        return list;
    }

    public void showBookingByPassengerName(String name) {

        ArrayList<Booking> nameList = filterByNameBookings(name);
        nameList.sort(new BookingComparator());
        for (Booking b : nameList) {
            displayInForm(b);
        }

    }


    public void displayAllBookings() {
        System.out.println("These are the current bookings\n");
        for (Booking b : bookingList) {
            System.out.println(b.toString());
        }
    }

    public void displayFutureBookings() {
        System.out.println("These are all the future bookings");
        LocalDateTime now = LocalDateTime.now();
        ArrayList<Booking> futureBookings = new ArrayList<>();
        for (Booking b : bookingList) {
            if (b.getBookingDateTime().isAfter(now)) {
                futureBookings.add(b);
            }
        }
        if (bookingList.isEmpty()) {
            System.out.println("There are currently now booking in the future");
        } else {
            futureBookings.sort(new BookingComparator());
            for (Booking b : futureBookings) {
                displayInForm(b);
            }
        }


    }

    public ArrayList<Booking> filterByNameBookings(String name) {
        Passenger passenger = passengerStore.findPassengerByName(name);
        ArrayList<Booking> nameFilter = new ArrayList<>();
        if (passenger == null) {
            System.out.println("No passenger with that name found");
            return null;
        }

        for (Booking b : bookingList) {
            if (b.getPassengerId() == passenger.getId()) {
                nameFilter.add(b);
            }
        }
        if (nameFilter.isEmpty()) {
            System.out.println("no booking with that passenger found");
            return null;
        }
        return nameFilter;
    }


    private void loadBookingDataFromFile(String filename) {

        try {
            FileWriter writer = new FileWriter("Booking.txt", true);

            Scanner sc = new Scanner(new File(filename));
//           Delimiter: set the delimiter to be a comma character ","
//                    or a carriage-return '\r', or a newline '\n'
            sc.useDelimiter("[,\r\n]+");

            while (sc.hasNext()) {
                int id = sc.nextInt();
                int passengerID = sc.nextInt();
                int vehicleID = sc.nextInt();
                int year = sc.nextInt();
                int month = sc.nextInt();
                int day = sc.nextInt();
                int hour = sc.nextInt();
                int minute = sc.nextInt();
                double startLatitude = sc.nextDouble();
                double startLongitude = sc.nextDouble();
                double endLatitude = sc.nextDouble();
                double endLongitude = sc.nextDouble();
                double cost = sc.nextDouble();

                String date = year + "-" + month + "-" + day + " " + hour + ":" + minute;
                DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-M-d H:m");
                LocalDateTime dateTime = LocalDateTime.parse(date, dTF);

                LocationGPS startLocation = new LocationGPS(startLatitude, startLongitude);
                LocationGPS endLocation = new LocationGPS(endLatitude, endLongitude);


                bookingList.add(new Booking(id, passengerID, vehicleID, dateTime, startLocation, endLocation, cost));
            }
            sc.close();

        } catch (IOException e) {
            System.out.println("Exception thrown. " + e);
        }
    }


    public boolean checkAvailability(int vehicleID, LocalDateTime bookingTime) {

        ArrayList<Booking> list = findBookingByVehicleID(vehicleID);

        if (!list.isEmpty()) {
            for (Booking b : list) {
                if (b.getBookingDateTime().isEqual(bookingTime)) {
                    return true;

                }
            }
        }

        return false;
    }


    public double Distance(LocationGPS start, LocationGPS end) {

        double startLat = Math.toRadians(start.getLatitude());
        double startLon = Math.toRadians(start.getLongitude());
        double endLat = Math.toRadians(end.getLatitude());
        double endLon = Math.toRadians(end.getLongitude());

        double dlong = endLon - startLon;
        double dlat = endLat - startLat;

        double ans = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(startLat) * Math.cos(endLat) * Math.pow(Math.sin(dlong / 2), 2);
        ans = 2 * Math.asin(Math.sqrt(ans));

        double R = 3956;

        ans = ans * R;

        return ans;

    }


    public double averageJourney() {


        int count = 0;
        double distance = 0;
        for (Booking b : bookingList) {

            Vehicle vehicle = vehicleManager.findVehicleById(b.getVehicleId());


            distance += Distance(vehicle.getDepotGPSLocation(), b.getStartLocation());
            distance += Distance(b.getStartLocation(), b.getEndLocation());
            distance += Distance(b.getEndLocation(), vehicle.getDepotGPSLocation());

            count += 1;

        }


        return (distance / count);
    }


    public boolean checkBookingTime(int bID, int pID) {

        Booking booking = findBookingById(bID);
        LocalDateTime date = booking.getBookingDateTime();

        for (Booking b : bookingList) {
            if (b.getPassengerId() == pID && b.getBookingId() != bID) {

                if (b.getBookingDateTime().equals(date)) {
                    return false;
                }

            }
        }
        return true;
    }



    public void save() {
        try {
            FileOutputStream f = new FileOutputStream("Booking.txt");
            ObjectOutputStream o = new ObjectOutputStream(f);

            for (Booking b : bookingList) {
                o.writeObject(b);
            }
            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}