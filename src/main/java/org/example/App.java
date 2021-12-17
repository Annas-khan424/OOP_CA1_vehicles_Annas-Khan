package org.example;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
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
    PassengerStore passengerStore;  // encapsulates access to list of Passengers
    VehicleManager vehicleManager;  // encapsulates access to list of Vehicles
    BookingManager bookingManager;// deals with all bookings
    Email email = new Email();
    private IdGenerator idGenerator;
    private static final Scanner KB = new Scanner(System.in);


    public static void main(String[] args) {
        App app = new App();
        app.start();
        app.close();
    }

    private void start() {



       final String emailFile = "emails.ser";


        passengerStore = new PassengerStore("passengers.txt");
        vehicleManager = new VehicleManager("vehicles.txt");
        bookingManager = new BookingManager("Booking.txt", passengerStore, vehicleManager);




//        // create VehicleManager, and load all vehicles from text file


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
                + "Enter Option [1 - 5]";

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
                        displayBookingMenu();

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
                + "4. Edit Passenger\n"
                + "5. Delete Passenger\n"
                + "6. Exit\n"
                + "Enter Option [1 - 6]";

        final int SHOW_ALL      = 1;
        final int FIND_BY_NAME  = 2;
        final int ADD           = 3;
        final int EDIT          = 4;
        final int DELETE        = 5;
        final int EXIT          = 6;

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
                    case EDIT:
                        System.out.println("Edit Passenger chosen\n");
                        editPassengerMenu();
                        break;
                    case DELETE:
                        System.out.println("Delete Passenger chosen\n");
                        System.out.println("Enter Passenger id:");
                        int id = KB.nextInt();
                        KB.nextLine();

                        Passenger pass = passengerStore.findPassengerById(id);
                        if (pass == null) {
                            System.out.println("Passenger not found");

                            break;
                        }
                        passengerStore.removePassenger(pass);
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
    public void editPassengerMenu() {


        Scanner keyboard = new Scanner(System.in);

        Passenger passenger = null;

        while (true) {
            System.out.print("Enter Passenger Name:");
            String pName = keyboard.nextLine();
            if (passengerStore.findPassengerByName(pName) != null) {
                passenger = passengerStore.findPassengerByName(pName);
                System.out.println("Passenger found...\n" + passenger.toString());


                break;
            }
            System.out.println("Cannot find passenger in our system. Try again.");
        }


        int pID = passenger.getId();


        final String MENU_ITEMS = "\n*** EDIT PASSENGER MENU ***\n"
                + "1.Edit Name\n"
                + "2.Edit Email\n"
                + "3.Edit Phone\n"
                + "4.Edit Location\n"
                + "5.Exit\n"
                + "Enter Option [1,2,3,4,5]";

        final int EDIT_NAME = 1;
        final int EDIT_EMAIL = 2;
        final int EDIT_PHONE = 3;
        final int EDIT_LOCATION = 4;
        final int EXIT = 5;


        int option = 0;

        System.out.println("\n" + MENU_ITEMS);
        try {
            String usersInput = keyboard.nextLine();
            option = Integer.parseInt(usersInput);

            switch (option) {
                case EDIT_NAME:

                    while (true) {

                        String name = validateName();


                        if (passengerStore.checkPassengerName(name, pID)) {

                            passenger.setName(name);
                            System.out.println("Name has been changed to " + name);
                            break;
                        } else {
                            System.out.println("This passenger is already exist please try again:");
                        }

                    }


                    break;
                case EDIT_EMAIL:
                    while (true) {

                        String email = validateEmail();

                        if (passengerStore.checkPassengerEmail(email, pID)) {

                            System.out.println("Changed email from " + passenger.getEmail() + " to " + email);
                            passenger.setEmail(email);
                            break;
                        }
                        System.out.println("Email is not unique try again..");
                    }


                    break;

                case EDIT_PHONE:


                    while (true) {
                        String phone = validatePhone();

                        if (passengerStore.checkPassengerPhone(phone, pID)) {

                            System.out.println("Changed Phone number from " + passenger.getPhone() + " to " + phone);
                            passenger.setPhone(phone);
                            break;
                        }
                        System.out.println("Phone number is not unique try again..");
                    }

                    break;
                case EDIT_LOCATION:

                    while (true) {

                        double latitude, longitude;
                        while (true) {


                            System.out.print("Enter latitude: ");
                            while (!keyboard.hasNextDouble()) {
                                keyboard.next();

                                System.out.print("Wrong input enter double value: ");
                            }


                            latitude = keyboard.nextDouble();
                            break;
                        }


                        while (true) {
                            System.out.print("Enter longitude: ");
                            while (!keyboard.hasNextDouble()) {
                                String input = keyboard.next();

                                System.out.print("Wrong input enter double value: ");
                            }
                            longitude = keyboard.nextDouble();
                            break;

                        }


                        if (passengerStore.checkPassengerLocation(latitude, longitude, pID)) {
                            System.out.println("Changed location ");
                            passenger.setLocation(latitude, longitude);
                            break;
                        } else {
                            System.out.println("Location is not unique");
                        }

                    }

                    break;

                case EXIT:
                    System.out.println("Exit Menu option chosen");
                    break;
                default:
                    System.out.print("Invalid option - please enter number in range");
                    break;
            }

        } catch (InputMismatchException | NumberFormatException | NullPointerException e) {
            System.out.print("Invalid option - please enter number in range");
        }

    }
    public String validateEmail() {

        Scanner keyboard = new Scanner(System.in);

        String email;

        while (true) {
            System.out.print("Enter email: ");
            email = keyboard.nextLine();

            String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                return email;
            }
            System.out.println("\nWrong email input ");

        }


    }

    public String validatePhone() {

        Scanner keyboard = new Scanner(System.in);

        while (true) {
            System.out.print("Enter phone number: ");
            String phone = keyboard.nextLine();

            String regex = "^[0-10]+(-[0-10]+)+$";
            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(phone);
            if (matcher.matches()) {
                return phone;
            }
            System.out.println("\nWrong phone input: Phone must only contain  0-9 and - \n please try again");

        }
    }

    public String validateName() {

        Scanner keyboard = new Scanner(System.in);

        while (true) {
            System.out.println("Enter Passenger name:  ");
            String name = keyboard.nextLine();

            String regex = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
            Pattern pattern = Pattern.compile(regex);

            Matcher matcher = pattern.matcher(name);
            if (matcher.matches()) {
                return name;
            }
            System.out.println("Name can only contain a-z,A-Z ',.-");
        }

    }

        private void vehicleSubmenu() {
        final String MENU_ITEMS = "\n*** VEHICLE MENU ***\n"
                + "1. Display all Vehicles\n"
                + "2. Find Vehicles by Registration\n"
                + "3. Search Vehicles\n"
                + "4. Exit\n"
                + "Enter Option [1 - 4]";

        final int Display_ALL = 1;
        final int FIND_BY_REGISTRATION = 2;
        final int SEARCH = 3;
        final int EXIT = 4;

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
                + "4. Search by Vehicle(cars) Seats\n"
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
    private void displayBookingMenu() {
        {
            final String MENU_ITEMS = "\n*** BOOKING MENU ***\n"
                    + "1. Display Bookings\n"
                    + "2. Make a booking\n"
                    + "3. Delete a booking\n"
                    + "4. Edit Booking\n"
                    + "5. Exit\n"
                    + "Enter Option [1, 5]";

            final int SHOW_ALL = 1;
            final int MAKE_BOOKING = 2;
            final int DELETE_BOOKING = 3;
            final int EDIT_BOOKING = 4;
            final int EXIT = 5;

            Scanner keyboard = new Scanner(System.in);
            int option = 0;
            do {
                System.out.println("\n" + MENU_ITEMS);
                try {
                    String usersInput = keyboard.nextLine();
                    option = Integer.parseInt(usersInput);

                    switch (option) {
                        case SHOW_ALL:
                            bookingViews();
                            break;
                        case MAKE_BOOKING:
                            bookingManager.addNewBooking();

                            break;
                        case DELETE_BOOKING:
                            System.out.println("Delete Booking by ID");

                            System.out.println("Enter Booking id:");
                            int id = keyboard.nextInt();
                            keyboard.nextLine();

                            Booking booking = bookingManager.findBookingById(id);
                            if (booking == null) {
                                System.out.println("Booking not found");

                                break;
                            }
                            bookingManager.removeBooking(booking);


                            break;

                        case EDIT_BOOKING:
                            editBookingMenu();
                            keyboard.nextLine();
                            break;
                        case EXIT:
                            System.out.println("Exit Menu option chosen");
                            break;
                        default:
                            System.out.print("Invalid option - please enter number in range");
                            break;
                    }

                } catch (InputMismatchException | NumberFormatException | NullPointerException e) {
                }
            } while (option != EXIT);

        }
    }




    private void bookingViews() {
        {
            final String MENU_ITEMS = "\n*** BOOKING Views ***\n"
                    + "1. Display all\n"
                    + "2. Display by Passenger ID\n"
                    + "3. Display by Passenger name\n"
                    + "4. Display by Booking ID\n"
                    + "5. Display all future bookings\n"
                    + "6. Display Average journey length\n"
                    + "Enter Option [1-6]";

            final int SHOW_ALL = 1;
            final int SHOW_PASSENGER_ID = 2;
            final int SHOW_PASSENGER_NAME = 3;
            final int SHOW_BOOKING_ID = 4;
            final int SHOW_FUTURE = 5;
            final int SHOW_AVG_LENGTH = 6;


            Scanner keyboard = new Scanner(System.in);
            int option = 0;

            System.out.println("\n" + MENU_ITEMS);
            try {
                String usersInput = keyboard.nextLine();
                option = Integer.parseInt(usersInput);

                switch (option) {
                    case SHOW_ALL:
                        bookingManager. displayAllBookings();
                        bookingManager.displayAllForm();

                        break;
                    case SHOW_BOOKING_ID:

                        System.out.print("Enter Booking ID: ");
                        int bookingID = keyboard.nextInt();
                        Booking booking = bookingManager.findBookingById(bookingID);

                        if (booking == null) {
                            System.out.println("Booking not found");
                        } else {
                            bookingManager.displayInForm(booking);

                        }

                        break;
                    case SHOW_PASSENGER_ID:


                        System.out.print("Enter Passenger ID: ");
                        int passengerID = keyboard.nextInt();
                        keyboard.nextLine();
                        ArrayList<Booking> bookingList = bookingManager.findBookingByPassengerID(passengerID);

                        if (!bookingList.isEmpty()) {

                            for (Booking b : bookingList) {
                                bookingManager.displayInForm(b);
                            }

                        } else {
                            System.out.println("Booking not found");

                        }


                        break;
                    case SHOW_PASSENGER_NAME:
                        System.out.print("Enter Passenger name:");
                        String name = keyboard.nextLine();

                        bookingManager.showBookingByPassengerName(name);

                        break;

                    case SHOW_FUTURE:
                        bookingManager.displayFutureBookings();

                        break;
                    case SHOW_AVG_LENGTH:
                        System.out.println("Average journey = " + bookingManager.averageJourney());

                        break;
                    default:
                        System.out.print("Invalid option - please enter number in range");
                        break;
                }

            } catch (InputMismatchException | NumberFormatException | NullPointerException e) {
                System.out.print("Invalid option - please enter number in range");
            }


        }

    }

    public void editBookingMenu() {

        {


            Scanner keyboard = new Scanner(System.in);
            Booking booking;
            int bID = 0;
            while (true) {


                System.out.println("Enter Booking ID");
                while (!keyboard.hasNextInt()) {
                    String input = keyboard.next();

                    System.out.println("Wrong input enter int value: ");
                }
                bID = keyboard.nextInt();
                keyboard.nextLine();


                booking = bookingManager.findBookingById(bID);
                if (booking != null) {
                    break;
                }
                System.out.println("Booking doesnt exist");
            }

            System.out.println("Booking found..\n " + booking);


            final String MENU_ITEMS = "\n*** Edit Booking ***\n"
                    + "1. Edit Passenger ID\n"
                    + "2. Edit Vehicle ID\n"
                    + "3. Edit Date\n"
                    + "4. Edit Start location\n"
                    + "5. Edit End location\n"
                    + "6. Edit cost\n"
                    + "7. Exit\n"
                    + "Enter Option [1-7]";

            final int EDIT_PASSENGER_ID = 1;
            final int EDIT_VEHICLE_ID = 2;
            final int EDIT_DATE = 3;
            final int EDIT_START = 4;
            final int EDIT_END = 5;
            final int EDIT_COST = 6;
            final int EXIT = 7;


            int option = 0;

            System.out.println("\n" + MENU_ITEMS);
            try {
                String usersInput = keyboard.nextLine();
                option = Integer.parseInt(usersInput);

                switch (option) {
                    case EDIT_PASSENGER_ID:

                        int pid;

                        while (true) {
                            System.out.print("Enter Passenger ID: ");
                            while (!keyboard.hasNextDouble()) {
                                String input = keyboard.next();
                                System.out.print("Wrong input enter int value: ");
                            }
                            pid = keyboard.nextInt();
                            break;
                        }

                        if (passengerStore.checkPassengerID(pid)) {
                            System.out.println("Passenger exists");
                        } else {
                            System.out.println("Passenger doesnt exist");

                            break;
                        }

                        int oldPID = booking.getPassengerId();

                        booking.setPassengerId(pid);

                        if (!bookingManager.checkBookingTime(bID, pid)) {
                            System.out.println("Can't change passenger ID, Passenger would have two booking at the same time");
                            booking.setPassengerId(oldPID);
                        } else {
                            booking.setPassengerId(pid);
                            System.out.println("Passenger ID changed");

                        }

                        break;
                    case EDIT_VEHICLE_ID:
                        System.out.println("Edit Vehicle");
                        System.out.println("Enter new Vehicle ID:");
                        int newVehicleId = keyboard.nextInt();
                        booking.setVehicleId(newVehicleId);
                        System.out.println("Vehicle Updated");

                        break;
                    case EDIT_DATE:

                        LocalDateTime dateTime = validateDate();

                        if (bookingManager.checkAvailability(booking.getVehicleId(), dateTime)) {
                            System.out.println("Can't change Date as another booking has the same car booked at this time");
                        } else {
                            System.out.println("Booking date changed");
                            booking.setBookingDateTime(dateTime);
                        }


                        break;


                    case EDIT_START:

                        System.out.println("Edit starting Longitude");
                        System.out.println("Enter new starting Longitude:");
                        double newStartLongitude = keyboard.nextDouble();
                        booking.setStartLocation(newStartLongitude, booking.getStartLocation().getLatitude());
                        System.out.println("Longitude Updated");
                        break;


                    case EDIT_END:

                        System.out.println("Edit ending Longitude");
                        System.out.println("Enter new ending Latitude:");
                        double newEndLongitude = keyboard.nextDouble();
                        booking.setStartLocation(booking.getEndLocation().getLongitude(), newEndLongitude);
                        System.out.println("Longitude Updated");

                        break;

                    case EDIT_COST:

                        double cost = validateDouble("cost");
                        booking.setCost(cost);
                        break;

                    case EXIT:
                        System.out.println("Exit Menu option chosen");
                        break;
                    default:
                        System.out.print("Invalid option - please enter number in range");
                        break;
                }

            } catch (InputMismatchException | NumberFormatException | NullPointerException e) {
                System.out.print("Invalid option - please enter number in range");
            }


        }


    }




    public double validateDouble(String coord) {

        while (true) {
            Scanner keyboard = new Scanner(System.in);

            while (true) {
                System.out.print("Enter " + coord + " : ");
                while (!keyboard.hasNextDouble()) {
                    keyboard.next();
                    System.out.print("Wrong input enter double value: ");
                }
                return keyboard.nextDouble();
            }

        }
    }
    public LocalDateTime validateDate() {

        Scanner keyboard = new Scanner(System.in);

        String date = "";
        int year = 0, month = 0, day = 0, hour = 0, minute = 0;


        while (true) {

            System.out.print("enter booking year: ");
            while (!keyboard.hasNextInt()) {
                keyboard.next();

                System.out.print("Wrong input enter int value: ");
            }

            year = keyboard.nextInt();


            if (year > 2020 && year < 2025) {
                break;
            }
            System.out.print("Please enter year between 2020-2025");

        }


        date += Integer.toString(year);
        date += "-";

        while (true) {

            System.out.print("enter booking month: ");
            while (!keyboard.hasNextInt()) {
                keyboard.next();

                System.out.print("Wrong input enter int value: ");
            }

            month = keyboard.nextInt();

            if (month >= 1 && month <= 12) {
                break;
            }
            System.out.println("Please enter month between 1-12");

        }


        date += Integer.toString(month);
        date += "-";

        while (true) {

            System.out.println("enter booking day: ");
            while (!keyboard.hasNextInt()) {
                keyboard.next();

                System.out.println("Wrong input enter int value: ");
            }

            day = keyboard.nextInt();

            if (day >= 1 && day <= 31) {
                break;
            }
            System.out.println("Please enter day between 1-31");

        }


        date += Integer.toString(day);
        date += " ";

        while (true) {

            System.out.print("enter booking hour: ");
            while (!keyboard.hasNextInt()) {
                keyboard.next();

                System.out.print("Wrong input enter int value: ");
            }

            hour = keyboard.nextInt();

            if (hour >= 1 && hour <= 24) {
                break;
            }
            System.out.println("Please enter hour between 1-24");

        }


        date += Integer.toString(hour);
        date += ":";


        while (true) {

            System.out.print("enter booking  minute: ");
            while (!keyboard.hasNextInt()) {
                keyboard.next();

                System.out.print("Wrong input enter int value: ");
            }

            minute = keyboard.nextInt();

            if (minute >= 1 && minute <= 59) {
                break;
            }
            System.out.println("Please enter minute between 1-59");

        }


        date += Integer.toString(minute);


        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-M-d H:m");
        return LocalDateTime.parse(date, dTF);


    }


    private void close() {
        vehicleManager.save();
        bookingManager.save();

        System.out.println("Program exiting... Goodbye");
    }



}



