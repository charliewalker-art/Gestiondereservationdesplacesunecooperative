INSERT INTO users (id, password, username)
SELECT 1, '$2a$10$gvVBR1uZCzSW23KTTbEXHedMG9P.E5mHNvzCrwUGoTvEuptZBnLjm', 'charlie'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE id = 1);