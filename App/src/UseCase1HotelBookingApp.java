public class UseCase1HotelBookingApp {
  import java.util.*;

    // Reservation
    class Reservation {
        private String guestName;
        private String roomType;
        private String reservationId;

        public Reservation(String guestName, String roomType, String reservationId) {
            this.guestName = guestName;
            this.roomType = roomType;
            this.reservationId = reservationId;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
        public String getReservationId() { return reservationId; }
    }

    // Add-On Service
    class AddOnService {
        private String name;
        private double price;

        public AddOnService(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() { return name; }
        public double getPrice() { return price; }

        public void display() {
            System.out.println(name + " (₹" + price + ")");
        }
    }

    // Add-On Service Manager
    class AddOnServiceManager {

        // Map: ReservationID -> List of Services
        private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

        // Add service
        public void addService(String reservationId, AddOnService service) {
            serviceMap
                    .computeIfAbsent(reservationId, k -> new ArrayList<>())
                    .add(service);

            System.out.println("Added " + service.getName() +
                    " to Reservation " + reservationId);
        }

        // Get services
        public List<AddOnService> getServices(String reservationId) {
            return serviceMap.getOrDefault(reservationId, new ArrayList<>());
        }

        // Calculate cost
        public double calculateTotalCost(String reservationId) {
            double total = 0;

            for (AddOnService s : getServices(reservationId)) {
                total += s.getPrice();
            }

            return total;
        }

        // Display services
        public void displayServices(String reservationId) {
            System.out.println("=== Services for " + reservationId + " ===");

            List<AddOnService> services = getServices(reservationId);

            if (services.isEmpty()) {
                System.out.println("No services selected.");
            } else {
                for (AddOnService s : services) {
                    s.display();
                }
                System.out.println("Total Cost: ₹" + calculateTotalCost(reservationId));
            }

            System.out.println("----------------------------");
        }
    }

    // Main
    public class HotelApp {
        public static void main(String[] args) {

            // Simulated reservations (already confirmed)
            Reservation r1 = new Reservation("Ram", "Single Room", "SI1");
            Reservation r2 = new Reservation("Arun", "Double Room", "DO2");

            // Service Manager
            AddOnServiceManager manager = new AddOnServiceManager();

            // Services
            AddOnService breakfast = new AddOnService("Breakfast", 500);
            AddOnService wifi = new AddOnService("WiFi", 200);
            AddOnService spa = new AddOnService("Spa", 1500);

            // Attach services
            manager.addService(r1.getReservationId(), breakfast);
            manager.addService(r1.getReservationId(), wifi);
            manager.addService(r2.getReservationId(), spa);

            // Display
            manager.displayServices("SI1");
            manager.displayServices("DO2");
        }
    }
}
