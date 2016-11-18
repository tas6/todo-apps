CREATE TABLE tasks (
  id          INT           PRIMARY KEY AUTO_INCREMENT,
  subject     VARCHAR(100),
  description VARCHAR(500),
  done        BOOLEAN,
  version     INT
);