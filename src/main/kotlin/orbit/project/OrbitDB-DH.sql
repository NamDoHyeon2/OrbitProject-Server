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
CREATE TABLE `orbit_db`.`project` (
                                   `project_id` INT NOT NULL AUTO_INCREMENT,
                                   `project_name` VARCHAR(30) NOT NULL,
                                   `project_description` VARCHAR(100) NOT NULL, #프로젝트 설명
                                   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                   `created_by` varchar(100) NOT NULL,
                                   PRIMARY KEY (`project_id`)
);

CREATE TABLE `orbit_db`.`project_member` (
                                          `project_id` INT NOT NULL,
                                          `member_id` INT NOT NULL,
                                          `joined_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 멤버가 프로젝트에 참여한 날짜
                                          PRIMARY KEY (`project_id`, `member_id`), -- 복합 키로 유일성 보장
                                          CONSTRAINT `fk_project` FOREIGN KEY (`project_id`) REFERENCES `project`(`project_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                          CONSTRAINT `fk_member` FOREIGN KEY (`member_id`) REFERENCES `member`(`member_id`) ON DELETE CASCADE ON UPDATE CASCADE
);


#멤버가 특정 프로젝트에 속해 있는지 확인
#→ SELECT * FROM project_member WHERE project_id = ? AND member_id = ?;

#특정 프로젝트에 속한 모든 멤버 조회
#→ SELECT m.* FROM member m INNER JOIN project_member pm ON m.member_id = pm.member_id WHERE pm.project_id = ?;

#특정 멤버가 참여 중인 모든 프로젝트 조회
#→ SELECT p.* FROM project p INNER JOIN project_member pm ON p.project_id = pm.project_id WHERE pm.member_id = ?;

