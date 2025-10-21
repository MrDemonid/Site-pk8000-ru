package mr.demonid.pk8000.ru.services.menu;

import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {

    private String title;
    private String icon;
    private Integer order;

    private String endpoint;
    private String path;

    private List<MenuItem> children;

    // поле ввода
    private String menuId;          // для идентификатора поля ввода, чтобы знать к каким меню применять его значение
    private String type;            // для поля ввода = "input"
    private String placeholder;     // значение по умолчанию

    private String searchId;        // ссылка на menuId поля ввода строки поиска
    private boolean adminOnly;


    public void setIcon(String icon) {
        if (icon != null && !icon.isEmpty()) {
            this.icon = icon;
        } else {
            this.icon = "default.png";
        }
    }

}
