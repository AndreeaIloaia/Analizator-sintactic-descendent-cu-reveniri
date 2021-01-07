public class RP {
    private String membruStang;
    private String membruDrept;
    private int number;

    public RP(String membruStang, String membruDrept, int number) {
        this.membruStang = membruStang;
        this.membruDrept = membruDrept;
        this.number = number;
    }

    public String getMembruStang() {
        return membruStang;
    }

    public void setMembruStang(String membruStang) {
        this.membruStang = membruStang;
    }

    public String getMembruDrept() {
        return membruDrept;
    }

    public void setMembruDrept(String membruDrept) {
        this.membruDrept = membruDrept;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return membruStang + " -> "  + membruDrept + " (" + number + ") ";
    }
}
