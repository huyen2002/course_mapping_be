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
(1, 2, 'QHI', 'Đại học Công nghệ - ĐHQGHN được thành lập năm 2005. Đào tạo 20 chuyên ngành.', 1),
(2, 3, 'KHA', 'Đại học Kinh tế Quốc dân được thành lập năm 2009. Đào tạo 25 chuyên ngành', 3),
(3, 4, 'BKA', 'Đại học Bách Khoa Hà Nội được thành lập năm 2010. Đào tạo 30 chuyên ngành', 2);

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

INSERT INTO program_educations (id, name, code, language, duration_year, level_of_education, num_credits, start_year, end_year, university_id, major_id, introduction) VALUES
(1, 'Công nghệ thông tin','CN1', 'VI', 4, 'BACHELOR', 120, 2020, 2025, 1, 1, 'Ngành Công nghệ Thông tin đào tạo kiến thức cũng như các kỹ năng quan trọng và hiện đại về Công nghệ thông tin với mục tiêu đạt trình độ chung của khu vực và quốc tế, cho phép sinh viên sau khi tốt nghiệp có thể nhanh chóng đáp ứng các nhu cầu về hoạt động công nghệ thông tin của xã hội cũng như tiếp tục học tập, nghiên cứu và phát triển ngành Công nghệ Thông tin. Sinh viên tốt nghiệp ngành CNTT có thể làm việc tại các công ty công nghệ với nhiều vị trí khác nhau từ lập trình viên, lập trình các hệ thống thông minh, phân tích thiết kế hệ thống, kiểm thử phần mềm, quản trị mạng…'),
(2, 'Khoa học máy tính', 'CN8', 'VI', 4.5, 'BACHELOR', 130, 2020, 2025, 1, 1, 'Ngành Khoa học máy tính cung cấp cho sinh viên các kiến thức, kỹ năng cơ bản và hiện đại trong các lĩnh vực truyền thống của CNTT như công nghệ phần mềm, cơ sở dữ liệu, mạng máy tính, kiến trúc máy tính; song các lĩnh vực mới và không kém phần thú vị như xử lý ngôn ngữ tự nhiên, học máy – trí tuệ nhân tạo, tin sinh học, xử lý tiếng nói, xử lý ảnh, tương tác người – máy,… cũng được chú trọng phát triển và đào tạo.'),
(3, 'Kỹ thuật máy tính','CN2', 'VI', 4, 'ENGINEER', 120, 2019, 2025, 1, 3, 'Ngành Khoa học máy tính cung cấp cho sinh viên các kiến thức, kỹ năng cơ bản và hiện đại trong các lĩnh vực truyền thống của CNTT như công nghệ phần mềm, cơ sở dữ liệu, mạng máy tính, kiến trúc máy tính; song các lĩnh vực mới và không kém phần thú vị như xử lý ngôn ngữ tự nhiên, học máy – trí tuệ nhân tạo, tin sinh học, xử lý tiếng nói, xử lý ảnh, tương tác người – máy,… cũng được chú trọng phát triển và đào tạo.'),
(4, 'Trí tuệ nhân tạo','CN12', 'VI', 4, 'BACHELOR', 120, 2019, 2025, 1, 9, 'Ngành Trí tuệ nhân tạo là một đổi mới trong thông tin tuyển sinh năm 2022 của trường Đại học Công nghệ – Đại học Quốc gia Hà Nội, một hướng phát triển mới dành cho sinh viên nhóm ngành Công nghệ thông tin. Sinh viên ngành Trí tuệ nhân tạo sẽ được giới thiệu và đi sâu nghiên cứu các hệ chuyên gia, tương tác người – máy và ứng dụng trong các môi trường như hệ đa phương tiện; xử lý hình ảnh, âm thanh; phân tích dữ liệu lớn.'),
(5, 'Kế toán', 'VI','7340301', 4, 'BACHELOR', 120, 2019, 2025, 2, 13, null),
(6, 'Kinh tế quốc tế - TT23','7310106', 'VI', 4, 'BACHELOR', 110, 2019, 2022, 2, 11, null),
(7, 'Hệ thống thông tin', 'CN14', 'VI', 4.5, 'BACHELOR', 130, 2020, 2023, 1, 1, 'Chương trình đào tạo cử nhân Hệ thống thông tin (HTTT)  tại Trường Đại học Công nghệ (ĐHCN) thuộc Đại học Quốc gia Hà Nội (ĐHQGHN) nhằm mục tiêu đào tạo các sinh viên trở thành các chuyên gia HTTT chất lượng cao có phẩm chất tốt, có trình độ chuyên môn giỏi, có năng lực tự học suốt đời về HTTT, định hướng vào bốn chủ đề hiện đại của Công nghệ Thông tin (CNTT) trong thời đại số hóa (còn được gọi là Công nghiệp 4.0) là tích hợp dịch vụ, quản lý dữ liệu lớn, khoa học dữ liệu và phân tích kinh doanh.'),
(8, 'Khoa học máy tính', 'IT1', 'VI', 4, 'BACHELOR', 130, 2020, 2025, 3, 1, 'Ngành Khoa học máy tính cung cấp cho sinh viên các kiến thức, kỹ năng cơ bản và hiện đại trong các lĩnh vực truyền thống của CNTT như công nghệ phần mềm, cơ sở dữ liệu, mạng máy tính, kiến trúc máy tính; song các lĩnh vực mới và không kém phần thú vị như xử lý ngôn ngữ tự nhiên, học máy – trí tuệ nhân tạo, tin sinh học, xử lý tiếng nói, xử lý ảnh, tương tác người – máy,… cũng được chú trọng phát triển và đào tạo.'),
(9, 'Kỹ thuật máy tính','IT2', 'VI', 4, 'BACHELOR', 120, 2019, 2025, 3, 3, 'Ngành Kỹ thuật máy tính cung cấp cho sinh viên các kiến thức, kỹ năng cơ bản và hiện đại trong các lĩnh vực truyền thống của CNTT như công nghệ phần mềm, cơ sở dữ liệu, mạng máy tính, kiến trúc máy tính; song các lĩnh vực mới và không kém phần thú vị như xử lý ngôn ngữ tự nhiên, học máy – trí tuệ nhân tạo, tin sinh học, xử lý tiếng nói, xử lý ảnh, tương tác người – máy,… cũng được chú trọng phát triển và đào tạo.');


INSERT INTO courses (id, name, code, language, university_id) VALUES
(1, 'Công nghệ phần mềm', 'INT2208', 'VI', 1),
(2, 'Nhập môn lập trình', 'INT1008', 'VI', 1),
(3, 'Lập trình nâng cao', 'INT2215', 'VI', 1),
(4, 'Triết học Mác Lê-nin', 'PHI1006', 'VI', 2),
(5, 'Giải tích 1', 'MI1111', 'VI', 3),
(6, 'Giải tích 2', 'MI1112', 'VI', 3),
(7, 'Xác suất thống kê', 'MAT1101', 'VI', 1),
(8, 'Kinh tế học', 'NEU1111', 'VI', 2),
(9, 'Kế toán cơ bản', 'NEU1112', 'VI', 2),
(10, 'Kế toán quản trị', 'NEU1113', 'VI', 2),
(11, 'Phát triển ứng dụng web', 'INT3306', 'VI', 1),
(12, 'Nhập môn Công nghệ phần mềm', 'IT3180', 'VI', 3),
(13, 'Xác suất thống kê', 'MI2020', 'VI', 3),
(14, 'Giải tích 1', 'MAT1041', 'VI', 1),
(15, 'Giải tích 2', 'MAT1042', 'VI', 1);

INSERT INTO program_education_course (id, course_id, program_education_id, compulsory, num_credits) VALUES
(1, 1, 1, true, 3),
(2,2,1,true, 4),
(3,3,1,true, 4),
(4,4,1,true,3),
(5,14,1,true,4),
(6,15,1,true,4),
(7,7,1,false,3),
(8,11,1,true, 3),
(9, 1, 2, true, 3),
(10,2,2,true, 4),
(11,3,2,true, 4),
(12,4,2,true,3),
(13,14,2,true,4),
(14,15,2,true,4),
(15,7,2,false,3),
(16,11,2,true, 3),
(17, 1, 3, false, 3),
(18,2,3,true, 4),
(19,3,3,true, 4),
(20,4,3,true,3),
(21,14,3,true,4),
(22,15,3,true,4),
(23,7,3,false,3),
(24,11,3,true, 3),
(25, 5,8,true,4),
(26, 6,8,true,4),
(27,13,8,true, 3),
(28,12,8,true,3);
