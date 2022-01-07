-- DROP TABLE IF EXISTS students CASCADE;
-- DROP TABLE IF EXISTS applications CASCADE;
-- DROP TABLE IF EXISTS consultants CASCADE;
-- DROP TABLE IF EXISTS universities CASCADE;
-- DROP TABLE IF EXISTS foreign_students CASCADE;
-- DROP TABLE IF EXISTS login_infos CASCADE;

CREATE TABLE IF NOT EXISTS students (
    std_id integer PRIMARY KEY,
    exam_result integer CHECK (exam_result > 0),
    GPA numeric(3, 2) CHECK (GPA > 2.0),
    department varchar(50),
    consultant_id integer,
    fname varchar(50) NOT NULL,
    lname varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS applications (
    application_id integer PRIMARY KEY,
    std_id integer,
    target_uni_id integer,
    priority integer NOT NULL,
    fund float CHECK (fund BETWEEN 0 AND 400),
    result boolean,
    term varchar(20) CHECK (term IN ('fall', 'spring', 'full_year')),
    UNIQUE (std_id, target_uni_id, term)
);

CREATE TABLE IF NOT EXISTS consultants (
    consultant_id integer PRIMARY KEY,
    fname varchar(50),
    lname varchar(50),
    department varchar(50),
    rank varchar(50)
);

CREATE TABLE IF NOT EXISTS universities (
    uni_id integer PRIMARY KEY,
    country varchar(50),
    name varchar(50),
    capacity integer CHECK (capacity >= 0),
    fall_applicant_count integer CHECK (fall_applicant_count >= 0),
    spring_applicant_count integer CHECK (spring_applicant_count >= 0)
);

CREATE TABLE IF NOT EXISTS foreign_students (
    foreign_std_id integer PRIMARY KEY,
    department varchar(50),
    consultant_id integer,
    fname varchar(50) NOT NULL,
    lname varchar(50) NOT NULL,
    src_uni_id integer NOT NULL
);

CREATE TABLE IF NOT EXISTS login_infos (
    username varchar(64) PRIMARY KEY,
    password varchar(100) NOT NULL,
    role varchar(50) NOT NULL CHECK (ROLE IN ('student', 'manager', 'it_staff'))
);

ALTER TABLE universities
    ADD CONSTRAINT check_uni_capacity CHECK (fall_applicant_count <= capacity and spring_applicant_count <= capacity);

ALTER TABLE students
    ADD FOREIGN KEY (consultant_id) REFERENCES consultants (consultant_id) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE applications
    ADD FOREIGN KEY (std_id) REFERENCES students (std_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE applications
    ADD FOREIGN KEY (target_uni_id) REFERENCES universities (uni_id) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE foreign_students
    ADD FOREIGN KEY (consultant_id) REFERENCES consultants (consultant_id) ON DELETE SET NULL ON UPDATE CASCADE;

ALTER TABLE foreign_students
    ADD FOREIGN KEY (src_uni_id) REFERENCES universities (uni_id) ON DELETE SET NULL ON UPDATE CASCADE;

--Table Comments
COMMENT ON TABLE students IS 'Local Erasmus students';

COMMENT ON TABLE applications IS 'Application records of all students to universities';

COMMENT ON TABLE consultants IS 'Consultants of Erasmus students';

COMMENT ON TABLE universities IS 'Universities that currently accept student exchange';

COMMENT ON TABLE foreign_students IS 'Foreign Erasmus students';

COMMENT ON TABLE login_infos IS 'Username and password infos to be used in the login page';

