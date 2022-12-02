insert into generation(generation_id, description, location, longitude, latitude,  course_start_date, course_end_date, created_date, modified_date)
    values(2, '2기 수강생', null, '126.980182', '37.5685379', '2022-10-11', '2023-01-19', sysdate(), sysdate());

INSERT INTO user(created_date, modified_date, email, github_url, image_url, is_leader, name, contact, dislikes, interests, likes, mbti, oneliner, provider, refresh_token, role, devpart_id, generation_id, team_id, username)
values (sysdate(),sysdate(), 'user1@email.com', 'user1@github.com', '/resources/static/kyeboard-small.gif', 0, '유저1', null, null, null, null, null, '한줄소개입니다.', 'GITHUB', null, 'USER', 2, 2, null, 'user1');

INSERT INTO devpart(name) value ('react');
INSERT INTO devpart(name) value ('spring');
INSERT INTO devpart(name) value ('android');
INSERT INTO devpart(name) value ('unity');

ALTER TABLE devpart DROP COLUMN language;