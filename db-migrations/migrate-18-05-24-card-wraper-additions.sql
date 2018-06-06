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
