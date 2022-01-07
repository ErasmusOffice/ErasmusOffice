/* INSERTING CONSULTANTS */
insert into consultants (consultant_id, fname, lname, department, rank)
values
(1, 'Mazlum', 'Coker', 'Medicine', 'Professor'),
(2, 'Nurhan', 'Varurer', 'Biology', 'Lecturer'),
(3, 'Yasar Gokhan', 'Herisci', 'Chemistry', 'Lecturer' ),
(4, 'Gulden', 'Kaykisiz', 'Electrical Engineering', 'Instructor'),
(5, 'Dila Tanriverdi', 'Yilmaz', 'History', 'Research Assistant'),
(6, 'Ayden','Teoman' , 'Computer Science', 'Asisstant Professor'),
(7,'Remziye' ,'Cukurlugoz' , 'General Business', 'Instructor'),
(8,'Tahsin Batuhan' , 'Zengil', 'Marketing and Management', 'Instructor'),
(9, 'Ezel', 'Ozkaynar', 'Linguistics', 'Research Assistant' ),
(10, 'Abdulsamet','Cerid' , 'Mathematics', 'Professor');


/* INSERTING STUDENTS */
insert into students (std_id, password, exam_result, gpa, department, consultant_id, fname, lname)
values
       (1, '0001', 80, 2.8, 'Biology', 2, 'Yuksel', 'Altin'),
       (2, '0002', 90, 2.8, 'Chemistry', 3, 'Alpay', 'Dereli'),
       (3, '0003', 70, 2.8, 'Electrical Engineering', 4, 'Ersoy', 'Karakus'),
       (4, '0004', 85, 2.8, 'History', 5, 'Toprak', 'Isler'),
       (5, '0005', 95, 2.8, 'Computer Science', 6, 'Kudret', 'Dincer'),
       (6, '0006', 70, 2.8, 'General Business', 7, 'Leyla', 'Tansu'),
       (7, '0007', 95, 2.8, 'Marketing and Management', 8, 'Berna', 'Yavuz'),
       (8, '0008', 85, 2.8, 'Linguistics', 9, 'Varol', 'Caglayan'),
       (9, '0009', 85, 2.8, 'Medicine', 1, 'Erdem', 'Karaca'),
       (10, '0010', 75, 2.8, 'Mathematics', 10, 'Aydiner', 'Sisli');


/* INSERTING UNIVERSITIES */
insert into universities (uni_id, country, name, capacity, fall_applicant_count, spring_applicant_count)
values
(1, 'Portugal', 'Universidade de Coimbra', 5, 1, 0),
(2, 'United Kingdom', 'University of Kent', 10, 0, 1),
(3, 'Spain', 'Universidad de Salamanca', 35, 1, 0),
(4, 'Czech Republic', 'Masarykova univerzita', 30, 0, 0),
(5, 'Switzerland', 'Universitat Basel', 3, 3, 0),
(6, 'Germany', 'Universitat Stuttgart', 30, 1, 1),
(7, 'Russia', 'Kazan Federal University', 30, 0, 1),
(8, 'Italy', 'Universita degli Studi di Genova', 40, 1, 1),
(9, 'France', 'Universite Paris-Saclay', 40, 0, 1),
(10, 'Italy', 'Universita degli Studi di Pisa', 30, 0, 0);


/* INSERTING FOREIGN STUDENTS */
insert into foreign_students (foreign_std_id, department, consultant_id, fname, lname, src_uni_id)
values
       (1, 'Chemistry', 3, 'Julien', 'Davenport', 4),
       (2, 'History', 5, 'Nyah', 'Becker', 1),
       (3, 'Biology', 2, 'Tonicha', 'Glenn', 3),
       (4, 'Medicine', 1, 'Pearce', 'Sanderson', 2),
       (5, 'Electrical Engineering', 4, 'Jeevan', 'Leon', 8),
       (6, 'General Business', 7, 'Marcelina', 'Clayton', 7),
       (7, 'Marketing and Management', 8, 'Samira', 'Mora', 5),
       (8, 'Computer Science', 6, 'Ibrahim', 'Bate', 6),
       (9, 'Mathematics', 10, 'Carlos', 'Lynn', 10),
       (10, 'Linguistics', 9, 'Danniella', 'Matthams', 9);


/* INSERTING APPLICATIONS */
insert into applications (application_id, std_id, target_uni_id, priority, fund, result, term)
values
    (1, 1, 3, 1, 300, false, 'fall'),
    (2, 1, 5, 2, 400, false, 'fall'),
    (3, 2, 5, 1, 350, false, 'fall'),
    (4, 2, 2, 2, 300, false, 'spring'),
    (5, 2, 1, 3, 400, false, 'fall'),
    (6, 3, 5, 1, 200, false, 'fall'),
    (7, 3, 6, 2, 300, false, 'full_year'),
    (8, 4, 7, 1, 350, false, 'spring'),
    (9, 4, 8, 2, 300, false, 'full_year'),
    (10, 5, 9, 1, 400, false, 'spring');


/* INSERTING LOGIN INFOS */
insert into login_infos (username, password, role)
values
    ('manager1', '1234', 'manager'),
    ('manager2', '1234', 'manager'),
    ('manager3', '1234', 'manager'),
    ('manager4', '1234', 'manager'),
    ('itstaff1', '1234', 'it_staff'),
    ('itstaff2', '1234', 'it_staff'),
    ('itstaff3', '1234', 'it_staff');