INSERT INTO catalog_db.product_description_files (
    product_id, file_path, file_size, file_created_at, file_modified_at
)
SELECT
    p.id,
    'klad.md',
    0,
    0,
    0
FROM catalog_db.products p
WHERE p.name = 'Клад';
