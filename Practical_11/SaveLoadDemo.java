import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveLoadDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path file = Path.of("data", "records.dat");
        Files.createDirectories(file.getParent());

        SerializableRecord[] original = {
                new SerializableRecord(101, "Riya", 87.5, "active session"),
                new SerializableRecord(102, "Aarav", 92.0, "temporary note")
        };

        try (ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(file))) {
            output.writeObject(original);
        }

        SerializableRecord[] restored;
        try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file))) {
            restored = (SerializableRecord[]) input.readObject();
        }

        System.out.println("Saved records to: " + file.toAbsolutePath());
        System.out.println("Records after loading:");
        for (SerializableRecord record : restored) {
            System.out.println(record);
        }
        System.out.println("Transient sessionNote after loading: "
                + restored[0].getSessionNote());
    }
}
