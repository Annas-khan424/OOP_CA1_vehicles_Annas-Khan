package org.example;

import org.javatuples.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

/** Annas Khan
 * This Vehicle Bookings Management Systems manages the booking of Vehicles
 * by Passengers.
 *
 * This program reads from 3 text files:
 * "vehicles.txt", "passengers.txt", and "next-id-store.txt"
 * You should be able to see them in the project pane.
 * You will create "bookings.txt" at a later stage, to store booking records.
 *
 * "next-id-store.txt" contains one number ("201"), which will be the
 * next auto-generated id to be used to when new vehicles, passengers, or
 * bookings are created.  The value in the file will be updated when new objects
 * are created - but not when objects are recreated from records in
 * the files - as they already have IDs.  Dont change it - it will be updated by
 * the IdGenerator class.
 */

public class App {
    private static PassengerStore passengerStore;
    private static VehicleManager vehicleManager;
    private static BookingManager bookingManager;
    private static final Scanner KB = new Scanner(System.in);

    public static void main(String[] args) {
        App app = new App();
        app.start();
        app.close();
    }

    private void start() {
        passengerStore = new PassengerStore("passengers.txt");
        vehicleManager = new VehicleManager("vehicles.txt");
        System.out.println("\n-+-+-+-+-+-+-+-+-+-+-+-+Welcome to the VEHICLE BOOKINGS MANAGEMENT SYSTEM-+-+-+-+-+-+-+-+-+-+-+-+\n");
        try {
            displayMainMenu();        // User Interface - Menu
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayMainMenu() throws IOException {
        final String MENU_ITEMS = "\n*** Welcome TO MAIN MENU ***\n"
                + "1. Passengers\n"
                + "2. Vehicles\n"
                + "3. Bookings\n"
                + "4. Exit\n"
                + "Enter Option [1 - 4]";

        final int PASSENGERS = 1;
        final int VEHICLES = 2;
        final int BOOKINGS = 3;
        final int EXIT = 4;

        int option = 0;
        do {
            System.out.println("\n" + MENU_ITEMS);
            try {
                String usersInput = KB.nextLine();
                option = Integer.parseInt(usersInput);

                switch (option) {
                    case PASSENGERS:
                        System.out.println("You Chosen Passengers option ");
                        passengerSubmenu();
                        break;
                    case VEHICLES:
                        System.out.println("You chosen Vehicles option");
                        vehicleSubmenu();
                        break;
                    case BOOKINGS:
                        System.out.println("You chosen Bookings option");
                        break;
                    case EXIT:
                        System.out.println("You chosen Exit Menu option");
                        break;
                    default:
                        System.out.print("Invalid option - please enter number in range");
                        break;
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.print("Invalid option - please enter number in range");
            }
        } while (option != EXIT);

        System.out.println("\nExiting Main Menu, goodbye.");
    }

    private void passengerSubmenu() throws IOException {
        final String MENU_ITEMS = "\n*** PASSENGER MENU ***\n"
                + "1. Show all Passengers\n"
                + "2. Find Passenger by Name\n"
                + "3. Add new Passenger\n"
                + "4. Exit\n"
                + "Enter Option [1 - 4]";

        final int SHOW_ALL      = 1;
        final int FIND_BY_NAME  = 2;
        final int ADD           = 3;
        final int EXIT          = 4;

        int option = 0;
        do {
            System.out.println("\n" + MENU_ITEMS);
            try {
                String usersInput = KB.nextLine();
                option = Integer.parseInt(usersInput);
                switch (option) {
                    case SHOW_ALL:
                        System.out.println("Display ALL Passengers");
                        passengerStore.displayAllForm();
                        break;
                    case FIND_BY_NAME:
                        System.out.println("Find Passenger by Name");
                        System.out.println("Enter passenger name: ");
                        String name = KB.nextLine();
                        Passenger p = passengerStore.findPassengerByName(name);
                        if(p==null)
                            System.out.println("No passenger matching the name \"" + name +"\"");
                        else
                            System.out.println("Found passenger: \n" + p.toString());
                        break;
                    case ADD:
                        System.out.println("Add New Passenger\n");
                        addNewPassenger();
                        break;
                    case EXIT:
                        System.out.println("Exit Menu option chosen");
                        break;
                    default:
                        System.out.print("Invalid option - please enter number in range");
                        break;
                }

            } catch (InputMismatchException | NumberFormatException e) {
                System.out.print("Invalid option - please enter number in range");
            }
        } while (option != EXIT);
    }

    private void addNewPassenger() {
        Pattern phoneRegex = Pattern.compile("^\\+?(\\d+-?)+$");
        Pattern doubleRegex = Pattern.compile("^-?(\\d+)(?:\\.\\d+)?$");
        Pattern emailRegex = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", Pattern.CASE_INSENSITIVE);
        String name = "", email, phone, latitude, longitude;
        boolean valid = true;

        do {
            System.out.print("Enter name: ");
            name = KB.nextLine();
            valid = name.length() > 1;
            if (!valid)
                System.out.println("\nInvalid input!\n");
        } while (!valid);
        do {
            System.out.print("Enter email: ");
            email = KB.nextLine();
            valid = emailRegex.matcher(email).find();
            if (!valid)
                System.out.println("\nInvalid input!\n" + email + name);
        } while (!valid);
        do {
            System.out.print("Enter phone number: ");
            phone = KB.nextLine();
            valid = phoneRegex.matcher(phone).find();
            if (!valid)
                System.out.println("\nInvalid input!\n");
        } while (!valid);
        do {
            System.out.print("Enter latitude [-90, 90]: ");
            latitude = KB.nextLine();
            valid = doubleRegex.matcher(latitude).find() && (Double.parseDouble(latitude) >= -90 && Double.parseDouble(latitude) <= 90);
            if (!valid)
                System.out.println("\nInvalid input!\n");
        } while (!valid);
        do {
            System.out.print("Enter longitude [-180, 180]: ");
            longitude = KB.nextLine();
            valid = doubleRegex.matcher(longitude).find() && (Double.parseDouble(longitude) >= -180 && Double.parseDouble(longitude) <= 180);
            if (!valid)
                System.out.println("\nInvalid input!\n");
        } while (!valid);

        System.out.println(passengerStore.addPassenger(name, email, phone, Double.parseDouble(latitude), Double.parseDouble(longitude)));
    }

    private void vehicleSubmenu() {
        final String MENU_ITEMS = "\n*** VEHICLE MENU ***\n"
                + "1. Display all Vehicles\n"
                + "2. Find Vehicles by Registration\n"
                + "3. Search Vehicles\n"
                + "4. Add a Vehicles\n"
                + "5. Exit\n"
                + "Enter Option [1 - 5]";

        final int Display_ALL = 1;
        final int FIND_BY_REGISTRATION = 2;
        final int SEARCH = 3;
        final int ADD = 4;
        final int EXIT = 5;

        int option = 0;
        do {
            System.out.println("\n" + MENU_ITEMS);
            try {
                String usersInput = KB.nextLine();
                option = Integer.parseInt(usersInput);
                switch (option) {
                    case Display_ALL:
                        System.out.println("Display ALL Vehicles");
                        vehicleManager.displayAllVehicles();
                        break;
                    case FIND_BY_REGISTRATION:

                        System.out.println("Enter vehicle registration: ");
                        String registration = KB.nextLine();
                        Vehicle v = vehicleManager.findByRegistration(registration);
                        if(v==null)
                            System.out.println("No vehicle matching the registration \"" + registration +"\"");
                        else
                            System.out.println("Found Vehicle: \n" + v.toString());
                        break;
                    case SEARCH:

                        System.out.println("Search for Vehicle(s)\n");
                        searchVehicles();

                        break;
                    case ADD:
                        System.out.println("Add New Vehicle\n");
//
                        break;
                    case EXIT:
                        System.out.println("Exit Menu option chosen");
                        break;
                    default:
                        System.out.print("Invalid option - please enter number in range");
                        break;
                }

            } catch (InputMismatchException | NumberFormatException e) {
                System.out.print("Invalid option - please enter number in range");
            }
        } while (option != EXIT);
    }
    private void searchVehicles() {
        final String MENU_ITEMS = "\n*** VEHICLE SEARCH MENU ***\n"
                + "1. Search by Vehicle Type\n"
                + "2. Search by Vehicle Make\n"
                + "3. Search by Vehicle Model\n"
                + "4. Search by Vehicle Seats\n"
                + "5. Exit\n"
                + "Enter Option [1, 5]";

        final int SEARCH_TYPE   = 1;
        final int SEARCH_MAKE   = 2;
        final int SEARCH_MODEL  = 3;
        final int SEARCH_SEATS  = 4;
        final int EXIT          = 5;

        VehicleSearch searchType = null;
        String searchBy = null;

        int option = 0;
        boolean valid = true;
        do {
            System.out.println("\n" + MENU_ITEMS);
            try {
                String usersInput = KB.nextLine();
                option = Integer.parseInt(usersInput);
                valid = true;
                ArrayList<Vehicle> result;
                switch (option) {
                    case SEARCH_TYPE:
                        System.out.println("Search by Vehicle Type");
                        System.out.print("Enter a vehicle type: ");
                        searchBy = KB.nextLine();
                        searchType = VehicleSearch.TYPE;
                        break;
                    case SEARCH_MAKE:
                        System.out.println("Search by Vehicle Make");
                        System.out.print("Enter a vehicle make: ");
                        searchBy = KB.nextLine();
                        searchType = VehicleSearch.MAKE;
                        break;
                    case SEARCH_MODEL:
                        System.out.println("Search by Vehicle Model");
                        System.out.print("Enter a vehicle model: ");
                        searchBy = KB.nextLine();
                        searchType = VehicleSearch.MODEL;
                        break;
                    case SEARCH_SEATS:
                        System.out.println("Search by Vehicle Seats");
                        System.out.print("Enter a vehicle seats: ");
                        searchBy = KB.nextLine();
                        searchType = VehicleSearch.SEATS;
                        if(searchType==null)
                            System.out.println("No Vehicle matching the Seat \"" + searchBy +"\"");
                        else
                            System.out.println("Found Vehicle by Seat: \n" + VehicleSearch.SEATS .toString());
                        break;
                    case EXIT:
                        System.out.println("Exit Menu option chosen");
                        break;
                    default:
                        System.out.print("Invalid option - please enter number in range");
                        valid = false;
                        break;
                }

                if (searchBy != null && searchType != null) {
                    result = vehicleManager.searchVehicleList(searchType, searchBy);

                    if (result == null) {
                        System.out.println("\nNo vehicles found");
                    }
                    else {
                        System.out.println("\nSearch Results:");
                        for (Vehicle v: result)
                            System.out.println(v);
                    }
                }
            } catch (InputMismatchException | NumberFormatException e) {
                System.out.print("Invalid option - please enter number in range");
            }
        } while (option != EXIT || !valid);
    }


    private void close() {
        vehicleManager.save();
        System.out.println("Program exiting... Goodbye");
    }



}


//        System.out.println("\nFind vehicle by Reg Number");
//
//        if (vehicleManager.FindVehicleByRegNumber(151D987105) == null)
//        {
//        System.out.println("Vehcle not found");
//        }
//        else{
//        System.out.println(vehicleManager.FindVehicleByRegNumber("151D987105"));
//        }
//
//        System.out.println("Program exiting... Goodbye");
//    }
//}
