public class UseCase1HotelBookingApp {
    import java.util.*;

    // Reservation (Booking Request)
    class Reservation {
        private String guestName;
        private String roomType;

        public Reservation(String guestName, String roomType) {
            this.guestName = guestName;
            this.roomType = roomType;
        }

        public String getGuestName() { return guestName; }
        public String getRoomType() { return roomType; }

        public void display() {
            System.out.println("Guest: " + guestName + " | Room: " + roomType);
        }
    }

    // Booking Queue (FIFO)
    class BookingQueue {
        private Queue<Reservation> queue = new LinkedList<>();

        // Add request
        public void addRequest(Reservation r) {
            queue.offer(r);
            System.out.println("Request added: " + r.getGuestName());
        }

        // View all requests
        public void displayQueue() {
            System.out.println("=== Booking Queue ===");
            for (Reservation r : queue) {
                r.display();
            }
            System.out.println("----------------------");
        }

        // Peek next (no removal)
        public Reservation peekNext() {
            return queue.peek();
        }
    }

    // Main
    public class HotelApp {
        public static void main(String[] args) {

            BookingQueue queue = new BookingQueue();

            // Guest booking requests
            queue.addRequest(new Reservation("Ram", "Single Room"));
            queue.addRequest(new Reservation("Arun", "Double Room"));
            queue.addRequest(new Reservation("Priya", "Suite Room"));

            // Display queue (FIFO order)
            queue.displayQueue();

            // Peek next request
            System.out.println("Next to process:");
            queue.peekNext().display();
        }
    }
}
