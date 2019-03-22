---------------------------------------------------------
-- Dump all data on a local DB created with the current version of the webapp
-- Make a partial dump of the database with the following command
-- /usr/bin/pg_dump --host localhost --port 5432 --username "postgres" --password --format plain --data-only --disable-triggers --exclude-table app_users --exclude-table users_push_subscriptions --verbose --file "/home/pepo/workspace/c1data/bk-098-limited" "c1db-bk"
-- fill a clean DB with it using \i
-- THEN run this script to fill all the app_users table
-- THEN make a CUSTOM dump of the contents with this command
-- /usr/bin/pg_dump --cluster 9.6/main --host localhost --port 5432 --username "postgres" --no-password  --format custom --blobs --no-privileges --no-tablespaces --verbose --file "/home/pepo/workspace/c1data/c1-migration-097-custom" "c1db"
-- THEN delete all tables online and restore using this commmand
-- /usr/bin/pg_restore --cluster 9.6/main --host ec2-54-75-239-237.eu-west-1.compute.amazonaws.com --port 5432 --username "sqiowmccjkebrr" --dbname "d4j1dmpkvm9qvu" --no-password --no-owner  --verbose "/home/pepo/workspace/c1data/c1-migration-097-custom"
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

-- online status not present anymore
INSERT INTO public.app_users (
  c1id,
  email,
  email_notifications_enabled,
  last_seen)
SELECT 
  c1id,
  email,
  email_notifications_enabled,
  last_seen
FROM masterschema.app_users;

-- notifications subscription must comply with presets

DELETE FROM notifications WHERE subscriber_id IN (SELECT id FROM subscribers WHERE type = 'SECTION');
DELETE FROM subscribers WHERE type = 'SECTION';

UPDATE subscribers SET 
in_app_config = 'ALL_EVENTS',
push_config = 'ONLY_MENTIONS',
emails_now_config = 'ONLY_MENTIONS',
emails_summary_config = 'ALL_EVENTS',
emails_summary_period_config = 'WEEKLY',
inherit_config = 'CUSTOM'
WHERE type = 'COLLECTIVEONE';

UPDATE subscribers SET 
in_app_config = null,
push_config = null,
emails_now_config = null,
emails_summary_config = null,
emails_summary_period_config = null,
inherit_config = 'INHERIT'
WHERE type = 'INITIATIVE';

--------------------------------------------------------
-- DROP masterschema
---------------------------------------------------------
DROP SCHEMA masterschema CASCADE;
DROP EXTENSION postgres_fdw CASCADE;
