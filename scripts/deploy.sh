#!/usr/bin/env bash

mvn cleana package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa_drucoder \
target/Kyrsovay-0.0.1-SNAPSHOT.jar \
root@192.168.0.107:/home/kyrsovay

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa_drucoder root@192.168.0.36 << EOP

pgreg java | xargs kill -9
nohup java -jar Kyrsovay-0.0.1-SNAPSHOT.jar > log.txt &

EOP

echo 'Bye'
