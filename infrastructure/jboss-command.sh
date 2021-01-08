#!/bin/bash

#
# Copyright 2020 Gepardec IT Services GmbH and the CheetUnit contributors
# SPDX-License-Identifier: Apache-2.0
#

JBOSS_HOME=/opt/jboss/wildfly
JBOSS_CLI=$JBOSS_HOME/bin/jboss-cli.sh
JBOSS_MODE=${1:-"standalone"}
JBOSS_CONFIG=${2:-"$JBOSS_MODE.xml"}

function wait_for_wildfly() {
  until `$JBOSS_CLI -c "ls /deployment" &> /dev/null`; do
    sleep 1
  done
}

echo "==> Starting WildFly..."
$JBOSS_HOME/bin/$JBOSS_MODE.sh -c $JBOSS_CONFIG &

echo "==> Waiting..."
wait_for_wildfly

echo "==> Enable cheetunit..."
$JBOSS_CLI -c --file=`dirname "$0"`/set-cheetunit-property.cli

echo "==> Setup Datasource..."
$JBOSS_CLI -c --file=`dirname "$0"`/install-module.cli

echo "==> Shutting down WildFly..."
if [ "$JBOSS_MODE" = "standalone" ]; then
  $JBOSS_CLI -c ":shutdown"
else
  $JBOSS_CLI -c "/host=*:shutdown"
fi
