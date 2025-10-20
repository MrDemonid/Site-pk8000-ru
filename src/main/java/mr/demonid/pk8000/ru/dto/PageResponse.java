package mr.demonid.pk8000.ru.dto;


import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse {
    private String name;
    private String body;
    private String filePath;
}
