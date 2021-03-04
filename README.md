# Proyecto financiera

## Docker

El backend tiene su propia imagen pero depende de un contenedor de mysql preexistente y con una DB creada de nombre exchange. Esto NO es necesario si se usa el docker-compose del proyecto DevOps.

```
docker run -d -p 3306:3306 --name db_exchange -v /data/docker/volume/exchange:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=exchange2020 -e MYSQL_DATABASE=exchange mysql:8
```

Para construir la imagen docker del backend se debe *empaquetar* el jar y luego hacer el build:
```
mvn package -Dskiptests \
docker build -t exchange/backend:latest .
```

Y para iniciar un contenedor:
```
docker run -d -p 8080:8080 exchange/backend:latest
```


---

## Swagger OpenAPI v3

Documentación de API disponible en http://localhost:8080/api-docs/swagger.html


---

## Configuración Firebase
```
<!-- The core Firebase JS SDK is always required and must be listed first -->
<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-app.js"></script>

<!-- TODO: Add SDKs for Firebase products that you want to use
     https://firebase.google.com/docs/web/setup#available-libraries -->
<script src="https://www.gstatic.com/firebasejs/8.0.0/firebase-analytics.js"></script>

<script>
  // Your web app's Firebase configuration
  // For Firebase JS SDK v7.20.0 and later, measurementId is optional
  var firebaseConfig = {
    apiKey: "AIzaSyBBRJsV7UeQwGV4enNaCwgtHPNP6wDsYGE",
    authDomain: "exchangetest-4cfad.firebaseapp.com",
    databaseURL: "https://exchangetest-4cfad.firebaseio.com",
    projectId: "exchangetest-4cfad",
    storageBucket: "exchangetest-4cfad.appspot.com",
    messagingSenderId: "764386022821",
    appId: "1:764386022821:web:19f94f8cd4a3ec5267444c",
    measurementId: "G-YZ05E2THH4"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  firebase.analytics();
</script>
```


## Liquibase

Es una herramienta para manejar versionado del esquema de base de datos. Permite generar un changelog para los cambios que se realizan sobre la DB, y restaurar a un punto anterior o recuperar completamente el esquema en una DB nueva.

Primero se genera un changelog para la estructura de los objetos de DB y luego otro para los datos iniciales.
```
mvn liquibase:generateChangeLog -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/db.changelog-1.0.xml

mvn liquibase:generateChangeLog -Dliquibase.diffTypes=data -Dliquibase.outputChangeLogFile=src/main/resources/db/changelog/db.changelog-1.1.xml
```

Luego se ejecuta la tarea *diff* para generar un nuevo changelog (o snapshot de la DB) con los cambios realizados sobre las entidades. Al finalizar este paso se debe actualizar el db.changelog-master.xml para que incluya el nuevo snapshot.
```
mvn liquibase:diff -Dliquibase.diffChangeLogFile=src/main/resources/db/changelog/changelog-1.X.xml
```

Por último, para actualizar la DB a partir del modelo de los changelog se ejecuta este comando:
```
mvn liquibase:update
```

Como se habilitó Liquibase para la aplicación y entra en conflicto con la autogeneración de tablas de Hibernate, se modificó esta property del application.properties. Aunque también se podrían impactar los cambios contra el esquema utilizando la funcionalidad de Hibernate para luego generar un nuevo changelog, lo recomendable es hacerlo directamente con el plugin de maven.

spring.jpa.hibernate.ddl-auto=none
