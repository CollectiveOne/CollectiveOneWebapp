SELECT * from members WHERE user_c1id = 'ac12d946-5d60-1f44-815d-60bfa34c0000' AND initiative_id='ac119496-5e3e-1db5-815e-3f192a890001'
SELECT * from members WHERE user_c1id IN (SELECT user_c1id from receivers WHERE assignation_id = 'ac11499a-5e4c-1ebd-815e-4ca0ca3f0010') AND initiative_id='ac119496-5e3e-1db5-815e-3f192a890001'

SELECT * FROM assignations WHERE initiative_id = 'ac119496-5e3e-1db5-815e-3f192a890001'

SELECT * from member_transfers WHERE member_id IN 
( SELECT id from members 
WHERE user_c1id IN (SELECT user_c1id from receivers WHERE assignation_id = 'ac11499a-5e4c-1ebd-815e-4ca0ca3f0010') 
AND initiative_id='ac119496-5e3e-1db5-815e-3f192a890001')


DELETE FROM activity WHERE assignation_id = 'ac11499a-5e4c-1ebd-815e-4ca0ca3f0010';
DELETE FROM bills WHERE assignation_id = 'ac11499a-5e4c-1ebd-815e-4ca0ca3f0010';
DELETE FROM receivers WHERE assignation_id = 'ac11499a-5e4c-1ebd-815e-4ca0ca3f0010';
DELETE FROM assignations WHERE id = 'ac11499a-5e4c-1ebd-815e-4ca0ca3f0010';

DELETE FROM member_transfers WHERE member_id = 'ac119496-5e3e-1db5-815e-3f192bb50003';
DELETE FROM token_holders WHERE holder_id = 'ac12d946-5d60-1f44-815d-60bfa34c0000' AND token_type_id = 'ac119496-5e3e-1db5-815e-3f192e660006';
DELETE FROM activity WHERE initiative_id='ac119496-5e3e-1db5-815e-3f192a890001' AND type != 'INITIATIVE_CREATED';
DELETE FROM token_mints WHERE token_id = 'ac119496-5e3e-1db5-815e-3f192e660006';
