public class UseCase1HotelBookingApp {
    public class BookMyStayApp {
    import java.util.List;

        class RoomSearchService {

            private RoomInventory inventory;

            public RoomSearchService(RoomInventory inventory) {
                this.inventory = inventory;
            }

            // Read-only search
            public void searchAvailableRooms(List<Room> rooms) {
                System.out.println("=== Available Rooms ===");

                for (Room room : rooms) {
                    String type = room.getRoomType();
                    int available = inventory.getAvailability(type);

                    // Defensive check: only show available rooms
                    if (available > 0) {
                        room.displayDetails(available);
                    }
                }

                System.out.println("------------------------");
            }
        }
    }
}
