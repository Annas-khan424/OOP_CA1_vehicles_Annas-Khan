package org.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileWriter;

public class PassengerStore {

    private final ArrayList<Passenger> passengerList;

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
    public List<Passenger> getAllPassengers() {
        return this.passengerList;
    }

    public void displayAllPassengers() {
        for (Passenger p : this.passengerList) {
            System.out.println(p.toString());
        }
    }
    public void displayAllForm() {
        ArrayList<Passenger> passengers = passengerList;
        passengers.sort(new PassengerNameComparator());
        for (Passenger p : passengers) {
            displayForm(p);
        }
    }
    public void displayForm(Passenger p) {
        System.out.println("---------------------------------");
        System.out.println("Passenger ID: " + p.getId());
        System.out.println("Passenger Name: " + p.getName());
        System.out.println("Passenger Email: " + p.getEmail());
        System.out.println("Passenger Phone: " + p.getPhone());
        System.out.println("Location: " + p.getLocation());
        System.out.println("---------------------------------");

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
    public void passengerDelete(String name, String email)
    {
        try{
            FileWriter writer = new FileWriter("passengers.txt",true);
            for(Passenger p : passengerList){
                if (p.getName().equalsIgnoreCase(name) && p.getEmail().equalsIgnoreCase(email)){
                    passengerList.remove(p);
                    break;
                }
            }
        } catch (IOException e){
            System.out.println("Exception Thrown. "+ e);
        }
    }




    public Passenger findPassengerByName(String query) {
        for (Passenger p : this.passengerList) {
            if (p.getName().toLowerCase().equals(query.trim().toLowerCase()))
                return p;
        }
        return null;
    }




    // TODO - see functional spec for details of code to add

 } // end class
