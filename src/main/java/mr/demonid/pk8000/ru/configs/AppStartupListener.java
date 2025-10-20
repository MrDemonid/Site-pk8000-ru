package mr.demonid.pk8000.ru.configs;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mr.demonid.pk8000.ru.services.AdminServiceImpl;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Log4j2
public class AppStartupListener {

    private AdminServiceImpl adminService;


    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("🚀 Приложение полностью готово к работе!");

        String about = """
                # Описание.
                ___
                
                **ПК8000** - это советский компьютер. \s
                Процессор: КР580 \s
                Шина: 8 bit \s
                
                ### Что умеет.
                
                ***Всё умеет.***
                
                ### Что не умеет.
                
                *Ничего*
                
                ___Что еще нужно___
                
                ~~Зачеркнутый текст~~
                
                
                > А это цитата
                > > Подцитата
                
                1) Список
                2) Он же
                
                - Еще список
                - Лн же
                 \s
                
                - [x] Чек-бокс
                - [ ] Пассивный чек-бокс\s
                
                
                [Ссылка](https://pk8000.ru "Всплывающая подсказка")
                
                ![Изображение](./images/icons/camera.png "Это камера")
                
                ![Или так][1]
                ![продолжаем][2]
                
                
                | Столбец 1     |  Столбец 2  |                   Столбец 3 |
                |:--------------|:-----------:|----------------------------:|
                | Первая ячейка |  Втор. яч.  | Выравниваем по правому краю |
                | еще           |      ?      |                  Что-то еще |
                
                
                <span style="color:red">
                Текст
                </span>
                <span style=" color: blue;background-color: yellow;">
                Текст
                </span>
                """;
//        if (documentService.getDocument("about") == null) {
//            DocumentRequest request = new DocumentRequest("about", about);
//            adminService.addNewDocument(request);
//            log.info("Document added");
//        }

    }

}
