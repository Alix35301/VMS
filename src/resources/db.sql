CREATE TABLE `VMS`.`staff` (
  `idstaff` INT NOT NULL AUTO_INCREMENT,
  `staff_name` VARCHAR(45) NULL,
  `staff_pass` VARCHAR(45) NULL,
  `staff_role` VARCHAR(45) NULL,
  PRIMARY KEY (`idstaff`),
  UNIQUE INDEX `idstaff_UNIQUE` (`idstaff` ASC) VISIBLE);


CREATE TABLE `VMS`.`new_table` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `email` VARCHAR(255) NULL,
  `password` VARCHAR(45) NULL,
  `phone` VARCHAR(45) NULL,
  `address` VARCHAR(45) NULL,
  PRIMARY KEY (`id`),
UNIQUE KEY unique_email (email));
