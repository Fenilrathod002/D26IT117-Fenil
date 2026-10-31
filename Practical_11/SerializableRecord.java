import java.io.Serial;
import java.io.Serializable;

/** A small object used to demonstrate Java object serialization. */
public class SerializableRecord implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final int id;
    private final String name;
    private final double score;
    private transient String sessionNote;

    public SerializableRecord(int id, String name, double score, String sessionNote) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.sessionNote = sessionNote;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getScore() { return score; }
    public String getSessionNote() { return sessionNote; }

    @Override
    public String toString() {
        return "Record{id=" + id + ", name='" + name + "', score=" + score
                + ", sessionNote=" + sessionNote + "}";
    }
}
