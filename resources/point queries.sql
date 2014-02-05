-- Table: temp_points

-- DROP TABLE temp_points;

CREATE TABLE temp_points
(
  id SERIAL NOT NULL,
  latitude double precision,
  longitude double precision,
  weight double precision,
  CONSTRAINT temp_points_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);


--  in psql => 
-- \copy temp_points (latitude, longitude, weight) from 'C:\\src\\Geocoder\\resources\\test_weighted_points.csv' with HEADER DELIMITER AS ',' CSV

SELECT AddGeometryColumn ('temp_points','geom',4326,'POINT',2);

update temp_points set geom = st_setsrid(st_makepoint(latitude, longitude),4326)


select count (*) 
from temp_points
where ST_Contains((select geom from city_boundary), geom)


select st_astext(st_centroid(geom)) from city_boundary

select st_astext(geom) from temp_points limit 10

