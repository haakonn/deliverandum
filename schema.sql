CREATE TABLE assignments (
	id SERIAL PRIMARY KEY,
	courseName VARCHAR,
	name VARCHAR,
	beginTime TIMESTAMP,
	endTime TIMESTAMP
);

CREATE TABLE deliveries (
	id SERIAL PRIMARY KEY,
	assignmentId SERIAL REFERENCES assignments(id) ON DELETE CASCADE,
	username VARCHAR,
	deliveredAt TIMESTAMP,
	assignedTo VARCHAR,
	state VARCHAR,
	grade VARCHAR,
	gradeComment VARCHAR
);

