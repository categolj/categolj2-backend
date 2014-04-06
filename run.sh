#/bin/sh

if [ -f setenv.sh ]; then
    . setenv.sh
fi

if [ -z "$PORT" ]; then
    PORT=8080
fi

if [ -z "$HTTPS_PORT" ]; then
    HTTPS_PORT=0
fi

if [ -z "$AJP_PORT" ]; then
    AJP_PORT=0
fi

java $JAVA_OPTS -jar target/categolj2-backend.jar -X -httpPort $PORT -httpsPort $HTTPS_PORT -ajpPort $AJP_PORT -uriEncoding UTF-8 -loggerName slf4j -resetExtract $@