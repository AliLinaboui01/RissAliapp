package ali.org.rissali.Domain;

public class Location {
    private int Id;
    private String loc;

    public Location() {
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @Override
    public String toString() {
        return loc;
    }
}
