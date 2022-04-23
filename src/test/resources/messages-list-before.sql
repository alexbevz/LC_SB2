delete from message;

insert into message(id, text, tag, user_id)
values (1, 'first', 'tag1', 1),
       (2, 'second', 'tag2', 1),
       (3, 'third', 'tag3', 2),
       (4, 'fourth', 'tag1', 2);

alter sequence hibernate_sequence restart with 10;