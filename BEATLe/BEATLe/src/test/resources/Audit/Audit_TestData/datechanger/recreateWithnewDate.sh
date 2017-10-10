display_usage() {
        echo "This script must be run with user configured privileges."
        echo -e "Usage:\n$0 <LogFileName> <DelimiterLine>\n"
}

if [ $# -le 1 ]
then
   display_usage
   exit 1
fi

filename=`unzip $1 | grep inflating | awk '{print $2}'`
let end=`expr $2 - 1`
sed -n '1,'$end''p $filename > /tmp/$$_first.out
sed -n ''$2',$'p $filename > /tmp/$$_second.out
date=`date '+%F %T'`
sed -r "s/^[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}/$date/g" /tmp/$$_second.out > /tmp/$$_second_now.out
cat /tmp/$$_first.out > $filename
cat /tmp/$$_second_now.out >> $filename
zip $1 $filename 1>/dev/null
rm -rf $filename
rm -rf /tmp/$$_first.out /tmp/$$_second.out /tmp/$$_second_now.out