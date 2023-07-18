-- User
INSERT INTO User (id, email, nickname, profile_image, is_active)
VALUES ('user1', 'user1@example.com', 'User 1', 'profile1.jpg', true),
       ('user2', 'user2@example.com', 'User 2', 'profile2.jpg', false),
       ('user3', 'user3@example.com', 'User 3', 'profile3.jpg', true),
       ('user4', 'user4@example.com', 'User 4', 'profile4.jpg', true),
       ('user5', 'user5@example.com', 'User 5', 'profile5.jpg', false);


--workspace
INSERT INTO Workspace (id, name, creator_id)
VALUES ('workspace1', 'Workspace 1', 'user1'),
       ('workspace2', 'Workspace 2', 'user1'),
       ('workspace3', 'Workspace 3', 'user2'),
       ('workspace4', 'Workspace 4', 'user2'),
       ('workspace5', 'Workspace 5', 'user3'),
       ('workspace6', 'Workspace 6', 'user3'),
       ('workspace7', 'Workspace 7', 'user4'),
       ('workspace8', 'Workspace 8', 'user4'),
       ('workspace9', 'Workspace 9', 'user5'),
       ('workspace10', 'Workspace 10', 'user5');

--workspace_member
INSERT INTO Member (user_id, workspace_id)
VALUES ('user2','workspace1'),
       ('user3','workspace1'),
       ('user4','workspace1'),
       ('user2','workspace2'),
       ('user3','workspace2'),
       ('user1','workspace5'),
       ('user2','workspace5'),
       ('user5','workspace5'),
       ('user1','workspace6'),
       ('user2','workspace6');


--channel
INSERT INTO Channel (id, name, description, creator_id, workspace_id, is_private)
VALUES
    (1, 'Channel 1', 'Description 1', 'user1', 'workspace1', false),
    (2, 'Channel 2', 'Description 2', 'user1', 'workspace1', false),
    (3, 'Channel 3', 'Description 3', 'user1', 'workspace2', false),
    (4, 'Channel 4', 'Description 4', 'user1', 'workspace2', false),
    (5, 'Channel 5', 'Description 5', 'user2', 'workspace3', false),
    (6, 'Channel 6', 'Description 6', 'user2', 'workspace3', false),
    (7, 'Channel 7', 'Description 7', 'user2', 'workspace4', false),
    (8, 'Channel 8', 'Description 8', 'user3', 'workspace5', false),
    (9, 'Channel 9', 'Description 9', 'user3', 'workspace6', false),
    (10, 'Channel 10', 'Description 10', 'user3', 'workspace6', false),
    (11, 'Channel 11', 'Description 11', 'user4', 'workspace7', false),
    (12, 'Channel 12', 'Description 12', 'user4', 'workspace7', false),
    (13, 'Channel 13', 'Description 13', 'user4', 'workspace8', false),
    (14, 'Channel 14', 'Description 14', 'user5', 'workspace9', false),
    (15, 'Channel 15', 'Description 15', 'user5', 'workspace10', false);

--messages
--messages
INSERT INTO Message (id, channel_id, sender_id, content, created_at, updated_at)
VALUES
    -- Workspace 1에 속한 사용자만 메시지를 작성할 수 있도록 함
    (1, 1, 'user1', 'Message 1', '2023-07-18 10:00:00', '2023-07-18 10:00:00'),
    (2, 1, 'user2', 'Message 2', '2023-07-18 10:01:00', '2023-07-18 10:01:00'),
    (3, 1, 'user3', 'Message 3', '2023-07-18 10:02:00', '2023-07-18 10:02:00'),
    (4, 2, 'user1', 'Message 4', '2023-07-18 10:03:00', '2023-07-18 10:03:00'),
    -- Workspace 2에 속한 사용자만 메시지를 작성할 수 있도록 함
    (5, 3, 'user1', 'Message 5', '2023-07-18 10:04:00', '2023-07-18 10:04:00'),
    (6, 3, 'user2', 'Message 6', '2023-07-18 10:05:00', '2023-07-18 10:05:00'),
    (7, 3, 'user3', 'Message 7', '2023-07-18 10:06:00', '2023-07-18 10:06:00'),
    -- Workspace 3에 속한 사용자만 메시지를 작성할 수 있도록 함
    (8, 5, 'user2', 'Message 8', '2023-07-18 10:07:00', '2023-07-18 10:07:00'),
    (9, 5, 'user3', 'Message 9', '2023-07-18 10:08:00', '2023-07-18 10:08:00'),
    -- Workspace 4에 속한 사용자만 메시지를 작성할 수 있도록 함
    (10, 7, 'user2', 'Message 10', '2023-07-18 10:09:00', '2023-07-18 10:09:00'),
    -- Workspace 5에 속한 사용자만 메시지를 작성할 수 있도록 함
    (11, 8, 'user5', 'Message 11', '2023-07-18 10:10:00', '2023-07-18 10:10:00'),
    (12, 9, 'user1', 'Message 12', '2023-07-18 10:11:00', '2023-07-18 10:11:00'),
    -- Workspace 6에 속한 사용자만 메시지를 작성할 수 있도록 함
    (13, 9, 'user2', 'Message 13', '2023-07-18 10:12:00', '2023-07-18 10:12:00'),
    (14, 10, 'user3', 'Message 14', '2023-07-18 10:13:00', '2023-07-18 10:13:00'),
    -- Workspace 7에 속한 사용자만 메시지를 작성할 수 있도록 함
    (15, 11, 'user4', 'Message 15', '2023-07-18 10:14:00', '2023-07-18 10:14:00');
