create table Tour
(
    id          serial primary key         not null,
    name        varchar(128) unique        not null,
    description varchar(1024) default ('') not null
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
    foreign key (tourId) references Tour
);

insert into Tour
values (DEFAULT, 'Car Tour');
insert into Tour
values (DEFAULT, 'Bicycle Tour');
insert into Tour
values (DEFAULT, 'Walking');


insert into Log (tourId, startTime, endTime, startLocation, endLocation, rating, meansOfTransport)
values (1, '2021-01-01 12:0:00-0', '2021-01-02 12:0:00-0', 'Wien', 'Wiener Neustadt', 5, 'Car');
insert into Log (tourId, startTime, endTime, startLocation, endLocation, rating, meansOfTransport)
values (1, '2021-01-02 12:0:00-0', '2021-01-02 15:0:00-0', 'Wiener Neustadt', 'Graz', 5, 'Car');
insert into Log (tourId, startTime, endTime, startLocation, endLocation, rating, meansOfTransport)
values (1, '2021-01-01 16:0:00-0', '2021-01-02 17:0:00-0', 'Graz', 'Wien', 5, 'Car');
