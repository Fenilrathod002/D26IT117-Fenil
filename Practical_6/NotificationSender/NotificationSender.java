
// package NotificationSender;

/*  Functional interface */
interface Notifier {
    void send(String message);
}

// Marker interface
interface Urgent {
}

public class NotificationSender {

    public static void main(String[] args) {

        // Email sender using lambda
        Notifier email = message ->
            System.out.println("Email: " + message);

        // SMS sender using lambda
        Notifier sms = message ->
            System.out.println("SMS: " + message);

        // Urgent Email class
        class UrgentEmail implements Notifier, Urgent {

            @Override
            public void send(String message) {
                System.out.println("Urgent Email: " + message);
            }
        }

        Notifier urgentEmail = new UrgentEmail();

        // Array of senders
        Notifier[] senders = {email, sms, urgentEmail};

        String message = "Class starts at 9:30 AM";

        // Send notification from all senders
        for (Notifier sender : senders) {
            sender.send(message);

            // Check whether sender is Urgent
            if (sender instanceof Urgent) {
                System.out.println("This is an urgent notification.");
            }
        }
    }
}