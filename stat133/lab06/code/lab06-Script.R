# Title: Data manipulation
# Description: Will deal more with data manipulation and visualization using dplyr and ggplot
# Input(s): nba2017-player.csv
# Output(s): what are the main outputs (list of outputs)
# Author(s): Hanmaro Song
# Date: 03-01-2018

library(dplyr)
library(ggplot2)
library(readr)

dat = read_csv('../data/nba2017-players.csv')
warriors = data.frame(arrange(filter(dat, team == "GSW"), salary))
write.csv(warriors, '../data/warriors.csv', row.names=FALSE)

lakers = data.frame(arrange(filter(dat, team == "LAL"), desc(experience)))
write.csv(lakers, '../data/lakers.csv', row.names=FALSE)

# Use ls in terminal in data directory


#------------------------------#

sink(file = '../output/summary-height-weight.txt')
summary(dat[ ,c('height', 'weight')])
sink()

sink(file = '../output/data-structure.txt')
str(dat)
sink()

sink(file = '../output/summary-warriors.txt')
summary(warriors)
sink()

sink(file = '../output/summary-lakers.txt')
summary(lakers)
sink()


#------------------------------#

png(filename = '../images/scatterplot-height-weight.png')
plot(dat$height, dat$weight, pch = 20, 
     xlab = 'Height', ylab = 'Weight')
dev.off()


png(filename = '../images/scatterplot-height-weight-higher-resolution.png', height = 1200, width = 1200, res = 288)
plot(dat$height, dat$weight, pch = 20, xlab = 'Height', ylab = 'Weight')
dev.off()

jpeg(filename = '../images/histogram-age.jpeg', width = 600, height = 400)
hist(dat$age)
dev.off()


pdf(file = '../images/histogram-age.pdf', width = 7, height = 5)
hist(dat$age)
dev.off()

#------------------------------#

gg_pts_salary = ggplot(dat, aes(x = points, y = salary)) + geom_point()
ggsave(filename = '../images/points_salary.pdf', width = 7, height = 5)

gg_ht_wt_position = ggplot(dat, aes(height, weight)) + geom_point() + facet_wrap(~ position)
ggsave(filename ='../images/height_weight_by_position.pdf', width = 6, height = 4)

#------------------------------#
# display the player names of Lakers 'LAL'
dat %>% 
  filter(team=='LAL') %>%
  select('player')


#display the name and salary of GSW point guards 'PG'.
dat %>% 
  filter(position=='PG') %>%
  filter(team == 'GSW') %>%
  select('player', 'salary')



#dislay the name, age, and team, of players with more than 10 years of experience, making 10 million dollars or less.
dat %>% 
  filter(experience > 10) %>%
  filter(salary <= 10000000) %>%
  select('player', 'age', 'team')


#select the name, team, height, and weight, of rookie players, 20 years old, 
#displaying only the first five occurrences (i.e. rows).
dat %>% 
  filter(age == 20) %>%
  select('player', 'team', 'height', 'weight') %>%
  head(5)


#create a data frame gsw_mpg of GSW players, that contains variables for 
#player name, experience, and min_per_game (minutes per game), sorted by min_per_game (in descending order).
dat %>%
  filter(team=='GSW') %>%
  mutate(min_per_game = minutes / games) %>%
  arrange(desc(min_per_game)) %>%
  select('player', 'experience', 'min_per_game') %>%
  data.frame()


#display the average triple points by team, in ascending order, of the bottom-5 teams (worst 3pointer teams).
dat %>%
  group_by(team) %>%
  summarise(avg = mean(points3)) %>%
  arrange(avg) %>%
  head(5)


#obtain the mean and standard deviation of age, for Power Forwards, with 5 and 10 years (including) of experience.
dat %>%
  filter(position == "PG") %>%
  filter(experience > 4 & experience < 11) %>%
  summarise(avg = mean(age), standard_deviation = sd(age))


















