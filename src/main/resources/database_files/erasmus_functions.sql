DROP TYPE IF EXISTS application_type CASCADE;

drop function  manager_get_applications(student_id_input int);
CREATE OR REPLACE FUNCTION manager_get_applications(student_id_input int)
    RETURNS table(application_id int, std_id int, name varchar(50), term varchar(20))
AS
$$
BEGIN
    return query
        SELECT a.application_id,
               a.std_id,
               u.name,
               a.term
        FROM applications AS a
                 INNER JOIN universities u ON a.target_uni_id = u.uni_id
        WHERE a.std_id = student_id_input;
END;
$$
    LANGUAGE 'plpgsql';

SELECT * from manager_get_applications(3);

