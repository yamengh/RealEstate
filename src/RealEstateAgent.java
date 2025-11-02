import java.io.*;
import java.util.*;
import java.util.logging.*;

/**
 * Enumeration of property genres.
 */
enum Genre {
    FAMILYHOUSE,
    CONDOMINIUM,
    FARM
}

/**
 * Defines required operations for all property types.
 */
interface PropertyInterface {

    /**
     * Applies a discount to the property’s price.
     *
     * @param percentage The discount percentage (0–100).
     */
    void makeDiscount(int percentage);

    /**
     * Calculates the total price of the property based on city and area.
     *
     * @return The total price as an integer.
     */
    int getTotalPrice();

    /**
     * Calculates the average square meters per room.
     *
     * @return The average sqm per room as a double.
     */
    double averageSqmPerRoom();

    /**
     * Returns a formatted string with property details.
     *
     * @return String representation of the property.
     */
    String toString();
}

/**
 * Represents a general real estate property.
 */
class RealEstate implements PropertyInterface, Comparable<RealEstate> {

    protected String city;
    protected double price;
    protected int sqm;
    protected double numberOfRooms;
    protected Genre genre;

    private static final Logger logger = Logger.getLogger(RealEstate.class.getName());

    /**
     * Creates a RealEstate instance.
     *
     * @param city City where the property is located
     * @param price Price per square meter
     * @param sqm Area of the property in square meters
     * @param numberOfRooms Number of rooms in the property
     * @param genre Type of property (family house, condominium, etc.)
     */
    public RealEstate(String city, double price, int sqm, double numberOfRooms, Genre genre) {
        logger.info("Creating RealEstate: " + city);
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
        logger.info("Setting city to " + city);
        this.city = city;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        logger.info("Setting price to " + price);
        this.price = price;
    }

    public int getSqm() {
        return sqm;
    }

    public void setSqm(int sqm) {
        logger.info("Setting sqm to " + sqm);
        this.sqm = sqm;
    }

    public double getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(double numberOfRooms) {
        logger.info("Setting number of rooms to " + numberOfRooms);
        this.numberOfRooms = numberOfRooms;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        logger.info("Setting genre to " + genre);
        this.genre = genre;
    }

    @Override
    public void makeDiscount(int percentage) {
        logger.info("Applying discount: " + percentage + "%");
        try {
            if (percentage < 0 || percentage > 100)
                throw new IllegalArgumentException("Invalid discount: " + percentage);
            this.price = this.price * (100 - percentage) / 100.0;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error applying discount", e);
        }
    }

    @Override
    public int getTotalPrice() {
        logger.info("Calculating total price for " + city);
        double basePrice = price * sqm;
        double modifier = 1.0;

        switch (city) {
            case "Budapest": modifier = 1.30; break;
            case "Debrecen": modifier = 1.20; break;
            case "Nyíregyháza": modifier = 1.15; break;
        }

        return (int) (basePrice * modifier);
    }

    @Override
    public double averageSqmPerRoom() {
        logger.info("Calculating average sqm/room for " + city);
        try {
            if (numberOfRooms <= 0) throw new ArithmeticException("Number of rooms must be > 0");
            return sqm / numberOfRooms;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error calculating average sqm per room", e);
            return 0;
        }
    }

    @Override
    public String toString() {
        logger.info("toString() called for " + city);
        return String.format(
                "City: %s, Price/sqm: %.2f, Area: %d sqm, Rooms: %.1f, Genre: %s, Total Price: %d, Avg sqm/room: %.2f",
                city, price, sqm, numberOfRooms, genre, getTotalPrice(), averageSqmPerRoom());
    }

    @Override
    public int compareTo(RealEstate other) {
        logger.info("Comparing " + this.city + " and " + other.city);
        return Integer.compare(this.getTotalPrice(), other.getTotalPrice());
    }
}

/**
 * Interface defining additional operations for panel-type properties.
 */
interface PanelInterface {

    /**
     * Checks whether another property has the same total price.
     *
     * @param other The property to compare with.
     * @return true if both have the same total price, false otherwise.
     */
    boolean hasSameAmount(RealEstate other);

    /**
     * Calculates the price per room for the property.
     *
     * @return Price per room as an integer.
     */
    int roomprice();
}

/**
 * Represents a specific property type: Panel.
 */
class Panel extends RealEstate implements PanelInterface {

    private int floor;
    private boolean isInsulated;
    private static final Logger logger = Logger.getLogger(Panel.class.getName());

    /**
     * Creates a Panel instance.
     *
     * @param city City where the panel is located
     * @param price Price per square meter
     * @param sqm Area in square meters
     * @param numberOfRooms Number of rooms
     * @param genre Property genre
     * @param floor Floor number
     * @param isInsulated Whether the panel is insulated
     */
    public Panel(String city, double price, int sqm, double numberOfRooms, Genre genre, int floor, boolean isInsulated) {
        super(city, price, sqm, numberOfRooms, genre);
        logger.info("Creating Panel in " + city);
        this.floor = floor;
        this.isInsulated = isInsulated;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        logger.info("Setting floor to " + floor);
        this.floor = floor;
    }

    public boolean isInsulated() {
        return isInsulated;
    }

    public void setInsulated(boolean insulated) {
        logger.info("Setting insulated to " + insulated);
        isInsulated = insulated;
    }

    @Override
    public int getTotalPrice() {
        logger.info("Calculating total price for panel in " + city);
        int baseTotal = super.getTotalPrice();
        double modifier = 1.0;

        if (floor >= 0 && floor <= 2) modifier += 0.05;
        else if (floor == 10) modifier -= 0.05;

        if (isInsulated) modifier += 0.05;

        return (int) (baseTotal * modifier);
    }

    @Override
    public String toString() {
        logger.info("toString() called for panel in " + city);
        return String.format(
                "Panel - City: %s, Price/sqm: %.2f, Area: %d sqm, Rooms: %.1f, Genre: %s, Floor: %d, Insulated: %s, Total Price: %d, Avg sqm/room: %.2f",
                city, price, sqm, numberOfRooms, genre, floor, (isInsulated ? "yes" : "no"),
                getTotalPrice(), averageSqmPerRoom());
    }

    @Override
    public boolean hasSameAmount(RealEstate other) {
        logger.info("Comparing total price equality between two properties");
        return this.getTotalPrice() == other.getTotalPrice();
    }

    @Override
    public int roomprice() {
        logger.info("Calculating price per room for panel in " + city);
        return (int) ((price * sqm) / numberOfRooms);
    }
}

/**
 * Manages real estate properties, loads data, and generates reports.
 */
public class RealEstateAgent {

    private static final Logger logger = Logger.getLogger(RealEstateAgent.class.getName());
    private static final TreeSet<RealEstate> properties = new TreeSet<>();

    static {
        try {
            LogManager.getLogManager().reset();

            FileHandler fh = new FileHandler("realEstateApp.log", true);
            fh.setFormatter(new SimpleFormatter());
            fh.setLevel(Level.ALL);

            ConsoleHandler ch = new ConsoleHandler();
            ch.setLevel(Level.INFO);

            logger.addHandler(fh);
            logger.addHandler(ch);

            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger", e);
        }
    }

    /**
     * Loads real estate data from a file.
     *
     * @param filename The input filename containing real estate data.
     */
    public static void loadFromFile(String filename) {
        logger.info("Loading properties from file: " + filename);
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                parseLine(line);
            }
            logger.info("Loaded " + properties.size() + " properties from file.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading file, loading sample data", e);
            loadSampleData();
        }
    }

    /**
     * Parses one line of data and creates a RealEstate or Panel object.
     *
     * @param line One line of input data to parse.
     */
    private static void parseLine(String line) {
        logger.info("Parsing line: " + line);
        try {
            String[] parts = line.split("#");
            String className = parts[0];
            String city = parts[1];
            double price = Double.parseDouble(parts[2]);
            int sqm = Integer.parseInt(parts[3]);
            double numberOfRooms = Double.parseDouble(parts[4]);
            Genre genre = Genre.valueOf(parts[5]);

            if (className.equals("PANEL") && parts.length >= 8) {
                int floor = Integer.parseInt(parts[6]);
                boolean isInsulated = parts[7].equalsIgnoreCase("yes");
                properties.add(new Panel(city, price, sqm, numberOfRooms, genre, floor, isInsulated));
            } else {
                properties.add(new RealEstate(city, price, sqm, numberOfRooms, genre));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error parsing line: " + line, e);
        }
    }

    /**
     * Loads fallback sample data.
     */
    private static void loadSampleData() {
        logger.info("Loading sample data.");
        properties.add(new RealEstate("Budapest", 250000, 100, 4, Genre.CONDOMINIUM));
        properties.add(new RealEstate("Debrecen", 220000, 120, 5, Genre.FAMILYHOUSE));
        properties.add(new RealEstate("Nyíregyháza", 110000, 60, 2, Genre.FARM));
        properties.add(new Panel("Budapest", 180000, 70, 3, Genre.CONDOMINIUM, 4, false));
    }

    /**
     * Generates and saves a summary report to a text file.
     *
     * @param outputFile The name of the output report file.
     */
    public static void generateReport(String outputFile) {
        logger.info("Generating report: " + outputFile);
        StringBuilder output = new StringBuilder();

        double avgSqmPrice = properties.stream()
                .mapToDouble(RealEstate::getPrice)
                .average()
                .orElse(0.0);
        output.append(String.format("Average sqm price: %.2f%n", avgSqmPrice));

        int cheapestPrice = properties.stream()
                .mapToInt(RealEstate::getTotalPrice)
                .min().orElse(0);
        output.append(String.format("Cheapest property: %d%n", cheapestPrice));

        long totalPrice = properties.stream()
                .mapToLong(RealEstate::getTotalPrice)
                .sum();
        output.append(String.format("Total of all properties: %d%n", totalPrice));

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
            writer.print(output.toString());
            logger.info("Report successfully written to " + outputFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error writing report", e);
        }
    }

    /**
     * Application entry point.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        logger.info("Application started.");
        System.out.println("Real Estate Management System\n");

        loadFromFile("realestates.txt");

        System.out.println("\n=== REPORT ===\n");
        generateReport("outputRealEstate.txt");

        logger.info("Application finished.");
    }
}
