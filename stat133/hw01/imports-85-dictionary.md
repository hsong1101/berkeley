Title: 1985 Auto Imports Database

Source Information:
   -- Creator/Donor: Jeffrey C. Schlimmer (Jeffrey.Schlimmer@a.gp.cs.cmu.edu)
   -- Date: 19 May 1987
   -- Sources:
     1) 1985 Model Import Car and Truck Specifications, 1985 Ward's
        Automotive Yearbook.
     2) Personal Auto Manuals, Insurance Services Office, 160 Water
        Street, New York, NY 10038 
     3) Insurance Collision Report, Insurance Institute for Highway
        Safety, Watergate 600, Washington, DC 20037

Description:
      This data set consists of three types of entities: (a) the
      specification of an auto in terms of various characteristics, (b)
      its assigned insurance risk rating, (c) its normalized losses in use
      as compared to other cars.  The second rating corresponds to the
      degree to which the auto is more risky than its price indicates.
      Cars are initially assigned a risk factor symbol associated with its
      price.   Then, if it is more risky (or less), this symbol is
      adjusted by moving it up (or down) the scale.  Actuarians call this
      process "symboling".  A value of +3 indicates that the auto is
      risky, -3 that it is probably pretty safe.

The data imported has 26 features and total of 205 instances.

1. symboling(double)
2. normalized_losses(character)
3. make(character)
4. fuel_type(character)
5. aspiration(character)
6. num_of_doors(character)
7. body_style(character)
8. drive_wheel(character)
9. engine_location(character)
10. wheel_base(double)
11. length(double)
12. width(double)
13. height(double)
14. curb_weight(integer)
15. engine_type(character)
16. num_of_cylinders(character)
17. engine_size(integer)
18. fuel_system(character)
19. bore(double)
20. stroke(double)
21. compression_ratio(integer)
22. horsepower(integer)
23. peak_rpm(integer)
24. city_mpg(integer)
25. highway_mpg(integer)
26. price(integer)

Some of the features include NA values. (2, 6, 19, 20, 22, 23, 26)
