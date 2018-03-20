rm -r backend/src/main/resources/public
mvn install
rm -r ../c1compiled/src
cp -r backend/src/ ../c1compileddev
cp -r LICENSE.md ../c1compileddev
