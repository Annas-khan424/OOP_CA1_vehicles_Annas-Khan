package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;

public class PassengerStore {

    private final ArrayList<Passenger> passengerList;
    private final String filename = "passengers.txt";

    public PassengerStore(String fileName) {
        this.passengerList = new ArrayList<>();
        loadPassengerDataFromFile(fileName);
    }


    /**
     * Read Passenger records from a text file and create and add Passenger
     * objects to the PassengerStore.
     */
    private void loadPassengerDataFromFile(String filename) {

        try {
            Scanner sc = new Scanner(new File(filename));
//           Delimiter: set the delimiter to be a comma character ","
//                    or a carriage-return '\r', or a newline '\n'
            sc.useDelimiter("[,\r\n]+");

            while (sc.hasNext()) {
                int id = sc.nextInt();
                String name = sc.next();
                String email = sc.next();
                String phone = sc.next();
                double latitude = sc.nextDouble();
                double longitude = sc.nextDouble();

                // construct a Passenger object and add it to the passenger list
                passengerList.add(new Passenger(id, name, email, phone, latitude, longitude));
            }
            sc.close();

        } catch (IOException e) {
            System.out.println("Exception thrown. " + e);
        }
    }
//
    public void displayAllForm() {
        ArrayList<Passenger> passengers = passengerList;
        passengers.sort(new PassengerNameComparator());
        for (Passenger p : passengers) {
            displayInForm(p);
        }
    }
    public void displayInForm(Passenger p) {
        System.out.println("---------------------------------");
        System.out.println("Passenger ID: " + p.getId());
        System.out.println("Passenger Name: " + p.getName());
        System.out.println("Passenger Email: " + p.getEmail());
        System.out.println("Passenger Phone: " + p.getPhone());
        System.out.println("Location: " + p.getLocation());
        System.out.println("---------------------------------");

    }
    public void displayAllPassengerId() {
        for (Passenger p : passengerList) {
            System.out.println(p.getId());
        }
    }

    public String addPassenger(String name, String email, String phone, double latitude, double longitude) {
        try {
            FileWriter writer = new FileWriter("passengers.txt", true);

            for (Passenger p : this.passengerList) {
                if (p.getName().toLowerCase().equals(name.toLowerCase()) &&
                        p.getEmail().toLowerCase().equals(email.toLowerCase()))
                    return "\nPassenger " + name + " with email " + " is already stored";
            }

                Passenger newPassenger = new Passenger(name, email, phone, latitude, longitude);
                this.passengerList.add(newPassenger);


                writer.write("\n" + newPassenger.inTXT());
                writer.close();
                return "\nPassenger \"" + name + "\" with email \"" + email + "\" has been added";


        } catch (IOException e) {
            System.out.println("Exception Thrown. " + e);
        }
        return null;
    }
    public Passenger findPassengerById(int id) {
        for (Passenger p : passengerList) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public void removePassenger(Passenger p) {
        try{
            FileWriter writer = new FileWriter("passengers.txt",true);
        System.out.println("Passenger " + p.getId() + " removed");
        passengerList.remove(p);
            writer.write("\n" + p.inTXT());
            writer.close();
            }catch (IOException e){
          System.out.println("Exception Thrown. "+ e);
        }
    }

    //

    public boolean checkPassengerID(int pID) {

        for (Passenger p : passengerList) {

            if (p.getId() == pID) {
                return true;
            }
        }
        return false;
    }
    public boolean checkPassengerName(String name, int pID) {
            for (Passenger p : passengerList) {
            if (p.getName().toLowerCase().contains(name.toLowerCase()) && p.getId() != pID) {
                return false;
            }
        }
        return true;
    }


    public boolean checkPassengerEmail(String email, int pID) {
        for (Passenger p : passengerList) {
            if (p.getEmail().equalsIgnoreCase(email) && p.getId() != pID) {
                return false;
            }
        }
        return true;
    }

    public boolean checkPassengerPhone(String phone, int pID) {
        for (Passenger p : passengerList) {

            if (p.getPhone().equalsIgnoreCase(phone) && p.getId() != pID) {
                return false;
            }

        }
        return true;
    }

    public boolean checkPassengerLocation(double latitude, double longitude, int pID) {

        for (Passenger p : passengerList) {

            if (p.getLocation().getLatitude() == latitude && p.getLocation().getLongitude() == longitude && p.getId() != pID) {
                return false;
            }
        }
        return true;
    }




    public Passenger findPassengerByName(String query) {
        for (Passenger p : this.passengerList) {
            if (p.getName().toLowerCase().equals(query.trim().toLowerCase()))
                return p;
        }
        return null;
    }





}// end class
