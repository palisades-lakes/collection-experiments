# multimethod-experiments
# intersects/baselines.R
# palisades dot lakes at gmail dot com
# since 2017-07-30
# version 2017-09-02
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
  model='20ERCTO1WW',
  nelements=4194304,
  theday='[0-9]{8}-[0-9]{4}',
  parentFolder='data') {
  
  data.folder <- paste(parentFolder,benchmark,'bench',sep='/')
  pattern <- paste('LENOVO',model,'*',nelements,theday,'tsv',sep='.')
  print(data.folder)
  print(pattern)
  list.files(path=data.folder,pattern=pattern,full.names=TRUE) }
#-----------------------------------------------------------------
read.data <- function (
  benchmark=NULL,
  model=NULL,
  nelements=NULL,
  theday=NULL,
  parentFolder='data') {
  stopifnot(
    !is.null(benchmark),
    !is.null(model),
    !is.null(nelements),
    !is.null(theday))
  files <- data.files(
    benchmark=benchmark, 
    model=model, 
    nelements=nelements,
    theday=theday,
    parentFolder=parentFolder)
  #print(files)
  data <- NULL
  for (f in files) {
    print(f)
    tmp <- read.csv(f,sep='\t',as.is=TRUE)
    tmp$nanosec <- (1000000 * tmp$millisec) / tmp$nelements
    # print(summary(tmp))
    tmp$benchmark <- benchmark
    #print(summary(tmp))
    data <- rbind(data,tmp) }
  data$algorithm <- factor(
    data$algorithm,
    levels=sort(unique(data$algorithm)))
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
  'invokestaticPrimitive'='#888888',
  'invokevirtualPrimitive'='#888888',
  'invokeinterfacePrimitive'='#888888',
  'invokestatic'='#666666',
  'invokevirtual'='#666666',
  'invokeinterface'='#666666',
  'instanceof'='#1b9e77',
  'instancefn'='#1b9e77',
  'dynamap'='#b66638',
  'dynalin'='#b66638',
  'dynafun'='#a65628',
  'nohierarchy'='#377eb8',
  'signatures'='#377eb8',
  'hashmaps'='#377eb8',
  'protocols'='#e41a1c',
  'defmulti'='#e41a1c')
#-----------------------------------------------------------------
quantile.plot <- function(data, fname,
  ymin='lower.q', y='median', ymax='upper.q',
  suffix='runtimes', scales='free_y',
  ylabel='milliseconds',
  width=24, height=14) {
  plot.file <- file.path(
    plot.folder,paste(fname,'quantiles','png',sep='.'))
  
  p <- ggplot(
      data=data,
      aes_string(
        x='algorithm',  
        ymin=ymin, y=y, ymax=ymax, 
        group='generators',
        #fill=log2(nmethods), 
        color='nmethods'))  +
    facet_wrap(~benchmark,scales=scales) +
    theme_bw() +
    theme(plot.title = element_text(hjust = 0.5)) +
    theme(
      axis.text.x=element_text(angle=-90,hjust=0,vjust=0.5),
      axis.title.x=element_blank()) + 
    #theme(legend.position='none') +
    #scale_fill_manual(values=algorithm.colors) +
    #scale_color_manual(values=algorithm.colors) +
    #scale_fill_brewer(palette=palette) +
    #scale_color_brewer(palette=palette) +
    scale_color_gradient( 
      #low=muted('blue'), high=muted('red'),
      low='#0571b0', high='#ca0020',
      trans='log') +
    ylab(ylabel) +
    geom_crossbar(width=0.25) +
    geom_line() +
    ggtitle(paste('[0.05,0.50,0.95] quantiles for', suffix)) +
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
