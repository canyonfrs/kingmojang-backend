INSERT INTO member (name, email, token, phone_number, role, created_date_time, updated_date_time, active_status) VALUES ('streamer', 'streamer12@gmail.com', 'streamer_token', '010-1234-1234','STREAMER', NOW(), NOW(), 'ACTIVE');

INSERT INTO memo (content, writer_id, visibility, created_date_time, updated_date_time, active_status) VALUES ('memo1', 1, 'PUBLIC', NOW(), NOW(), 'ACTIVE');