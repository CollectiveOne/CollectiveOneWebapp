-- CONTRIBUIDORES ac1066fe-6036-15b5-8160-37676bf30019
-- DELETE THE TWO ASSIGNATION TO CONTRIBUIDORES
DELETE FROM activity WHERE initiative_transfer_id IN (SELECT id FROM initiative_transfers WHERE to_id = 'ac1066fe-6036-15b5-8160-37676bf30019');
DELETE FROM activity_initiative_transfers WHERE initiative_transfers_id IN (SELECT id FROM initiative_transfers WHERE to_id = 'ac1066fe-6036-15b5-8160-37676bf30019');
DELETE FROM initiative_transfers WHERE to_id = 'ac1066fe-6036-15b5-8160-37676bf30019';

-- DELETE ASSIGNATION IN CONTRIBUIDORES TO ANALA
DELETE FROM activity WHERE assignation_id = 'ac1066fe-6036-15b5-8160-376a0881006a';
DELETE FROM assignations_also_in_initiatives WHERE assignation_id = 'ac1066fe-6036-15b5-8160-376a0881006a';
DELETE FROM bills WHERE assignation_id = 'ac1066fe-6036-15b5-8160-376a0881006a';
DELETE FROM receivers WHERE assignation_id = 'ac1066fe-6036-15b5-8160-376a0881006a';
DELETE FROM assignations WHERE id = 'ac1066fe-6036-15b5-8160-376a0881006a';

-- MANUALLY DELETE CONTRIBUTORS INITIATIVE FROM WEBAPP

-- DELETE TOKENS FROM GOBERNANZA
UPDATE token_holders SET tokens = 0 WHERE holder_id = 'ac11534a-5d60-1195-815d-6100ee70003b';

-- DELETE TRANSFER FROM CONTRIBUTORS TO GOBERNANZA (MADE AUTOMATICALLY WHEN DELETING)
DELETE FROM notifications WHERE activity_id IN (SELECT id FROM activity WHERE initiative_transfer_id IN (SELECT id FROM initiative_transfers WHERE from_id = 'ac1066fe-6036-15b5-8160-37676bf30019' AND to_id = 'ac11534a-5d60-1195-815d-6100ee70003b'));
DELETE FROM activity WHERE initiative_transfer_id IN (SELECT id FROM initiative_transfers WHERE from_id = 'ac1066fe-6036-15b5-8160-37676bf30019' AND to_id = 'ac11534a-5d60-1195-815d-6100ee70003b');
DELETE FROM initiative_transfers WHERE from_id = 'ac1066fe-6036-15b5-8160-37676bf30019' AND to_id = 'ac11534a-5d60-1195-815d-6100ee70003b';

-- DELETE TRANSFER FROM OPERATIVA TO GOBERNANZA
DELETE FROM notifications WHERE activity_id IN (SELECT id FROM activity WHERE initiative_transfer_id IN (SELECT id FROM initiative_transfers WHERE from_id = 'ac11534a-5d60-1195-815d-612cd000008b' AND to_id = 'ac11534a-5d60-1195-815d-6100ee70003b'));
DELETE FROM activity WHERE initiative_transfer_id IN (SELECT id FROM initiative_transfers WHERE from_id = 'ac11534a-5d60-1195-815d-612cd000008b' AND to_id = 'ac11534a-5d60-1195-815d-6100ee70003b');
DELETE FROM initiative_transfers WHERE from_id = 'ac11534a-5d60-1195-815d-612cd000008b' AND to_id = 'ac11534a-5d60-1195-815d-6100ee70003b';

-- CHANGE ASSIGNATION TO ANA
-- ANA ID "ac11534a-5d60-1195-815d-64d8bb2900f0"
-- ANALU ID "ac10cdc2-5eb9-1391-815e-bde843e80001"

-- SELECT * FROM members WHERE initiative_id = 'ac11534a-5d60-1195-815d-612cd000008b' AND user_c1id = 'ac11534a-5d60-1195-815d-64d8bb2900f0';
-- ANA MEMBER ID TO OPERATIVA "ac1153fa-6031-1f63-8160-32db6ac50016"
-- SELECT * FROM member_transfers WHERE member_id = 'ac1153fa-6031-1f63-8160-32db6ac50016';
-- ANA MEMBER TRANSFER ID "ac10e7b2-607a-1620-8160-7ece14750042"
UPDATE member_transfers SET value = 288 WHERE id = 'ac10e7b2-607a-1620-8160-7ece14750042';

-- SELECT * FROM assignations WHERE initiative_id = 'ac11534a-5d60-1195-815d-612cd000008b';
-- ANA ASSIGNATION ID = "ac10e7b2-607a-1620-8160-7ece1443003f"
-- ANA BILL ID = "ac10e7b2-607a-1620-8160-7ece145f0041"
-- SELECT * FROM bills WHERE assignation_id = 'ac10e7b2-607a-1620-8160-7ece1443003f';
-- SELECT * FROM app_users WHERE c1Id IN (SELECT user_c1id FROM members WHERE initiative_id = 'ac11534a-5d60-1195-815d-612cd000008b');
UPDATE bills SET value = 288 WHERE id = 'ac10e7b2-607a-1620-8160-7ece145f0041';

-- CREATE TOKENS FOR ANALU IN OPERATIVE
UPDATE token_holders SET tokens = 288 WHERE holder_id = 'ac11534a-5d60-1195-815d-612cd000008b';

-- MANUALLY MAKE A DIRECT TRANSFER OF 288 tokens TO ANALU

-- THEN SCALE PEER-REVIEW
-- SELECT * FROM assignations WHERE id = 'ac10e7b2-607a-1620-8160-7ecf143d005c';
-- SELECT * FROM bills WHERE assignation_id = 'ac10e7b2-607a-1620-8160-7ecf143d005c';
-- PEER REVIEW bill ID "ac10e7b2-607a-1620-8160-7ecf14930063"
UPDATE bills SET value = 144 WHERE id = 'ac10e7b2-607a-1620-8160-7ecf14930063';

-- SCALE ALL RECEIVERS
-- SELECT * FROM member_transfers WHERE member_id IN (SELECT id FROM members WHERE initiative_id = 'ac11534a-5d60-1195-815d-612cd000008b');
-- SELECT user_c1id FROM members WHERE id IN (SELECT member_id FROM member_transfers WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1fd70006');

-- SANTIAGE MEMBER TRANSFER ID "ac11c8da-60b9-1c85-8160-bc0a1dd20000" C1ID = "ac101306-5d7e-1082-815d-80b6ed2c00e6"
-- EMILY MEMBER TRANSFER ID "ac11c8da-60b9-1c85-8160-bc0a1f0d0003" c1id = "ac1153fa-6031-1f63-8160-35c6a8b50086"
-- MIRIMA MEMBER TRANSFER ID "ac11c8da-60b9-1c85-8160-bc0a1f4a0004" c1id = "ac119fe2-5da2-1e75-815d-a71185260000"
-- PEPO MEMBER TRANSFER ID "ac11c8da-60b9-1c85-8160-bc0a1f840005" c1id = "ac12d946-5d60-1f44-815d-60bfa34c0000"
-- ALICIA MEMBER TRANSFER ID "ac11c8da-60b9-1c85-8160-bc0a1fd70006" c1id = "ac12fa3a-5d73-191b-815d-74e4b8200000"

SELECT * FROM token_holders WHERE token_type_id = 'ac11534a-5d60-1195-815d-60c9b3de0007' AND holder_id IN (SELECT user_c1id FROM members WHERE initiative_id = 'ac11534a-5d60-1195-815d-612cd000008b');
-- SELECT * FROM member_transfers WHERE member_id IN (SELECT id FROM members WHERE user_c1id = 'ac101306-5d7e-1082-815d-80b6ed2c00e6');

/* extend the amount of tokens held by all peer review receivers */
UPDATE token_holders SET tokens = ((SELECT tokens FROM token_holders WHERE holder_id = 'ac101306-5d7e-1082-815d-80b6ed2c00e6' AND token_type_id = 'ac11534a-5d60-1195-815d-60c9b3de0007') + ((SELECT value FROM member_transfers WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1dd20000') * 2.0 / 3.0)) WHERE holder_id = 'ac101306-5d7e-1082-815d-80b6ed2c00e6';
UPDATE token_holders SET tokens = ((SELECT tokens FROM token_holders WHERE holder_id = 'ac1153fa-6031-1f63-8160-35c6a8b50086' AND token_type_id = 'ac11534a-5d60-1195-815d-60c9b3de0007') + ((SELECT value FROM member_transfers WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1f0d0003') * 2.0 / 3.0)) WHERE holder_id = 'ac1153fa-6031-1f63-8160-35c6a8b50086';
UPDATE token_holders SET tokens = ((SELECT tokens FROM token_holders WHERE holder_id = 'ac119fe2-5da2-1e75-815d-a71185260000' AND token_type_id = 'ac11534a-5d60-1195-815d-60c9b3de0007') + ((SELECT value FROM member_transfers WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1f4a0004') * 2.0 / 3.0)) WHERE holder_id = 'ac119fe2-5da2-1e75-815d-a71185260000';
UPDATE token_holders SET tokens = ((SELECT tokens FROM token_holders WHERE holder_id = 'ac12d946-5d60-1f44-815d-60bfa34c0000' AND token_type_id = 'ac11534a-5d60-1195-815d-60c9b3de0007') + ((SELECT value FROM member_transfers WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1f840005') * 2.0 / 3.0)) WHERE holder_id = 'ac12d946-5d60-1f44-815d-60bfa34c0000';
UPDATE token_holders SET tokens = ((SELECT tokens FROM token_holders WHERE holder_id = 'ac12fa3a-5d73-191b-815d-74e4b8200000' AND token_type_id = 'ac11534a-5d60-1195-815d-60c9b3de0007') + ((SELECT value FROM member_transfers WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1fd70006') * 2.0 / 3.0)) WHERE holder_id = 'ac12fa3a-5d73-191b-815d-74e4b8200000';

/* cahnge the amount of token transfers sent to peer-review receivers */
UPDATE member_transfers SET value = ((SELECT value FROM member_transfers WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1dd20000') * 5.0 / 3.0 ) WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1dd20000';
UPDATE member_transfers SET value = ((SELECT value FROM member_transfers WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1f0d0003') * 5.0 / 3.0 ) WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1f0d0003';
UPDATE member_transfers SET value = ((SELECT value FROM member_transfers WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1f4a0004') * 5.0 / 3.0 ) WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1f4a0004';
UPDATE member_transfers SET value = ((SELECT value FROM member_transfers WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1f840005') * 5.0 / 3.0 ) WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1f840005';
UPDATE member_transfers SET value = ((SELECT value FROM member_transfers WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1fd70006') * 5.0 / 3.0 ) WHERE id = 'ac11c8da-60b9-1c85-8160-bc0a1fd70006';

-- CHECK
-- SELECT SUM(tokens) FROM token_holders WHERE token_type_id = 'ac11534a-5d60-1195-815d-60c9b3de0007';