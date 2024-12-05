CREATE TABLE `orbit_db`.`Group` (
                                    `group_id` INT NOT NULL AUTO_INCREMENT,
                                    `group_name` VARCHAR(30) NOT NULL,
                                    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    PRIMARY KEY (`group_id`)
);

INSERT INTO `Group` (group_name) VALUES ('test_group');

CREATE TABLE `orbit_db`.`Role` (
                                   `role_id` INT NOT NULL AUTO_INCREMENT,
                                   `role_name` VARCHAR(10) NOT NULL,
                                   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   PRIMARY KEY (`role_id`)
);

INSERT INTO `Role` (role_name) VALUES ('test_role');

CREATE TABLE `orbit_db`.`Member` (
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