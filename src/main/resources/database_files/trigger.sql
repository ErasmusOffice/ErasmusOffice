
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
    RETURN OLD;
END;
$$ language 'plpgsql';

DROP TRIGGER IF EXISTS decrease_university_applicant_count ON applications;

CREATE TRIGGER decrease_university_applicant_count
    AFTER DELETE ON applications
    FOR EACH ROW EXECUTE PROCEDURE decrease_university_applicant_count_func();


CREATE OR REPLACE FUNCTION check_count_of_application_func()
RETURNS TRIGGER AS $$
    DECLARE
        count INTEGER;
    BEGIN
        SELECT COUNT(*) INTO count FROM applications WHERE std_id = NEW.std_id;
        IF count = 5 THEN
            RAISE EXCEPTION '5ten fazla basvuru olamaz';
        ELSE
            RETURN NEW;
        END IF;
    END;
    $$language 'plpgsql';
CREATE TRIGGER check_count_of_application
    BEFORE INSERT ON applications
    FOR EACH ROW EXECUTE PROCEDURE check_count_of_application_func();

CREATE OR REPLACE FUNCTION setConsultantFunc() RETURNS TRIGGER AS $$
DECLARE
    const_id INTEGER;
    count INTEGER;
BEGIN
    IF old.result <> new.result THEN
        select consultant_id into const_id from ((select distinct consultant_id from consultants)
        except
        (SELECT consultant_id FROM students GROUP BY consultant_id HAVING COUNT (*) >= 3)) as a
        LIMIT 1;
        IF const_id IS NOT NULL THEN
            UPDATE students SET consultant_id = const_id WHERE std_id = NEW.std_id;
        ELSE
            UPDATE students SET consultant_id = NULL WHERE std_id = NEW.std_id;
            RAISE EXCEPTION 'Bosta akademisyen bulunmamaktadir';
        END IF;
    ELSE
        IF old.result = true THEN
            UPDATE universities SET fall_applicant_count = fall_applicant_count - 1 WHERE old
                .target_uni_id = universities.uni_id and old.term = 'fall';
            UPDATE universities SET spring_applicant_count = spring_applicant_count - 1 WHERE old
                .target_uni_id = universities.uni_id and old.term = 'spring';
            UPDATE universities SET fall_applicant_count = fall_applicant_count - 1,
                                    spring_applicant_count = spring_applicant_count - 1 WHERE old
                .target_uni_id = universities.uni_id and old.term = 'full_year';
            UPDATE universities SET fall_applicant_count = fall_applicant_count + 1 WHERE new
                .target_uni_id = universities.uni_id and new.term = 'fall';
            UPDATE universities SET spring_applicant_count = spring_applicant_count + 1 WHERE new
                .target_uni_id = universities.uni_id and new.term = 'spring';
            UPDATE universities SET fall_applicant_count = fall_applicant_count + 1,
                                    spring_applicant_count = spring_applicant_count + 1 WHERE new
                .target_uni_id = universities.uni_id and new.term = 'full_year';
        END IF;
    END IF;
    RETURN NEW;
END;
$$
    LANGUAGE 'plpgsql';

CREATE TRIGGER senConsultant
AFTER UPDATE ON applications
FOR EACH ROW EXECUTE PROCEDURE setConsultantFunc();
