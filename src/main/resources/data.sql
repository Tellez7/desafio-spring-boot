INSERT INTO estados_tarea (id, name) VALUES (1, 'TODO'), (2, 'IN_PROGRESS'), (3, 'DONE');

--TODO: check role y language
-- "admin123"
INSERT INTO usuarios (id, username, password, role)
VALUES
  (1, 'admin', '$2a$10$dOJ9D5j6Q1zuAFiVwQH1..m2uOe0D9oy6C5M6hVOXCqrjpuMX6lG', 'ROLE_ADMIN');

INSERT INTO tareas (title, description, status_id, user_id)
VALUES
  ('Aprender H2', 'Probar carga automática', 1, 1);
