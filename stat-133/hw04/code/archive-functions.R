############################################################
# title : archive-functions.R
# description : functions for cleaning data
# input : raw dataframe
# output : cleaned dataframe
############################################################


#`@ title : read_archive
#`@ description : read HTML table from online
#`@ parameter : name of package
#`@ return : HTML table of the package
read_archive = function(x) {
  address = 'http://cran.r-project.org/src/contrib/Archive/'
  
  tbl_html = readHTMLTable(paste0(address, x))
  
  return(tbl_html)
  
}

#`@ title : clean_archive
#`@ description : clean the dataframe
#`@ parameter : dataframe
#`@ return : cleaned dataframe
clean_archive = function(tbl) {
  dt_html = data.frame(tbl, stringsAsFactors = FALSE)
  dt_html[1] = NULL
  dt_html = dt_html[-c(1, 2, nrow(dt_html)) ,]
  colnames(dt_html) = c('name', 'date', 'size', 'description')
  dt_html['description'] = NULL
  
  # add a column of version
  dt_html = get_version(dt_html)

  # modify name column
  dt_html = get_name(dt_html)
  
  # modifny size column
  dt_html = get_size(dt_html)
  dt_html['size'] = as.numeric(dt_html$size)
  
  # modify date column
  dt_html = get_date(dt_html)
  dt_html['date'] = as.Date(dt_html$date)

  #reorder columns
  dt_html = dt_html[, c('name', 'version', 'date', 'size')]
  
  
  return(dt_html)
}

#`@ title : get_version
#`@ description : create column for version and save
#`@ parameter : dataframe
#`@ return : dataframe with column version
get_version = function(tbl) {
  tbl['version'] = sub('(.*_)', '', tbl$name)
  tbl['version'] = sub('(.tar.gz)', '', tbl$version)
  return(tbl)
  
}

#`@ title : get_name
#`@ description : clean the name column
#`@ parameter : dataframe
#`@ return : dataframe with cleaned name column
get_name = function(tbl) {
  tbl['name'] = sub('(_.*)', '', tbl$name)
  return(tbl)
}

#`@ title : get_size
#`@ description : clean the size column
#`@ parameter : dataframe
#`@ return : dataframe with cleaned size column
get_size = function(tbl) {
  
  size = rep(0, nrow(tbl['size']))
  unit = tbl$size
  
  for (i in 1:nrow(tbl['size'])) {
    if (str_sub(unit[i], start = -1) == 'M') {
      size[i] = as.numeric(str_sub(unit[i], start = 1, end = -2)) * 1024
    } else {
      size[i] = str_sub(unit[i], start = 1, end = -2)
    }
  }
  
  tbl['size'] = size

  return(tbl)
}

#`@ title : get_date
#`@ description : clean the date column
#`@ parameter : dataframe
#`@ return : dataframe with cleaned date column
get_date = function(tbl) {
  tbl['date'] = str_sub(tbl$date, start = 1, end = -6)
  tbl['date'] = str_replace_all(tbl$date, '[ ].*', '')
  return(tbl)
}


#`@ title : plot_archive
#`@ description : plot the dataframe by its date and its size
#`@ parameter : dataframe
#`@ return : a plot
plot_archive = function(tbl) {
  
  return(ggplot(tbl, aes(x=date, y = size)) +
    geom_step() +
    geom_point() +
    labs(x = 'date', y='size') +
    ggtitle('stringr: timeline of version sizes'))
}


































