create table user_subscriptions
(
    channel_id    int8 not null references "user",
    subscriber_id int8 not null references "user",
    primary key (channel_id, subscriber_id)
);