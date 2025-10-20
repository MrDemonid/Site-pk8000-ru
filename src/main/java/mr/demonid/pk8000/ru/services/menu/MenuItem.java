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

    private String id;
    private String type;
    private String placeholder;
    private String searchId;        // ссылка на id поля ввода строки поиска
    private boolean adminOnly;


    public void setIcon(String icon) {
        if (icon != null && !icon.isEmpty()) {
            this.icon = icon;
        } else {
            this.icon = "default.png";
        }
    }

}
