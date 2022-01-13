--DROP TYPE IF EXISTS application_type CASCADE;

--drop function  manager_get_applications(student_id_input int);
CREATE OR REPLACE FUNCTION manager_get_applications(student_id_input int)
    RETURNS table(application_id int, std_id int, name varchar(50), term varchar(20), result bool)
AS
$$
BEGIN
    return query
        SELECT a.application_id,
               a.std_id,
               u.name,
               a.term,
               a.result
        FROM applications AS a
                 INNER JOIN universities u ON a.target_uni_id = u.uni_id
        WHERE a.std_id = student_id_input;
END;
$$
    LANGUAGE 'plpgsql';

SELECT * from manager_get_applications(3);

DROP TYPE IF EXISTS placement_result_type CASCADE ;
CREATE TYPE placement_result_type AS (std_id INTEGER, target_uni_id INTEGER, term
VARCHAR(20), app_id INTEGER);

--DROP FUNCTION place_students();
CREATE OR REPLACE FUNCTION place_students()
RETURNS TABLE(std_id INTEGER, target_uni_id INTEGER, term VARCHAR(20), app_id INTEGER)
AS $$
DECLARE
    application_curs CURSOR FOR SELECT
                                    s.std_id,
                                    a.target_uni_id,
                                    a.term,
                                    a.application_id
                                FROM
                                    applications AS a
                                        INNER JOIN students AS s ON a.std_id = s.std_id
                                WHERE
                                    a.std_id NOT IN (SELECT b.std_id FROM applications b WHERE
                                                                                             result =
                                                                                         true)
                                ORDER BY
                                    (100 / 3 * (gpa - 2.5) + 0.5 * exam_result) DESC,
                                    priority;
    placement_result PLACEMENT_RESULT_TYPE;
    uni_fall_count INTEGER;
    uni_spring_count INTEGER;
    uni_capacity INTEGER;
    old_std_id INTEGER;
--     i INTEGER;
--     placed_students PLACEMENT_RESULT_TYPE[];
BEGIN
    CREATE TEMP TABLE IF NOT EXISTS placed_students(
        std_id INTEGER,
        target_uni_id INTEGER,
        term VARCHAR(20),
        app_id INTEGER
    ) ON COMMIT DROP;
--     i = 1;
    open application_curs;
    FETCH NEXT FROM application_curs INTO placement_result;
    WHILE placement_result IS NOT NULL LOOP
        RAISE NOTICE 'Selected student id is %', placement_result.std_id;
        old_std_id = placement_result.std_id;
        CASE placement_result.term
            WHEN 'fall' THEN
                SELECT fall_applicant_count, capacity INTO uni_fall_count, uni_capacity FROM
                                                                                             universities WHERE uni_id = placement_result.target_uni_id;
                IF uni_fall_count < uni_capacity THEN
                    RAISE NOTICE 'TRUE BRANCH: uni_fall_count: %, uni_capacity: %', uni_fall_count,
                        uni_capacity;
                    UPDATE universities SET fall_applicant_count = fall_applicant_count + 1 WHERE
                                                                                                  uni_id = placement_result.target_uni_id;
                    UPDATE applications SET result = true WHERE application_id = placement_result
                        .app_id;
                    INSERT INTO placed_students VALUES(placement_result.std_id, placement_result
                        .target_uni_id, placement_result.term, placement_result.app_id);
--                     placed_students[i] = placement_result;
--                     i = i + 1;
                    FETCH NEXT FROM application_curs INTO placement_result;
                    WHILE placement_result.std_id = old_std_id LOOP
                            FETCH NEXT FROM application_curs INTO placement_result;
                    END LOOP;
                ELSE
                    RAISE NOTICE 'FALSE BRANCH: uni_fall_count: %, uni_capacity: %', uni_fall_count,
                        uni_capacity;
                    FETCH NEXT FROM application_curs INTO placement_result;
                END IF;
            WHEN 'spring' THEN
                SELECT spring_applicant_count, capacity INTO uni_spring_count, uni_capacity FROM
                    universities WHERE uni_id = placement_result.target_uni_id;
                IF uni_spring_count < uni_capacity THEN
                    UPDATE universities SET spring_applicant_count = spring_applicant_count + 1 WHERE
                            uni_id = placement_result.target_uni_id;
                    UPDATE applications SET result = true WHERE application_id = placement_result
                        .app_id;
                    INSERT INTO placed_students VALUES(placement_result.std_id, placement_result
                        .target_uni_id, placement_result.term, placement_result.app_id);
--                     placed_students[i] = placement_result;
--                     i = i + 1;
                    FETCH NEXT FROM application_curs INTO placement_result;
                    WHILE placement_result.std_id = old_std_id LOOP
                            FETCH NEXT FROM application_curs INTO placement_result;
                        END LOOP;
                ELSE
                    FETCH NEXT FROM application_curs INTO placement_result;
                END IF;
            WHEN 'full_year' THEN
                SELECT fall_applicant_count, spring_applicant_count, capacity INTO
                    uni_fall_count, uni_spring_count, uni_capacity FROM
                    universities WHERE uni_id = placement_result.target_uni_id;
                IF uni_fall_count < uni_capacity AND uni_spring_count < uni_capacity THEN
                    UPDATE universities SET fall_applicant_count = fall_applicant_count + 1,
                                            spring_applicant_count = spring_applicant_count + 1
                    WHERE
                            uni_id = placement_result.target_uni_id;
                    UPDATE applications SET result = true WHERE application_id = placement_result
                        .app_id;
                    INSERT INTO placed_students VALUES(placement_result.std_id, placement_result
                        .target_uni_id, placement_result.term, placement_result.app_id);
--                     placed_students[i] = placement_result;
--                     i = i + 1;
                    FETCH NEXT FROM application_curs INTO placement_result;
                    WHILE placement_result.std_id = old_std_id LOOP
                            FETCH NEXT FROM application_curs INTO placement_result;
                        END LOOP;
                ELSE
                    FETCH NEXT FROM application_curs INTO placement_result;
                END IF;
        END CASE;
    end loop;
    close application_curs;
--     FOR row IN placed_students LOOP
--         RAISE NOTICE 'row: %', row;
--     END LOOP;
--     RAISE NOTICE 'row: %', placed_students[6];
    RETURN QUERY SELECT * FROM placed_students;
END;
$$ Language 'plpgsql';
