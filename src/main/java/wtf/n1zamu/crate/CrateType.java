package wtf.n1zamu.crate;

public enum CrateType {
    VAULT("vault"),
    POINTS("points"),
    CMD("cmd");

    private final String name;

    CrateType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CrateType getCrateTypeByName(String name) {
        for (CrateType ct : values()) {
            if (!ct.getName().equals(name)) {
                continue;
            }
            return ct;
        }
        return null;
    }
}
