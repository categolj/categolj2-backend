#!/bin/sh
NEW_VERSION=$1
if [ "${NEW_VERSION}" == "" ];then
    echo "ERROR: update-version.sh NEW_VERSION"
    exit 1
fi
OLD_VERSION=`awk 'match($0, /^    <version>(.+)<\/version>/){print substr($0, RSTART + 13, RLENGTH - 23)}' backend-api/pom.xml`


echo "update from ${OLD_VERSION} to ${NEW_VERSION}"
PROJECTS="frontend-ui backend-ui backend-api"

for p in ${PROJECTS};do
    echo "update ${p}"
    mvn -q -f $p versions:set -DoldVersion=${OLD_VERSION} -DnewVersion=${NEW_VERSION} -DallowSnapshots=true -DgenerateBackupPoms=false
done

echo "Don't forget to update README.md if needed!"
