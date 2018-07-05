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

-- message threads were also removed, this force manual migration of some tables

INSERT INTO public.initiatives (
  id,
  status,
  creator_c1id,
  governance_id,
  meta_id)
SELECT 
  id,
  status, 
  creator_c1id, 
  governance_id, 
  meta_id 
FROM masterschema.initiatives;

INSERT INTO model_sections (
  id,
  description,
  title,
  initiative_id)
SELECT 
  id, 
  description, 
  title, 
  initiative_id 
FROM masterschema.model_sections;

INSERT INTO model_cards_wrapper (
  id,
  creation_date,
  last_edited,
  card_id,
  creator_c1id,
  initiative_id)
SELECT 
  id, 
  creation_date, 
  last_edited, 
  card_id, 
  creator_c1id, 
  initiative_id 
FROM masterschema.model_cards_wrapper;

INSERT INTO messages (
  id,
  text,
  "timestamp",
  author_c1id)
SELECT 
  id, 
  text, 
  "timestamp", 
  author_c1id 
FROM masterschema.messages;

INSERT INTO model_cards_wrapper_additions (
  id,
  scope,
  status,
  adder_c1id,
  after_element_id,
  before_element_id,
  card_wrapper_id,
  section_id)
SELECT 
  id, 
  scope, 
  status, 
  adder_c1id, 
  after_card_wrapper_addition_id, 
  before_card_wrapper_addition_id, 
  card_wrapper_id, 
  section_id 
FROM masterschema.model_cards_wrapper_additions;


-- now the important thing: new Entity model_subsections
INSERT INTO model_subsections 
  (id, 
  parent_section_id,
  section_id,
  adder_c1id,
  status,
  scope)
SELECT 
  md5(random()::text || clock_timestamp()::text)::uuid, 
  sec_subsec.model_section_id, 
  sec_subsec.subsections_id,
  'ac12d946-5d60-1f44-815d-60bfa34c0000',
  'VALID', 
  'COMMON'
FROM masterschema.model_sections_subsections AS sec_subsec;

-- there are sections (top model ones) which are not in the model_sections_subsections table 
-- so migrate them leaving the parent_section_id as null
INSERT INTO model_subsections 
  (id, 
  section_id,
  adder_c1id,
  status,
  scope)
SELECT 
  md5(random()::text || clock_timestamp()::text)::uuid, 
  sec.id,
  'ac12d946-5d60-1f44-815d-60bfa34c0000',
  'VALID', 
  'COMMON'
FROM masterschema.model_sections AS sec
WHERE sec.id NOT IN (
SELECT subsections_id FROM masterschema.model_sections_subsections);

--------------------------------------------------------
-- DROP masterschema
---------------------------------------------------------
DROP SCHEMA masterschema CASCADE;
DROP EXTENSION postgres_fdw CASCADE;