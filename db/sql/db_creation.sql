SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`class`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`class` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `priority` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE,
  UNIQUE INDEX `priority_UNIQUE` (`priority` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`apartment_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`apartment_status` (
  `id` INT NOT NULL,
  `name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `apartment_status_pk` (`name` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`apartment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`apartment` (
  `floor` INT NOT NULL,
  `number` INT NOT NULL,
  `class_id` INT NOT NULL,
  `demand` INT NOT NULL DEFAULT '0',
  `price` DECIMAL(10,7) NOT NULL,
  `status_id` INT NOT NULL,
  `number_of_people` INT NOT NULL,
  PRIMARY KEY (`number`),
  INDEX `apartment_class_fk_idx` (`class_id` ASC) VISIBLE,
  INDEX `apartment_status_fk` (`status_id` ASC) VISIBLE,
  CONSTRAINT `apartment_class_fk`
    FOREIGN KEY (`class_id`)
    REFERENCES `mydb`.`class` (`id`),
  CONSTRAINT `apartment_status_fk`
    FOREIGN KEY (`status_id`)
    REFERENCES `mydb`.`apartment_status` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`user_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user_status` (
  `id` INT NOT NULL,
  `name` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(16) NOT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `password` VARCHAR(32) NOT NULL,
  `firstname` VARCHAR(20) NOT NULL,
  `lastname` VARCHAR(20) NOT NULL,
  `phone` VARCHAR(45) NOT NULL,
  `status_id` INT NOT NULL,
  `money` DECIMAL(10,5) NOT NULL DEFAULT '0.00000',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login_UNIQUE` (`login` ASC) VISIBLE,
  INDEX `user_status_fk_idx` (`status_id` ASC) VISIBLE,
  CONSTRAINT `user_status_fk`
    FOREIGN KEY (`status_id`)
    REFERENCES `mydb`.`user_status` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 28
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`application_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`application_status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`application`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`application` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `client_id` INT NOT NULL,
  `apartment_id` INT NOT NULL,
  `status_id` INT NOT NULL,
  `price` DECIMAL(10,7) NOT NULL,
  `creation_date` DATETIME NOT NULL,
  `last_modified` DATETIME NOT NULL,
  `start_date` DATE NULL DEFAULT NULL,
  `end_date` DATE NULL DEFAULT NULL,
  `stay_length` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `application_client_fk_idx` (`client_id` ASC) VISIBLE,
  INDEX `application_status_fk_idx` (`status_id` ASC) VISIBLE,
  INDEX `application_apartment_fk_idx` (`apartment_id` ASC) VISIBLE,
  CONSTRAINT `application_apartment_fk`
    FOREIGN KEY (`apartment_id`)
    REFERENCES `mydb`.`apartment` (`number`),
  CONSTRAINT `application_client_fk`
    FOREIGN KEY (`client_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `application_status_fk`
    FOREIGN KEY (`status_id`)
    REFERENCES `mydb`.`application_status` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT)
ENGINE = InnoDB
AUTO_INCREMENT = 50
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role` VARCHAR(255) NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_role_fk_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `user_role_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 208
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`temporary_application`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`temporary_application` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `stay_length` INT NOT NULL,
  `number_of_people` INT NOT NULL,
  `class_id` INT NOT NULL,
  `client_login` VARCHAR(16) NOT NULL,
  `creation_date` DATE NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `apartment_request_class_null_fk` (`class_id` ASC) VISIBLE,
  INDEX `apartment_request_user_null_fk` (`client_login` ASC) VISIBLE,
  CONSTRAINT `apartment_request_class_null_fk`
    FOREIGN KEY (`class_id`)
    REFERENCES `mydb`.`class` (`id`),
  CONSTRAINT `apartment_request_user_null_fk`
    FOREIGN KEY (`client_login`)
    REFERENCES `mydb`.`user` (`login`))
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
