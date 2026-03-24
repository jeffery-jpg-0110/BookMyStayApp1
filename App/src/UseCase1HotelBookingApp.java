public class UseCase1HotelBookingApp {
    public class BookMyStayApp {
   import java.util.*;

        // Reservation Record (Confirmed Booking)
        class ReservationRecord {
            private String guestName;
            private String roomType;
            private String roomId;
            private boolean active = true;

            public ReservationRecord(String guestName, String roomType, String roomId) {
                this.guestName = guestName;
                this.roomType = roomType;
                this.roomId = roomId;
            }

            public String getGuestName() { return guestName; }
            public String getRoomType() { return roomType; }
            public String getRoomId() { return roomId; }
            public boolean isActive() { return active; }

            public void cancel() { this.active = false; }

            public void display() {
                System.out.println("Guest: " + guestName +
                        " | Room: " + roomType +
                        " | ID: " + roomId +
                        " | Status: " + (active ? "ACTIVE" : "CANCELLED"));
            }
        }

        // Inventory
        class RoomInventory {
            private Map<String, Integer> availability = new HashMap<>();

            public RoomInventory() {
                availability.put("Single Room", 1);
                availability.put("Double Room", 1);
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

        // Booking History
        class BookingHistory {
            private List<ReservationRecord> history = new ArrayList<>();

            public void addRecord(ReservationRecord r) {
                history.add(r);
            }

            public ReservationRecord findById(String roomId) {
                for (ReservationRecord r : history) {
                    if (r.getRoomId().equals(roomId)) {
                        return r;
                    }
                }
                return null;
            }

            public void display() {
                System.out.println("=== Booking History ===");
                for (ReservationRecord r : history) {
                    r.display();
                }
                System.out.println("------------------------");
            }
        }

        // Booking Service (for initial allocation)
        class BookingService {
            private RoomInventory inventory;
            private BookingHistory history;
            private int idCounter = 1;

            public BookingService(RoomInventory inventory, BookingHistory history) {
                this.inventory = inventory;
                this.history = history;
            }

            public void confirmBooking(String guest, String type) {
                int available = inventory.getAvailability(type);

                if (available > 0) {
                    String roomId = type.substring(0, 2).toUpperCase() + idCounter++;

                    inventory.updateAvailability(type, available - 1);

                    history.addRecord(new ReservationRecord(guest, type, roomId));

                    System.out.println("Confirmed: " + guest + " | ID: " + roomId);
                } else {
                    System.out.println("Booking Failed: No availability");
                }
            }
        }

        // Cancellation Service (Rollback using Stack)
        class CancellationService {

            private RoomInventory inventory;
            private BookingHistory history;

            // Stack for rollback tracking (LIFO)
            private Stack<String> rollbackStack = new Stack<>();

            public CancellationService(RoomInventory inventory, BookingHistory history) {
                this.inventory = inventory;
                this.history = history;
            }

            public void cancelBooking(String roomId) {
                System.out.println("Attempting cancellation for ID: " + roomId);

                ReservationRecord record = history.findById(roomId);

                // Validate existence
                if (record == null) {
                    System.out.println("Cancellation Failed: Reservation not found");
                    return;
                }

                // Validate active
                if (!record.isActive()) {
                    System.out.println("Cancellation Failed: Already cancelled");
                    return;
                }

                // Push to rollback stack
                rollbackStack.push(roomId);

                // Restore inventory
                String type = record.getRoomType();
                int available = inventory.getAvailability(type);
                inventory.updateAvailability(type, available + 1);

                // Mark cancelled
                record.cancel();

                System.out.println("Cancelled: " + roomId);
            }

            public void displayRollbackStack() {
                System.out.println("Rollback Stack: " + rollbackStack);
            }
        }

        // Main
        public class HotelApp {
            public static void main(String[] args) {

                RoomInventory inventory = new RoomInventory();
                BookingHistory history = new BookingHistory();

                BookingService bookingService =
                        new BookingService(inventory, history);

                // Confirm bookings
                bookingService.confirmBooking("Ram", "Single Room");   // SI1
                bookingService.confirmBooking("Arun", "Double Room");  // DO2

                history.display();
                inventory.display();

                // Cancellation
                CancellationService cancelService =
                        new CancellationService(inventory, history);

                cancelService.cancelBooking("SI1"); // valid
                cancelService.cancelBooking("SI1"); // duplicate
                cancelService.cancelBooking("XX9"); // invalid

                cancelService.displayRollbackStack();

                history.display();
                inventory.display();
            }
        }
    }
}
