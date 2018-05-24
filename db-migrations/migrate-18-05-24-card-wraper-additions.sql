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


/* remove duplicate cards in sections (there are two...) */
CREATE TABLE masterschema.model_sections_cards_wrappers_temp (LIKE masterschema.model_sections_cards_wrappers);

INSERT INTO masterschema.model_sections_cards_wrappers_temp
  (model_section_id, cards_wrappers_id, cards_order)
SELECT 
  DISTINCT ON (model_section_id, cards_wrappers_id) model_section_id,
  cards_wrappers_id,
  cards_order
FROM masterschema.model_sections_cards_wrappers; 

DROP TABLE masterschema.model_sections_cards_wrappers;
ALTER TABLE masterschema.model_sections_cards_wrappers_temp RENAME TO model_sections_cards_wrappers;   

-- Model Card Wrappers withput state control
INSERT INTO model_cards_wrapper_additions 
  (id, 
  card_wrapper_id, 
  section_id, 
  adder_c1id, 
  status, 
  scope)
SELECT 
  md5(random()::text || clock_timestamp()::text)::uuid, 
  sec_crd_wrp.cards_wrappers_id, 
  sec_crd_wrp.model_section_id, 
  crd_wrp.creator_c1id,
  'VALID', 
  'COMMON'
FROM masterschema.model_sections_cards_wrappers AS sec_crd_wrp 
JOIN masterschema.model_cards_wrapper AS crd_wrp 
ON sec_crd_wrp.cards_wrappers_id = crd_wrp.id;


/* store order of common cards (all current cards) */
INSERT INTO model_sections_cards_wrappers_additions_common 
  (cards_wrappers_additions_common_id, model_section_id, cards_order)
SELECT 
  crd_wrp_add.id,
  sec_crd_wrp.model_section_id, 
  sec_crd_wrp.cards_order
FROM masterschema.model_sections_cards_wrappers AS sec_crd_wrp 
JOIN model_cards_wrapper_additions AS crd_wrp_add
ON (crd_wrp_add.card_wrapper_id = sec_crd_wrp.cards_wrappers_id AND crd_wrp_add.section_id = sec_crd_wrp.model_section_id);

--------------------------------------------------------
-- DROP masterschema
---------------------------------------------------------
DROP SCHEMA masterschema CASCADE;
DROP EXTENSION postgres_fdw CASCADE;
