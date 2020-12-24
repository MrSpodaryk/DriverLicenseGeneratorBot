-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema Bot
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema Bot
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `Bot` DEFAULT CHARACTER SET utf8;
USE `Bot`;

-- -----------------------------------------------------
-- Table `Bot`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Bot`.`user`
(
    `id`         INT         NOT NULL AUTO_INCREMENT,
    `chat_id`    INT         NOT NULL,
    `state`      VARCHAR(45) NOT NULL,
    `first_name` VARCHAR(45) NOT NULL,
    `last_name`  VARCHAR(45) NOT NULL,
    `email`      VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Bot`.`driver_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Bot`.`driver_category`
(
    `id`       INT         NOT NULL AUTO_INCREMENT,
    `category` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Bot`.`gender`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Bot`.`gender`
(
    `id`     INT         NOT NULL AUTO_INCREMENT,
    `gender` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Bot`.`driver_license_template`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Bot`.`driver_license_template`
(
    `id`            INT          NOT NULL AUTO_INCREMENT,
    `Uid`           VARCHAR(45)  NULL,
    `first_name`    VARCHAR(45)  NULL,
    `last_name`     VARCHAR(45)  NULL,
    `date_time`     VARCHAR(45)  NULL,
    `date_of_birth` VARCHAR(45)  NULL,
    `email`         VARCHAR(45)  NULL,
    `is_finished`   TINYINT      NULL,
    `img_url`       VARCHAR(300) NULL,
    `gender_id`     INT          NOT NULL,
    `user_id`       INT          NOT NULL,
    PRIMARY KEY (`id`, `gender_id`, `user_id`),
    INDEX `fk_driver_license_template_gender_idx` (`gender_id` ASC) VISIBLE,
    INDEX `fk_driver_license_template_user1_idx` (`user_id` ASC) VISIBLE,
    CONSTRAINT `fk_driver_license_template_gender`
        FOREIGN KEY (`gender_id`)
            REFERENCES `Bot`.`gender` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_driver_license_template_user1`
        FOREIGN KEY (`user_id`)
            REFERENCES `Bot`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Bot`.`driver_license_template_driver_category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `Bot`.`driver_license_template_driver_category`
(
    `driver_category_id`         INT NOT NULL,
    `driver_license_template_id` INT NOT NULL,
    PRIMARY KEY (`driver_category_id`, `driver_license_template_id`),
    INDEX `fk_driver_category_has_driver_license_template_driver_lican_idx` (`driver_license_template_id` ASC) VISIBLE,
    INDEX `fk_driver_category_has_driver_license_template_driver_categ_idx` (`driver_category_id` ASC) VISIBLE,
    CONSTRAINT `fk_driver_category_has_driver_license_template_driver_category1`
        FOREIGN KEY (`driver_category_id`)
            REFERENCES `Bot`.`driver_category` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_driver_category_has_driver_license_template_driver_license1`
        FOREIGN KEY (`driver_license_template_id`)
            REFERENCES `Bot`.`driver_license_template` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
