public class UseCase1HotelBookingApp {
    public class BookMyStayApp {
   import java.io.*;
import java.util.*;

        // Reservation Record (Serializable)
        class ReservationRecord implements Serializable {
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
                        " | ID: " + roomId);
            }
        }

        // Inventory (Serializable)
        class RoomInventory implements Serializable {
            private Map<String, Integer> availability = new HashMap<>();

            public RoomInventory() {
                availability.put("Single Room", 5);
                availability.put("Double Room", 3);
                availability.put("Suite Room", 2);
            }

            public Map<String, Integer> getAvailabilityMap() {
                return availability;
            }

            public void display() {
                System.out.println("=== Inventory ===");
                for (Map.Entry<String, Integer> e : availability.entrySet()) {
                    System.out.println(e.getKey() + " -> " + e.getValue());
                }
                System.out.println("------------------");
            }
        }

        // Booking History (Serializable)
        class BookingHistory implements Serializable {
            private List<ReservationRecord> history = new ArrayList<>();

            public void addRecord(ReservationRecord r) {
                history.add(r);
            }

            public List<ReservationRecord> getAllRecords() {
                return history;
            }

            public void display() {
                System.out.println("=== Booking History ===");
                for (ReservationRecord r : history) {
                    r.display();
                }
                System.out.println("------------------------");
            }
        }

        // Persistence Service
        class PersistenceService {

            private static final String FILE_NAME = "hotel_state.dat";

            // Save state
            public static void save(RoomInventory inventory, BookingHistory history) {
                try (ObjectOutputStream oos =
                             new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

                    oos.writeObject(inventory);
                    oos.writeObject(history);

                    System.out.println("State saved successfully.");

                } catch (IOException e) {
                    System.out.println("Error saving state: " + e.getMessage());
                }
            }

            // Load state
            public static Object[] load() {
                try (ObjectInputStream ois =
                             new ObjectInputStream(new FileInputStream(FILE_NAME))) {

                    RoomInventory inventory = (RoomInventory) ois.readObject();
                    BookingHistory history = (BookingHistory) ois.readObject();

                    System.out.println("State restored successfully.");
                    return new Object[]{inventory, history};

                } catch (Exception e) {
                    System.out.println("No valid saved state found. Starting fresh.");
                    return null;
                }
            }
        }

        // Main
        public class HotelApp {
            public static void main(String[] args) {

                RoomInventory inventory;
                BookingHistory history;

                // Load persisted state
                Object[] data = PersistenceService.load();

                if (data != null) {
                    inventory = (RoomInventory) data[0];
                    history = (BookingHistory) data[1];
                } else {
                    inventory = new RoomInventory();
                    history = new BookingHistory();
                }

                // Simulate new bookings
                history.addRecord(new ReservationRecord("Ram", "Single Room", "SI1"));
                history.addRecord(new ReservationRecord("Arun", "Double Room", "DO2"));

                // Display current state
                inventory.display();
                history.display();

                // Save state before shutdown
                PersistenceService.save(inventory, history);

                System.out.println("Application running safely...");
            }
        }
    }
}
