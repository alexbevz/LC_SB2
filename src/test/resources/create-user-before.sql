delete
from user_role;
delete
from "user";

insert into "user"(id, active, password, username)
values (1, true, '$2a$06$7Y5i60TT5RwmKV7/OK2DYekwtJl42Fe.8SXfa9IXCRTl2zfUueU1O', 'admin'),
       (2, true, '$2a$06$7Y5i60TT5RwmKV7/OK2DYekwtJl42Fe.8SXfa9IXCRTl2zfUueU1O', 'mike');

insert into user_role(user_id, roles)
values (1, 'USER'),
       (1, 'ADMIN'),
       (2, 'USER');