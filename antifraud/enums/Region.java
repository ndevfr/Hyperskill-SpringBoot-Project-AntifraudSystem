package antifraud.enums;

public enum Region {
    EAP("East Asia and Pacific"),
    ECA("Europe and Central Asia"),
    HIC("High-Income countries"),
    LAC("Latin America and the Caribbean"),
    MENA("The Middle East and North Africa"),
    SA("South Asia"),
    SSA("Sub-Saharan Africa");

    private final String fullName;

    Region(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}