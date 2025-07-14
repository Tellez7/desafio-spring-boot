CREATE TABLE users (
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  username  VARCHAR(50) NOT NULL UNIQUE,
  password  VARCHAR(120) NOT NULL,
  role      VARCHAR(30) NOT NULL
);

CREATE TABLE task_status (
  id   BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(30) NOT NULL UNIQUE
);

CREATE TABLE tasks (
  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
  title       VARCHAR(150) NOT NULL,
  description VARCHAR(500),
  status_id   BIGINT NOT NULL,
  user_id     BIGINT NOT NULL,
  CONSTRAINT fk_tasks_status
      FOREIGN KEY (status_id) REFERENCES task_status(id),
  CONSTRAINT fk_tasks_user
      FOREIGN KEY (user_id)   REFERENCES users(id)
);

INSERT INTO users (username, password, role)
VALUES ('admin',
        '$2a$10$Iyp6D1HrwF1CmUOnLmoQV.YO/rLDPSfij67ZHKh6gzz5UcbVO6xUG', -- "admin123"
        'ROLE_USER');

INSERT INTO task_status (name) VALUES
 ('PENDING'), ('IN_PROGRESS'), ('DONE');
