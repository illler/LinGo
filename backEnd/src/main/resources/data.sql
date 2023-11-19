CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Создание таблицы "user" для хранения информации о пользователях
CREATE TABLE "user" (
                        id uuid DEFAULT Uuid_generate_v4 () PRIMARY KEY,
                        name VARCHAR(255),
                        email VARCHAR(255) not null,
                        password VARCHAR(255) not null,
                        description text
);

CREATE TABLE UsersLanguages(
                               id serial primary key,
                               user_id uuid REFERENCES "user"(id) on delete cascade,
                               language uuid REFERENCES language(id)
);

CREATE TABLE language(
                         id uuid PRIMARY KEY,
                         name VARCHAR(32)
);

-- CREATE TABLE deletedChats(
--                              id serial primary key,
--                              user_id uuid REFERENCES "user"(id) on delete cascade,
--                              chat_id uuid REFERENCES chat(id) on delete restrict
-- );

-- Создание таблицы "chat" для хранения информации о чатах
CREATE TABLE chat (
                      id uuid DEFAULT Uuid_generate_v4 () PRIMARY KEY,
                      user1_id uuid REFERENCES "user"(id) on delete restrict,
                      user2_id uuid REFERENCES "user"(id) on delete restrict
);


-- Создание таблицы "message" для хранения информации о текстовых сообщениях
CREATE TABLE message (
                         id uuid DEFAULT Uuid_generate_v4 () PRIMARY KEY,
                         user_id uuid REFERENCES "user"(id) on delete restrict,
                         chat_id uuid REFERENCES chat(id) on delete restrict,
                         message_text text,
                         create_date timestamp,
                         status varchar(32)
);

-- Создание таблицы "files" для хранения информации о файлах, прикрепленных к сообщениям
-- CREATE TABLE files (
--                        id uuid PRIMARY KEY,
--                        message_id INT REFERENCES message(id),
--                        file_ Varchar(255)
-- );

INSERT INTO public.friends (user_id, friend_list)
VALUES ('9e365c6b-a954-4706-8204-f31734d3bd7b', '{"d6d42b30-733b-438c-87d7-711dca78bf0c", "947a34c1-a873-4fdc-aaa3-284641363821"}');

INSERT INTO public.friends (user_id, friend_list)
VALUES ('d6d42b30-733b-438c-87d7-711dca78bf0c', '{"947a34c1-a873-4fdc-aaa3-284641363821", "9e365c6b-a954-4706-8204-f31734d3bd7b"}');

INSERT INTO public.friends (user_id, friend_list)
VALUES ('947a34c1-a873-4fdc-aaa3-284641363821', '{"9e365c6b-a954-4706-8204-f31734d3bd7b", "d6d42b30-733b-438c-87d7-711dca78bf0c"}');