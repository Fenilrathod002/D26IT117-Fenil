class SeatBooking {
    int seatsLeft = 5;
    boolean book() {
        if (seatsLeft > 0) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            seatsLeft--;
            return true;
        }
        return false;
    }
}
class BookingThread extends Thread {
    SeatBooking booking;
    BookingThread(SeatBooking booking) {
        this.booking = booking;
    }
    public void run() {
        boolean success = booking.book();
        if (success) {
            System.out.println(
                Thread.currentThread().getName() +
                " booked a seat"
            );
        } else {
            System.out.println(
                Thread.currentThread().getName() +
                " failed - no seat"
            );
        }
    }
}
public class SeatBookingRace {

    public static void main(String[] args) throws InterruptedException {

        SeatBooking booking = new SeatBooking();

        int numberOfThreads = 10;

        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {

            threads[i] = new BookingThread(booking);
            threads[i].setName("Customer-" + (i + 1));

            threads[i].start();
        }

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i].join();
        }

        System.out.println("Seats left: " + booking.seatsLeft);
    }
}
