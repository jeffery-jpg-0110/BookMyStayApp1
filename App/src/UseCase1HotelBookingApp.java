public class UseCase1HotelBookingApp {
    public class BookMyStayApp {
        // Inventory Class (Centralized State Management)
import java.util.HashMap;
import java.util.Map;

        class RoomInventory {
            private Map<String, Integer> availabilityMap;

            // Constructor - initialize inventory
            public RoomInventory() {
                availabilityMap = new HashMap<>();

                // Register room types with counts
                availabilityMap.put("Single Room", 5);
                availabilityMap.put("Double Room", 3);
                availabilityMap.put("Suite Room", 2);
            }

            // Get availability
            public int getAvailability(String roomType) {
                return availabilityMap.getOrDefault(roomType, 0);
            }

            // Update availability (controlled)
            public void updateAvailability(String roomType, int newCount) {
                if (availabilityMap.containsKey(roomType)) {
                    availabilityMap.put(roomType, newCount);
                } else {
                    System.out.println("Room type not found!");
                }
            }

            // Display full inventory
            public void displayInventory() {
                System.out.println("=== Room Inventory ===");
                for (Map.Entry<String, Integer> entry : availabilityMap.entrySet()) {
                    System.out.println(entry.getKey() + " -> Available: " + entry.getValue());
                }
                System.out.println("----------------------");
            }
        }
    }
}
