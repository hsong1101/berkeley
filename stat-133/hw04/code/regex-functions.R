############################################################
# title : regex-functions.R
# description : functions for manipulating strings
# input : string or vector of strings
# output : extracted characters(string)
############################################################


#`@ title : split_chars
#`@ description : split the string into characters
#`@ parameter : string
#`@ return : vector of split string
split_chars = function(x) {
  return(strsplit(x, NULL))
}

#`@ title : num_vowels
#`@ description : return the number of vowels in a string
#`@ parameter : string
#`@ return : number of vowels
num_vowels = function(x) {

  vowels = c(0,0,0,0,0)
  names(vowels) = c('a', 'e', 'i', 'o', 'u')
  
  vowels['a'] = str_count(x, 'a')
  vowels['e'] = str_count(x, 'e')
  vowels['i'] = str_count(x, 'i')
  vowels['o'] = str_count(x, 'o')
  vowels['u'] = str_count(x, 'u')
  return(vowels)
  
}

#`@ title : count_vowels
#`@ description : count the number of vowels in a string
#`@ parameter : string
#`@ return : number of vowels
count_vowels = function(x) {
  x = sapply(x, tolower)
  return(num_vowels(split_chars(x)))
}

#`@ title : reverse_chars
#`@ description : reverse the order of the characters
#`@ parameter : string
#`@ return : reversed vector of characters
reverse_chars = function(x) {
  return(sapply(lapply(split_chars(x), rev), paste, collapse = ""))
}

#`@ title : reverse_words
#`@ description : reverse the order of the words
#`@ parameter : string
#`@ return : reversed vector of words
reverse_words = function(x) {
  return(sapply(lapply(strsplit(x, ' '), rev), paste, collapse = ' '))  
}










