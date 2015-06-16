SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `filehosting_tool` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `filehosting_tool` ;

-- -----------------------------------------------------
-- Table `filehosting_tool`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filehosting_tool`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `language` VARCHAR(45) NULL,
  `disk_quota` INT NULL,
  `profile_image` VARCHAR(45) NULL,
  `display_name` VARCHAR(100) NULL,
  `active` INT NULL,
  `notification_disk_full` INT NULL,
  `date_created` TIMESTAMP NULL,
  `last_login` TIMESTAMP NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `filehosting_tool`.`group`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filehosting_tool`.`group` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NULL,
  `is_admin` INT NULL,
  `date_created` TIMESTAMP NULL,
  `last_updated` TIMESTAMP NULL,
  `updated_user` VARCHAR(100) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `filehosting_tool`.`group_user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filehosting_tool`.`group_user` (
  `user_id` INT NOT NULL,
  `group_id` INT NOT NULL,
  INDEX `fk_group_user_group_idx` (`group_id` ASC),
  INDEX `fk_group_user_user_idx` (`user_id` ASC),
  CONSTRAINT `fk_group_user_group`
    FOREIGN KEY (`group_id`)
    REFERENCES `filehosting_tool`.`group` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_group_user_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `filehosting_tool`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `filehosting_tool`.`file`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filehosting_tool`.`file` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `path` VARCHAR(4000) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL,
  `parent` INT NULL,
  `name` VARCHAR(250) CHARACTER SET 'utf8' COLLATE 'utf8_bin' NULL,
  `mimetype` VARCHAR(100) NULL,
  `size` BIGINT NULL,
  `last_modified` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_file_user_idx` (`user_id` ASC),
  CONSTRAINT `fk_file_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `filehosting_tool`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `filehosting_tool`.`configuration`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filehosting_tool`.`configuration` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `conf_name` VARCHAR(100) NULL,
  `conf_value` LONGTEXT NULL,
  `updated_user` VARCHAR(100) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `filehosting_tool`.`log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filehosting_tool`.`log` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `event_date` TIMESTAMP NULL,
  `level` VARCHAR(45) NULL,
  `logger` VARCHAR(255) NULL,
  `message` TEXT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `filehosting_tool`.`files_deleted`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `filehosting_tool`.`files_deleted` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `path` VARCHAR(4000) NULL,
  `name` VARCHAR(200) NULL,
  `mimetype` VARCHAR(100) NULL,
  `last_modified` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_files_deleted_user_idx` (`user_id` ASC),
  CONSTRAINT `fk_files_deleted_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `filehosting_tool`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
