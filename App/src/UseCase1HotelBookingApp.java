public class UseCase1HotelBookingApp {
    public class BookMyStayApp {
    import java.util.*;

        // Custom Exception
        class InvalidBookingException extends Exception {
            public InvalidBookingException(String message) {
                super(message);
            }
        }

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

        // Inventory
        class RoomInventory {
            private Map<String, Integer> availability = new HashMap<>();

            public RoomInventory() {
                availability.put("Single Room", 1);
                availability.put("Double Room", 1);
                availability.put("Suite Room", 0);
            }

            public int getAvailability(String type) throws InvalidBookingException {
                if (!availability.containsKey(type)) {
                    throw new InvalidBookingException("Invalid room type: " + type);
                }
                return availability.get(type);
            }

            public void updateAvailability(String type, int count) throws InvalidBookingException {
                if (count < 0) {
                    throw new InvalidBookingException("Inventory cannot be negative for " + type);
                }
                availability.put(type, count);
            }
        }

        // Validator
        class BookingValidator {

            public static void validate(Reservation r, RoomInventory inventory)
                    throws InvalidBookingException {

                if (r.getGuestName() == null || r.getGuestName().isEmpty()) {
                    throw new InvalidBookingException("Guest name cannot be empty");
                }

                int available = inventory.getAvailability(r.getRoomType());

                if (available <= 0) {
                    throw new InvalidBookingException("No rooms available for " + r.getRoomType());
                }
            }
        }

        // Queue
        class BookingQueue {
            private Queue<Reservation> queue = new LinkedList<>();

            public void addRequest(Reservation r) {
                queue.offer(r);
            }

            public Reservation pollRequest() {
                return queue.poll();
            }
        }

        // Booking Service (with validation)
        class BookingService {

            private RoomInventory inventory;
            private BookingQueue queue;
            private int idCounter = 1;

            public BookingService(RoomInventory inventory, BookingQueue queue) {
                this.inventory = inventory;
                this.queue = queue;
            }

            public void processBookings() {
                System.out.println("=== Processing Bookings ===");

                Reservation req;

                while ((req = queue.pollRequest()) != null) {
                    try {
                        // Validate before processing
                        BookingValidator.validate(req, inventory);

                        int available = inventory.getAvailability(req.getRoomType());

                        // Allocate
                        String roomId = req.getRoomType().substring(0, 2).toUpperCase() + idCounter++;
                        inventory.updateAvailability(req.getRoomType(), available - 1);

                        System.out.println("Confirmed: " + req.getGuestName()
                                + " | ID: " + roomId);

                    } catch (InvalidBookingException e) {
                        // Graceful failure
                        System.out.println("Booking Failed: " + e.getMessage());
                    }
                }

                System.out.println("----------------------------");
            }
        }

        // Main
        public class HotelApp {
            public static void main(String[] args) {

                RoomInventory inventory = new RoomInventory();
                BookingQueue queue = new BookingQueue();

                // Valid + Invalid cases
                queue.addRequest(new Reservation("Ram", "Single Room"));   // valid
                queue.addRequest(new Reservation("", "Double Room"));      // invalid name
                queue.addRequest(new Reservation("Arun", "Suite Room"));   // no availability
                queue.addRequest(new Reservation("Priya", "Luxury Room")); // invalid type

                BookingService service = new BookingService(inventory, queue);

                service.processBookings();

                System.out.println("Application continues safely...");
            }
        }
    }
}
