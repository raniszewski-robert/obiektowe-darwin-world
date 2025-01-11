package agh.ics.oop.model.enums;

public enum GenomeVariant {
    NORMAL,
    CRAZY;

    public static GenomeVariant parser(String variant) {
        switch (variant) {
            case "Pełna predestynacja" -> {
                return GenomeVariant.NORMAL;
            }
            case "Nieco szaleństwa" -> {
                return GenomeVariant.CRAZY;
            }
        }
        return GenomeVariant.NORMAL;
    }
}
