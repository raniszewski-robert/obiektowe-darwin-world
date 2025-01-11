package agh.ics.oop.model.enums;

public enum MapVariant {
    NORMAL,
    FIRE;

    public static MapVariant parser(String variant) {
        switch (variant) {
            case "Kula ziemska" -> {
                return MapVariant.NORMAL;
            }
            case "Pożary" -> {
                return MapVariant.FIRE;
            }
        }
        return MapVariant.NORMAL;
    }
}
