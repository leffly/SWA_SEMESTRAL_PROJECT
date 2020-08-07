cd BookService
mvn clean package
cd ..

cd OrderService
mvn clean package
cd ..

cd StoreService
mvn clean package
cd ..

cd eureka-server
mvn clean package
cd ..

docker-compose build