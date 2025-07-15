INSERT INTO estados_tarea (name) VALUES ('TODO'), ('IN_PROGRESS'), ('DONE');

--password: admin123
INSERT INTO usuarios (username, password, role)
VALUES
  ('esteban', '$2a$10$7GD/sIj.Aul8302DnYzOE.AjcMBV6xDDsNHSmCzLAfhIBMduF86vS', 'ADMIN');

--password: user123
INSERT INTO usuarios (username, password, role)
VALUES
  ('mateo', '$2a$10$7IzqkVHt/9afhwswFFgTBetRbjt2TEhnnDT9HXyAakxe83b/yrxOe', 'USER');

INSERT INTO tareas (title, description, status_id, user_id)
VALUES
  ('Aprender H2', 'Probar carga automática', 1, 1);
