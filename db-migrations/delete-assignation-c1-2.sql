DECLARE @I;
SET @I = 'ac111f3e-6120-1d63-8161-238794d20036';

DELETE FROM notifications WHERE activity_id IN (SELECT id FROM activity WHERE assignation_id = '@I');
DELETE FROM activity WHERE assignation_id = '@I';
DELETE FROM assignations_also_in_initiatives WHERE assignation_id = '@I';
DELETE FROM bills WHERE assignation_id = '@I';
DELETE FROM receivers WHERE assignation_id = '@I';
DELETE FROM assignations WHERE id = '@I';




