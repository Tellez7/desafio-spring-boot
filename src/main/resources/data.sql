INSERT INTO estados_tarea (id, name) VALUES (1, 'TODO'), (2, 'IN_PROGRESS'), (3, 'DONE');

--TODO: check role y language, quitar id si funciona
-- "admin123"
INSERT INTO usuarios (id, username, password, role)
VALUES
  (1, 'esteban', '$2a$10$7GD/sIj.Aul8302DnYzOE.AjcMBV6xDDsNHSmCzLAfhIBMduF86vS', 'ADMIN');

INSERT INTO usuarios (id, username, password, role)
VALUES
  (2, 'mateo', '$2a$10$7GD/sIj.Aul8302DnYzOE.AjcMBV6xDDsNHSmCzLAfhIBMduF86vS', 'OTHER_ROLE');

INSERT INTO tareas (title, description, status_id, user_id)
VALUES
  ('Aprender H2', 'Probar carga automática', 1, 1);
