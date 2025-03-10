package antifraud.enums;

public enum LockOperation {
    LOCK(false), UNLOCK(true);

    private final boolean value;

    LockOperation(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }
}