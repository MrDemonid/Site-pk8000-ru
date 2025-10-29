USE catalog_db;

-- 1. Клад
INSERT INTO product_archives (product_id, archive_file)
SELECT id, img FROM products, (SELECT 'files/klad_com.zip' AS img UNION ALL
                               SELECT 'files/klad_cas.zip' UNION ALL
                               SELECT 'files/klad_bas.zip') AS archives
WHERE name='Клад';

-- 2. Пилот
-- 3. Питон (Фотон)
-- 4. Угадай число
INSERT INTO product_archives (product_id, archive_file)
SELECT id, img FROM products, (SELECT 'files/chislo_com.zip' AS img UNION ALL
                               SELECT 'files/chislo_cas.zip' UNION ALL
                               SELECT 'files/chislo_bas.zip') AS archives
WHERE name='Угадай число';

-- 5. Тяп-ляп
INSERT INTO product_archives (product_id, archive_file)
SELECT id, img FROM products, (SELECT 'files/typlyp_com.zip' AS img UNION ALL
                               SELECT 'files/typlyp_cas.zip') AS archives
WHERE name='Тяп-ляп';

-- 6. Тест
INSERT INTO product_archives (product_id, archive_file) SELECT id, 'files/tect.zip' FROM products
WHERE name = 'Тест';

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
INSERT INTO product_archives (product_id, archive_file) SELECT id, 'files/fm.zip' FROM products
WHERE name = 'File manager';

-- 22. Image view
INSERT INTO product_archives (product_id, archive_file) SELECT id, 'files/iview.zip' FROM products
WHERE name = 'Image view';

-- 23. STC Player
INSERT INTO product_archives (product_id, archive_file) SELECT id, 'files/stcpl.zip' FROM products
WHERE name = 'STC Player';
-- 24. Питон
INSERT INTO product_archives (product_id, archive_file)
SELECT id, img FROM products, (SELECT 'files/piton_com.zip' AS img UNION ALL
                               SELECT 'files/piton_cas.zip') AS archives
WHERE name='Питон';

-- 25. Двигалка
INSERT INTO product_archives (product_id, archive_file) SELECT id, 'files/dvig.zip' FROM products
WHERE name = 'Двигалка';

-- 26. Bolder dash
-- 27. High way
