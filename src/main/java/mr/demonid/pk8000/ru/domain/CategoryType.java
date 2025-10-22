package mr.demonid.pk8000.ru.domain;


import lombok.Getter;


@Getter
public enum CategoryType {
    ARCADE("Аркады", "gamepad"),
    RACING("Гонки", "car"),
    SPORT("Спорт", "football"),
    LOGIC("Логические", "puzzle"),
    EDU("Образовательные", "book"),
    OTHER_GAMES("Прочие", "box"),

    TOOLS("Утилиты", "tools"),
    SYSTEM("Системные", "system"),
    PROGRAMMING("Программирование", "code"),
    OTHER_SOFTWARE("Остальные", "other-soft");


    private final String displayName;
    private final String icon;

    CategoryType(String displayName, String icon) {
        this.displayName = displayName;
        this.icon = icon;
    }

    public static CategoryType fromString(String name) {
        for (CategoryType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return OTHER_GAMES;
    }


}
