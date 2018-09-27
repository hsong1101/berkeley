###############
# This exercise has 4 parts:
# 1. Introduction to R 
# 2. Setting the working directory
# 3. Loading data from a csv file
# 4. Basic data manipulations - Data frames

#####################
# 1. INTRODUCTION TO R

# Everything following a hash sign is a comment

# Open the file "Lab1.R"

# File -> Open Document
# Navigate to where you saved the files you downloaded for this class, select this script file, and click open.


# BASIC CALCULATIONS IN R

# Always type commands after the greater-than sign in the console

# Or run lines in a script file
# Highlight the line
# Mac, Command-Enter
# PC, Control-r.

# Try running these lines:
8*6
2^16 

# The [1] is just R's way of labeling the output.

# Incomplete command. R will show you a + sign - hit escape
2^
  
# Scroll through previous commands using up and down arrows
  
# Your turn:
# Compute 283455 divided by 3.67778
  
  
  
# FUNCTIONS AND VARIABLES

# A function can take in several arguments or inputs, and returns an output value.
# Ex: square root and absolute value functions:
sqrt(2)
abs(-65)

# Thousands of functions in R
# http://www.statmethods.net/management/functions.html

# Get help on any function:
?sqrt

# Save the output to a variable. 
SquareRoot2 <- sqrt(2) 

# or 
SquareRoot2Two = sqrt(2)

# See the value by typing its name
SquareRoot2Two

# Basic variable naming rules: 
# Don't use spaces (periods or capital letters instead)
# Don't start names with a number
# Variable names are case sensitive - capital and lowercase letters are different

# list of all of the variables you have created.
ls()

# Your turn:
# Create a variable called "SecondsDay" that is equal to the number of seconds in a day, and output its value.


#####################
# 2. SETTING THE WORKING DIRECTORY.

# (Make sure the csv file and the .R file are in the same folder)
# There are three methods:
# Method 1. (Easiest)
# Go to Session -> Set Working Directory -> To Source File Location.
# This changes the working directory to the one in which you open this file.

# Method 2.
# Go to Session -> Set Working Directory -> Choose directory -> Select the location of the folder you want.

# Method 3.
# Session -> Set Working Directory -> To Files Pane Location 
# will set the working directory to the folder you see on the right side.

# To change that, click on the "..." symbol on the line with the Home symbol, and 
# navigate to the desired folder.

# Method 4.
# By command
getwd()
setwd("/Users/paulgrigas/Dropbox/Teaching/IEOR242-Spring2018/Week1")
getwd()

#####################
# 3. LOADING CSV FILES

# Read in the data file 
WHO = read.csv("WHO.csv")

# On the right hand side, in the Environment tab, click on the table symbol on the
# WHO line -> you should see the data in the familiar Excel format.
# Delete an object
rm(WHO)

# Structure of the data
str(WHO)

# Recent statistics from the World Health Organization (WHO)
# The variables are: 
# the name of the country
# the region the country is in
# the population in thousands
# the percentage of the population under 15 and over 60
# the fertility rate (average number of children per woman)
# the Life Expectancy in years
# the Child Mortality rate (the number of children who die by age 5 per 1000 births)
# the number of cellular subscribers per 100 population
# the literacy rate among adults aged >= 15
# the gross national income per capita
# the percentage of male children enrolled in primary school
# the percentage of female children enrolled in primary school

# Statistical summary of the data:
summary(WHO)

# Display a few data points at the "head" (start) of the dataset
# The first 6 records
head(WHO)
# The last few records
tail(WHO)

#####################
# Use packages for more functionality
library()   # see all packages installed 
search()    # see packages currently loaded
# install.packages('dplyr')
?filter
library(dplyr)
?filter

#####################
# 4. BASIC DATA MANIPULATIONS AND ANALYSIS - DATA FRAMES.
# Subset with only the countries in Europe
WHO.Europe <- filter(WHO, Region == "Europe")

str(WHO.Europe)

# Other subsets:
WHO.AsiaEurope <- filter(WHO, Region == "Europe" | Region == "South-East Asia" | Region == "Eastern Mediterranean")

str(WHO.AsiaEurope)
summary(WHO.AsiaEurope$Region)

# Save data frame to a csv file
write.csv(WHO.AsiaEurope, "WHO_AsiaEurope.csv", row.names=FALSE)

# Your turn:
# How many countries have population greater than 50 million? 
Answer <- filter(WHO, Population > 50000)
nrow(Answer)


# BASIC DATA ANALYSIS

# To access a variable in a data frame, you always have to link it to the data frame with the dollar sign.
LifeExpectancy
# This will give you an error!

# Now, run this.
WHO$LifeExpectancy

# Statistics about this variable
mean(WHO$LifeExpectancy)
max(WHO$LifeExpectancy)
min(WHO$LifeExpectancy)

# Standard deviation
sd(WHO$LifeExpectancy)
summary(WHO$LifeExpectancy)

summary(WHO$GNI) 
# what's different here? 

# Identify countries corresponding to max and min

LifeExpectancyMinID <- which.min(WHO$LifeExpectancy)
WHO$Country[LifeExpectancyMinID]

LifeExpectancyMaxID <- which.max(WHO$LifeExpectancy)
WHO$Country[LifeExpectancyMaxID]

# Your turn:
# What is the largest population value among all countries?
# Which country has the largest population?



# Dealing with NA (Missing data)
# Try:

sd(WHO$LiteracyRate)

# We need to remove the NA's before we can compute statistics. To do that, try

sd(WHO$LiteracyRate, na.rm=TRUE)

# na.rm stands for na.remove.
# It doesn't consider the points with na.

