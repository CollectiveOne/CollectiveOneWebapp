---------------------------------------------------------
-- Make a partial dump of the database with the following command
-- /usr/bin/pg_dump --host ec2-54-83-11-247.compute-1.amazonaws.com --port 5432 --username "pmiyqtnfrstmst" --password --format plain --data-only --disable-triggers --exclude-table initiatives --exclude-table app_users --exclude-table users_push_subscriptions --verbose --file "/home/pepo/workspace/c1data/bk-097-limited" "d20ec3jfi8l031"
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

-- message threads were also removed, this force manual migration of some tables

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

--------------------------------------------------------
-- DROP masterschema
---------------------------------------------------------
DROP SCHEMA masterschema CASCADE;
DROP EXTENSION postgres_fdw CASCADE;
