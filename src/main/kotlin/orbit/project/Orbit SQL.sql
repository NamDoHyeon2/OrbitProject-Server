CREATE TABLE `orbit`.`Group` (
                                 `group_id` INT NOT NULL AUTO_INCREMENT,
                                 `group_name` VARCHAR(30) NOT NULL,
                                 `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`group_id`)
);

INSERT INTO `Group` (group_name) VALUES ('test_group');

CREATE TABLE `orbit`.`Role` (
                                `role_id` INT NOT NULL AUTO_INCREMENT,
                                `role_name` VARCHAR(10) NOT NULL,
                                `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                PRIMARY KEY (`role_id`)
);

INSERT INTO `Role` (role_name) VALUES ('test_role');

CREATE TABLE `orbit`.`Member` (
                                  `member_id` INT NOT NULL AUTO_INCREMENT,
                                  `group_id` INT NOT NULL,
                                  `role_id` INT NOT NULL,
                                  `name` VARCHAR(10) NOT NULL,
                                  `email` VARCHAR(50) NOT NULL UNIQUE,
                                  `login_id` VARCHAR(255) NOT NULL,
                                  `password` VARCHAR(255) NOT NULL,
                                  `auth_type` VARCHAR(15) NOT NULL,
                                  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  `last_login` DATETIME DEFAULT NULL,
                                  PRIMARY KEY (`Member_id`),
                                  CONSTRAINT `fk_group` FOREIGN KEY (`group_id`) REFERENCES `Group`(`group_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                  CONSTRAINT `fk_role` FOREIGN KEY (`role_id`) REFERENCES `Role`(`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
);
--
-- CREATE TABLE `orbit`.`Project` (
--   `project_id` INT NOT NULL AUTO_INCREMENT,
--   `project_name` VARCHAR(30) NOT NULL,
--   `project_member` VARCHAR(100) NOT NULL,
--   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--   `created_by` INT NOT NULL,
--   PRIMARY KEY (`project_id`));
--
-- CREATE TABLE `orbit`.`Brain` (
--   `brain_id` INT NOT NULL AUTO_INCREMENT,
--   `brain_name` VARCHAR(30) NOT NULL,
--   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--   `created_by` INT NOT NULL,
--   PRIMARY KEY (`brain_id`));
--
-- CREATE TABLE `orbit`.`Keyword` (
--   `keyword_id` INT NOT NULL AUTO_INCREMENT,
--   `keyword_value` VARCHAR(30) NOT NULL,
--   `keyword_color` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--   `created_by` INT NOT NULL,
--   PRIMARY KEY (`keyword_id`));
--
-- CREATE TABLE `orbit`.`Calendar` (
--   `calendar_id` INT NOT NULL AUTO_INCREMENT,
--   `group_id` INT NOT NULL,
--   `calendar_name` VARCHAR(30) NOT NULL,
--   `calendar_stats`VARCHAR(200) NOT NULL,
--   `calendar_start_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `calendar_end_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--   `created_by` INT NOT NULL,
--   PRIMARY KEY (`calendar_id`),
--   CONSTRAINT `group_fk` FOREIGN KEY (`group_id`) REFERENCES `Group`(`group_id`) ON DELETE CASCADE ON UPDATE CASCADE);
--
-- CREATE TABLE `orbit`.`ToDo` (
--   `todo_id` INT NOT NULL AUTO_INCREMENT,
--   `member_id` INT NOT NULL,
--   `todo_name` VARCHAR(30) NOT NULL,
--   `todo_stats`VARCHAR(200) NOT NULL,
--   `todo_start_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `todo_end_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--   `created_by` INT NOT NULL,
--   PRIMARY KEY (`todo_id`),
--   CONSTRAINT `fk_member` FOREIGN KEY (`member_id`) REFERENCES `Member`(`member_id`) ON DELETE CASCADE ON UPDATE CASCADE);
--
-- CREATE TABLE `orbit`.`Docs` (
--   `docs_id` INT NOT NULL AUTO_INCREMENT,
--   `docs_name` VARCHAR(30) NOT NULL,
--   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--   `created_by` INT NOT NULL,
--   PRIMARY KEY (`docs_id`));
--
-- CREATE TABLE `orbit`.`Value` (
--   `value_id` INT NOT NULL AUTO_INCREMENT,
--   `value` VARCHAR(50) NOT NULL,
--   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--   `created_by` INT NOT NULL,
--   PRIMARY KEY (`value_id`));
--
-- CREATE TABLE `orbit`.`Image` (
--   `image_id` INT NOT NULL AUTO_INCREMENT,
--   `image_name` VARCHAR(100) NOT NULL,
--   `image_path` VARCHAR(300) NOT NULL,
--   `linked_resource` VARCHAR(255) DEFAULT NULL,
--   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--   `created_by` INT NOT NULL,
--   PRIMARY KEY (`image_id`));
--
-- CREATE TABLE `orbit`.`File` (
--   `file_id` INT NOT NULL AUTO_INCREMENT,
--   `file_name` VARCHAR(100) NOT NULL,
--   `file_path` VARCHAR(300) NOT NULL,
--   `linked_resource` VARCHAR(255) DEFAULT NULL,
--   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
--   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
--   `created_by` INT NOT NULL,
--   PRIMARY KEY (`file_id`));
--
-- CREATE TABLE `orbit`.`Brain_Keyword` (
--   `brain_id` INT NOT NULL,
--   `keyword_id` INT NOT NULL,
--   `circle_line` VARCHAR(255) DEFAULT NULL,
--   PRIMARY KEY (`brain_id`, `keyword_id`),
--   CONSTRAINT `fk_brain` FOREIGN KEY (`brain_id`) REFERENCES `Brain`(`brain_id`) ON DELETE CASCADE ON UPDATE CASCADE,
--   CONSTRAINT `fk_keyword` FOREIGN KEY (`keyword_id`) REFERENCES `Keyword`(`keyword_id`) ON DELETE CASCADE ON UPDATE CASCADE
-- );
--
-- CREATE TABLE `orbit`.`Project_Brain` (
--   `project_id` INT NOT NULL,
--   `brain_id` INT NOT NULL,
--   `can_read` TINYINT NOT NULL DEFAULT 0,
--   `can_write` TINYINT NOT NULL DEFAULT 0,
--   PRIMARY KEY (`project_id`, `brain_id`),
--   CONSTRAINT `fk_project` FOREIGN KEY (`project_id`) REFERENCES `Project`(`project_id`) ON DELETE CASCADE ON UPDATE CASCADE,
--   CONSTRAINT `brain_fk` FOREIGN KEY (`brain_id`) REFERENCES `Brain`(`brain_id`) ON DELETE CASCADE ON UPDATE CASCADE
-- );
--
-- CREATE TABLE `orbit`.`Docs_Value` (
--   `docs_id` INT NOT NULL,
--   `value_id` INT NOT NULL,
--   `write_line` VARCHAR(255) DEFAULT NULL,
--   PRIMARY KEY (`docs_id`, `value_id`),
--   CONSTRAINT `fk_docs` FOREIGN KEY (`docs_id`) REFERENCES `Docs`(`docs_id`) ON DELETE CASCADE ON UPDATE CASCADE,
--   CONSTRAINT `fk_value` FOREIGN KEY (`value_id`) REFERENCES `Value`(`value_id`) ON DELETE CASCADE ON UPDATE CASCADE
-- );
--