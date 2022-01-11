
CREATE OR REPLACE FUNCTION decrease_priority_func()
    RETURNS TRIGGER AS $$
BEGIN
    UPDATE applications
    SET priority = priority - 1
    WHERE std_id = OLD.std_id and priority > OLD.priority;
    RETURN OLD;
END;
$$ language 'plpgsql';


DROP TRIGGER IF EXISTS decrease_priority ON applications;

CREATE TRIGGER decrease_priority
    AFTER DELETE ON applications
    FOR EACH ROW EXECUTE PROCEDURE decrease_priority_func();
