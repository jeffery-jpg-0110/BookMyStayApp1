public class UseCase1HotelBookingApp {
   import java.util.*;

    // Reservation (Request)
    class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }
    }

    // Inventory (State Holder)
    class RoomInventory {
        private Map<String, Integer> availability = new HashMap<>();

        public RoomInventory() {
            availability.put("Single Room", 2);
            availability.put("Double Room", 2);
            availability.put("Suite Room", 1);
        }

        public int getAvailability(String type) {
            return availability.getOrDefault(type, 0);
        }

        public void updateAvailability(String type, int count) {
            availability.put(type, count);
        }

        public void display() {
            System.out.println("=== Inventory ===");
            for (Map.Entry<String, Integer> e : availability.entrySet()) {
                System.out.println(e.getKey() + " -> " + e.getValue());
            }
            System.out.println("------------------");
        }
    }

    // Booking Queue (FIFO)
    class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        public void addRequest(Reservation r) {
            queue.offer(r);
        }

        public Reservation pollRequest() {
            return queue.poll();
        }
    }

    // Booking Service (Safe Allocation)
    class BookingService {

        private RoomInventory inventory;
        private BookingQueue queue;

        // Track allocated IDs
        private Set<String> allRoomIds = new HashSet<>();

        // Track per room type
        private Map<String, Set<String>> allocatedRooms = new HashMap<>();

        private int idCounter = 1;

        public BookingService(RoomInventory inventory, BookingQueue queue) {
            this.inventory = inventory;
            this.queue = queue;
        }

        public void processBookings() {
            System.out.println("=== Processing Bookings ===");

            Reservation req;

            while ((req = queue.pollRequest()) != null) {

                String type = req.getRoomType();
                int available = inventory.getAvailability(type);

                if (available > 0) {

                    // Generate unique ID
                    String roomId = generateRoomId(type);

                    while (allRoomIds.contains(roomId)) {
                        roomId = generateRoomId(type);
                    }

                    // Store globally
                    allRoomIds.add(roomId);

                    // Store per type
                    allocatedRooms
                            .computeIfAbsent(type, k -> new HashSet<>())
                            .add(roomId);

                    // Update inventory
                    inventory.updateAvailability(type, available - 1);

                    System.out.println("Confirmed: " + req.getGuestName()
                            + " | " + type
                            + " | ID: " + roomId);

                } else {
                    System.out.println("Failed (No Rooms): " + req.getGuestName()
                            + " | " + type);
                }
            }

            System.out.println("----------------------------");
        }

        private String generateRoomId(String type) {
            return type.substring(0, 2).toUpperCase() + idCounter++;
        }

        public void displayAllocations() {
            System.out.println("=== Allocated Rooms ===");
            for (Map.Entry<String, Set<String>> e : allocatedRooms.entrySet()) {
                System.out.println(e.getKey() + " -> " + e.getValue());
            }
            System.out.println("------------------------");
        }
    }

    // Main
    public class HotelApp {
        public static void main(String[] args) {

            RoomInventory inventory = new RoomInventory();
            BookingQueue queue = new BookingQueue();

            // Requests (FIFO)
            queue.addRequest(new Reservation("Ram", "Single Room"));
            queue.addRequest(new Reservation("Arun", "Single Room"));
            queue.addRequest(new Reservation("Priya", "Suite Room"));
            queue.addRequest(new Reservation("John", "Suite Room")); // should fail

            BookingService service = new BookingService(inventory, queue);

            service.processBookings();
            service.displayAllocations();
            inventory.display();
        }
    }
}
