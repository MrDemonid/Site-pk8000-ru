USE catalog_db;

-- 1. Клад
INSERT INTO product_archives (product_id, archive_file)
SELECT id, img FROM products, (SELECT 'klad_com.zip' AS img UNION ALL
                               SELECT 'klad_cas.zip' UNION ALL
                               SELECT 'klad_bas.zip') AS archives
WHERE name='Клад';

-- 2. Пилот
-- 3. Питон (Фотон)
-- 4. Угадай число
-- 5. Тяп-ляп
-- 6. Тест
-- 7. Минер
-- 8. Морской бой
-- 9. Автодром
-- 10. Сура
-- 11. Танец
-- 12. Шахматы
-- 13. Пожарник
-- 14. Тест-Хобби
-- 15. Алибаба
-- 16. Binary land
-- 17. Crux
-- 18. Кобра
-- 19. Boulder dash
-- 20. Bomber man
-- 21. File manager
-- 22. Image view
-- 23. STC Player
-- 24. Питон
-- 25. Двигалка
-- 26. Bolder dash
-- 27. High way
