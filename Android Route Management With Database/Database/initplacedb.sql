DROP TABLE places;

CREATE TABLE places (name TEXT PRIMARY KEY,description TEXT,category TEXT,address_title TEXT,address_street TEXT,elevation TEXT,latitude TEXT,longitude TEXT);


INSERT INTO places VALUES('ASU-West','Home of ASUs Applied Computing Program','School','ASU West Campus','13591 N 47th Ave$Phoenix AZ 85051','1100.0','33.608979','-112.159469');
INSERT INTO places VALUES('UAK-Anchorage','University of Alaskas largest campus','School','University of Alaska at Anchorage','290 Spirit Dr$Anchorage AK 99508','0.0','61.189748','-149.826721');
