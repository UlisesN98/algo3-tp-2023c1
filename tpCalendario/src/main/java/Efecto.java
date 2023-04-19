public enum Efecto {
    NOTIFICACION,
    SONIDO,
    MAIL;

    public int getValue() {
        return ordinal() + 1;
    }

}

