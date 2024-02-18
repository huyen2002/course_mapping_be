USE course_mapping;
SET names 'utf8';

INSERT INTO users (id, email, password, name, role, enabled, create_at, update_at) VALUES
(1, 'thanhhuyennt02@gmail.com', '$2a$10$JS5wbPFgs/n4pmsHjkIOBONvlvaGaTj0bIubHO6bVNSN3mxj4vqpK', 'Admin', 'ADMIN', true, '2024-02-18', '2024-02-18'),
(2, '20020420@vnu.edu.vn', '$2a$10$JS5wbPFgs/n4pmsHjkIOBONvlvaGaTj0bIubHO6bVNSN3mxj4vqpK', 'Đại học Công nghệ - ĐHQGHN', 'UNIVERSITY', true, '2024-02-18', '2024-02-18'),
(3, '20020421@neu.edu.vn', '$2a$10$JS5wbPFgs/n4pmsHjkIOBONvlvaGaTj0bIubHO6bVNSN3mxj4vqpK', 'Đại học Kinh tế Quốc dân', 'UNIVERSITY', true, '2024-02-18', '2024-02-18'),
(4, '20020422@hust.edu.vn', '$2a$10$JS5wbPFgs/n4pmsHjkIOBONvlvaGaTj0bIubHO6bVNSN3mxj4vqpK', 'Đại học Bách Khoa Hà Nội', 'UNIVERSITY', true, '2024-02-18', '2024-02-18'),
(5, 'thanhhuyen@gmail.com', '$2a$10$JS5wbPFgs/n4pmsHjkIOBONvlvaGaTj0bIubHO6bVNSN3mxj4vqpK', 'Huyen', 'USER', true, '2024-02-18', '2024-02-18');

INSERT INTO address (id, detail, district, city, country) VALUES
(1, '144 Xuân Thủy', 'Cầu Giấy', 'Hà Nội', 'Việt Nam'),
(2, 'Số 1 Đại Cồ Việt', 'Hai Bà Trưng', 'Hà Nội', 'Việt Nam'),
(3, '207 Đường Giải Phóng', 'Hai Bà Trưng', 'Hà Nội', 'Việt Nam');

INSERT INTO university (id, user_id, code, introduction, address_id ) VALUES
(1, 2, 'UET', 'Đại học Công nghệ - ĐHQGHN được thành lập năm 2005. Đào tạo 20 chuyên ngành.', 1),
(2, 3, 'NEU', 'Đại học Kinh tế Quốc dân được thành lập năm 2009. Đào tạo 25 chuyên ngành', 3),
(3, 4, 'HUST', 'Đại học Bách Khoa Hà Nội được thành lập năm 2010. Đào tạo 30 chuyên ngành', 2);

INSERT INTO majors (id, code, name) VALUES
(1, '7480201', 'Công nghệ thông tin'),
(2, '7480202', 'Kỹ thuật phần mềm'),
(3, '7480203', 'Khoa học máy tính'),
(4, '7480204', 'Kỹ thuật điện tử - viễn thông'),
(5, '7480205', 'Kỹ thuật điều khiển và tự động hóa'),
(6, '7480206', 'Kỹ thuật máy tính và mạng'),
(7, '7480207', 'Kỹ thuật máy tính và robot'),
(8, '7480208', 'Kỹ thuật máy tính và truyền thông'),
(9, '7480209', 'Trí tuệ nhân tạo'),
(10, '7480210', 'Hệ thống thông tin'),
(11, '12345', 'Kinh tế quốc tế'),
(12, '12346', 'Kinh tế đối ngoại'),
(13, '12347', 'Kế toán');

INSERT INTO program_educations (id, name, language, duration_year, level_of_education, num_credits, start_year, end_year, university_id, major_id) VALUES
(1, 'Công nghệ thông tin', 'VI', 4, 'BACHELOR', 120, 2020, 2025, 1, 1),
(2, 'Công nghệ thông tin Chất lượng cao', 'VI', 4, 'BACHELOR', 130, 2020, 2025, 1, 1),
(3, 'Khoa học máy tính', 'VI', 4, 'BACHELOR', 120, 2019, 2025, 3, 3),
(4, 'Trí tuệ nhân tạo', 'VI', 4, 'BACHELOR', 120, 2019, 2025, 1, 9),
(5, 'Kế toán', 'VI', 4, 'BACHELOR', 120, 2019, 2025, 2, 13);

INSERT INTO courses (id, name, code, language, university_id) VALUES
(1, 'Công nghệ phần mềm', 'INT_2345', 'VI', 1),
(2, 'Nhập môn lập trình', 'INT_1234', 'VI', 1),
(3, 'Lập trình nâng cao', 'INT_1110', 'VI', 1),
(4, 'Triết học Mác Lê-nin', 'NEU_1110', 'VI', 2),
(5, 'Giải tích 1', 'HUST_1110', 'VI', 3),
(6, 'Giải tích 2', 'HUST_1111', 'VI', 3);

INSERT INTO program_education_course (id, course_id, program_education_id, compulsory, num_credits) VALUES
(1, 1, 1, true, 4),
(2, 2, 1, true, 3),
(3, 3, 1, true, 4),
(4, 4, 5, true, 3),
(5, 5, 3, true, 4),
(6, 6, 3, true, 4);
