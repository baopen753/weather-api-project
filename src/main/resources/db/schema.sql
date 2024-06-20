
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
    precipitation integer,
    temperature   integer,
    location_code varchar(12) not null,
    status        varchar(50),
    primary key (hour_of_day, location_code)
);

create table if not exists realtime_weather
(
    humidity      integer,
    precipitation integer,
    temperature   integer,
    wind_speed    integer,
    last_updated  datetime(6),
    location_code varchar(12) not null,
    status        varchar(50),
    primary key (location_code)
);



