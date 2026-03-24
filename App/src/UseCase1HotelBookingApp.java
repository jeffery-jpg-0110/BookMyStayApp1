public class UseCase1HotelBookingApp {
    public class BookMyStayApp {
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

        // Thread-safe Inventory
        class RoomInventory {
            private Map<String, Integer> availability = new HashMap<>();

            public RoomInventory() {
                availability.put("Single Room", 2);
                availability.put("Double Room", 1);
            }

            // synchronized critical section
            public synchronized boolean allocateRoom(String type) {
                int available = availability.getOrDefault(type, 0);

                if (available > 0) {
                    availability.put(type, available - 1);
                    return true;
                }
                return false;
            }

            public void display() {
                System.out.println("Final Inventory: " + availability);
            }
        }

        // Thread-safe Queue
        class BookingQueue {
            private Queue<Reservation> queue = new LinkedList<>();

            public synchronized void addRequest(Reservation r) {
                queue.offer(r);
            }

            public synchronized Reservation getNext() {
                return queue.poll();
            }
        }

        // Booking Processor (Thread)
        class BookingProcessor extends Thread {

            private BookingQueue queue;
            private RoomInventory inventory;
            private static int idCounter = 1;

            public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
                this.queue = queue;
                this.inventory = inventory;
            }

            public void run() {
                while (true) {

                    Reservation req;

                    // synchronized fetch
                    synchronized (queue) {
                        req = queue.getNext();
                    }

                    if (req == null) break;

                    // synchronized allocation (inside inventory)
                    boolean success = inventory.allocateRoom(req.getRoomType());

                    if (success) {
                        synchronized (BookingProcessor.class) {
                            String roomId = req.getRoomType().substring(0, 2).toUpperCase() + idCounter++;
                            System.out.println(Thread.currentThread().getName()
                                    + " Confirmed: " + req.getGuestName()
                                    + " | ID: " + roomId);
                        }
                    } else {
                        System.out.println(Thread.currentThread().getName()
                                + " Failed: " + req.getGuestName());
                    }
                }
            }
        }

        // Main
        public class HotelApp {
            public static void main(String[] args) throws InterruptedException {

                RoomInventory inventory = new RoomInventory();
                BookingQueue queue = new BookingQueue();

                // Simulate concurrent requests
                queue.addRequest(new Reservation("Ram", "Single Room"));
                queue.addRequest(new Reservation("Arun", "Single Room"));
                queue.addRequest(new Reservation("Priya", "Single Room"));
                queue.addRequest(new Reservation("John", "Double Room"));
                queue.addRequest(new Reservation("Meena", "Double Room"));

                // Multiple threads (guests)
                Thread t1 = new BookingProcessor(queue, inventory);
                Thread t2 = new BookingProcessor(queue, inventory);
                Thread t3 = new BookingProcessor(queue, inventory);

                t1.start();
                t2.start();
                t3.start();

                t1.join();
                t2.join();
                t3.join();

                inventory.display();
            }
        }
    }
}
