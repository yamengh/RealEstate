import java.io.*;
import java.util.*;

enum Genre {
    FAMILYHOUSE,
    CONDOMINIUM,
    FARM
}

interface PropertyInterface {
    void makeDiscount(int percentage);
    int getTotalPrice();
    double averageSqmPerRoom();
    String toString();
}

class RealEstate implements PropertyInterface, Comparable<RealEstate> {
    protected String city;
    protected double price;
    protected int sqm;
    protected double numberOfRooms;
    protected Genre genre;

    public RealEstate(String city, double price, int sqm, double numberOfRooms, Genre genre) {
        this.city = city;
        this.price = price;
        this.sqm = sqm;
        this.numberOfRooms = numberOfRooms;
        this.genre = genre;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSqm() {
        return sqm;
    }

    public void setSqm(int sqm) {
        this.sqm = sqm;
    }

    public double getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(double numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public void makeDiscount(int percentage) {
        this.price = this.price * (100 - percentage) / 100.0;
    }

    @Override
    public int getTotalPrice() {
        double basePrice = price * sqm;
        double modifier = 1.0;

        switch (city) {
            case "Budapest":
                modifier = 1.30;
                break;
            case "Debrecen":
                modifier = 1.20;
                break;
            case "Nyíregyháza":
                modifier = 1.15;
                break;
        }

        return (int) (basePrice * modifier);
    }

    @Override
    public double averageSqmPerRoom() {
        return sqm / numberOfRooms;
    }

    @Override
    public String toString() {
        return String.format("City: %s, Price/sqm: %.2f, Area: %d sqm, Rooms: %.1f, Genre: %s, Total Price: %d, Avg sqm/room: %.2f",
                city, price, sqm, numberOfRooms, genre, getTotalPrice(), averageSqmPerRoom());
    }

    @Override
    public int compareTo(RealEstate other) {
        return Integer.compare(this.getTotalPrice(), other.getTotalPrice());
    }
}

interface PanelInterface {
    boolean hasSameAmount(RealEstate other);
    int roomprice();
}

class Panel extends RealEstate implements PanelInterface {
    private int floor;
    private boolean isInsulated;

    public Panel(String city, double price, int sqm, double numberOfRooms, Genre genre, int floor, boolean isInsulated) {
        super(city, price, sqm, numberOfRooms, genre);
        this.floor = floor;
        this.isInsulated = isInsulated;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public boolean isInsulated() {
        return isInsulated;
    }

    public void setInsulated(boolean insulated) {
        isInsulated = insulated;
    }

    @Override
    public int getTotalPrice() {
        int baseTotal = super.getTotalPrice();
        double modifier = 1.0;

        if (floor >= 0 && floor <= 2) {
            modifier += 0.05;
        } else if (floor == 10) {
            modifier -= 0.05;
        }

        if (isInsulated) {
            modifier += 0.05;
        }

        return (int) (baseTotal * modifier);
    }

    @Override
    public String toString() {
        return String.format("Panel - City: %s, Price/sqm: %.2f, Area: %d sqm, Rooms: %.1f, Genre: %s, Floor: %d, Insulated: %s, Total Price: %d, Avg sqm/room: %.2f",
                city, price, sqm, numberOfRooms, genre, floor, (isInsulated ? "yes" : "no"), getTotalPrice(), averageSqmPerRoom());
    }

    @Override
    public boolean hasSameAmount(RealEstate other) {
        return this.getTotalPrice() == other.getTotalPrice();
    }

    @Override
    public int roomprice() {
        return (int) ((price * sqm) / numberOfRooms);
    }
}

public class RealEstateAgent {
    private static TreeSet<RealEstate> properties = new TreeSet<>();

    public static void loadFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
            System.out.println("Loaded " + properties.size() + " properties from file.");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.out.println("Loading sample data instead...");
            loadSampleData();
        }
    }

    private static void parseLine(String line) {
        try {
            String[] parts = line.split("#");
            String className = parts[0];
            String city = parts[1];
            double price = Double.parseDouble(parts[2]);
            int sqm = Integer.parseInt(parts[3]);
            double numberOfRooms = Double.parseDouble(parts[4]);

            Genre genre;
            try {
                genre = Genre.valueOf(parts[5]);
            } catch (IllegalArgumentException e) {
                return;
            }

            if (className.equals("PANEL") && parts.length >= 8) {
                int floor = Integer.parseInt(parts[6]);
                boolean isInsulated = parts[7].equalsIgnoreCase("yes");
                properties.add(new Panel(city, price, sqm, numberOfRooms, genre, floor, isInsulated));
            } else if (className.equals("REALESTATE")) {
                properties.add(new RealEstate(city, price, sqm, numberOfRooms, genre));
            }
        } catch (Exception e) {
            System.err.println("Error parsing line: " + line + " - " + e.getMessage());
        }
    }

    private static void loadSampleData() {
        properties.add(new RealEstate("Budapest", 250000, 100, 4, Genre.CONDOMINIUM));
        properties.add(new RealEstate("Debrecen", 220000, 120, 5, Genre.FAMILYHOUSE));
        properties.add(new RealEstate("Nyíregyháza", 110000, 60, 2, Genre.FARM));
        properties.add(new RealEstate("Nyíregyháza", 250000, 160, 6, Genre.FAMILYHOUSE));
        properties.add(new RealEstate("Kisvárda", 150000, 50, 2, Genre.CONDOMINIUM));
        properties.add(new Panel("Budapest", 180000, 70, 3, Genre.CONDOMINIUM, 4, false));
        properties.add(new Panel("Debrecen", 120000, 35, 2, Genre.CONDOMINIUM, 0, true));
        properties.add(new Panel("Tiszaújváros", 120000, 750, 3, Genre.CONDOMINIUM, 10, false));
        properties.add(new Panel("Nyíregyháza", 170000, 80, 3, Genre.CONDOMINIUM, 7, false));
    }

    public static void generateReport(String outputFile) {
        StringBuilder output = new StringBuilder();

        double avgSqmPrice = properties.stream()
                .mapToDouble(RealEstate::getPrice)
                .average()
                .orElse(0.0);
        String result1 = String.format("Average square meter price: %.2f\n", avgSqmPrice);
        System.out.print(result1);
        output.append(result1);

        int cheapestPrice = properties.stream()
                .mapToInt(RealEstate::getTotalPrice)
                .min()
                .orElse(0);
        String result2 = String.format("Cheapest property price: %d\n", cheapestPrice);
        System.out.print(result2);
        output.append(result2);

        Optional<RealEstate> mostExpensiveBudapest = properties.stream()
                .filter(p -> p.getCity().equals("Budapest"))
                .max(Comparator.comparingInt(RealEstate::getTotalPrice));

        if (mostExpensiveBudapest.isPresent()) {
            double avgSqmPerRoom = mostExpensiveBudapest.get().averageSqmPerRoom();
            String result3 = String.format("Average sqm per room of most expensive Budapest property: %.2f\n", avgSqmPerRoom);
            System.out.print(result3);
            output.append(result3);
        }

        long totalPrice = properties.stream()
                .mapToLong(RealEstate::getTotalPrice)
                .sum();
        String result4 = String.format("Total price of all properties: %d\n", totalPrice);
        System.out.print(result4);
        output.append(result4);

        double averagePrice = properties.stream()
                .mapToInt(RealEstate::getTotalPrice)
                .average()
                .orElse(0.0);

        String result5 = "\nCondominiums with price not exceeding average:\n";
        System.out.print(result5);
        output.append(result5);

        properties.stream()
                .filter(p -> p.getGenre() == Genre.CONDOMINIUM)
                .filter(p -> p.getTotalPrice() <= averagePrice)
                .forEach(p -> {
                    String line = p.toString() + "\n";
                    System.out.print(line);
                    output.append(line);
                });

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.print(output.toString());
            System.out.println("\nReport written to " + outputFile);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Real Estate Management System\n");

        loadFromFile("realestates.txt");

        System.out.println("\n=== REPORT ===\n");
        generateReport("outputRealEstate.txt");
    }
}