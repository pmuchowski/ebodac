DROP PROCEDURE IF EXISTS addGroupColumn;

DELIMITER //
CREATE PROCEDURE addGroupColumn()
BEGIN

IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE table_schema=DATABASE() AND table_name='EBODAC_MODULE_ENROLLMENT') THEN

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema=DATABASE() AND table_name='EBODAC_MODULE_ENROLLMENT' AND column_name='group') THEN

ALTER TABLE EBODAC_MODULE_ENROLLMENT ADD `group` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL;

END IF;

END IF;

IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE table_schema=DATABASE() AND table_name='EBODAC_MODULE_ENROLLMENT__TRASH') THEN

IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema=DATABASE() AND table_name='EBODAC_MODULE_ENROLLMENT__TRASH' AND column_name='group') THEN

ALTER TABLE EBODAC_MODULE_ENROLLMENT__TRASH ADD `group` varchar(255) CHARACTER SET latin1 COLLATE latin1_bin DEFAULT NULL;

END IF;

END IF;

END//

DELIMITER ;

CALL addGroupColumn();

DROP PROCEDURE IF EXISTS addGroupColumn;


DROP PROCEDURE IF EXISTS updateGroup;

DELIMITER //
CREATE PROCEDURE updateGroup()
BEGIN

IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema=DATABASE() AND table_name='EBODAC_MODULE_ENROLLMENT' AND column_name='group') THEN

UPDATE EBODAC_MODULE_ENROLLMENT SET `group` = (CASE WHEN campaignName REGEXP '.* Vaccination.*Follow-up visit' THEN 'Vaccination Follow-up visit'
WHEN campaignName REGEXP '.* Long-term Follow-up visit' THEN 'Long-term Follow-up visit' ELSE campaignName END) WHERE `group` IS NULL;

END IF;

IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema=DATABASE() AND table_name='EBODAC_MODULE_ENROLLMENT__TRASH' AND column_name='group') THEN

UPDATE EBODAC_MODULE_ENROLLMENT__TRASH SET `group` = (CASE WHEN campaignName REGEXP '.* Vaccination.*Follow-up visit' THEN 'Vaccination Follow-up visit'
WHEN campaignName REGEXP '.* Long-term Follow-up visit' THEN 'Long-term Follow-up visit' ELSE campaignName END) WHERE `group` IS NULL;

END IF;

END//

DELIMITER ;

CALL updateGroup();

DROP PROCEDURE IF EXISTS updateGroup;