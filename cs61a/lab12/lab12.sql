.read sp17data.sql
.read fa17data.sql

CREATE TABLE obedience AS
  SELECT seven, denero, hilfinger FROM students;

CREATE TABLE smallest_int AS
  SELECT time, smallest FROM students WHERE smallest > 18 ORDER BY smallest LIMIT 20;

CREATE TABLE greatstudents AS
  SELECT a.date, a.color, a.pet, a.number, b.number from students as a, sp17students as b where a.date = b.date and a.color = b.color and a.pet = b.pet;
.
CREATE TABLE sevens AS
  SELECT a.seven FROM students AS a, checkboxes AS b WHERE a.time = b.time AND a.number = 7 and b.'7' = 'True';

CREATE TABLE matchmaker AS
  SELECT a.pet, a.song, a.color, b.color from students as a, students as b where a.pet = b.pet and a.song = b.song and a.time <> b.time;
