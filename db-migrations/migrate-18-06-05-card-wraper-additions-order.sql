---------------------------------------------------------
-- Make a full dump of the database fill a clean DB with it using \i,  migration script (an error for public.model_sections_cards_wrappers will be shown, its ok)
-- THEN run this script to fill the sections
-- THEN make a CUSTOM dump of the contents with this command
-- /usr/bin/pg_dump --cluster 9.6/main --host localhost --port 5432 --username "postgres" --no-password  --format custom --blobs --no-privileges --no-tablespaces --verbose --file "/home/pepo/workspace/c1data/c1-migration-065-custom" "c1db"
-- THEN delete all tables online and restor using this commmand
-- /usr/bin/pg_restore --cluster 9.6/main --host ec2-54-75-239-237.eu-west-1.compute.amazonaws.com --port 5432 --username "sqiowmccjkebrr" --dbname "d4j1dmpkvm9qvu" --no-password --no-owner  --verbose "/home/pepo/workspace/c1data/c1-migration-065-custom"
---------------------------------------------------------

DROP SCHEMA IF EXISTS masterschema CASCADE;
CREATE SCHEMA masterschema;

---------------------------------------------------------
-- Connect to other server
---------------------------------------------------------
CREATE EXTENSION postgres_fdw;
CREATE SERVER master FOREIGN DATA WRAPPER postgres_fdw OPTIONS (host 'localhost', dbname 'c1db-bk', port '5432');
CREATE USER MAPPING FOR postgres SERVER master OPTIONS (user 'postgres', password 'iristra');
IMPORT FOREIGN SCHEMA public FROM SERVER master INTO masterschema;

SELECT * FROM model_cards_wrapper_additions crd_add
INNER JOIN masterschema.model_sections_cards_wrappers_additions_common crd_add_com
ON (crd_add_com.cards_wrappers_additions_common_id = crd_add.id)
WHERE crd_add.section_id = 'ac1031fa-5e4d-1452-815e-51cdc32d002f'
ORDER BY crd_add.section_id, crd_add_com.cards_order;

UPDATE model_cards_wrapper_additions crd_add 
SET before_card_wrapper_addition_id = (
	SELECT cards_wrappers_additions_common_id 
	FROM masterschema.model_sections_cards_wrappers_additions_common crd_add_com 
	JOIN model_cards_wrapper_additions crd_add
	ON crd_add_com.cards_wrappers_additions_common_id = crd_add.id
	WHERE crd_add_com.cards_order = 1
	AND crd_add_com.model_section_id = 'ac1031fa-5e4d-1452-815e-51cdc32d002f') 
WHERE id = (
	SELECT cards_wrappers_additions_common_id 
	FROM masterschema.model_sections_cards_wrappers_additions_common crd_add_com 
	JOIN model_cards_wrapper_additions crd_add
	ON crd_add_com.cards_wrappers_additions_common_id = crd_add.id
	WHERE crd_add_com.cards_order = 0
	AND crd_add_com.model_section_id = 'ac1031fa-5e4d-1452-815e-51cdc32d002f'
);

UPDATE model_cards_wrapper_additions crd_add 
SET after_card_wrapper_addition_id = (
	SELECT cards_wrappers_additions_common_id 
	FROM masterschema.model_sections_cards_wrappers_additions_common crd_add_com 
	JOIN model_cards_wrapper_additions crd_add
	ON crd_add_com.cards_wrappers_additions_common_id = crd_add.id
	WHERE crd_add_com.cards_order = 0
	AND crd_add_com.model_section_id = 'ac1031fa-5e4d-1452-815e-51cdc32d002f') 
WHERE id = (
	SELECT cards_wrappers_additions_common_id 
	FROM masterschema.model_sections_cards_wrappers_additions_common crd_add_com 
	JOIN model_cards_wrapper_additions crd_add
	ON crd_add_com.cards_wrappers_additions_common_id = crd_add.id
	WHERE crd_add_com.cards_order = 1
	AND crd_add_com.model_section_id = 'ac1031fa-5e4d-1452-815e-51cdc32d002f'
);

SELECT * FROM model_cards_wrapper_additions WHERE section_id = 'ac1031fa-5e4d-1452-815e-51cdc32d002f';
SELECT * FROM masterschema.model_sections_cards_wrappers_additions_common WHERE model_section_id = 'ac1031fa-5e4d-1452-815e-51cdc32d002f';

-- Model Card Wrappers withput state control
INSERT INTO model_cards_wrapper_additions 
  (id,
  scope,
  status,
  adder_c1id,
  card_wrapper_id,
  section_id)
SELECT 
  id,
  scope,
  status,
  adder_c1id,
  card_wrapper_id,
  section_id
FROM masterschema.model_cards_wrapper_additions;

--------------------------------------------------------
-- DROP masterschema
---------------------------------------------------------
DROP SCHEMA masterschema CASCADE;
DROP EXTENSION postgres_fdw CASCADE;
