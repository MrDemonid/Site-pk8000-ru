USE catalog_db;

-- 1. Клад
INSERT INTO product_archives (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'klad_com.zip' AS img UNION ALL
                                  SELECT 'klad_cas.zip' UNION ALL
                                  SELECT 'klad_bas.zip') AS archives
WHERE name='Клад';

-- 2. Пилот
-- (нет архивов в исходных данных)

-- 3. Питон (Фотон)
-- (нет архивов в исходных данных)

-- 4. Угадай число
INSERT INTO product_archives (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'chislo_com.zip' AS img UNION ALL
                                  SELECT 'chislo_cas.zip' UNION ALL
                                  SELECT 'chislo_bas.zip') AS archives
WHERE name='Угадай число';

-- 5. Тяп-ляп
INSERT INTO product_archives (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'typlyp_com.zip' AS img UNION ALL
                                  SELECT 'typlyp_cas.zip') AS archives
WHERE name='Тяп-ляп';

-- 6. Тест
INSERT INTO product_archives (product_id, file_name, version)
SELECT id, 'tect.zip', 1 FROM products
WHERE name='Тест';

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
INSERT INTO product_archives (product_id, file_name, version)
SELECT id, 'fm.zip', 1 FROM products
WHERE name='File manager';

-- 22. Image view
INSERT INTO product_archives (product_id, file_name, version)
SELECT id, 'iview.zip', 1 FROM products
WHERE name='Image view';

-- 23. STC Player
INSERT INTO product_archives (product_id, file_name, version)
SELECT id, 'stcpl.zip', 1 FROM products
WHERE name='STC Player';

-- 24. Питон
INSERT INTO product_archives (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'piton_com.zip' AS img UNION ALL
                                  SELECT 'piton_cas.zip') AS archives
WHERE name='Питон';

-- 25. Двигалка
INSERT INTO product_archives (product_id, file_name, version)
SELECT id, 'dvig.zip', 1 FROM products
WHERE name='Двигалка';

-- 26. Bolder dash
-- (нет архивов в исходных данных)

-- 27. High way
-- (нет архивов в исходных данных)

# -- 1. Клад
# INSERT INTO product_archives (product_id, archive_file)
# SELECT id, img FROM products, (SELECT 'klad_com.zip' AS img UNION ALL
#                                SELECT 'klad_cas.zip' UNION ALL
#                                SELECT 'klad_bas.zip') AS archives
# WHERE name='Клад';
#
# -- 2. Пилот
# -- 3. Питон (Фотон)
# -- 4. Угадай число
# INSERT INTO product_archives (product_id, archive_file)
# SELECT id, img FROM products, (SELECT 'chislo_com.zip' AS img UNION ALL
#                                SELECT 'chislo_cas.zip' UNION ALL
#                                SELECT 'chislo_bas.zip') AS archives
# WHERE name='Угадай число';
#
# -- 5. Тяп-ляп
# INSERT INTO product_archives (product_id, archive_file)
# SELECT id, img FROM products, (SELECT 'typlyp_com.zip' AS img UNION ALL
#                                SELECT 'typlyp_cas.zip') AS archives
# WHERE name='Тяп-ляп';
#
# -- 6. Тест
# INSERT INTO product_archives (product_id, archive_file) SELECT id, 'tect.zip' FROM products
# WHERE name = 'Тест';
#
# -- 7. Минер
# -- 8. Морской бой
# -- 9. Автодром
# -- 10. Сура
# -- 11. Танец
# -- 12. Шахматы
# -- 13. Пожарник
# -- 14. Тест-Хобби
# -- 15. Алибаба
# -- 16. Binary land
# -- 17. Crux
# -- 18. Кобра
# -- 19. Boulder dash
# -- 20. Bomber man
# -- 21. File manager
# INSERT INTO product_archives (product_id, archive_file) SELECT id, 'fm.zip' FROM products
# WHERE name = 'File manager';
#
# -- 22. Image view
# INSERT INTO product_archives (product_id, archive_file) SELECT id, 'iview.zip' FROM products
# WHERE name = 'Image view';
#
# -- 23. STC Player
# INSERT INTO product_archives (product_id, archive_file) SELECT id, 'stcpl.zip' FROM products
# WHERE name = 'STC Player';
# -- 24. Питон
# INSERT INTO product_archives (product_id, archive_file)
# SELECT id, img FROM products, (SELECT 'piton_com.zip' AS img UNION ALL
#                                SELECT 'piton_cas.zip') AS archives
# WHERE name='Питон';
#
# -- 25. Двигалка
# INSERT INTO product_archives (product_id, archive_file) SELECT id, 'dvig.zip' FROM products
# WHERE name = 'Двигалка';
#
# -- 26. Bolder dash
# -- 27. High way
