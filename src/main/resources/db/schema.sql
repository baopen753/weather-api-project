
create table if not exists locations
(
    enabled      bit          not null,
    trashed      bit          not null,
    country_code varchar(6)   not null,
    code         varchar(12)  not null,
    city_name    varchar(128) not null,
    country_name varchar(128) not null,
    region_name  varchar(128) not null,
    primary key (code)
);

create table if not exists hourly_weather
(
    hour_of_day   integer     not null,
    precipitation integer     not null,
    temperature   integer     not null,
    location_code varchar(12) not null,
    status        varchar(50) not null,
    primary key (hour_of_day, location_code)
);

create table if not exists realtime_weather
(
    humidity      integer  not null,
    precipitation integer  not null,
    temperature   integer  not null,
    wind_speed    integer  not null,
    last_updated  datetime(6) not null,
    location_code varchar(12) not null,
    status        varchar(50) not null,
    primary key (location_code)
);


create  table if not exists daily_weather
(
   day_of_month   integer      not null,
   month          integer      not null,
   min_temp       integer      not null,
   max_temp       integer      not null,
   precipitation  integer      not null,
   status         varchar(50)  not null,
   location_code varchar(12)   not null,
   primary key (day_of_month, month, location_code);
);







