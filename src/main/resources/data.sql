INSERT INTO estados_tarea (name) VALUES ('TODO'), ('IN_PROGRESS'), ('DONE');

INSERT INTO usuarios (username, password, role)
VALUES
  ('esteban', '$2a$10$7GD/sIj.Aul8302DnYzOE.AjcMBV6xDDsNHSmCzLAfhIBMduF86vS', 'ADMIN');

INSERT INTO usuarios (username, password, role)
VALUES
  ('mateo', '$2a$10$7GD/sIj.Aul8302DnYzOE.AjcMBV6xDDsNHSmCzLAfhIBMduF86vS', 'OTHER_ROLE');

INSERT INTO tareas (title, description, status_id, user_id)
VALUES
  ('Aprender H2', 'Probar carga automática', 1, 1);
