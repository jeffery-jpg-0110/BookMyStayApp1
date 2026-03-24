public class UseCase1HotelBookingApp {
    public class BookMyStayApp {
    import java.util.*;

        // Reservation
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

        // Reservation Record (Confirmed Booking)
        class ReservationRecord {
            private String guestName;
            private String roomType;
            private String roomId;

            public ReservationRecord(String guestName, String roomType, String roomId) {
                this.guestName = guestName;
                this.roomType = roomType;
                this.roomId = roomId;
            }

            public String getGuestName() { return guestName; }
            public String getRoomType() { return roomType; }
            public String getRoomId() { return roomId; }

            public void display() {
                System.out.println("Guest: " + guestName +
                        " | Room: " + roomType +
                        " | Room ID: " + roomId);
            }
        }

        // Inventory
        class RoomInventory {
            private Map<String, Integer> availability = new HashMap<>();

            public RoomInventory() {
                availability.put("Single Room", 5);
                availability.put("Double Room", 3);
                availability.put("Suite Room", 2);
            }

            public int getAvailability(String type) {
                return availability.getOrDefault(type, 0);
            }

            public void updateAvailability(String type, int count) {
                availability.put(type, count);
            }
        }

        // Queue
        class BookingQueue {
            private Queue<Reservation> queue = new LinkedList<>();

            public void addRequest(Reservation r) { queue.offer(r); }
            public Reservation pollRequest() { return queue.poll(); }
        }

        // Booking History
        class BookingHistory {
            private List<ReservationRecord> history = new ArrayList<>();

            public void addRecord(ReservationRecord r) { history.add(r); }
            public List<ReservationRecord> getAllRecords() { return history; }
        }

        // Booking Service
        class BookingService {
            private RoomInventory inventory;
            private BookingQueue queue;
            private BookingHistory history;

            private Set<String> allRoomIds = new HashSet<>();
            private int idCounter = 1;

            public BookingService(RoomInventory inventory, BookingQueue queue, BookingHistory history) {
                this.inventory = inventory;
                this.queue = queue;
                this.history = history;
            }

            public void processBookings() {
                Reservation req;

                while ((req = queue.pollRequest()) != null) {

                    String type = req.getRoomType();
                    int available = inventory.getAvailability(type);

                    if (available > 0) {
                        String roomId = type.substring(0, 2).toUpperCase() + idCounter++;

                        while (allRoomIds.contains(roomId)) {
                            roomId = type.substring(0, 2).toUpperCase() + idCounter++;
                        }

                        allRoomIds.add(roomId);
                        inventory.updateAvailability(type, available - 1);

                        history.addRecord(new ReservationRecord(
                                req.getGuestName(),
                                type,
                                roomId
                        ));

                        System.out.println("Confirmed: " + req.getGuestName() + " | " + roomId);
                    } else {
                        System.out.println("Failed: " + req.getGuestName());
                    }
                }
            }
        }

        // Report Service
        class BookingReportService {
            private BookingHistory history;

            public BookingReportService(BookingHistory history) {
                this.history = history;
            }

            public void generateSummary() {
                Map<String, Integer> count = new HashMap<>();

                for (ReservationRecord r : history.getAllRecords()) {
                    count.put(r.getRoomType(),
                            count.getOrDefault(r.getRoomType(), 0) + 1);
                }

                for (Map.Entry<String, Integer> e : count.entrySet()) {
                    System.out.println(e.getKey() + " -> " + e.getValue());
                }
            }

            public void generateDetailedReport() {
                for (ReservationRecord r : history.getAllRecords()) {
                    r.display();
                }
            }
        }

        // Main
        public class HotelApp {
            public static void main(String[] args) {

                RoomInventory inventory = new RoomInventory();
                BookingQueue queue = new BookingQueue();
                BookingHistory history = new BookingHistory();

                queue.addRequest(new Reservation("Ram", "Single Room"));
                queue.addRequest(new Reservation("Arun", "Double Room"));

                BookingService bookingService =
                        new BookingService(inventory, queue, history);

                bookingService.processBookings();

                BookingReportService report =
                        new BookingReportService(history);

                report.generateSummary();
                report.generateDetailedReport();
            }
        }
    }
}
