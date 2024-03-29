insert into member (account, picture, role, username, member_id) values ('campOwner@test.com', NULL, 'OWNER', 'owner', 100);

insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 광진구 능동로 209', '캠프소개', NULL, NULL, 37.5507345, 127.0741314, 100, '캠핑의 정석', '010-1234-1234', 2);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 마포구 와우산로21길 21-16', '캠프소개', NULL, NULL, 37.5516821, 126.9221623, 100, '캠핑 민족', '010-1234-1234', 3);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 용산구 백범로99길 50', '캠프소개', NULL, NULL, 37.5360397, 126.972224, 100, '놀러와요 동물의 숲', '010-1234-1234', 4);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 강남구 도산대로15길 32-10', '캠프소개', NULL, NULL, 37.5209049, 127.0235436, 100, '서울 캠핑 1호점', '010-1234-1234', 5);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 강남구 봉은사로18길 80', '캠프소개', NULL, NULL, 37.5015718, 127.0273738, 100, '서울 캠핑 2호점', '010-1234-1234', 6);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 영등포구 경인로79길 17-2', '캠프소개', NULL, NULL, 37.5133211, 126.8933711, 100, '고기굽는 캠핑장', '010-1234-1234', 7);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 마포구 성미산로 161-15', '캠프소개', NULL, NULL, 37.5653218, 126.9235254, 100, '와요', '010-1234-1234', 8);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 종로구 경희궁길 41', '캠프소개', NULL, NULL, 37.5729273, 126.9692798, 100, '오늘캠핑', '010-1234-1234', 9);
insert into camp (address, camp_introduction, camp_operation_end, camp_operation_start, latitude, longitude, member_id, name, phone, camp_id) values ('서울 동작구 동작대로13길 6-7', '캠프소개', NULL, NULL, 37.4820582, 126.9811174, 100, '중앙캠핑', '010-1234-1234', 10);

insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (2, 2, 1, '객실1', 15000, 20000, 11);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (2, 2, 1, '객실2', 15000, 20000, 12);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (2, 4, 1, '객실3', 25000, 30000, 13);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (2, 4, 1, '객실4', 25000, 30000, 14);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (3, 2, 3, '2인', 15000, 20000, 15);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (3, 4, 3, '4인', 25000, 30000, 16);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (3, 6, 1, '6인', 50000, 80000, 17);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (4, 2, 1, 'typeA', 15000, 20000, 18);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (4, 2, 1, 'typeB', 15000, 20000, 19);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (4, 4, 1, 'typeC', 25000, 30000, 20);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (4, 4, 1, 'typeD', 25000, 30000, 21);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (5, 2, 3, '2인', 15000, 20000, 22);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (6, 4, 3, '4인', 25000, 30000, 23);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (7, 6, 1, '6인', 50000, 80000, 24);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (8, 2, 1, 'typeA', 15000, 20000, 25);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (9, 2, 1, 'typeB', 15000, 20000, 26);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (10, 10, 1, '단체1', 150000, 250000, 27);
insert into room (camp_id, capacity, cnt, name, week_price, weekend_price, room_id) values (10, 10, 1, '단체2', 150000, 250000, 28);

insert into image (image_id, file_name, path, camp_id, post_id, review_id, room_id) values (101, 'a9419d73-c13f-450f-b4c4-1a68df2de9e3.jpg' , 'https://gamja-camp.s3.ap-northeast-2.amazonaws.com/a9419d73-c13f-450f-b4c4-1a68df2de9e3.jpg'  , 2, null, null, null);
insert into image (image_id, file_name, path, camp_id, post_id, review_id, room_id) values (102, '958395d7-92f5-4805-85a4-8401c0cc7501.jpg' , 'https://gamja-camp.s3.ap-northeast-2.amazonaws.com/958395d7-92f5-4805-85a4-8401c0cc7501.jpg'  , 2, null, null, null);
insert into image (image_id, file_name, path, camp_id, post_id, review_id, room_id) values (103, 'e1c2d8bb-e0d9-4071-afb2-f8bfdd9bd266.jpg' , 'https://gamja-camp.s3.ap-northeast-2.amazonaws.com/e1c2d8bb-e0d9-4071-afb2-f8bfdd9bd266.jpg'  , 3, null, null, null);
insert into image (image_id, file_name, path, camp_id, post_id, review_id, room_id) values (104, 'a61ffe75-4a8e-42a9-890d-6fd6f2c0dd96.jpg' , 'https://gamja-camp.s3.ap-northeast-2.amazonaws.com/a61ffe75-4a8e-42a9-890d-6fd6f2c0dd96.jpg'  , 4, null, null, null);
insert into image (image_id, file_name, path, camp_id, post_id, review_id, room_id) values (105, '7e9fcacd-f50c-4c50-b136-0dc22eea5dd5.jpg' , 'https://gamja-camp.s3.ap-northeast-2.amazonaws.com/7e9fcacd-f50c-4c50-b136-0dc22eea5dd5.jpg'  , 5, null, null, null);
insert into image (image_id, file_name, path, camp_id, post_id, review_id, room_id) values (106, '12d8f24e-d3cc-41a3-9c81-828b8d0fb093.jpg' , 'https://gamja-camp.s3.ap-northeast-2.amazonaws.com/12d8f24e-d3cc-41a3-9c81-828b8d0fb093.jpg'  , 6, null, null, null);
insert into image (image_id, file_name, path, camp_id, post_id, review_id, room_id) values (107, '07e0e10b-4c30-45ee-ad13-723547239103.jpg' , 'https://gamja-camp.s3.ap-northeast-2.amazonaws.com/07e0e10b-4c30-45ee-ad13-723547239103.jpg'  , 7, null, null, null);
insert into image (image_id, file_name, path, camp_id, post_id, review_id, room_id) values (108, 'a5e65053-7e6e-4c99-9aca-7bb02607573a.jpg' , 'https://gamja-camp.s3.ap-northeast-2.amazonaws.com/a5e65053-7e6e-4c99-9aca-7bb02607573a.jpg'  , 8, null, null, null);
insert into image (image_id, file_name, path, camp_id, post_id, review_id, room_id) values (109, '8c99c62f-a700-44f9-8032-920bf9420dc3.jpg' , 'https://gamja-camp.s3.ap-northeast-2.amazonaws.com/8c99c62f-a700-44f9-8032-920bf9420dc3.jpg'  , 9, null, null, null);


