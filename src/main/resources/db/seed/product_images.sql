USE catalog_db;

-- 1. Клад
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/klad-title.png' AS img UNION ALL
                               SELECT 'images/klad-1.png' UNION ALL
                               SELECT 'images/klad-2.png' UNION ALL
                               SELECT 'images/klad-3.png' UNION ALL
                               SELECT 'images/klad-4.png') AS images
WHERE name='Клад';

-- 2. Пилот
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/pilot-title.png' AS img UNION ALL
                               SELECT 'images/pilot-1.png' UNION ALL
                               SELECT 'images/pilot-2.png' UNION ALL
                               SELECT 'images/pilot-3.png' UNION ALL
                               SELECT 'images/pilot-4.png' UNION ALL
                               SELECT 'images/pilot-5.png') AS images
WHERE name='Пилот';

-- 3. Питон (Фотон)
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/piton-foton-title.png' AS img UNION ALL
                               SELECT 'images/piton-foton-1.png' UNION ALL
                               SELECT 'images/piton-foton-2.png' UNION ALL
                               SELECT 'images/piton-foton-3.png') AS images
WHERE name='Питон (Фотон)';

-- 4. Угадай число
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/chislo-title.png' AS img UNION ALL
                               SELECT 'images/chislo-1.png' UNION ALL
                               SELECT 'images/chislo-2.png' UNION ALL
                               SELECT 'images/chislo-3.png') AS images
WHERE name='Угадай число';

-- 5. Тяп-ляп
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/typ-lyp-title.png' AS img UNION ALL
                               SELECT 'images/typ-lyp-1.png') AS images
WHERE name='Тяп-ляп';

-- 6. Тест
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/tect-title.png' AS img UNION ALL
                               SELECT 'images/tect-1.png' UNION ALL
                               SELECT 'images/tect-2.png') AS images
WHERE name='Тест';

-- 7. Минер
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/miner-title.png' AS img UNION ALL
                               SELECT 'images/miner-0.png' UNION ALL
                               SELECT 'images/miner-1.png' UNION ALL
                               SELECT 'images/miner-2.png') AS images
WHERE name='Минер';

-- 8. Морской бой
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/mor-boy-title.png' AS img UNION ALL
                               SELECT 'images/mor-boy-1.png' UNION ALL
                               SELECT 'images/mor-boy-2.png' UNION ALL
                               SELECT 'images/mor-boy-3.png') AS images
WHERE name='Морской бой';

-- 9. Автодром
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/avto-title.png' AS img UNION ALL
                               SELECT 'images/avto-1.png' UNION ALL
                               SELECT 'images/avto-2.png') AS images
WHERE name='Автодром';

-- 10. Сура
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/sura-title.png' AS img UNION ALL
                               SELECT 'images/sura-1.png' UNION ALL
                               SELECT 'images/sura-2.png') AS images
WHERE name='Сура';

-- 11. Танец
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/tanec-1.png' AS img UNION ALL
                               SELECT 'images/tanec-2.png' UNION ALL
                               SELECT 'images/tanec-3.png') AS images
WHERE name='Танец';

-- 12. Шахматы
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/chess-title.png' AS img UNION ALL
                               SELECT 'images/chess-1.png' UNION ALL
                               SELECT 'images/chess-2.png' UNION ALL
                               SELECT 'images/chess-3.png') AS images
WHERE name='Шахматы';

-- 13. Пожарник
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/fire-title.png' AS img UNION ALL
                               SELECT 'images/fire-1.png' UNION ALL
                               SELECT 'images/fire-2.png' UNION ALL
                               SELECT 'images/fire-3.png' UNION ALL
                               SELECT 'images/fire-4.png') AS images
WHERE name='Пожарник';

-- 14. Тест-Хобби
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/test-title.png' AS img UNION ALL
                               SELECT 'images/test-1.png' UNION ALL
                               SELECT 'images/test-2.png' UNION ALL
                               SELECT 'images/test-3.png' UNION ALL
                               SELECT 'images/test-4.png') AS images
WHERE name='Тест-Хобби';

-- 15. Алибаба
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/alibab-title.png' AS img UNION ALL
                               SELECT 'images/alibab-1.png') AS images
WHERE name='Алибаба';

-- 16. Binary land
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/binary-title.png' AS img UNION ALL
                               SELECT 'images/binary-1.png' UNION ALL
                               SELECT 'images/binary-2.png' UNION ALL
                               SELECT 'images/binary-3.png') AS images
WHERE name='Binary land';

-- 17. Crux
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/crux-title.png' AS img UNION ALL
                               SELECT 'images/crux-1.png' UNION ALL
                               SELECT 'images/crux-2.png') AS images
WHERE name='Crux';

-- 18. Кобра
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/cobra-1.png' AS img UNION ALL
                               SELECT 'images/cobra-2.png') AS images
WHERE name='Кобра';

-- 19. Boulder dash
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/boulder-dash-title.png' AS img UNION ALL
                               SELECT 'images/boulder-dash-1.png' UNION ALL
                               SELECT 'images/boulder-dash-2.png') AS images
WHERE name='Boulder dash';

-- 20. Bomber man
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/bomb-title.png' AS img UNION ALL
                               SELECT 'images/bomb-title-1.png' UNION ALL
                               SELECT 'images/bobm-1.png' UNION ALL
                               SELECT 'images/bobm-2.png') AS images
WHERE name='Bomber man';

-- 21. File manager
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/fm-1.png' AS img UNION ALL
                               SELECT 'images/fm-2.png' UNION ALL
                               SELECT 'images/fm-3.png' UNION ALL
                               SELECT 'images/fm-4.png' UNION ALL
                               SELECT 'images/fm-5.png' UNION ALL
                               SELECT 'images/fm-6.png' UNION ALL
                               SELECT 'images/fm-7.png') AS images
WHERE name='File manager';

-- 22. Image view
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/iview-1.png' AS img UNION ALL
                               SELECT 'images/iview-2.png' UNION ALL
                               SELECT 'images/iview-3.png' UNION ALL
                               SELECT 'images/iview-4.png' UNION ALL
                               SELECT 'images/iview-5.png' UNION ALL
                               SELECT 'images/iview-6.png') AS images
WHERE name='Image view';

-- 23. STC Player
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/stcpl-1.png' AS img UNION ALL
                               SELECT 'images/stcpl-2.png' UNION ALL
                               SELECT 'images/stcpl-3.png' UNION ALL
                               SELECT 'images/stcpl-4.png' UNION ALL
                               SELECT 'images/stcpl-5.png' UNION ALL
                               SELECT 'images/stcpl-6.png' UNION ALL
                               SELECT 'images/stcpl-7.png') AS images
WHERE name='STC Player';

-- 24. Питон
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/piton-title.png' AS img UNION ALL
                               SELECT 'images/piton-1.png' UNION ALL
                               SELECT 'images/piton-2.png' UNION ALL
                               SELECT 'images/piton-3.png') AS images
WHERE name='Питон';

-- 25. Двигалка
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/dvig-title.png' AS img UNION ALL
                               SELECT 'images/dvig-1.png' UNION ALL
                               SELECT 'images/dvig-2.png' UNION ALL
                               SELECT 'images/dvig-3.png' UNION ALL
                               SELECT 'images/dvig-4.png') AS images
WHERE name='Двигалка';

-- 26. Bolder dash
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/bolder-title.png' AS img UNION ALL
                               SELECT 'images/bolder-1.png' UNION ALL
                               SELECT 'images/bolder-2.png' UNION ALL
                               SELECT 'images/bolder-3.png' UNION ALL
                               SELECT 'images/bolder-4.png' UNION ALL
                               SELECT 'images/bolder-5.png' UNION ALL
                               SELECT 'images/bolder-6.png') AS images
WHERE name='Bolder dash';

-- 27. High way
INSERT INTO product_images (product_id, image_file)
SELECT id, img FROM products, (SELECT 'images/higway-title.png' AS img UNION ALL
                               SELECT 'images/higway-title-2.png' UNION ALL
                               SELECT 'images/higway-1.png' UNION ALL
                               SELECT 'images/higway-2.png') AS images
WHERE name='High way';
