
INSERT INTO user(username,name, surname,password, state, dni, email, notes, created_date) VALUES ('juan','juan','Perez','$2a$10$486eQMHqIgEIqaSjgGWS2.zZHHNyj2cQpy2J1fztnGxFLRCHeNr/u',1,'38.458.365', 'juan@juan.com', 'una nota', '2000-01-01');
INSERT INTO user(username, name, surname, password, state, dni, email, notes, created_date) VALUES ('lola','luciana','ape','$2a$10$c/9vScJhZ0tVbYtYaXe3WODGmYnRtJU5ZnExt9iyPnwqt1MrKFvmi',1,'38.458.365', 'juan@juan.com', 'una nota', '2000-01-01');
INSERT INTO user(username,name, surname,password,state, dni, email, notes, created_date) VALUES ('roberto','roberto','lopez','$2a$10$amNYoyhbLu4KEzaMG0awPufZ5oKRust4/OQUmJL7wT8t.El/opiny',0 ,'38.458.365', 'juan@juan.com', 'una nota','2000-01-01');

INSERT INTO role (name) VALUES ('ROLE_ADMINISTRADOR');
INSERT INTO role (name) VALUES ('ROLE_TESORERO');
INSERT INTO role (name) VALUES ('ROLE_SUPERVISOR_GENERAL');
INSERT INTO role (name) VALUES ('ROLE_COMERCIAL');
INSERT INTO role (name) VALUES ('ROLE_DELIVERY');
INSERT INTO role (name) VALUES ('ROLE_COMISIONISTA');


INSERT INTO user_roles (user_id, roles_id) VALUES (1,1);
INSERT INTO user_roles (user_id, roles_id) VALUES (1,2);
INSERT INTO user_roles (user_id, roles_id) VALUES (1,6);
INSERT INTO user_roles (user_id, roles_id) VALUES (2,1);
INSERT INTO user_roles (user_id, roles_id) VALUES (2,2);

INSERT into client (alias, first_name, last_name, notes, created_date) values ('juan', 'juan', 'perez','una nota...','2000-01-01');
INSERT into client (alias, first_name, last_name, notes, created_date) values ('juan2', 'juan2', 'perez2','una nota...','2000-01-01');
INSERT into client (alias, first_name, last_name, notes, created_date) values ('juan3', 'juan3', 'perez3','una nota...','2000-01-01');

INSERT INTO commission_type (id, description) VALUES (1,'Porcentaje');
INSERT INTO commission_type (id, description) VALUES (2,'Importe fijo');

INSERT INTO phone_type (id, description) VALUES (1,'Movil');
INSERT INTO phone_type (id, description) VALUES (2,'Trabajo');
INSERT INTO phone_type (id, description) VALUES (3,'Casa');
INSERT INTO phone_type (id, description) VALUES (4,'Principal');

INSERT INTO currency (description, symbol) VALUES ('Pesos','$');
INSERT INTO currency (description, symbol) VALUES ('Dolares','U$S');
INSERT INTO currency (description, symbol) VALUES ('Euros','€');
insert  into currency(id,description,symbol) values (4,'Reales','R$');



INSERT INTO phone (id, alias, create_at, last_update, notes, number, state, client_id, phone_type)VALUES(2, 'juan', NULL, NULL, NULL, '1234-4532', NULL, 1, 1);

INSERT INTO pair_quote (origin_currency_id,destination_currency_id, bid, ask, created_date) VALUES (1,2,0.013,180.92, '2000-01-01');
INSERT INTO pair_quote (origin_currency_id,destination_currency_id, bid, ask, created_date) VALUES (2,1,180.92,0.013, '2000-01-01');


INSERT INTO device (fcm, internal_version,name,user_id) VALUES ('a','a','a',1);

INSERT INTO operation_status (id, description) VALUES (1,'Ingresada');
INSERT INTO operation_status (id, description) VALUES (2,'Aprobada');
INSERT INTO operation_status (id, description) VALUES (3,'Asignada');
INSERT INTO operation_status (id, description) VALUES (4,'Aceptada');
INSERT INTO operation_status (id, description) VALUES (5,'Arribada');
INSERT INTO operation_status (id, description) VALUES (6,'Finalizada');
INSERT INTO operation_status (id, description) VALUES (7,'Suspendida');
INSERT INTO operation_status (id, description) VALUES (8,'Cancelada');
INSERT INTO operation_status (id, description) VALUES (9,'Rechazada');
INSERT INTO operation_status (id, description) VALUES (10,'Archivada');



INSERT INTO operation_type (id, description) VALUES (1,'Inicio de jornada');
INSERT INTO operation_type (id, description) VALUES (2,'Cierre de jornada');
INSERT INTO operation_type (id, description) VALUES (3,'Compra moneda extranjera');
INSERT INTO operation_type (id, description) VALUES (4,'Venta moneda extranjera');
INSERT INTO operation_type (id, description) VALUES (5,'Transferencia');
INSERT INTO operation_type (id, description) VALUES (6,'Gastos');
INSERT INTO operation_type (id, description) VALUES (7,'Sueldos');
INSERT INTO operation_type (id, description) VALUES (8,'Cobros');
INSERT INTO operation_type (id, description) VALUES (9,'Pagos');
INSERT INTO operation_type (id, description) VALUES (10,'Resto');


INSERT INTO  transaction_status (id, description) values (1,'Pendiente acreditación');
INSERT INTO  transaction_status (id, description) values (2,'Acreditada');

INSERT INTO  transaction_type (id, description) values (1,'Ingreso');
INSERT INTO  transaction_type (id, description) values (2,'Egreso');

INSERT  INTO account_type (id, description) values (1,'Caja');
INSERT  INTO account_type (id, description) values (2,'Cuenta Corriente');

INSERT INTO account (balance, description, state, user_id, currency_id, account_type_id, created_date) VALUES ('12000', 'cuenta empresarial', 1,1,1,1,'2000-01-01');
INSERT INTO account (balance, description, state, user_id, currency_id, account_type_id, created_date) VALUES ('12000', 'cuenta empresarial', 1,1,2,1,'2000-01-01');
INSERT INTO account (balance, description, state, user_id, currency_id, account_type_id, created_date) VALUES ('15000', 'cuenta', 1,2,1,1,'2000-01-01');
INSERT INTO account (balance, description, state, user_id, currency_id, account_type_id, created_date) VALUES ('15000', 'delivery', 1,2,2,1,'2000-01-01');
INSERT INTO account (balance, description, state, currency_id, client_id, account_type_id, created_date) VALUES ('12000', 'es un cliente', 1,1,1,2,'2000-01-01');
INSERT INTO account (balance, description, state, currency_id, client_id, account_type_id, created_date) VALUES ('12000', 'es un cliente', 1,2,1,2,'2000-01-01');

INSERT  INTO phone_status (id, description) values (1,'Pendiente de verificación');
INSERT  INTO phone_status (id, description) values (2,'Verificado');
INSERT  INTO phone_status (id, description) values (3,'Suspendido');

INSERT  INTO address_status( id, description) values (1,'Pendiente de verificación');
INSERT  INTO address_status( id, description) values (2,'Verificada');
INSERT  INTO address_status( id, description) values (3,'Suspendida');




INSERT INTO state(id,description,latitude,longitude) VALUES (2,'Ciudad Autónoma de Buenos Aires',-34.61449341,-58.44585635);
INSERT INTO state(id,description,latitude,longitude) VALUES (6,'Buenos Aires',-36.67694152,-60.55883198);
INSERT INTO state(id,description,latitude,longitude) VALUES (10,'Catamarca',-27.33583328,-66.94768243);
INSERT INTO state(id,description,latitude,longitude) VALUES (14,'Córdoba',-32.14293266,-63.80175327);
INSERT INTO state(id,description,latitude,longitude) VALUES (18,'Corrientes',-28.77430470,-57.80121920);
INSERT INTO state(id,description,latitude,longitude) VALUES (22,'Chaco',-26.38643091,-60.76583074);
INSERT INTO state(id,description,latitude,longitude) VALUES (26,'Chubut',-43.78862335,-68.52675939);
INSERT INTO state(id,description,latitude,longitude) VALUES (30,'Entre Ríos',-32.05887354,-59.20144755);
INSERT INTO state(id,description,latitude,longitude) VALUES (34,'Formosa',-24.89497259,-59.93244058);
INSERT INTO state(id,description,latitude,longitude) VALUES (38,'Jujuy',-23.32007842,-65.76425222);
INSERT INTO state(id,description,latitude,longitude) VALUES (42,'La Pampa',-37.13155377,-65.44665466);
INSERT INTO state(id,description,latitude,longitude) VALUES (46,'La Rioja',-29.68577630,-67.18173597);
INSERT INTO state(id,description,latitude,longitude) VALUES (50,'Mendoza',-34.62988731,-68.58312282);
INSERT INTO state(id,description,latitude,longitude) VALUES (54,'Misiones',-26.87539651,-54.65169662);
INSERT INTO state(id,description,latitude,longitude) VALUES (58,'Neuquén',-38.64175758,-70.11857052);
INSERT INTO state(id,description,latitude,longitude) VALUES (62,'Río Negro',-40.40579572,-67.22932989);
INSERT INTO state(id,description,latitude,longitude) VALUES (66,'Salta',-24.29913445,-64.81446296);
INSERT INTO state(id,description,latitude,longitude) VALUES (70,'San Juan',-30.86536800,-68.88949085);
INSERT INTO state(id,description,latitude,longitude) VALUES (74,'San Luis',-33.75772574,-66.02812982);
INSERT INTO state(id,description,latitude,longitude) VALUES (78,'Santa Cruz',-48.81548518,-69.95576217);
INSERT INTO state(id,description,latitude,longitude) VALUES (82,'Santa Fe',-30.70692716,-60.94983694);
INSERT INTO state(id,description,latitude,longitude) VALUES (86,'Santiago del Estero',-27.78241166,-63.25238666);
INSERT INTO state(id,description,latitude,longitude) VALUES (90,'Tucumán',-26.94780018,-65.36475794);
INSERT INTO state(id,description,latitude,longitude) VALUES (94,'Tierra del Fuego, Antártida e Islas del Atlántico Sur',-82.52151781,-50.74274860);

insert  into locality(id,state_id,description,latitude,longitude) values (2007010001,2,'constitucion',-34.62504786,-58.38438723);
insert  into locality(id,state_id,description,latitude,longitude) values (1,18,'prueba',-34.62504786,-58.38438723);


INSERT INTO address (street,number,floor,apt,client_id, locality_id, created_date) values ('callefalsa',1597,'6C','apt',1, 2007010001, '2000-01-01');

INSERT INTO operation ( destination_amount, created_date, description, notes, origin_amount, state, operation_status_id, address_id, operation_type_id, origin_currency, destination_currency) values ( '500', '2000-01-01','desc','notes','20000',1,1,1,1,1,2); 

INSERT INTO transaction (time_range, amount, account_id, created_date) VALUES ('2000-01-01', 4500,1, '2000-01-01');
