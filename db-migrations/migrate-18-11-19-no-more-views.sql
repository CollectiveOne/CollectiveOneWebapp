---------------------------------------------------------
-- Make a partial dump of the database with the command below and fill a clean DB with it using \i,  migration script
-- /usr/bin/pg_dump --host ec2-54-83-11-247.compute-1.amazonaws.com --port 5432 --username "pmiyqtnfrstmst" --password --format plain --data-only --disable-triggers --exclude-table model_cards_wrapper --exclude-table model_views --exclude-table activity --exclude-table initiatives_model_views --exclude-table initiatives_model_views_trash --exclude-table model_views_sections --exclude-table model_views_sections_trash --exclude-table initiatives_other_tokens --exclude-table likes --verbose --file "/home/pepo/workspace/c1data/bk-062-base" "d20ec3jfi8l031"
-- THEN run the branch code to create the top level sections, 
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
CREATE SERVER master FOREIGN DATA WRAPPER postgres_fdw OPTIONS (host 'ec2-54-83-11-247.compute-1.amazonaws.com', dbname 'd20ec3jfi8l031', port '5432');
CREATE USER MAPPING FOR postgres SERVER master OPTIONS (user 'pmiyqtnfrstmst', password '58433182ce1b5042f8fecd6e26a89c2a0b923fd4cd519ff6a048eea13b113560');
IMPORT FOREIGN SCHEMA public FROM SERVER master INTO masterschema;


-- Model Card Wrappers withput state control
INSERT INTO model_cards_wrapper 
  (id, card_id, initiative_id, message_thread_id)
SELECT 
  id, card_id, initiative_id, message_thread_id
FROM masterschema.model_cards_wrapper;

-- Convert views and theis subsections into sections and subsections
INSERT INTO model_sections
  (id, title, description, initiative_id, message_thread_id)
SELECT 
  id, title, description, initiative_id, message_thread_id
FROM masterschema.model_views;

INSERT INTO model_sections_subsections
  (model_section_id, subsections_id, subsections_order)
SELECT 
  model_view_id, sections_id, sections_order
FROM masterschema.model_views_sections;

-- Set initiative views as top section subsections
INSERT INTO model_sections_subsections 
  (model_section_id, subsections_id, subsections_order)
SELECT ini.top_model_section_id, viw.model_views_id, viw.views_order FROM initiatives ini
INNER JOIN masterschema.initiatives_model_views viw ON ini.id = viw.initiative_id;

-- Change Message Threads context
UPDATE message_threads SET context_type = 'MODEL_SECTION' WHERE context_type = 'MODEL_VIEW';

-- Fill activity table 
INSERT INTO activity
  (id, old_driver, old_name, timestamp, type, assignation_id, from_section_id, initiative_id, initiative_transfer_id, message_id, mint_id, model_card_wrapper_id, model_section_id, on_section_id, sub_initiative_id, token_type_id, trigger_user_c1id)  
SELECT 
  id, old_driver, old_name, timestamp, type, assignation_id, from_section_id, initiative_id, initiative_transfer_id, message_id, mint_id, model_card_wrapper_id, model_section_id, on_section_id, sub_initiative_id, token_type_id, trigger_user_c1id
FROM masterschema.activity
WHERE model_view_id IS NULL AND on_view_id IS NULL;

INSERT INTO activity
  (id, timestamp, type, initiative_id, trigger_user_c1id, model_section_id)  
SELECT 
  id, timestamp, type, initiative_id, trigger_user_c1id, model_view_id
FROM masterschema.activity
WHERE model_view_id IS NOT NULL;

INSERT INTO activity
  (id, timestamp, type, initiative_id, trigger_user_c1id, on_section_id)  
SELECT 
  id, timestamp, type, initiative_id, trigger_user_c1id, on_view_id
FROM masterschema.activity
WHERE on_view_id IS NOT NULL;

-- UPDATE ACTIVITY TYPE
UPDATE activity SET type = 'MODEL_SECTION_CREATED' WHERE type = 'MODEL_VIEW_CREATED';
UPDATE activity SET type = 'MODEL_SECTION_EDITED' WHERE type = 'MODEL_VIEW_EDITED';
UPDATE activity SET type = 'MODEL_SECTION_DELETED' WHERE type = 'MODEL_VIEW_DELETED';
UPDATE activity SET type = 'MODEL_SECTION_MOVED' WHERE type = 'MODEL_VIEW_MOVED';


--------------------------------------------------------
-- DROP masterschema
---------------------------------------------------------
DROP SCHEMA masterschema CASCADE;
DROP EXTENSION postgres_fdw CASCADE;
