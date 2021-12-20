CREATE TABLE IF NOT EXISTS enginePart (partID BIGINT, partName VARCHAR (128), price INTEGER, availability VARCHAR (128), condition INTEGER, fuel VARCHAR (128), serialNumber INTEGER, volume FLOAT);
CREATE TABLE IF NOT EXISTS chassisPart (partID BIGINT, partName VARCHAR (128), price INTEGER, availability VARCHAR (128), condition INTEGER, side VARCHAR (128), chassisType VARCHAR (128));
CREATE TABLE IF NOT EXISTS electricityPart (partID BIGINT, partName VARCHAR (128), price INTEGER, availability VARCHAR (128), engineVolume FLOAT, power FLOAT);

CREATE TABLE IF NOT EXISTS car (carID BIGINT, brand VARCHAR (128), model VARCHAR (128), carYear INTEGER, engine VARCHAR (128));
CREATE TABLE IF NOT EXISTS client(clientID BIGINT, clientName VARCHAR (128), clientType VARCHAR (128), carID BIGINT, FOREIGN KEY(carID) references car(carID) ON DELETE CASCADE);

CREATE TABLE IF NOT EXISTS employee (employeeID BIGINT, employeeName VARCHAR (128));

CREATE TABLE IF NOT EXISTS orders (orderID BIGINT, employeeSalary DOUBLE, totalServiceIncome DOUBLE, totalEmployeeIncome DOUBLE, totalMarkup DOUBLE,
clientID BIGINT, FOREIGN KEY(clientID) references client(clientID),
employeeID BIGINT, FOREIGN KEY(employeeID) references employee(employeeID));

CREATE TABLE IF NOT EXISTS orders_engineParts (orderID BIGINT, partID BIGINT, FOREIGN KEY(orderID) references orders(orderID) ON DELETE CASCADE, FOREIGN KEY(partID) references enginePart(partID) ON DELETE CASCADE);
CREATE TABLE IF NOT EXISTS orders_chassisParts (orderID BIGINT, partID BIGINT, FOREIGN KEY(orderID) references orders(orderID) ON DELETE CASCADE, FOREIGN KEY(partID) references chassisPart(partID) ON DELETE CASCADE);
CREATE TABLE IF NOT EXISTS orders_electricityParts (orderID BIGINT, partID BIGINT, FOREIGN KEY(orderID) references orders(orderID) ON DELETE CASCADE, FOREIGN KEY(partID) references electricityPart(partID) ON DELETE CASCADE);








