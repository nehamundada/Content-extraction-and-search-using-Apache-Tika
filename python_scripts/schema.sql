CREATE TABLE address_info (
lat text,
long text,
street text,
city text,
state text,
county text, 
country text,
zipcode text,
countryCode text,
population int
);
CREATE INDEX idx_1_address_info on address_info(lat);
CREATE INDEX idx_2_address_info on address_info(long);
CREATE INDEX idx_3_address_info on address_info(city);
CREATE INDEX idx_4_address_info on address_info(state);


CREATE TABLE jobs (
id  text primary key,
hash  text,
postedDate text,
Location text,
department text,
Title text,
salary text,
start text,
duration text,
jobtype text,
applications text,
company text,
contactPerson text,
phoneNumber text,
faxNumber text,
Location2 text,
latitude text,
longitude text,
firstSeenDate text,
url text,
lastSeenDate text);

CREATE INDEX idx_jobs_1 on jobs(Location);
CREATE INDEX idx_jobs_2 on jobs(department);
CREATE INDEX idx_jobs_3 on jobs(jobtype);
CREATE INDEX idx_jobs_4 on jobs(longitude);
CREATE INDEX idx_jobs_5 on jobs(latitude);
CREATE INDEX idx_jobs_6 on jobs(Title);
CREATE INDEX idx_jobs_7 on jobs(postedDate);

