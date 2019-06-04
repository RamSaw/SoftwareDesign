[![Build Status](https://travis-ci.org/RamSaw/SoftwareDesign.svg?branch=master)](https://travis-ci.org/RamSaw/SoftwareDesign)
# SoftwareDesign. Roguelike Game.

Design Document: https://docs.google.com/document/d/1kU4-tOiCKhA8MUst_JWE9-xJY8V00--Y8pbNs2R6W_k/edit?usp=sharing

Use right/left/up/down arrows on your keyboard to move your player. 

To exit the game press 'q'.

'b' to take on equipment

'n' to take off equipment

To run server you need to do the following:

`
./gradlew build

./gradlew serverJar

java -jar ./build/libs/roguelike-server-1.0-SNAPSHOT.jar <port number>
`

To run client you need to do the following:

`
./gradlew build

./gradlew clientJar

java -jar ./build/libs/roguelike-client-1.0-SNAPSHOT.jar <args>
`
