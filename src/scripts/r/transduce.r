# filter-map-reduce experiments
# palisades dot lakes at gmail dot com
# version 2017-12-16
#-----------------------------------------------------------------
if (file.exists('e:/porta/projects/collection-experiments')) {
  setwd('e:/porta/projects/collection-experiments')
} else {
  setwd('c:/porta/projects/collection-experiments')
}
source('src/scripts/r/functions.R')
#-----------------------------------------------------------------
parentFolder <- 'data-jdk9.0.1-clj1.9.0/scripts/'
hardware <- 'LENOVO.20HRCTO1WW' # X1
#hardware <- 'LENOVO.20ERCTO1WW' # P70
theday = '2017121[567]-[0-9]{4}'
benchmark <- 'transduce'
#-----------------------------------------------------------------
data <- read.data(
  parentFolder=parentFolder,
  benchmark,hardware,theday)
#-----------------------------------------------------------------
plot.folder <- file.path('docs',hardware)
dir.create(
  plot.folder, 
  showWarnings=FALSE, 
  recursive=TRUE, 
  mode='0777')
#-----------------------------------------------------------------
quantile.log.log.plot(
  data=data,
  fname='all',
  ymin='lower.q', 
  y='median', 
  ymax='upper.q',
  plot.folder=plot.folder,
  group='algorithm',
  colors=algorithm.colors,
  facet='containers',
  ylabel='msec')
quantile.log.log.plot(
  data=data,
  fname='all',
  ymin='lower.q', 
  y='median', 
  ymax='upper.q',
  plot.folder=plot.folder,
  group='containers',
  colors=container.colors,
  facet='algorithm',
  ylabel='msec')
quantile.log.lin.plot(
  data=data,
  fname='all',
  ymin='lower.q.per.element', 
  y='median.per.element', 
  ymax='upper.q.per.element',
  plot.folder=plot.folder,
  group='algorithm',
  colors=algorithm.colors,
  facet='containers',
  ylabel='nanosec-per-element')
quantile.log.lin.plot(
  data=data,
  fname='all',
  ymin='lower.q.per.element', 
  y='median.per.element', 
  ymax='upper.q.per.element',
  plot.folder=plot.folder,
  group='containers',
  colors=container.colors,
  facet='algorithm',
  ylabel='nanosec-per-element')
#-----------------------------------------------------------------
#cols <- c('benchmark','algorithm','nmethods',
#  'lower.q','median', 'upper.q','millisec',
#  'overhead.lower.q','overhead.median', 'overhead.upper.q',
#  'overhead.millisec',
#  'nanosec','overhead.nanosec')
#-----------------------------------------------------------------