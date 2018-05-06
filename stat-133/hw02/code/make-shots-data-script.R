# title: "Data preparation for charts"
# description: "Five csv files are imported and added column 'minute' at which a shot was made by a player
#               After that, all five files are put together to create one super table. Each summary file was output"
# input: 'andre-iguodala.csv, kevin-durant.csv, stephen-curry.csv, draymond-green.csv, klay-thompson.csv'
# output: 'andre-iguodala-summary.txt, kevin-durant-summary.txt, stephen-curry-summary.txt, draymond-green-summary.txt, klay-thompson-summary.txt, shots-data.csv, shots-data-summary.txt'

############################################

iguodala = read.csv("../data/andre-iguodala.csv", stringsAsFactors = FALSE)
durant = read.csv("../data/kevin-durant.csv", stringsAsFactors = FALSE)
curry = read.csv("../data/stephen-curry.csv", stringsAsFactors = FALSE)
green = read.csv("../data/draymond-green.csv", stringsAsFactors = FALSE)
thompson = read.csv("../data/klay-thompson.csv", stringsAsFactors = FALSE)

iguodala$name = "Andre Iguodala"
durant$name = "Kevin Durant"
curry$name = "Stephen Curry"
green$name = "Draymond Green"
thompson$name = "Klay Thompson"

iguodala[iguodala['shot_made_flag'] == "n", 'shot_made_flag'] = "missed shot"
iguodala[iguodala['shot_made_flag'] == "y", 'shot_made_flag'] = "made shot"
durant[durant['shot_made_flag'] == "n", 'shot_made_flag'] = "missed shot"
durant[durant['shot_made_flag'] == "y", 'shot_made_flag'] = "made shot"
curry[curry['shot_made_flag'] == "n", 'shot_made_flag'] = "missed shot"
curry[curry['shot_made_flag'] == "y", 'shot_made_flag'] = "made shot"
green[green['shot_made_flag'] == "n", 'shot_made_flag'] = "missed shot"
green[green['shot_made_flag'] == "y", 'shot_made_flag'] = "made shot"
thompson[thompson['shot_made_flag'] == "n", 'shot_made_flag'] = "missed shot"
thompson[thompson['shot_made_flag'] == "y", 'shot_made_flag'] = "made shot"

iguodala = mutate(iguodala, minute = period*12 - minutes_remaining)
durant = mutate(iguodala, minute = period*12 - minutes_remaining)
curry = mutate(iguodala, minute = period*12 - minutes_remaining)
green = mutate(iguodala, minute = period*12 - minutes_remaining)
thompson = mutate(iguodala, minute = period*12 - minutes_remaining)

sink(file = '../output/andre-iguodala-summary.txt')
summary(iguodala)
sink()

sink(file = '../output/kevin-durant-summary.txt')
summary(iguodala)
sink()

sink(file = '../output/stephen-curry-summary.txt')
summary(iguodala)
sink()

sink(file = '../output/draymond-green-summary.txt')
summary(iguodala)
sink()

sink(file = '../output/klay-thompson-summary.txt')
summary(iguodala)
sink()

shots_data = rbind(iguodala, durant, curry, green, thompson)
write.csv(shots_data, "../data/shots-data.csv")

sink(file = '../output/shots-data-summary.txt')
summary(shots_data)
sink()
























