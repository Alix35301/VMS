SELECT `vehicle_status`.`id`,
    `vehicle_status`.`vehicle_type`,
    `vehicle_status`.`handover_date`,
    `vehicle_status`.`collection_date`,
    `status`.`status`,
    `vehicle_status`.`remarks`,
    customers.name,
    `staffs`.`first_name`
FROM `VMS`.`vehicle_status`
INNER JOIN `VMS`.`staffs` ON `vehicle_status`.`service_adviser_id`=`staffs`.`id`
INNER JOIN `VMS`.`status` ON `vehicle_status`.`status`=`status`.`id`
INNER JOIN VMS.customers ON vehicle_status.customer_id=customers.id;
