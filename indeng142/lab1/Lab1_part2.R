# 
# Make sure your working directory was set as "Source file location".
# Make sure your data files are in the same folder as this R script.
#
# install.packages(c("dplyr", "ggplot2", "GGally"))
library(dplyr)
library(ggplot2)
library(GGally)
#install.packages("car")
library(car) # for VIF

# Load data:
wine <- read.csv("wine_agg.csv") 

str(wine)
head(wine)

# Plot scatter matrix
ggscatmat(wine, columns = 2:9, alpha = 0.8)


# split into training and test sets 

wine.train <- filter(wine, Year <= 1985) 
head(wine.train)
tail(wine.train)
wine.test <- filter(wine, Year > 1985)

# train the model
#lm(y~x1+x2+...,data)
mod1 <- lm(LogAuctionIndex ~ WinterRain + HarvestRain + GrowTemp + HarvestTemp + Age + FrancePop + USAlcConsump, 
           data = wine.train)
summary(mod1)

# compute OSR^2

winePredictions <- predict(mod1, newdata=wine.test)
# this builds a vector of predicted values on the test set
SSE = sum((wine.test$LogAuctionIndex - winePredictions)^2)
SST = sum((wine.test$LogAuctionIndex - mean(wine.train$LogAuctionIndex))^2)
OSR2 = 1 - SSE/SST


# Confidence interval plot
ggcoef(
  mod1,
  vline_color = "red",
  vline_linetype =  "solid",
  errorbar_color = "blue",
  errorbar_height = .25,
  exclude_intercept = TRUE
)

vif(mod1)

# A better model...
# Remove FrancePop
mod2 <- lm(LogAuctionIndex ~ WinterRain + HarvestRain + GrowTemp + HarvestTemp + Age + USAlcConsump, 
           data = wine.train)
summary(mod2)
vif(mod2)

# Remove USAlcConsump
mod3 <- lm(LogAuctionIndex ~ WinterRain + HarvestRain + GrowTemp + HarvestTemp + Age, 
           data = wine.train)
summary(mod3)
vif(mod3)

# Remove HarvestTemp
mod4 <- lm(LogAuctionIndex ~ WinterRain + HarvestRain + GrowTemp + Age, 
           data = wine.train)
summary(mod4)
vif(mod4)


# Categorical Variables
wine.new <- read.csv("wine_disagg.csv")
wine.new.train <- filter(wine.new, Year <= 1985)
wine.new.test <- filter(wine.new, Year > 1985)

str(wine.new)
head(wine.new)

# Regression use new data
modOld <- lm(LogAuction ~ WinterRain + HarvestRain + GrowTemp + Age, 
             data = wine.new.train)
summary(modOld)


# Plot data
# ggplot(), geom_line() are from package "ggplot2"
# see http://ggplot2.org for reference 
# ggplot(data, aes(x = x_data, y = y_data))
# +geom_line() make line plots
# +geom_point() make point plots
# xlab(labelx),ylab(labely) axis label
# ggtitle(title)

ggplot(wine.new, aes(x=Year, y=LogAuction, color=Winery)) + geom_line() +
  xlab("Vintage Year") + ylab("log(2015 Auction Price)")

# Your turn: plot same data, make point plot,  name the axis and title.
ggplot(wine.new, aes(x=Year, y=LogAuction, color=Winery)) + geom_point() +
  xlab("Vintage Year") + ylab("log(2015 Auction Price)")+ggtitle("MyTitle")


# Two wineries
wine.two <- filter(wine.new, Winery == "Cheval Blanc" |Winery == "Cos d'Estournel")
ggplot(wine.two, aes(x=Year, y=LogAuction, color=Winery)) + geom_line() +
  xlab("Vintage Year") + ylab("log(2015 Auction Price)")

wine.two.train <- filter(wine.two, Year <= 1985)
wine.two.test <- filter(wine.two, Year > 1985)
head(wine.two.train)
tail(wine.two.train)
modTwo <- lm(LogAuction ~ Winery+WinterRain + HarvestRain + GrowTemp + Age, 
             data = wine.two.train)
summary(modTwo)

# To use a factor/categorical variable like Winery, 
# we don't actually have to do anything special 
# We can just provide it to lm, and R will handle all
# the logistics
modNew <- lm(LogAuction ~ Winery + Age + WinterRain + HarvestRain + GrowTemp, 
             data=wine.new.train)
summary(modNew)

# compute OSR^2
wineNewPredictions <- predict(modNew, newdata=wine.new.test)
SSE = sum((wine.new.test$LogAuction - wineNewPredictions)^2)
SST = sum((wine.new.test$LogAuction - mean(wine.new.train$LogAuction))^2)
OSR2 = 1 - SSE/SST

