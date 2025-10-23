package mr.demonid.pk8000.ru.domain;


import lombok.Getter;


@Getter
public enum CategoryType {
    ARCADE("Аркады", "gamepad", CategoryGroup.GAMES),
    RACING("Гонки", "car", CategoryGroup.GAMES),
    SPORT("Спорт", "football", CategoryGroup.GAMES),
    LOGIC("Логические", "puzzle", CategoryGroup.GAMES),
    EDU("Образовательные", "book", CategoryGroup.GAMES),
    OTHER_GAMES("Прочие", "box", CategoryGroup.GAMES),

    TOOLS("Утилиты", "tools", CategoryGroup.SOFTWARE),
    SYSTEM("Системные", "system", CategoryGroup.SOFTWARE),
    PROGRAMMING("Программирование", "code", CategoryGroup.SOFTWARE),
    OTHER_SOFTWARE("Остальные", "other-soft", CategoryGroup.SOFTWARE),;


    private final String displayName;
    private final String icon;
    private final CategoryGroup group;


    CategoryType(String displayName, String icon, CategoryGroup group) {
        this.displayName = displayName;
        this.icon = icon;
        this.group = group;
    }

//    public static CategoryType fromString(String name) {
//        for (CategoryType type : values()) {
//            if (type.name().equalsIgnoreCase(name)) {
//                return type;
//            }
//        }
//        return OTHER_GAMES;
//    }


}
