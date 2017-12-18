# collection-experiments
# intersects/baselines.R
# palisades dot lakes at gmail dot com
# version 2017-12-16
#-----------------------------------------------------------------
# libraries
#-----------------------------------------------------------------
require('ggplot2')
require('scales')
require('knitr')
require('kableExtra')
#-----------------------------------------------------------------
data.files <- function (
  benchmark='foo',
  hardware='LENOVO.20ERCTO1WW',
  #nelements=4194304,
  theday='[0-9]{8}-[0-9]{4}',
  parentFolder='data') {
  
  data.folder <- paste(parentFolder,benchmark,sep='/')
  pattern <- paste(hardware,'*.*',theday,'tsv',sep='.')
  print(data.folder)
  print(pattern)
  list.files(path=data.folder,pattern=pattern,full.names=TRUE) }
#-----------------------------------------------------------------
read.data <- function (
  benchmark=NULL,
  hardware=NULL,
  theday=NULL,
  parentFolder=NULL) {
  stopifnot(
    !is.null(parentFolder),
    !is.null(benchmark),
    !is.null(hardware),
    !is.null(theday))
  files <- data.files(
    parentFolder=parentFolder,
    benchmark=benchmark, 
    hardware=hardware, 
    theday=theday)
  #print(files)
  data <- NULL
  for (f in files) {
    print(f)
    tmp <- read.csv(f,sep='\t',as.is=TRUE)
    tmp$benchmark <- benchmark
    #print(summary(tmp))
    data <- rbind(data,tmp) }
  data$manufacturerModel <- factor(
    data$manufacturerModel,
    levels=sort(unique(data$manufacturerModel)))
  data$algorithm <- factor(
    data$algorithm,
    levels=sort(unique(data$algorithm)))
  data$containers <- factor(
    data$containers,
    levels=sort(unique(data$containers)))
  data$elements <- factor(
    data$elements,
    levels=sort(unique(data$elements)))
  data$generators <- factor(
    data$generators,
    levels=sort(unique(data$generators)))
  data$nanosec.per.element <- (1000000 * data$millisec) / data$nelements
  data$lower.q.per.element <- (1000000 * data$lower.q) / data$nelements
  data$median.per.element <- (1000000 * data$median) / data$nelements
  data$upper.q.per.element <- (1000000 * data$upper.q) / data$nelements
  data }
#-----------------------------------------------------------------
html.table <- function(data,fname,n) {
  html.file <- file(
    description=file.path(
      plot.folder,
      paste(fname,'html',sep='.')),
    encoding='UTF-8',
    open='wb')
  writeLines(
    kable(
      data[order(data$algorithm),],
      format='html',
      digits=1,
      caption=paste('milliseconds for',n,'intersection tests'),
      row.names=FALSE,
      col.names = c('algorithm','0.05','0.50','0.95','mean')),
    con=html.file,
    sep='\n')
  close(html.file) }
#-----------------------------------------------------------------
md.table <- function(data,fname,n) {
  md.file <- file(
    description=file.path(
      plot.folder,
      paste(fname,'md',sep='.')),
    encoding='UTF-8',
    open='wb')
  writeLines(
    kable(
      data[order(data$benchmark,data$nmethods,data$algorithm),],
      format='markdown',
      digits=2,
      caption=paste('milliseconds for',n,'intersection tests'),
      row.names=FALSE,
      col.names = c('benchmark','algorithm','nmethods',
        '0.05','0.50','0.95','mean',
        'overhead 0.05','overhead 0.50','overhead 0.95','overhead mean',
        'ns per op','overhead ns per op')),
    con=md.file,
    sep='\n') 
  close(md.file) }
#-----------------------------------------------------------------
algorithm.colors <- c(
  'fn'='#1b9e7766',
  'inline'='#377eb866',
  'manual_rmf'='#7570b366',
  'rmf'='#e7298a66',
  'transducer_rmf'='#ff7f0066')
container.colors <- c(
  'array_of_boxed_float'='#edaf5f66',
  'array_of_boxed_int'='#edaf5f66',
  'array_of_float'='#ff7f0066',
  'array_of_int'='#ff7f0066',
  'array_list'='#cab2d666',
  'immutable_list'='#6a3d9a66',
  'lazy_sequence'='#33a02c66',
  'persistent_list'='#a6cee3',
  'persistent_vector'='#1f78b466',
  'realized'='#b2df8a66')
#-----------------------------------------------------------------
quantile.log.log.plot <- function(
  data=NULL, 
  fname=NULL,
  ymin='lower.q', 
  y='median', 
  ymax='upper.q',
  group=NULL,
  facet=NULL,
  colors=NULL,
  scales='fixed', #'free_y',
  ylabel='milliseconds',
  width=36, 
  height=21,
  plot.folder=NULL) {
  stopifnot(
    !is.null(data),
    !is.null(fname),
    !is.null(plot.folder),
    !is.null(group),
    !is.null(facet),
    !is.null(colors))
  
  plot.file <- file.path(
    plot.folder,
    paste(fname,group,facet,ylabel,'quantiles','png',sep='.'))
  
  p <- ggplot(
      data=data,
      aes_string(
        x='nelements',  
        ymin=ymin, y=y, ymax=ymax, 
        group=group,
        fill=group, 
        color=group))  +
    facet_wrap(as.formula(paste0('~',facet)),scales=scales) +
    theme_bw() +
    theme(plot.title = element_text(hjust = 0.5)) +
    theme(
      axis.text.x=element_text(angle=-90,hjust=0,vjust=0.5),
      axis.title.x=element_blank()) + 
    geom_ribbon(aes_string(ymin = ymin, ymax = ymax, fill = group)) +
    geom_line(aes_string(y = ymin)) + 
    geom_line(aes_string(y = y)) + 
    geom_line(aes_string(y = ymax)) +   
    scale_fill_manual(values=colors) +
    scale_color_manual(values=colors) +
    scale_x_log10(breaks = (1000000*c(0.01,0.1,1,10))) + 
    scale_y_log10(limits=c(0.10,NA)) +
    ylab(ylabel) +
    ggtitle(paste('[0.05,0.50,0.95] quantiles for', ylabel)) +
    expand_limits(y=0); 
  print(plot.file)
  ggsave(p , 
    device='png', 
    file=plot.file, 
    width=width, 
    height=height, 
    units='cm', 
    dpi=300) }
#-----------------------------------------------------------------
quantile.log.lin.plot <- function(
  data=NULL, 
  fname=NULL,
  ymin='lower.q', 
  y='median', 
  ymax='upper.q',
  group=NULL,
  facet=NULL,
  colors=NULL,
  scales='fixed', #'free_y',
  ylabel='milliseconds',
  width=36, 
  height=21,
  plot.folder=NULL) {
  stopifnot(
    !is.null(data),
    !is.null(fname),
    !is.null(plot.folder),
    !is.null(group),
    !is.null(facet),
    !is.null(colors))
  
  plot.file <- file.path(
    plot.folder,
    paste(fname,group,facet,ylabel,'quantiles','png',sep='.'))
  
  p <- ggplot(
      data=data,
      aes_string(
        x='nelements',  
        ymin=ymin, y=y, ymax=ymax, 
        group=group,
        fill=group, 
        color=group))  +
    facet_wrap(as.formula(paste0('~',facet)),scales=scales) +
    theme_bw() +
    theme(plot.title = element_text(hjust = 0.5)) +
    theme(
      axis.text.x=element_text(angle=-90,hjust=0,vjust=0.5),
      axis.title.x=element_blank()) + 
    geom_ribbon(aes_string(ymin = ymin, ymax = ymax, fill = group)) +
    geom_line(aes_string(y = ymin)) + 
    geom_line(aes_string(y = y)) + 
    geom_line(aes_string(y = ymax)) +   
    scale_fill_manual(values=colors) +
    scale_color_manual(values=colors) +
    scale_x_log10(breaks = (1000000*c(0.01,0.1,1,10))) + 
    #scale_y_log10(limits=c(0.10,NA)) +
    ylab(ylabel) +
    ggtitle(paste('[0.05,0.50,0.95] quantiles for', ylabel)) +
    expand_limits(y=0); 
  print(plot.file)
  ggsave(p , 
    device='png', 
    file=plot.file, 
    width=width, 
    height=height, 
    units='cm', 
    dpi=300) }
#-----------------------------------------------------------------
