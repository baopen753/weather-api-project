


INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (false,false,'VN','HN_QB','Quangbinh','Socialist Republic of Vietnam','South');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (true,false,'USA','USA_LA','Los Angeles','United States of America','California');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (true,false,'VN','VN_BD','Binhduong','Socialist Republic of Vietnam','North');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (true,false,'VN','VN_DN','Danang','Socialist Republic of Vietnam','Middle');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (false,false,'VN','VN_HB','Hoabinh','The Republic Socialist of Vietnam','North');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (true,false,'VN','VN_HCM','Ho Chi Minh City','Socialist Republic of Vietnam','Middle');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (false,false,'VN','VN_HG','Hagiang','Socialist Republic of Vietnam','North');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (false,false,'VN','VN_HL','Halong','Socialist Republic of Vietnam','North');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (true,false,'VN','VN_HN','Hanoi','Vietnam','North');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (false,false,'VN','VN_HP','Haiphong','Socialist Republic of Vietnam','North');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (false,false,'VN','VN_HU','Hue','Socialist Republic of Vietnam','Middle');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (false,false,'VN','VN_NA','Nghean','Socialist Republic of Vietnam','North');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (true,false,'VN','VN_NT','Nhatrang','Socialist Republic of Vietnam','Middle');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (false,false,'VN','VN_QB','Quangbinh','Socialist Republic of Vietnam','South');

INSERT INTO locations(enabled,trashed,country_code,code,city_name,country_name,region_name)
VALUES (false,false,'VN','VN_CM','Camau','Socialist Republic of Vietnam','South');


INSERT INTO realtime_weather(humidity, precipitation, temperature, wind_speed, last_updated, location_code, status)
VALUES (80,80,80,80,now(),'VN_QB','1st updated from VN_QB');

INSERT INTO realtime_weather(humidity, precipitation, temperature, wind_speed, last_updated, location_code, status)
VALUES (60,60,60,60,now(),'USA_LA','1st updated from USA_LA');

INSERT INTO realtime_weather(humidity, precipitation, temperature, wind_speed, last_updated, location_code, status)
VALUES (70,70,70,70,now(),'VN_HCM','1st updated from VN_HCM');

INSERT INTO realtime_weather(humidity, precipitation, temperature, wind_speed, last_updated, location_code, status)
VALUES (65,65,65,65,now(),'VN_HN','1st updated from VN_HN');


INSERT INTO hourly_weather(hour_of_day, precipitation, temperature, location_code, status)
VALUES (9, 65, 65, 'VN_HN','Cloudy');

INSERT INTO hourly_weather(hour_of_day, precipitation, temperature, location_code, status)
VALUES (12, 70, 70, 'VN_HCM','Sunny');

INSERT INTO hourly_weather(hour_of_day, precipitation, temperature, location_code, status)
VALUES (8, 60, 60, 'USA_LA','Cloudy');

INSERT INTO hourly_weather(hour_of_day, precipitation, temperature, location_code, status)
VALUES (12, 80, 80, 'VN_QB','Hot');


INSERT INTO daily_weather(day_of_month, max_temperature,min_temperature, month, precipitation, status, location_code)
VALUES (12,50,34,2,56,'Cosy','VN_HN');

INSERT INTO daily_weather(day_of_month, max_temperature,min_temperature, month, precipitation, status, location_code)
VALUES (13,40,20,2,56,'Cold','VN_HN');

INSERT INTO daily_weather(day_of_month, max_temperature,min_temperature, month, precipitation, status, location_code)
VALUES (14,80,70,2,56,'Hot','VN_HN');



INSERT INTO daily_weather(day_of_month, max_temperature,min_temperature, month, precipitation, status, location_code)
VALUES (12,50,34,2,56,'Cosy','VN_HCM');

INSERT INTO daily_weather(day_of_month, max_temperature,min_temperature, month, precipitation, status, location_code)
VALUES (13,40,20,2,56,'Cold','VN_HCM');

INSERT INTO daily_weather(day_of_month, max_temperature,min_temperature, month, precipitation, status, location_code)
VALUES (14,80,70,2,56,'Hot','VN_HCM');