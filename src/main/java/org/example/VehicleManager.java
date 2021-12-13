package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

    public void displayAllVehicles() {
        for (Vehicle v : vehicleList)
            System.out.println(v.toString());
    }

    public Vehicle findByRegistration(String registration) {
        if (registration == null) return null;
        Vehicle v = null;
        for (Vehicle i : vehicleList)
            if (i.getRegistration().equals(registration))
                v = i;
        return v;
    }

    public ArrayList<Vehicle> searchVehicleList(VehicleSearch searchType, String searchBy) {
        ArrayList<Vehicle> res = new ArrayList<>();
        VehicleComparator comparator = new VehicleComparator();

        switch (searchType) {
            case TYPE:
                for (Vehicle v : this.vehicleList)
                    if (v.getType().toLowerCase().equals(searchBy.toLowerCase()))
                        res.add(v);
                break;
            case MAKE:
                for (Vehicle v : this.vehicleList)
                    if (v.getMake().toLowerCase().equals(searchBy.toLowerCase()))
                        res.add(v);
                break;
            case MODEL:
                for (Vehicle v : this.vehicleList)
                    if (v.getModel().toLowerCase().equals(searchBy.toLowerCase()))
                        res.add(v);
                break;
            case SEATS:
                for (Vehicle v : this.vehicleList)
                    if (v instanceof Car && ((Car) v).getSeats() == Integer.parseInt(searchBy))
                        res.add(v);
                break;
            default:
                res = null;
                break;
        }
        Collections.sort(res, comparator);
        return res;
    }
    public void save() {
        File file = new File(this.FILE_NAME);
        FileWriter fWriter = null;
        try {
            fWriter = new FileWriter(file);

            for (Vehicle v : this.vehicleList) {
                int id = v.getId();
                String type = v.getType();  // vehicle type
                String make = v.getMake();
                String model = v.getModel();
                double milesPerKwH = v.getMilesPerKm();
                String registration = v.getRegistration();
                double costPerMile = v.getCostPerMile();
                int year = v.getLastServicedDate().getYear();   // last service date
                int month = v.getLastServicedDate().getMonthValue();
                int day = v.getLastServicedDate().getDayOfMonth();
                int mileage = v.getMileage();
                double latitude = v.getDepotGPSLocation().getLatitude();  // Depot GPS location
                double longitude = v.getDepotGPSLocation().getLongitude();

                String info = id+","+type+","+make+","+model+","+milesPerKwH+","+registration+","+costPerMile+","+year+","+month+","+day+","+mileage+","+latitude+","+longitude+",";
                if (v instanceof Van)
                    info += ((Van) v).getLoadSpace();
                else
                    info += ((Car) v).getSeats();
                fWriter.write(info+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

