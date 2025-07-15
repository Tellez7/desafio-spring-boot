CREATE TABLE usuarios (
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  username  VARCHAR(50) NOT NULL UNIQUE,
  password  VARCHAR(120) NOT NULL,
  role      VARCHAR(30) NOT NULL
);

CREATE TABLE estados_tarea (
  id   BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE tareas (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  title       VARCHAR(150) NOT NULL,
  description VARCHAR(500),
  status_id   BIGINT NOT NULL,
  user_id     BIGINT NOT NULL,
  CONSTRAINT fk_tareas_estados
      FOREIGN KEY (status_id) REFERENCES estados_tarea(id),
  CONSTRAINT fk_tareas_usuarios
      FOREIGN KEY (user_id)   REFERENCES usuarios(id)
);