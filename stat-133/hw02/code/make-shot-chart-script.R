# title: "Charts for Player Scores"
# description: "Each player's score will have its shots graphed using ggplot point as well as one whole graph containing the five players shot charts"
# input: 'shots-data.csv, image file for background of charts'
# output: '5 pdf files for each player's shot chart and 1 pdf for chart containing all 5'

###########################################

library(ggplot2)
library(grid)
library(jpeg)


shots_data = read.csv('../data/shots-data.csv')

court_file = "../images/nba-court.jpg"
court_image = rasterGrob(
  readJPEG(court_file),
  width = unit(1, "npc"),
  height = unit(1, "npc"))

pdf(file = "../images/andre-iguodala-shot-chart.pdf", width = 6.5, height = 5)
ggplot(shots_data[shots_data['name'] == 'Andre Iguodala', ]) +
  annotation_custom(court_image, -250, 250, -50, 420) +
  geom_point(aes(x = x, y = y, color = shot_made_flag)) + 
  ylim(-50, 420) +
  ggtitle('Shot Chart : Andre Iguodala (2016 season)') +
  theme_minimal()
dev.off()

pdf(file = "../images/kevin-durant-shot-chart.pdf", width = 6.5, height = 5)
ggplot(shots_data[shots_data['name'] == 'Kevin Durant', ]) +
  annotation_custom(court_image, -250, 250, -50, 420) +
  geom_point(aes(x = x, y = y, color = shot_made_flag)) + 
  ylim(-50, 420) +
  ggtitle('Shot Chart : Kevin Durant (2016 season)') +
  theme_minimal()
dev.off()

pdf(file = "../images/stephen-curry-shot-chart.pdf", width = 6.5, height = 5)
ggplot(shots_data[shots_data['name'] == 'Stephen Curry', ]) +
  annotation_custom(court_image, -250, 250, -50, 420) +
  geom_point(aes(x = x, y = y, color = shot_made_flag)) + 
  ylim(-50, 420) +
  ggtitle('Shot Chart : Stephen Curry (2016 season)') +
  theme_minimal()
dev.off()

pdf(file = "../images/draymond-green-shot-chart.pdf", width = 6.5, height = 5)
ggplot(shots_data[shots_data['name'] == 'Draymond Green', ]) +
  annotation_custom(court_image, -250, 250, -50, 420) +
  geom_point(aes(x = x, y = y, color = shot_made_flag)) + 
  ylim(-50, 420) +
  ggtitle('Shot Chart : Draymond Green (2016 season)') +
  theme_minimal()
dev.off()

pdf(file = "../images/klay-thompson-shot-chart.pdf", width = 6.5, height = 5)
ggplot(shots_data[shots_data['name'] == 'Klay Thompson', ]) +
  annotation_custom(court_image, -250, 250, -50, 420) +
  geom_point(aes(x = x, y = y, color = shot_made_flag)) + 
  ylim(-50, 420) +
  ggtitle('Shot Chart : Klay Thompson (2016 season)') +
  theme_minimal()
dev.off()

pdf(file = "../images/gsw-shot-chart.pdf", width = 8, height = 7)
ggplot(data = shots_data) +
  annotation_custom(court_image, -250, 250, -50, 420) +
  geom_point(aes(x = x, y = y, color = shot_made_flag)) +
  theme(legend.position = "top", panel.background = element_rect(fill = NA), strip.background = element_rect(fill = NA), legend.key = element_rect(fill = NA), legend.title = element_blank()) +
  facet_wrap(~ name) +
  ylim(-50, 420) +
  ggtitle('Shot Charts: GSW (2016 season)')
dev.off()















