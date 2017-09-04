-- RESET receivers
UPDATE receivers SET transfer_id = null WHERE assignation_id='ac101cc2-5d9a-1054-815d-9d3e30560017';
UPDATE receivers SET state = 'PENDING' WHERE assignation_id='ac101cc2-5d9a-1054-815d-9d3e30560017';

-- REMOVE member transfers
DELETE FROM member_transfers WHERE member_id != 'ac1111b6-5d79-1784-815d-7e44e3d3000d' AND member_id IN (SELECT id FROM members WHERE initiative_id='ac11534a-5d60-1195-815d-612e4d210095');
DELETE FROM member_transfers WHERE member_id = 'ac1111b6-5d79-1784-815d-7e44e3d3000d' AND value = 0 AND member_id IN (SELECT id FROM members WHERE initiative_id='ac11534a-5d60-1195-815d-612e4d210095');

-- REMOVE erroneously transferred tokens
-- Andrea
UPDATE token_holders SET tokens = 242.4370261 WHERE holder_id = 'ac11534a-5d60-1195-815d-60c8a8180000';

-- Alicia
UPDATE token_holders SET tokens = 129.1071652 WHERE holder_id = 'ac12fa3a-5d73-191b-815d-74e4b8200000';

-- Cristobal
UPDATE token_holders SET tokens = 0 WHERE holder_id = 'ac10d68e-5d83-1046-815d-842797480006';

-- Iniciativa
UPDATE token_holders SET tokens = 324 WHERE holder_id = 'ac11534a-5d60-1195-815d-612e4d210095';

-- REOPEN assignation
UPDATE assignations SET state = 'OPEN' WHERE id = 'ac101cc2-5d9a-1054-815d-9d3e30560017';

