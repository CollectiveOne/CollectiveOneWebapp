SELECT COUNT(*) FROM activity act1 
WHERE type = 'MODEL_SECTION_CREATED'
AND (
  SELECT COUNT(*) FROM activity act2
  WHERE act1.model_section_id = act2.model_section_id  
  AND act2.type = 'MODEL_SECTION_CREATED'
) > 1;

DELETE FROM activity act1
USING activity act2
WHERE act1.type = 'MODEL_SECTION_CREATED'
AND (
  act1.id != act2.id
  AND act2.type = 'MODEL_SECTION_CREATED'
  AND act1.model_section_id = act2.model_section_id
);

UPDATE model_sections sec
SET creation_date = ( 
  SELECT timestamp FROM activity act 
  WHERE type = 'MODEL_SECTION_CREATED' 
  AND sec.id = act.model_section_id
);

