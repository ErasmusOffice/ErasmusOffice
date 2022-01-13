
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

CREATE OR REPLACE FUNCTION decrease_university_applicant_count_func()
    RETURNS TRIGGER AS $$
BEGIN
    IF OLD.result = true THEN
        IF OLD.term = 'fall' THEN
            UPDATE universities
            SET fall_applicant_count = fall_applicant_count - 1
            WHERE uni_id = OLD.target_uni_id;
        ELSIF OLD.term = 'spring' THEN
            UPDATE universities
            SET spring_applicant_count = spring_applicant_count - 1
            WHERE uni_id = OLD.target_uni_id;
        ELSE
            UPDATE universities
            SET spring_applicant_count = spring_applicant_count - 1,
                fall_applicant_count = fall_applicant_count - 1
            WHERE uni_id = OLD.target_uni_id;
        END IF;
    END IF;

END;
$$ language 'plpgsql';

DROP TRIGGER IF EXISTS decrease_university_applicant_count ON applications;

CREATE TRIGGER decrease_university_applicant_count
    AFTER DELETE ON applications
    FOR EACH ROW EXECUTE PROCEDURE decrease_university_applicant_count_func();
