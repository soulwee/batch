set LIB=www/WEB-INF/lib
set APPNPATH=..\ 
java -showversion -cp %LIB%\ojdbc14.jar;%LIB%\wizbank.jar; com.demo.ApplicationApp %APPNPATH% 1>>log.txt 2>&1