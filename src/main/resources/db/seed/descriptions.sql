INSERT INTO catalog_db.product_description_files (product_id, description_cache, file_path, file_size, file_created_at, file_modified_at)
SELECT p.id, '', 'descriptions/klad.md', 0, 0, 0
FROM catalog_db.products p
WHERE p.name = 'Клад';

INSERT INTO catalog_db.product_description_files (product_id, description_cache, file_path, file_size, file_created_at, file_modified_at)
SELECT p.id, '', 'descriptions/chislo.md', 0, 0, 0
FROM catalog_db.products p
WHERE p.name = 'Угадай число';

INSERT INTO catalog_db.product_description_files (product_id, description_cache, file_path, file_size, file_created_at, file_modified_at)
SELECT p.id, '', 'descriptions/test.md', 0, 0, 0
FROM catalog_db.products p
WHERE p.name = 'Тест';

INSERT INTO catalog_db.product_description_files (product_id, description_cache, file_path, file_size, file_created_at, file_modified_at)
SELECT p.id, '', 'descriptions/piton.md', 0, 0, 0
FROM catalog_db.products p
WHERE p.name = 'Питон';

INSERT INTO catalog_db.product_description_files (product_id, description_cache, file_path, file_size, file_created_at, file_modified_at)
SELECT p.id, '', 'descriptions/typ-lap.md', 0, 0, 0
FROM catalog_db.products p
WHERE p.name = 'Тяп-ляп';

