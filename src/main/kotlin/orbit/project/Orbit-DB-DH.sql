    CREATE TABLE `orbit`.`group` (
                                 `group_id` INT NOT NULL AUTO_INCREMENT,
                                 `group_name` VARCHAR(30) NOT NULL,
                                 `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`group_id`)
);

INSERT INTO `orbit`.`group` (group_name) VALUES ('test_group');

CREATE TABLE `orbit`.`role` (
                                `role_id` INT NOT NULL AUTO_INCREMENT,
                                `role_name` VARCHAR(10) NOT NULL,
                                `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                PRIMARY KEY (`role_id`)
);

INSERT INTO `orbit`.`role` (role_name) VALUES ('test_role');

CREATE TABLE `orbit`.`member` (
                                  `member_id` INT NOT NULL AUTO_INCREMENT,
                                  `group_id` INT NOT NULL,
                                  `role_id` INT NOT NULL,
                                  `email` VARCHAR(50) NOT NULL UNIQUE,
                                  `password` VARCHAR(255) NOT NULL,
                                  `name` VARCHAR(10) NOT NULL,
                                  `birth` VARCHAR(20) NOT NULL,
                                  `phone_number` VARCHAR(20) NOT NULL,
                                  `job` VARCHAR(30) NOT NULL,
                                  `auth_type` VARCHAR(15) NOT NULL,
                                  `invite_code` VARCHAR(20) NOT NULL,
                                  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                  `last_login` DATETIME DEFAULT NULL,
                                  PRIMARY KEY (`member_id`),
                                  CONSTRAINT `fk_group` FOREIGN KEY (`group_id`) REFERENCES `group`(`group_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                  CONSTRAINT `fk_role` FOREIGN KEY (`role_id`) REFERENCES `role`(`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

    CREATE TABLE `orbit`.`todo` (
                                    `todo_id` INT NOT NULL AUTO_INCREMENT,
                                    `maintask` VARCHAR(30) NOT NULL,
                                    `task` VARCHAR(30) NOT NULL,
                                    `detail` VARCHAR(200) NOT NULL,
                                    `tags` VARCHAR(50) NOT NULL,
                                    `is_check` TINYINT NOT NULL DEFAULT 0,
                                    `date` DATETIME NOT NULL,
                                    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    `created_by` INT NOT NULL,
                                    PRIMARY KEY (`todo_id`),
                                    CONSTRAINT `fk_created_by` FOREIGN KEY (`created_by`) REFERENCES `member`(`member_id`) ON DELETE CASCADE ON UPDATE CASCADE
    );