create table Tour
(
    id          serial primary key         not null,
    name        varchar(128)  default ('') not null,
    description varchar(1024) default ('') not null,
    distance    float                      not null
);

create table Log
(
    id               serial primary key not null,
    tourId           int                not null,
    startTime        timestamp          not null,
    endTime          timestamp          not null,
    startLocation    varchar(64)        not null,
    endLocation      varchar(64)        not null,
    rating           int                not null,
    meansOfTransport varchar(64)        not null,
    distance         float              not null,
    foreign key (tourId) references Tour on delete cascade
);

insert into Tour
values (DEFAULT, 'Car Tour', DEFAULT, 6);
insert into Tour
values (DEFAULT, 'Bicycle Tour', DEFAULT, 0);
insert into Tour
values (DEFAULT, 'Walking', DEFAULT, 0);


insert into Log (tourId, startTime, endTime, startLocation, endLocation, rating, meansOfTransport, distance)
values (1, '2021-01-01 12:0:00-0', '2021-01-02 12:0:00-0', 'Wien', 'Wiener Neustadt', 5, 'Car', 1);
insert into Log (tourId, startTime, endTime, startLocation, endLocation, rating, meansOfTransport, distance)
values (1, '2021-01-02 12:0:00-0', '2021-01-02 15:0:00-0', 'Wiener Neustadt', 'Graz', 5, 'Car', 2);
insert into Log (tourId, startTime, endTime, startLocation, endLocation, rating, meansOfTransport, distance)
values (1, '2021-01-01 16:0:00-0', '2021-01-02 17:0:00-0', 'Graz', 'Wien', 5, 'Car', 3);
