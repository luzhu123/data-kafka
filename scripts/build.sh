#!/bin/bash
export JAVA_HOME=/usr/java/latest
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
SOURCEPATH="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $SOURCEPATH/build.pro
DEP=$DEP
JAR=$JAR
SERNAME=$SERNAME
STIME=$STIME
TARGETPATH=/alidata/server/$DEP/$SERNAME
if [ ! -d $TARGETPATH ];then
echo "$TARGETPATH is not existed and need to create it"
mkdir -p $TARGETPATH
fi
cd $TARGETPATH|| exit
echo "delete the latest jar file"
rm -f $JAR
echo "copy the newest $JAR to $TARGETPATH"
cp $SOURCEPATH/$JAR  .
chmod +x $JAR
count=0
while true
do
  PID=`ps -ef |grep $JAR|grep -v grep|awk '{print $2}'`;
if [ "$PID" == "" ]; then
       echo "the $JAR services is not run"
       break
  else
       if [ "$count" -le 10 ]; then
            echo "the $JAR services is running and need to kill it"
            echo "kill -15 ${PID}"
            kill -15 ${PID}
            sleep $STIME
         fi
 let count=$count+1
           if [ "$count" -gt 10 ]; then
            echo " kill -15 can't kill the $JAR process and need to use "kill -9" kill it Forcibly"i
            echo "kill -9 ${PID}"
            kill -9 ${PID}  
           fi
fi
done
echo "to start $SERNAME services"
echo  'JAVA_OPTS="-server -Xmx512M -Xms256M -XX:MaxMetaspaceSize=128M -Xss256K"' > ${JAR/.jar/}.conf
echo "nohup >> nohup.out ./$JAR run $ENV $ONEAPM 2>&1 &"
echo  > nohup.out
nohup >> nohup.out ./$JAR run $ENV $ONEAPM 2>&1 &
while true 
do
   LOCALIP=`/sbin/ifconfig eth0|sed -n '2p'|awk -F " " '{print $2}'|awk -F ":" '{print $2}'`
   IP=`egrep '[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}' $SOURCEPATH/checkaddress |xargs -n 1` 
if [ -z "$LOCALIP" ] || [ $ENV = zbrd ] ;then
       echo "The health check address is Domain name or the env is zbrd"
       HEATHURL=`cat $SOURCEPATH/checkaddress`
       STATUS=`curl -s -I -XHEAD $HEATHURL --speed-time 5 --speed-limit 1|head -1|awk '{print $2}'`
       HEATH=`curl -s -I -XHEAD $HEATHURL --speed-time 5 --speed-limit 1`
 else
       echo "The health check address is ip"
       #CHECKIP=`echo -E "$IP"|awk -F "//" '{print $2}'|awk -F ":" '{print $1}'|grep -w $LOCALIP`
     if [ $LOCALIP = `echo -E "$IP"|awk -F "//" '{print $2}'|awk -F ":" '{print $1}'|grep -w $LOCALIP` ];then
            HEATHURL=`echo -E "$IP"|grep -w $LOCALIP`    
            echo "**$HEATHURL***"
            STATUS=`curl -s -I -XHEAD $HEATHURL --speed-time 5 --speed-limit 1|head -1|awk '{print $2}'`
            HEATH=`curl -s -I -XHEAD $HEATHURL --speed-time 5 --speed-limit 1`
       fi
  fi

if [[ $STATUS = 200 ]];then
       sleep $STIME
       tail -n 350 nohup.out
       echo "The $HEATHURL web site can access"
       NEWPID=`ps -ef |grep $JAR|grep -v grep|awk '{print $2}'`
       echo "The $SERNAME new's process PID is $NEWPID"
      break
 else
         if [ "$count" -le 10 ]; then
                echo "checking $SERNAME serverice start,pls wait"
                echo "$HEATHURL"
                sleep $STIME
           fi
    let count=$count+1
           if [ "$count" -gt 10 ]; then
                  sleep $STIME
                  tail -n 350 nohup.out
                  echo "web $HEATHURL access failed,Pls check it"
                  exit 1
              fi
       
    fi
done
