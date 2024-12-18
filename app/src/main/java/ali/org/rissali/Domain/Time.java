package ali.org.rissali.Domain;

public class Time {
    private int Id;
    private String Loc;

    public Time() {
    }

    @Override
    public String toString() {
        return  Loc;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }
}
