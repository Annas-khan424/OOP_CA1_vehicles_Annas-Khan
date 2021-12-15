package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class VehicleManager {
    private final ArrayList<Vehicle> vehicleList;  // for Car and Van objects
    private final String FILE_NAME;

    public VehicleManager(String fileName) {
        this.vehicleList = new ArrayList<>();
        this.FILE_NAME = fileName;
        loadVehiclesFromFile(fileName);
    }

    public void loadVehiclesFromFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
//           Delimiter: set the delimiter to be a comma character ","
//                    or a carriage-return '\r', or a newline '\n'
            sc.useDelimiter("[,\r\n]+");

            while (sc.hasNext()) {
                int id = sc.nextInt();
                String type = sc.next();  // vehicle type
                String make = sc.next();
                String model = sc.next();
                double milesPerKwH = sc.nextDouble();
                String registration = sc.next();
                double costPerMile = sc.nextDouble();
                int year = sc.nextInt();   // last service date
                int month = sc.nextInt();
                int day = sc.nextInt();
                int mileage = sc.nextInt();
                double latitude = sc.nextDouble();  // Depot GPS location
                double longitude = sc.nextDouble();

                if (
                        type.equalsIgnoreCase("Van") ||
                                type.equalsIgnoreCase("Truck")
                ) {
                    double loadSpace = sc.nextDouble();
                    vehicleList.add(new Van(id, type, make, model, milesPerKwH,
                            registration, costPerMile,
                            year, month, day,
                            mileage, latitude, longitude,
                            (int) loadSpace));
                }
                else if (
                        type.equalsIgnoreCase("Car") ||
                                type.equalsIgnoreCase("4x4")
                ) {
                    int seats = sc.nextInt();
                    vehicleList.add(new Car(id, type, make, model, milesPerKwH,
                            registration, costPerMile,
                            year, month, day,
                            mileage, latitude, longitude,
                            seats));
                }
            }
            sc.close();

        } catch (IOException e) {
            System.out.println("Exception thrown. " + e);
        }
    }

    public List<Vehicle> getAllVehicle() {
        return this.vehicleList;
    }

    public void displayAllVehicles() {
        for (Vehicle v : vehicleList)
            System.out.println(v.toString());
    }
    public void displayAllVehicleId() {
        for (Vehicle v : vehicleList)
            System.out.println(v.getId());
    }

    public ArrayList findVehicleByType(String type){
        ArrayList<Van>  vanList = new ArrayList<>();
        ArrayList<Car> carList = new ArrayList<>();


        for (Vehicle v: vehicleList){
            if (v.getType().equalsIgnoreCase(type)){
                if (type.equalsIgnoreCase("car")||type.equalsIgnoreCase("4x4")){
                    carList.add((Car) v);
                }
                else {
                    vanList.add((Van) v);
                }
            }
        }
        if (type.equalsIgnoreCase("car")||type.equalsIgnoreCase("4x4")){
            carList.sort(new VehicleComparator());
            return carList;
        }else{
            vanList.sort(new VehicleComparator());
            return vanList;
        }

    }


    public Vehicle findByRegistration(String registration) {
        if (registration == null) return null;
        Vehicle v = null;
        for (Vehicle i : vehicleList)
            if (i.getRegistration().equals(registration))
                v = i;
        return v;
    }
    public Vehicle findVehicleById(int id){
        for (Vehicle v: vehicleList){
            if (v.getId()== id){
                return v;
            }
        }
        return null;}


    public ArrayList<Vehicle> searchVehicleList(VehicleSearch searchType, String searchBy) {
        ArrayList<Vehicle> searchVeh = new ArrayList<>();
        VehicleComparator comparator = new VehicleComparator();

        switch (searchType) {
            case TYPE:
                for (Vehicle v : this.vehicleList)
                    if (v.getType().toLowerCase().equals(searchBy.toLowerCase()))
                        searchVeh.add(v);
                break;
            case MAKE:
                for (Vehicle v : this.vehicleList)
                    if (v.getMake().toLowerCase().equals(searchBy.toLowerCase()))
                        searchVeh.add(v);
                break;
            case MODEL:
                for (Vehicle v : this.vehicleList)
                    if (v.getModel().toLowerCase().equals(searchBy.toLowerCase()))
                        searchVeh.add(v);
                break;
            case SEATS:
                for (Vehicle v : this.vehicleList)
                    if (v instanceof Car && ((Car) v).getSeats() == Integer.parseInt(searchBy))
                        searchVeh.add(v);
                break;
            default:
                searchVeh = null;
                break;
        }
        Collections.sort(searchVeh, comparator);
        return searchVeh;
    }




    public void save() {
        try {
            FileOutputStream f = new FileOutputStream("vehicles.txt");
            ObjectOutputStream o = new ObjectOutputStream(f);

            for (Vehicle v: vehicleList){
                o.writeObject(v);
            }
            o.close();
            f.close();

        }catch (FileNotFoundException e){
            System.out.println("File Not Found");
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}

