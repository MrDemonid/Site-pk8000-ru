USE catalog_db;

-- 1. Клад
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'klad-title.png' AS img UNION ALL
                                  SELECT 'klad-1.png' UNION ALL
                                  SELECT 'klad-2.png' UNION ALL
                                  SELECT 'klad-3.png' UNION ALL
                                  SELECT 'klad-4.png') AS images
WHERE name='Клад';

-- 2. Пилот
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'pilot-title.png' AS img UNION ALL
                                  SELECT 'pilot-1.png' UNION ALL
                                  SELECT 'pilot-2.png' UNION ALL
                                  SELECT 'pilot-3.png' UNION ALL
                                  SELECT 'pilot-4.png' UNION ALL
                                  SELECT 'pilot-5.png') AS images
WHERE name='Пилот';

-- 3. Питон (Фотон)
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'piton-foton-title.png' AS img UNION ALL
                                  SELECT 'piton-foton-1.png' UNION ALL
                                  SELECT 'piton-foton-2.png' UNION ALL
                                  SELECT 'piton-foton-3.png') AS images
WHERE name='Питон (Фотон)';

-- 4. Угадай число
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'chislo-title.png' AS img UNION ALL
                                  SELECT 'chislo-1.png' UNION ALL
                                  SELECT 'chislo-2.png' UNION ALL
                                  SELECT 'chislo-3.png') AS images
WHERE name='Угадай число';

-- 5. Тяп-ляп
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'typ-lyp-title.png' AS img UNION ALL
                                  SELECT 'typ-lyp-1.png') AS images
WHERE name='Тяп-ляп';

-- 6. Тест
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'tect-title.png' AS img UNION ALL
                                  SELECT 'tect-1.png' UNION ALL
                                  SELECT 'tect-2.png') AS images
WHERE name='Тест';

-- 7. Минер
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'miner-title.png' AS img UNION ALL
                                  SELECT 'miner-0.png' UNION ALL
                                  SELECT 'miner-1.png' UNION ALL
                                  SELECT 'miner-2.png') AS images
WHERE name='Минер';

-- 8. Морской бой
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'mor-boy-title.png' AS img UNION ALL
                                  SELECT 'mor-boy-1.png' UNION ALL
                                  SELECT 'mor-boy-2.png' UNION ALL
                                  SELECT 'mor-boy-3.png') AS images
WHERE name='Морской бой';

-- 9. Автодром
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'avto-title.png' AS img UNION ALL
                                  SELECT 'avto-1.png' UNION ALL
                                  SELECT 'avto-2.png') AS images
WHERE name='Автодром';

-- 10. Сура
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'sura-title.png' AS img UNION ALL
                                  SELECT 'sura-1.png' UNION ALL
                                  SELECT 'sura-2.png') AS images
WHERE name='Сура';

-- 11. Танец
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'tanec-1.png' AS img UNION ALL
                                  SELECT 'tanec-2.png' UNION ALL
                                  SELECT 'tanec-3.png') AS images
WHERE name='Танец';

-- 12. Шахматы
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'chess-title.png' AS img UNION ALL
                                  SELECT 'chess-1.png' UNION ALL
                                  SELECT 'chess-2.png' UNION ALL
                                  SELECT 'chess-3.png') AS images
WHERE name='Шахматы';

-- 13. Пожарник
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'fire-title.png' AS img UNION ALL
                                  SELECT 'fire-1.png' UNION ALL
                                  SELECT 'fire-2.png' UNION ALL
                                  SELECT 'fire-3.png' UNION ALL
                                  SELECT 'fire-4.png') AS images
WHERE name='Пожарник';

-- 14. Тест-Хобби
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'test-title.png' AS img UNION ALL
                                  SELECT 'test-1.png' UNION ALL
                                  SELECT 'test-2.png' UNION ALL
                                  SELECT 'test-3.png' UNION ALL
                                  SELECT 'test-4.png') AS images
WHERE name='Тест-Хобби';

-- 15. Алибаба
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'alibab-title.png' AS img UNION ALL
                                  SELECT 'alibab-1.png') AS images
WHERE name='Алибаба';

-- 16. Binary land
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'binary-title.png' AS img UNION ALL
                                  SELECT 'binary-1.png' UNION ALL
                                  SELECT 'binary-2.png' UNION ALL
                                  SELECT 'binary-3.png') AS images
WHERE name='Binary land';

-- 17. Crux
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'crux-title.png' AS img UNION ALL
                                  SELECT 'crux-1.png' UNION ALL
                                  SELECT 'crux-2.png') AS images
WHERE name='Crux';

-- 18. Кобра
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'cobra-1.png' AS img UNION ALL
                                  SELECT 'cobra-2.png') AS images
WHERE name='Кобра';

-- 19. Boulder dash
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'boulder-dash-title.png' AS img UNION ALL
                                  SELECT 'boulder-dash-1.png' UNION ALL
                                  SELECT 'boulder-dash-2.png') AS images
WHERE name='Boulder dash';

-- 20. Bomber man
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'bomb-title.png' AS img UNION ALL
                                  SELECT 'bomb-title-1.png' UNION ALL
                                  SELECT 'bobm-1.png' UNION ALL
                                  SELECT 'bobm-2.png') AS images
WHERE name='Bomber man';

-- 21. File manager
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'fm-1.png' AS img UNION ALL
                                  SELECT 'fm-2.png' UNION ALL
                                  SELECT 'fm-3.png' UNION ALL
                                  SELECT 'fm-4.png' UNION ALL
                                  SELECT 'fm-5.png' UNION ALL
                                  SELECT 'fm-6.png' UNION ALL
                                  SELECT 'fm-7.png') AS images
WHERE name='File manager';

-- 22. Image view
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'iview-1.png' AS img UNION ALL
                                  SELECT 'iview-2.png' UNION ALL
                                  SELECT 'iview-3.png' UNION ALL
                                  SELECT 'iview-4.png' UNION ALL
                                  SELECT 'iview-5.png' UNION ALL
                                  SELECT 'iview-6.png') AS images
WHERE name='Image view';

-- 23. STC Player
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'stcpl-1.png' AS img UNION ALL
                                  SELECT 'stcpl-2.png' UNION ALL
                                  SELECT 'stcpl-3.png' UNION ALL
                                  SELECT 'stcpl-4.png' UNION ALL
                                  SELECT 'stcpl-5.png' UNION ALL
                                  SELECT 'stcpl-6.png' UNION ALL
                                  SELECT 'stcpl-7.png') AS images
WHERE name='STC Player';

-- 24. Питон
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'piton-title.png' AS img UNION ALL
                                  SELECT 'piton-1.png' UNION ALL
                                  SELECT 'piton-2.png' UNION ALL
                                  SELECT 'piton-3.png') AS images
WHERE name='Питон';

-- 25. Двигалка
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'dvig-title.png' AS img UNION ALL
                                  SELECT 'dvig-1.png' UNION ALL
                                  SELECT 'dvig-2.png' UNION ALL
                                  SELECT 'dvig-3.png' UNION ALL
                                  SELECT 'dvig-4.png') AS images
WHERE name='Двигалка';

-- 26. Bolder dash
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'bolder-title.png' AS img UNION ALL
                                  SELECT 'bolder-1.png' UNION ALL
                                  SELECT 'bolder-2.png' UNION ALL
                                  SELECT 'bolder-3.png' UNION ALL
                                  SELECT 'bolder-4.png' UNION ALL
                                  SELECT 'bolder-5.png' UNION ALL
                                  SELECT 'bolder-6.png') AS images
WHERE name='Bolder dash';

-- 27. High way
INSERT INTO product_images (product_id, file_name, version)
SELECT id, img, 1 FROM products, (SELECT 'higway-title.png' AS img UNION ALL
                                  SELECT 'higway-title-2.png' UNION ALL
                                  SELECT 'higway-1.png' UNION ALL
                                  SELECT 'higway-2.png') AS images
WHERE name='High way';
#
# -- 1. Клад
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'klad-title.png' AS img UNION ALL
#                                SELECT 'klad-1.png' UNION ALL
#                                SELECT 'klad-2.png' UNION ALL
#                                SELECT 'klad-3.png' UNION ALL
#                                SELECT 'klad-4.png') AS images
# WHERE name='Клад';
#
# -- 2. Пилот
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'pilot-title.png' AS img UNION ALL
#                                SELECT 'pilot-1.png' UNION ALL
#                                SELECT 'pilot-2.png' UNION ALL
#                                SELECT 'pilot-3.png' UNION ALL
#                                SELECT 'pilot-4.png' UNION ALL
#                                SELECT 'pilot-5.png') AS images
# WHERE name='Пилот';
#
# -- 3. Питон (Фотон)
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'piton-foton-title.png' AS img UNION ALL
#                                SELECT 'piton-foton-1.png' UNION ALL
#                                SELECT 'piton-foton-2.png' UNION ALL
#                                SELECT 'piton-foton-3.png') AS images
# WHERE name='Питон (Фотон)';
#
# -- 4. Угадай число
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'chislo-title.png' AS img UNION ALL
#                                SELECT 'chislo-1.png' UNION ALL
#                                SELECT 'chislo-2.png' UNION ALL
#                                SELECT 'chislo-3.png') AS images
# WHERE name='Угадай число';
#
# -- 5. Тяп-ляп
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'typ-lyp-title.png' AS img UNION ALL
#                                SELECT 'typ-lyp-1.png') AS images
# WHERE name='Тяп-ляп';
#
# -- 6. Тест
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'tect-title.png' AS img UNION ALL
#                                SELECT 'tect-1.png' UNION ALL
#                                SELECT 'tect-2.png') AS images
# WHERE name='Тест';
#
# -- 7. Минер
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'miner-title.png' AS img UNION ALL
#                                SELECT 'miner-0.png' UNION ALL
#                                SELECT 'miner-1.png' UNION ALL
#                                SELECT 'miner-2.png') AS images
# WHERE name='Минер';
#
# -- 8. Морской бой
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'mor-boy-title.png' AS img UNION ALL
#                                SELECT 'mor-boy-1.png' UNION ALL
#                                SELECT 'mor-boy-2.png' UNION ALL
#                                SELECT 'mor-boy-3.png') AS images
# WHERE name='Морской бой';
#
# -- 9. Автодром
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'avto-title.png' AS img UNION ALL
#                                SELECT 'avto-1.png' UNION ALL
#                                SELECT 'avto-2.png') AS images
# WHERE name='Автодром';
#
# -- 10. Сура
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'sura-title.png' AS img UNION ALL
#                                SELECT 'sura-1.png' UNION ALL
#                                SELECT 'sura-2.png') AS images
# WHERE name='Сура';
#
# -- 11. Танец
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'tanec-1.png' AS img UNION ALL
#                                SELECT 'tanec-2.png' UNION ALL
#                                SELECT 'tanec-3.png') AS images
# WHERE name='Танец';
#
# -- 12. Шахматы
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'chess-title.png' AS img UNION ALL
#                                SELECT 'chess-1.png' UNION ALL
#                                SELECT 'chess-2.png' UNION ALL
#                                SELECT 'chess-3.png') AS images
# WHERE name='Шахматы';
#
# -- 13. Пожарник
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'fire-title.png' AS img UNION ALL
#                                SELECT 'fire-1.png' UNION ALL
#                                SELECT 'fire-2.png' UNION ALL
#                                SELECT 'fire-3.png' UNION ALL
#                                SELECT 'fire-4.png') AS images
# WHERE name='Пожарник';
#
# -- 14. Тест-Хобби
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'test-title.png' AS img UNION ALL
#                                SELECT 'test-1.png' UNION ALL
#                                SELECT 'test-2.png' UNION ALL
#                                SELECT 'test-3.png' UNION ALL
#                                SELECT 'test-4.png') AS images
# WHERE name='Тест-Хобби';
#
# -- 15. Алибаба
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'alibab-title.png' AS img UNION ALL
#                                SELECT 'alibab-1.png') AS images
# WHERE name='Алибаба';
#
# -- 16. Binary land
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'binary-title.png' AS img UNION ALL
#                                SELECT 'binary-1.png' UNION ALL
#                                SELECT 'binary-2.png' UNION ALL
#                                SELECT 'binary-3.png') AS images
# WHERE name='Binary land';
#
# -- 17. Crux
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'crux-title.png' AS img UNION ALL
#                                SELECT 'crux-1.png' UNION ALL
#                                SELECT 'crux-2.png') AS images
# WHERE name='Crux';
#
# -- 18. Кобра
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'cobra-1.png' AS img UNION ALL
#                                SELECT 'cobra-2.png') AS images
# WHERE name='Кобра';
#
# -- 19. Boulder dash
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'boulder-dash-title.png' AS img UNION ALL
#                                SELECT 'boulder-dash-1.png' UNION ALL
#                                SELECT 'boulder-dash-2.png') AS images
# WHERE name='Boulder dash';
#
# -- 20. Bomber man
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'bomb-title.png' AS img UNION ALL
#                                SELECT 'bomb-title-1.png' UNION ALL
#                                SELECT 'bobm-1.png' UNION ALL
#                                SELECT 'bobm-2.png') AS images
# WHERE name='Bomber man';
#
# -- 21. File manager
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'fm-1.png' AS img UNION ALL
#                                SELECT 'fm-2.png' UNION ALL
#                                SELECT 'fm-3.png' UNION ALL
#                                SELECT 'fm-4.png' UNION ALL
#                                SELECT 'fm-5.png' UNION ALL
#                                SELECT 'fm-6.png' UNION ALL
#                                SELECT 'fm-7.png') AS images
# WHERE name='File manager';
#
# -- 22. Image view
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'iview-1.png' AS img UNION ALL
#                                SELECT 'iview-2.png' UNION ALL
#                                SELECT 'iview-3.png' UNION ALL
#                                SELECT 'iview-4.png' UNION ALL
#                                SELECT 'iview-5.png' UNION ALL
#                                SELECT 'iview-6.png') AS images
# WHERE name='Image view';
#
# -- 23. STC Player
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'stcpl-1.png' AS img UNION ALL
#                                SELECT 'stcpl-2.png' UNION ALL
#                                SELECT 'stcpl-3.png' UNION ALL
#                                SELECT 'stcpl-4.png' UNION ALL
#                                SELECT 'stcpl-5.png' UNION ALL
#                                SELECT 'stcpl-6.png' UNION ALL
#                                SELECT 'stcpl-7.png') AS images
# WHERE name='STC Player';
#
# -- 24. Питон
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'piton-title.png' AS img UNION ALL
#                                SELECT 'piton-1.png' UNION ALL
#                                SELECT 'piton-2.png' UNION ALL
#                                SELECT 'piton-3.png') AS images
# WHERE name='Питон';
#
# -- 25. Двигалка
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'dvig-title.png' AS img UNION ALL
#                                SELECT 'dvig-1.png' UNION ALL
#                                SELECT 'dvig-2.png' UNION ALL
#                                SELECT 'dvig-3.png' UNION ALL
#                                SELECT 'dvig-4.png') AS images
# WHERE name='Двигалка';
#
# -- 26. Bolder dash
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'bolder-title.png' AS img UNION ALL
#                                SELECT 'bolder-1.png' UNION ALL
#                                SELECT 'bolder-2.png' UNION ALL
#                                SELECT 'bolder-3.png' UNION ALL
#                                SELECT 'bolder-4.png' UNION ALL
#                                SELECT 'bolder-5.png' UNION ALL
#                                SELECT 'bolder-6.png') AS images
# WHERE name='Bolder dash';
#
# -- 27. High way
# INSERT INTO product_images (product_id, image_file)
# SELECT id, img FROM products, (SELECT 'higway-title.png' AS img UNION ALL
#                                SELECT 'higway-title-2.png' UNION ALL
#                                SELECT 'higway-1.png' UNION ALL
#                                SELECT 'higway-2.png') AS images
# WHERE name='High way';
