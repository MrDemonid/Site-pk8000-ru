package mr.demonid.pk8000.ru.domain;


public enum CategoryType {
    ARCADE("Аркады", "gamepad"),
    RACING("Гонки", "car"),
    SPORT("Спорт", "football"),
    LOGIC("Логические", "puzzle"),
    EDU("Образовательные", "book"),
    OTHER("Разное", "box"),
    PROGRAMMING("Программирование", "code");

    private final String displayName;
    private final String icon;

    CategoryType(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public static CategoryType fromString(String name) {
        for (CategoryType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return OTHER;
    }
}
