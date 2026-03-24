public class UseCase1HotelBookingApp {
    public class BookMyStayApp {
        // Abstract Class
        abstract class Room {
            private int beds;
            private double size;
            private double price;

            public Room(int beds, double size, double price) {
                this.beds = beds;
                this.size = size;
                this.price = price;
            }

            public int getBeds() {
                return beds;
            }

            public double getSize() {
                return size;
            }

            public double getPrice() {
                return price;
            }

            // Abstract method
            public abstract String getRoomType();

            // Common display method
            public void displayDetails(int availability) {
                System.out.println("Room Type: " + getRoomType());
                System.out.println("Beds: " + beds);
                System.out.println("Size: " + size + " sq.ft");
                System.out.println("Price: ₹" + price);
                System.out.println("Available: " + availability);
                System.out.println("-----------------------------");
            }
        }

        // Single Room
        class SingleRoom extends Room {
            public SingleRoom() {
                super(1, 200, 1500);
            }

            @Override
            public String getRoomType() {
                return "Single Room";
            }
        }

        // Double Room
        class DoubleRoom extends Room {
            public DoubleRoom() {
                super(2, 350, 3000);
            }

            @Override
            public String getRoomType() {
                return "Double Room";
            }
        }

        // Suite Room
        class SuiteRoom extends Room {
            public SuiteRoom() {
                super(3, 600, 7000);
            }

            @Override
            public String getRoomType() {
                return "Suite Room";
            }
        }

        // Main Application
        public class HotelApp {
            public static void main(String[] args) {

                // Room objects
                Room single = new SingleRoom();
                Room doub = new DoubleRoom();
                Room suite = new SuiteRoom();

                // Static availability (simple variables)
                int singleAvailable = 5;
                int doubleAvailable = 3;
                int suiteAvailable = 2;

                // Display
                single.displayDetails(singleAvailable);
                doub.displayDetails(doubleAvailable);
                suite.displayDetails(suiteAvailable);

                System.out.println("Application Terminated.");
            }
        }
    }
}
