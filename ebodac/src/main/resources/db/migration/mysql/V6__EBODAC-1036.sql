DROP PROCEDURE IF EXISTS removeNotNullConstraintFromSubjectIdOidColumn;

DELIMITER //
CREATE PROCEDURE removeNotNullConstraintFromSubjectIdOidColumn()
BEGIN

IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE table_schema=DATABASE() AND table_name='EBODAC_MODULE_VISIT') THEN

ALTER TABLE EBODAC_MODULE_VISIT MODIFY COLUMN subject_id_OID bigint(20) DEFAULT NULL;

END IF;

IF EXISTS (SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE table_schema=DATABASE() AND table_name='EBODAC_MODULE_VISIT__HISTORY' AND column_name='subject_ID') THEN

ALTER TABLE EBODAC_MODULE_VISIT__HISTORY MODIFY COLUMN subject_ID bigint(20) DEFAULT NULL;
END IF;

END//

DELIMITER ;

CALL removeNotNullConstraintFromSubjectIdOidColumn();

DROP PROCEDURE IF EXISTS removeNotNullConstraintFromSubjectIdOidColumn;
