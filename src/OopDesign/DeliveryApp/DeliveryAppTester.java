package OopDesign.DeliveryApp;

import java.util.*;

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

class DeliveryPerson{
    private String id;
    private String name;
    private Location location;
    private float zarabotka;
    private int deliveries;

    public DeliveryPerson(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.deliveries = 0;
        this.zarabotka = 0;
    }

    public Location getLocation() {
        return location;
    }
    public void addDelivery(){
        deliveries++;
    }

    public int getDeliveries() {
        return deliveries;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    public void addOrder(Location restaurant, Location user) {
        zarabotka += 90 + (restaurant.distance(user) /10)*10;
        addDelivery();
        setLocation(user);
    }

    public float getZarabotka() {
        return zarabotka;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %s Total delivery fee: %.2f Average delivery fee: %.2f", id, name, deliveries, zarabotka,getAverageZarabotka());
    }

    public float getAverageZarabotka() {
        if(deliveries == 0) return 0;
        return zarabotka/deliveries;
    }
}

class Restaurant{
    private String id;
    private String name;
    private Location location;
    private float zarabotka;
    private int deliveries;

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        zarabotka = 0;
        deliveries = 0;
    }

    public Location getLocation() {
        return location;
    }
    public void addOrder(float z){
        deliveries++;
        zarabotka = zarabotka + z;
    }
    public float getAverageZarabotka() {
        if(deliveries == 0) return 0;
        return zarabotka/deliveries;
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %s Total amount earned: %.2f Average amount earned: %.2f", id, name, deliveries, zarabotka, getAverageZarabotka());
    }
}

class Address{
    private String addressName;
    private Location location;
    public Address(String addressName, Location location) {
        this.addressName = addressName;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}

class User{
    private String id;
    private String name;
    private int totalOrders;
    private float totalSpent;
    private Map<String, Address> addresses;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
        totalOrders=0;
        totalSpent=0;
        this.addresses = new HashMap<>();
    }
    public void addAddress(String addressName, Location location) {
        addresses.put(addressName, new Address(addressName, location));
    }
    public void addOrder(float cost){
        totalOrders++;
        totalSpent+=cost;
    }
    public Address getAddress(String addressName){
        if (!addresses.containsKey(addressName)) throw new IllegalArgumentException("Address not found");
        return addresses.get(addressName);
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %s Total amount spent: %.2f Average amount spent: %.2f", id, name, totalOrders, totalSpent, totalOrders!=0 ? totalSpent/totalOrders : 0);
    }

    public float getTotalSpent() {
        return totalSpent;
    }
}

class DeliveryApp{
    private String name;
    private Map<String, DeliveryPerson> deliveryPersons;
    private Map<String, Restaurant> restaurants;
    private Map<String, User> users;

    public DeliveryApp (String name){
        this.name = name;
        deliveryPersons = new HashMap<>();
        restaurants = new HashMap<>();
        users = new HashMap<>();
    }

    public void registerDeliveryPerson (String id, String name, Location currentLocation){
        deliveryPersons.put(id, new DeliveryPerson(id, name, currentLocation));
    }

    public void addRestaurant (String id, String name, Location location){
        restaurants.put(id, new Restaurant(id, name, location));
    }

    public void addUser (String id, String name){
        users.put(id, new User(id, name));
    }

    public void addAddress (String id, String addressName, Location location){
        if(!users.containsKey(id)) return;
        users.get(id).addAddress(addressName, location);
    }
    public void orderFood(String userId, String userAddressName, String restaurantId, float cost){
        if(!users.containsKey(userId) || !restaurants.containsKey(restaurantId)) return;
        User user = users.get(userId);
        Location userLocation = user.getAddress(userAddressName).getLocation();
        Location restaurant = restaurants.get(restaurantId).getLocation();
        DeliveryPerson d = deliveryPersons.values().stream().min(Comparator
                        .comparingInt((DeliveryPerson dp) -> restaurant.distance(dp.getLocation()))
                        .thenComparingInt(DeliveryPerson::getDeliveries))
                .orElseThrow();
        user.addOrder(cost);
        restaurants.get(restaurantId).addOrder(cost);
        d.addOrder(restaurant,userLocation);
    }

    public void printUsers() {
        users.values().stream()
                .sorted(Comparator.comparing(User::getTotalSpent).reversed())
                .forEach(System.out::println);
    }

    public void printRestaurants(){
        restaurants.values()
                .stream()
                .sorted(Comparator.comparing(Restaurant::getAverageZarabotka).reversed())
                .forEach(System.out::println);
    }

    public void printDeliveryPeople(){
        deliveryPersons.values()
                .stream()
                .sorted(Comparator.comparing(DeliveryPerson::getZarabotka).reversed())
                .forEach(System.out::println);
    }

}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}
