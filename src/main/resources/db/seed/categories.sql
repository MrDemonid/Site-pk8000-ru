USE catalog_db;

INSERT INTO categories (id, name, description) VALUES
    (${CATEGORY_ARCADE}, 'Аркады', 'Быстрые динамичные игры с простым управлением');

INSERT INTO categories (id, name, description) VALUES
    (${CATEGORY_RACING}, 'Гонки', 'Гоночные игры');

INSERT INTO categories (id, name, description) VALUES
    (${CATEGORY_SPORT}, 'Спортивные', 'Спортивные игры');

INSERT INTO categories (id, name, description) VALUES
    (${CATEGORY_LOGIC}, 'Логические', 'Развивающие игры на логику');

INSERT INTO categories (id, name, description) VALUES
    (${CATEGORY_EDU}, 'Обучающие', 'Учебные и познавательные программы');

INSERT INTO categories (id, name, description) VALUES
    (${CATEGORY_OTHER_GAMES}, 'Остальные', 'Игры не подходящие ни под один из разделов');

INSERT INTO categories (id, name, description) VALUES
    (${CATEGORY_TOOLS}, 'Утилиты', 'Различные утилиты на все случаи жизни');

INSERT INTO categories (id, name, description) VALUES
    (${CATEGORY_SYSTEM}, 'Системные', 'Утилиты для работы на системном уровне');

INSERT INTO categories (id, name, description) VALUES
    (${CATEGORY_PROGRAMMING}, 'Программирование', 'Средства разработки кода, компиляторы, интерпретаторы, библиотеки');

INSERT INTO categories (id, name, description) VALUES
    (${CATEGORY_OTHER_SOFTWARE}, 'Остальные', 'Программы, не попадающие ни под одну из категорий');
