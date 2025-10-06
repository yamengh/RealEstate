public class RealEstateTest {

    interface PropertyInterface {
        void makeDiscount(int percent);
        int getTotalPrice();
        double averageSqmPerRoom();
    }

    interface PanelInterface {
        boolean hasSameAmount(RealEstate other);
        int roomprice();
    }

    static class RealEstate implements PropertyInterface {

        protected String city;
        protected double price;
        protected int sqm;
        protected double numberOfRooms;
        protected Genre genre;

        enum Genre { FAMILYHOUSE, CONDOMINIUM, FARM }

        public RealEstate(String city, double price, int sqm, double numberOfRooms, Genre genre) {
            this.city = city;
            this.price = price;
            this.sqm = sqm;
            this.numberOfRooms = numberOfRooms;
            this.genre = genre;
        }

        @Override
        public void makeDiscount(int percent) { price -= price * percent / 100.0; }

        @Override
        public int getTotalPrice() {
            double total = price * sqm;
            switch(city.toLowerCase()) {
                case "budapest": total *= 1.30; break;
                case "debrecen": total *= 1.20; break;
                case "nyíregyháza": case "nyiregyhaza": total *= 1.15; break;
            }
            return (int) total;
        }

        @Override
        public double averageSqmPerRoom() { return numberOfRooms == 0 ? 0 : sqm / numberOfRooms; }

        @Override
        public String toString() {
            return String.format("City: %s | Genre: %s | Price/sqm: %.2f | Area: %d sqm | Rooms: %.1f | Total: %d | Avg sqm/room: %.2f",
                    city, genre, price, sqm, numberOfRooms, getTotalPrice(), averageSqmPerRoom());
        }
    }

    static class Panel extends RealEstate implements PanelInterface {

        private int floor;
        private boolean isInsulated;

        public Panel(String city, double price, int sqm, double numberOfRooms, Genre genre, int floor, boolean isInsulated) {
            super(city, price, sqm, numberOfRooms, genre);
            this.floor = floor;
            this.isInsulated = isInsulated;
        }

        @Override
        public int getTotalPrice() {
            double total = price * sqm;
            switch(city.toLowerCase()) {
                case "budapest": total *= 1.30; break;
                case "debrecen": total *= 1.20; break;
                case "nyíregyháza": case "nyiregyhaza": total *= 1.15; break;
            }
            if(floor >= 0 && floor <= 2) total *= 1.05;
            else if(floor == 10) total *= 0.95;
            if(isInsulated) total *= 1.05;
            return (int) total;
        }

        @Override
        public boolean hasSameAmount(RealEstate other) { return this.getTotalPrice() == other.getTotalPrice(); }

        @Override
        public int roomprice() { return numberOfRooms == 0 ? 0 : (int)(price * sqm / numberOfRooms); }

        @Override
        public String toString() {
            return String.format("Panel | City: %s | Genre: %s | Floor: %d | Insulated: %s | Price/sqm: %.2f | Area: %d sqm | Rooms: %.1f | Total: %d | Avg sqm/room: %.2f",
                    city, genre, floor, isInsulated ? "Yes" : "No", price, sqm, numberOfRooms, getTotalPrice(), averageSqmPerRoom());
        }
    }

    public static void main(String[] args) {

        RealEstate r1 = new RealEstate("Budapest", 300000, 90, 3, RealEstate.Genre.CONDOMINIUM);
        RealEstate r2 = new RealEstate("Debrecen", 180000, 120, 4, RealEstate.Genre.FAMILYHOUSE);
        RealEstate r3 = new RealEstate("Nyíregyháza", 110000, 60, 2, RealEstate.Genre.FARM);

        Panel p1 = new Panel("Budapest", 200000, 75, 3, RealEstate.Genre.CONDOMINIUM, 1, true);
        Panel p2 = new Panel("Debrecen", 120000, 35, 2, RealEstate.Genre.CONDOMINIUM, 10, false);
        Panel p3 = new Panel("Nyíregyháza", 170000, 80, 3, RealEstate.Genre.CONDOMINIUM, 5, false);

        RealEstate[] properties = {r1, r2, r3, p1, p2, p3};

        System.out.println("=== Property List ===");
        for(RealEstate re : properties) {
            System.out.println(re);
        }

        System.out.println("\n=== Discounts & Checks ===");
        r1.makeDiscount(5);
        System.out.println("r1 after 5% discount: " + r1);
        System.out.println("p1 same total as r2? " + p1.hasSameAmount(r2));
        System.out.println("p3 average room price: " + p3.roomprice());
    }
}
