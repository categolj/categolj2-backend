#!/bin/sh
#
#     SUSE system statup script for CateoLJ2
#     Copyright (C) 2014  Toshiaki Maki
#     Copyright (C) 2007  Pascal Bleser
#
#     This library is free software; you can redistribute it and/or modify it
#     under the terms of the GNU Lesser General Public License as published by
#     the Free Software Foundation; either version 2.1 of the License, or (at
#     your option) any later version.
#
#     This library is distributed in the hope that it will be useful, but
#     WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
#     Lesser General Public License for more details.
#
#     You should have received a copy of the GNU Lesser General Public
#     License along with this library; if not, write to the Free Software
#     Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
#     USA.
#
### BEGIN INIT INFO
# Provides:          categolj2
# Required-Start:    $local_fs $remote_fs $network $time $named
# Should-Start: $time sendmail
# Required-Stop:     $local_fs $remote_fs $network $time $named
# Should-Stop: $time sendmail
# Default-Start:     3 5
# Default-Stop:      0 1 2 6
# Short-Description: CategoLJ2 REST API Server
# Description:       Start the CategoLJ2 REST API Server
### END INIT INFO

# Check for missing binaries (stale symlinks should not happen)
CATEGOLJ2_WAR="/usr/lib/categolj2/categolj2-backend.jar"
test -r "$CATEGOLJ2_WAR" || { echo "$CATEGOLJ2_WAR not installed";
	if [ "$1" = "stop" ]; then exit 0;
	else exit 5; fi; }

# Check for existence of needed config file and read it
CATEGOLJ2_CONFIG=/etc/sysconfig/categolj2
test -e "$CATEGOLJ2_CONFIG" || { echo "$CATEGOLJ2_CONFIG not existing";
	if [ "$1" = "stop" ]; then exit 0;
	else exit 6; fi; }
test -r "$CATEGOLJ2_CONFIG" || { echo "$CATEGOLJ2_CONFIG not readable. Perhaps you forgot 'sudo'?";
	if [ "$1" = "stop" ]; then exit 0;
	else exit 6; fi; }

CATEGOLJ2_PID_FILE="/var/run/categolj2.pid"

# Source function library.
. /etc/init.d/functions

# Read config
[ -f "$CATEGOLJ2_CONFIG" ] && . "$CATEGOLJ2_CONFIG"

# Search usable Java. We do this because various reports indicated
for candidate in /usr/lib/jvm/java-1.8.0/bin/java /etc/alternatives/java /usr/bin/java
do
  [ -x "$CATEGOLJ2_JAVA_CMD" ] && break
  CATEGOLJ2_JAVA_CMD="$candidate"
done

if [ ! -d /var/log/categolj2 ];then
    mkdir -p /var/log/categolj2
fi

JAVA_CMD="$CATEGOLJ2_JAVA_CMD $CATEGOLJ2_JAVA_OPTIONS -jar $CATEGOLJ2_WAR"
PARAMS="-uriEncoding UTF-8 -loggerName slf4j -resetExtract"
[ -n "$CATEGOLJ2_PORT" ] && PARAMS="$PARAMS -httpPort=$CATEGOLJ2_PORT"
[ -n "$CATEGOLJ2_HTTPS_PORT" ] && PARAMS="$PARAMS -httpsPort=$CATEGOLJ2_HTTPS_PORT"
[ -n "$CATEGOLJ2_AJP_PORT" ] && PARAMS="$PARAMS -ajpPort=$CATEGOLJ2_AJP_PORT"
[ -n "$CATEGOLJ2_ARGS" ] && PARAMS="$PARAMS $CATEGOLJ2_ARGS"

RETVAL=0

case "$1" in
    start)
	echo -n "Starting CategoLJ2 "
	nohup $JAVA_CMD $PARAMS < /dev/null > /var/log/categolj2/categolj2.log 2>&1 &
	pid=$!
	echo $pid > "$CATEGOLJ2_PID_FILE"
	success
	echo
	;;
    stop)
	echo -n "Shutting down CategoLJ2 "
	killproc categolj2
	RETVAL=$?
	echo
	;;
    try-restart|condrestart)
	if test "$1" = "condrestart"; then
		echo "${attn} Use try-restart ${done}(LSB)${attn} rather than condrestart ${warn}(RH)${norm}"
	fi
	$0 status
	if test $? = 0; then
		$0 restart
	else
		: # Not running is not a failure.
	fi
	;;
    restart)
	$0 stop
	$0 start
	;;
    force-reload)
	echo -n "Reload service CategoLJ2 "
	$0 try-restart
	;;
    reload)
    	$0 restart
	;;
    status)
    	status categolj2
	RETVAL=$?
	;;
    probe)
	## Optional: Probe for the necessity of a reload, print out the
	## argument to this init script which is required for a reload.
	## Note: probe is not (yet) part of LSB (as of 1.9)

	test "$CATEGOLJ2_CONFIG" -nt "$CATEGOLJ2_PID_FILE" && echo reload
	;;
    *)
	echo "Usage: $0 {start|stop|status|try-restart|restart|force-reload|reload|probe}"
	exit 1
	;;
esac
exit $RETVAL