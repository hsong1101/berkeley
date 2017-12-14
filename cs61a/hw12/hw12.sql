CREATE TABLE parents AS
  SELECT "abraham" AS parent, "barack" AS child UNION
  SELECT "abraham"          , "clinton"         UNION   
  SELECT "delano"           , "herbert"         UNION
  SELECT "fillmore"         , "abraham"         UNION
  SELECT "fillmore"         , "delano"          UNION
  SELECT "fillmore"         , "grover"          UNION
  SELECT "eisenhower"       , "fillmore";

CREATE TABLE dogs AS
  SELECT "abraham" AS name, "long" AS fur, 26 AS height UNION
  SELECT "barack"         , "short"      , 52           UNION
  SELECT "clinton"        , "long"       , 47           UNION
  SELECT "delano"         , "long"       , 46           UNION
  SELECT "eisenhower"     , "short"      , 35           UNION
  SELECT "fillmore"       , "curly"      , 32           UNION
  SELECT "grover"         , "short"      , 28           UNION
  SELECT "herbert"        , "curly"      , 31;

CREATE TABLE sizes AS
  SELECT "toy" AS size, 24 AS min, 28 AS max UNION
  SELECT "mini"       , 28       , 35        UNION
  SELECT "medium"     , 35       , 45        UNION
  SELECT "standard"   , 45       , 60;

-------------------------------------------------------------
-- PLEASE DO NOT CHANGE ANY SQL STATEMENTS ABOVE THIS LINE --
-------------------------------------------------------------

-- Create a table by_height that has a column of the names of all dogs that have a parent, ordered by the height of the parent from tallest parent to shortest parent.

-- All dogs with parents ordered by decreasing height of their parent
CREATE TABLE by_height AS
  SELECT child from dogs, parents where parent = name order by -height;
  
  
-- Create a size_of_dogs table with two columns, one for each dog's name and another for its size.
-- The size of each dog
CREATE TABLE size_of_dogs AS
  SELECT name, size from dogs, sizes where height > min and height <= max;




-- Sentences about siblings that are the same size
CREATE TABLE sentences AS
  WITH
    siblings(first, second) AS (
      SELECT a.child, b.child FROM parents AS a, parents AS b
        WHERE a.parent = b.parent AND a.child < b.child
    )
  SELECT first || " and " || second || " are " || a.size || " siblings"
    FROM siblings, size_of_dogs AS a, size_of_dogs AS b
    WHERE a.size = b.size AND a.name = first AND b.name = second;


-- Ways to stack 4 dogs to a height of at least 170, ordered by total height
CREATE TABLE stacks AS
  with 
    stack(dogs, total, n, max) as (
      select name, height, 1, height from dogs union
      select dogs || ', ' || name, total + height, n + 1, height 
        from stack, dogs where n < 4 and max < height
    )
select dogs, total from stack 
  where total >= 170 and n = 4 order by total;
