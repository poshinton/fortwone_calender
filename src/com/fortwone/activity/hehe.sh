for i in *.txt;do m=`echo $i | awk -F . '{print $1}'`;n=`echo $i | awk -F . '{print $2}'`;mv $i $m.$n ;done
