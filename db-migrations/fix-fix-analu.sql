-- ANALU ID 'ac10cdc2-5eb9-1391-815e-bde843e80001'
-- SELECT * FROM app_users WHERE c1Id IN (SELECT user_c1id FROM members WHERE initiative_id = 'ac11534a-5d60-1195-815d-612cd000008b');
-- SELECT * FROM members WHERE user_c1Id = 'ac10cdc2-5eb9-1391-815e-bde843e80001';
-- SELECT * FROM member_transfers WHERE member_id IN (SELECT id FROM members WHERE user_c1Id = 'ac10cdc2-5eb9-1391-815e-bde843e80001');

UPDATE member_transfers SET value = 0 WHERE id = 'ac1066fe-6036-15b5-8160-376a08ca006e';
-- SELECT * FROM token_holders WHERE holder_id = 'ac10cdc2-5eb9-1391-815e-bde843e80001' AND token_type_id = 'ac11534a-5d60-1195-815d-60c9b3de0007';
UPDATE token_holders SET tokens = 288 WHERE holder_id = 'ac10cdc2-5eb9-1391-815e-bde843e80001' AND token_type_id = 'ac11534a-5d60-1195-815d-60c9b3de0007';