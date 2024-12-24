CREATE TABLE `orbit_db`.`group` (
                                    `group_id` INT NOT NULL AUTO_INCREMENT,
                                    `group_name` VARCHAR(30) NOT NULL,
                                    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                    PRIMARY KEY (`group_id`)
);

INSERT INTO `group` (group_name) VALUES ('test_group');

CREATE TABLE `orbit_db`.`role` (
                                   `role_id` INT NOT NULL AUTO_INCREMENT,
                                   `role_name` VARCHAR(10) NOT NULL,
                                   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   PRIMARY KEY (`role_id`)
);

INSERT INTO `role` (role_name) VALUES ('test_role');

CREATE TABLE `orbit_db`.`member` (
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
                                     PRIMARY KEY (`member_id`),
                                     CONSTRAINT `fk_group` FOREIGN KEY (`group_id`) REFERENCES `group`(`group_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                     CONSTRAINT `fk_role` FOREIGN KEY (`role_id`) REFERENCES `role`(`role_id`) ON DELETE CASCADE ON UPDATE CASCADE
);
create table `orbit_db`.`image` (
                                    `image_id` int not null auto_increment,
                                    `image_name` varchar(100) not null,
                                    `image_path` varchar(300) not null,
                                    `image_url` varchar(300) not null,
                                    `image_unique_id` varchar(300) not null, #이미지 식별자
                                    `linked_resource` varchar(255) default null,
                                    `created_at` datetime not null default current_timestamp,
                                    `updated_at` datetime not null default current_timestamp on update current_timestamp,
                                    `created_by` varchar(100) not null,
                                    primary key (`image_id`)
);

