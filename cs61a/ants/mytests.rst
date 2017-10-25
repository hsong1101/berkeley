This file holds the tests that you create. Remember to import the python file(s)
you wish to test, along with any other modules you may need.
Run your tests with "python3 ok -t --suite SUITE_NAME --case CASE_NAME -v"
--------------------------------------------------------------------------------

Suite 1

	>>> from ants import *

	Case 1
		>>> from ants import *
		>>> hive, layout = Hive(AssaultPlan()), dry_layout
		>>> dimensions = (1, 9)
		>>> colony = AntColony(None, hive, ant_types(), layout, dimensions)
		>>> # Testing nearest_bee
		>>> thrower = ThrowerAnt()
		>>> colony.places['tunnel_0_0'].add_insect(thrower)
		>>> place = colony.places['tunnel_0_0']
		>>> near_bee = Bee(2)
		>>> far_bee = Bee(2)
		>>> colony.places["tunnel_0_3"].add_insect(near_bee)
		>>> colony.places["tunnel_0_6"].add_insect(far_bee)
		>>> hive = colony.hive
		>>> thrower.nearest_bee(hive) is far_bee
		False
		>>> thrower.nearest_bee(hive) is near_bee
		True
