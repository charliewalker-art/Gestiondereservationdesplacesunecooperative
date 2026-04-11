INSERT INTO users (id, password, username)
SELECT 1, '$2a$10$2n.Re2uv1yL83y1nNFRNlOYGkz8GfuxYRhW/aJHyoGNIxURMvHnpi', 'charlie'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 1);