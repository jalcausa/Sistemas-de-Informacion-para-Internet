# Apuntes de Docker

## 1. Introducción y Conceptos Básicos

### 1.1 ¿Qué es Docker?
Docker es una plataforma para desarrollar, enviar y ejecutar aplicaciones dentro de **contenedores**. Los contenedores permiten empaquetar una aplicación junto con todas sus dependencias y configuraciones, proporcionando un entorno de ejecución aislado y portátil.

### 1.2 Contenedores
Un **contenedor** es una instancia ligera y aislada que se ejecuta con el mismo kernel del sistema operativo de la maquina anfitriona.  
- Utilizan mecanismos como _cgroups_ y _namespaces_ para aislar recursos (procesos, memoria, usuarios, red, etc.).  
- Son mucho más eficientes que las máquinas virtuales, ya que comparten el kernel del host y tienen un overhead mínimo.

### 1.3 Imágenes
Una **imagen** es la plantilla o blueprint a partir de la cual se crean los contenedores.  
- Se puede pensar en la relación entre imagen y contenedor de forma similar a la que existe entre un programa y un proceso.  
- Existen imágenes oficiales que se encuentran en DockerHub, aunque también se pueden crear imágenes personalizadas o utilizar imágenes de terceros (por ejemplo, precediendo el nombre del autor).

### 1.4 Volúmenes y Bind Mounts
Los **volúmenes** son unidades de almacenamiento que permiten persistir datos fuera del ciclo de vida del contenedor.  
- Se crean y administran mediante comandos como `docker volume create` y se montan en contenedores con la opción `-v`.  
- Los **bind mounts** enlazan un directorio o fichero del sistema anfitrión a un punto de montaje dentro del contenedor, permitiendo, por ejemplo, desarrollar o editar ficheros en tiempo real.

### 1.5 Ventajas y Desventajas de los Contenedores

**Ventajas:**
- El entorno de ejecución se prepara una sola vez y se puede replicar en múltiples instancias.
- El aislamiento de contenedores previene conflictos de versiones y configuraciones.
- Amplia disponibilidad de contenedores pre-construidos (DockerHub).
- Facilidad para realizar copias de seguridad y restaurar entornos.
- Escalabilidad sencilla de aplicaciones y servicios.

**Desventajas:**
- Requieren recursos de CPU y espacio en disco.
- Puede haber un ligero aumento en el tiempo de ejecución si se utiliza un kernel distinto.
- Curva de aprendizaje inicial.
- La interfaz de uso es, en gran medida, de línea de comandos o basada en web.

---

## 2. Gestión de Contenedores, Imágenes y Volúmenes

### 2.1 Comandos Básicos
- **Ejecutar un contenedor interactivo (ejemplo con Alpine):**
  ```bash
  docker run -it alpine
  ```
  - `-i`: Conecta la entrada estándar.
  - `-t`: Crea un terminal virtual para interactuar con el contenedor.

- **Listar contenedores (incluyendo detenidos):**
  ```bash
  docker container ls -a
  ```

- **Arrancar un contenedor detenido:**
  ```bash
  docker container start myweb
  ```

- **Detener un contenedor:**
  ```bash
  docker container stop myweb
  ```

- **Ver logs del contenedor en tiempo real:**
  ```bash
  docker container logs -f myweb
  ```

- **Eliminar contenedores:**
  ```bash
  docker container rm myweb
  ```

- **Ejecutar contenedor en segundo plano (`-d`) y limpiar al finalizar (`--rm`):**
  ```bash
  docker run --rm -d -p 80:80 httpd:alpine
  ```

### 2.2 Imágenes
- **Listar imágenes:**
  ```bash
  docker image ls
  ```

- **Eliminar una imagen:**
  ```bash
  docker image rm alpine
  ```

- **Descargar una imagen (pull):**
  ```bash
  docker pull alpine
  ```

- **Construir una imagen personalizada:**
  ```bash
  docker build . -t mycont
  ```

### 2.3 Volúmenes
- **Crear un volumen:**
  ```bash
  docker volume create myapp
  ```

- **Ejecutar un contenedor montando un volumen:**
  ```bash
  docker run --rm -it -v myapp:/app alpine
  ```

- **Utilizar un bind mount (montar directorio actual):**
  ```bash
  docker run --rm -it -v $(pwd):/app alpine
  ```

- **Realizar una copia de seguridad de un volumen:**
  ```bash
  docker run --rm -v $(pwd):/backup -v myapp:/data alpine sh -c "tar czf /backup/archive.tgz -C /data ."
  ```

- **Listar volúmenes:**
  ```bash
  docker volume ls
  ```

- **Eliminar un volumen:**
  ```bash
  docker volume rm myapp
  ```

---

## 3. Dockerfile

El **Dockerfile** es un fichero de texto que contiene todas las instrucciones necesarias para construir una imagen Docker personalizada. Es el componente clave para automatizar la creación y configuración de la imagen.

### 3.1 Estructura y Comandos Básicos
Un Dockerfile se compone de una serie de instrucciones que se ejecutan en orden. Algunas de las instrucciones más importantes son:

- **FROM:** Define la imagen base.
- **LABEL:** Permite agregar metadatos a la imagen (por ejemplo, autor, versión, descripción).
- **RUN:** Ejecuta comandos durante la construcción de la imagen, por ejemplo, para instalar paquetes.
- **COPY y ADD:** Copian archivos o directorios desde el contexto local a la imagen.
- **WORKDIR:** Define el directorio de trabajo dentro de la imagen.
- **ENV:** Configura variables de entorno.
- **EXPOSE:** Informa sobre los puertos que se pretenden exponer (no los enlaza, solo la documentación).
- **CMD:** Define el comando por defecto que se ejecutará cuando se inicie un contenedor.
- **ENTRYPOINT:** Permite definir un comando fijo que se ejecutará y que puede complementarse con CMD para pasarle argumentos.

### 3.2 Ejemplo Básico
Imaginemos que deseamos construir una imagen de un servidor web simple usando la imagen oficial de Apache:

```dockerfile name=Dockerfile
# Usamos una imagen base
FROM httpd:alpine

# Copiamos el fichero index.html al directorio de documentos de Apache
COPY index.html /usr/local/apache2/htdocs/

# Exponemos el puerto 80 (documentativo)
EXPOSE 80

# Comando por defecto (en este caso no es necesario ya que la imagen base ya lo define)
CMD ["httpd-foreground"]
```

### 3.3 Detalles y Buenas Prácticas

- **Minimizar las capas:** Cada instrucción (como RUN, COPY, etc.) genera una nueva capa en la imagen. Es recomendable agrupar comandos cuando sea posible para reducir el tamaño final.
- **Usar multistage builds:** Permiten compilar y empaquetar aplicaciones en varias fases, eliminando dependencias innecesarias en la imagen final.
- **Variables y argumentos:** Utiliza `ARG` y `ENV` para parametrizar la construcción y facilitar la reutilización del Dockerfile en diferentes entornos (desarrollo, pruebas, producción).
- **Documentar el Dockerfile:** Añadir comentarios ayuda a entender la intención de cada instrucción y facilita el mantenimiento.
- **Limpiar la cache y archivos temporales:** En instrucciones como RUN, es habitual limpiar archivos que ya no se necesitan para reducir el tamaño de la imagen.

### 3.4 Construcción y Etiquetado
Para construir la imagen a partir del Dockerfile, se usa el siguiente comando:
```bash
docker build . -t mycont
```
La opción `-t` asigna el nombre o etiqueta (tag) a la imagen, facilitando su identificación para futuros despliegues o subidas a repositorios como DockerHub o GitHub Packages.

---

## 4. Docker Compose

Docker Compose es una herramienta que permite definir y ejecutar aplicaciones Docker multi-contenedor a partir de un archivo YAML (usualmente denominado `docker-compose.yml`). Esto permite orquestar servicios que, juntos, conforman una aplicación completa.

### 4.1 Conceptos Fundamentales
- **Servicios:** Cada servicio define un contenedor o grupo de contenedores basados en una imagen. Se pueden especificar detalles como build, imagen, variables de entorno, volúmenes, puertos, etc.
- **Redes:** Docker Compose crea redes virtuales internas para permitir la comunicación entre servicios.
- **Volúmenes:** Similar a Docker, se pueden definir volúmenes para persistir datos entre contenedores o compartir información entre ellos.

### 4.2 Ejemplo Básico de docker-compose.yml
El siguiente fichero define dos servicios: una aplicación web y una base de datos MySQL:
```yaml name=docker-compose.yml
version: '3.8'
services:
  web:
    build: ./web
    volumes:
      - "files:/var/www/html"
    ports:
      - "8081:80"
    restart: always
  db:
    image: "mysql:5.7.30"
    environment:
      MYSQL_ROOT_PASSWORD: root_password
      MYSQL_DATABASE: secret_db
      MYSQL_USER: secret_user
      MYSQL_PASSWORD: secret_password
    volumes:
      - "db:/var/lib/mysql"
    restart: always

volumes:
  db:
  files:
```
Este fichero permite levantar una infraestructura sencilla donde:
- El servicio "web" se construye a partir de un Dockerfile ubicado en `./web`.
- Se monta un volumen para almacenar los ficheros de la aplicación.
- Se mapea el puerto 80 del contenedor a 8081 del host.
- El servicio "db" utiliza la imagen oficial de MySQL con variables de entorno para la configuración de la base de datos y monta un volumen persistente.

### 4.3 Ejecución y Administración
- Para desplegar los servicios definidos en el fichero:
  ```bash
  docker-compose up -d
  ```
  O, en versiones modernas:
  ```bash
  docker compose up -d
  ```
- Para detener los contenedores sin borrarlos:
  ```bash
  docker-compose stop
  ```
- Para arrancar nuevamente los contenedores detenidos:
  ```bash
  docker-compose start
  ```
- Para eliminar contenedores y redes creadas (los volúmenes persisten a menos de que se indique lo contrario):
  ```bash
  docker-compose down
  ```
- Para visualizar los logs de todos los contenedores:
  ```bash
  docker-compose logs
  ```

### 4.4 Buenas Prácticas en Docker Compose

- **Separar la configuración:** Es recomendable separar los ficheros de configuración de la aplicación en distintos ficheros o servicios.  
- **Uso de variables de entorno:** Para diferentes entornos (producción, desarrollo, testing) se pueden parametrizar los ficheros con variables de entorno.
- **Versionado del fichero Compose:** Utilizar la versión adecuada (por ejemplo, '3.8') para aprovechar nuevas funcionalidades y mantener compatibilidad.
- **Persistencia de datos:** Asegurarse de definir volúmenes para servicios de bases de datos y otras aplicaciones que requieran persistencia.
- **Escalabilidad:** Docker Compose permite escalar el número de contenedores de un servicio con el comando `docker-compose up --scale <servicio>=<número>`.

### 4.5 Ejemplo de un Caso Complejo
Para una aplicación real más compleja que incluye múltiples contenedores (por ejemplo, backend, frontend, base de datos, cache, etc.), se pueden definir varios servicios en un fichero similar a este ejemplo:
```yaml name=docker-compose.yml
version: '3.1'
services:
  backend:
    image: localhost:5000/backend
    build: ./back
    restart: always
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: "jdbc:mysql://db:3306/exampledb"
      SPRING_DATASOURCE_DRIVERCLASSNAME: com.mysql.cj.jdbc.Driver
      SPRING_DATASOURCE_USERNAME: exampleuser
      SPRING_DATASOURCE_PASSWORD: examplepass
      SPRING_JPA_GENERATE_DDL: "true"
      SPRING_H2_CONSOLE_ENABLED: "false"
      SPRING_JPA_DATABASE_PLATFORM: org.hibernate.dialect.MySQLDialect

  db:
    image: mysql:5.7
    restart: always
    environment:
      MYSQL_DATABASE: exampledb
      MYSQL_USER: exampleuser
      MYSQL_PASSWORD: examplepass
      MYSQL_RANDOM_ROOT_PASSWORD: '1'
    volumes:
      - db:/var/lib/mysql
    healthcheck:
      test: "/usr/bin/mysql --user=exampleuser --password=examplepass --execute \"SHOW DATABASES;\""
      interval: 5s
      timeout: 2s
      retries: 60

  frontend:
    image: localhost:5000/frontend
    build: ./front
    ports:
      - "4200:80"
    
volumes:
  db:
```
Este ejemplo ilustra una aplicación completa, en la que:  
- Se construyen y/o usan imágenes personalizadas para el backend y frontend.  
- Se define un healthcheck para garantizar la correcta inicialización de la base de datos.  
- Se gestionan los volúmenes para mantener persistente la información alojada en MySQL.

---

## 5. Otros Conceptos Avanzados

### 5.1 Docker Swarm
Docker Swarm es una herramienta para orquestar contenedores en múltiples nodos (máquinas).  
- Permite gestionar clústeres y distribuir la carga entre varios servidores.
- Los comandos básicos incluyen `docker swarm init`, `docker service create`, `docker service scale`, y `docker stack deploy` cuando se administra un conjunto de servicios mediante un fichero `docker-stack.yml`.

### 5.2 Dockerización de Aplicaciones
El proceso para "dockerizar" una aplicación generalmente sigue estos pasos:
1. Identificar los servicios (web, base de datos, cache, entre otros) necesarios para la aplicación.
2. Crear o utilizar imágenes existentes para cada componente.
3. Configurar volúmenes o bind mounts donde se requiera persistencia.
4. Preparar y parametrizar los ficheros de configuración de las aplicaciones.
5. Definir la infraestructura a través de `docker-compose.yml` (o `docker-stack.yml` para despliegues en Swarm).
6. Iterar y ajustar hasta conseguir la funcionalidad completa.

---

## Resumen

Docker permite crear ambientes de ejecución portátiles y consistentes, facilitando el desarrollo, despliegue y escalamiento de aplicaciones. Tanto los Dockerfile como Docker Compose son herramientas esenciales:
- El **Dockerfile** se utiliza para definir y construir imágenes personalizadas.
- **Docker Compose** orquesta contenedores múltiples, facilitando la configuración y puesta en marcha de aplicaciones con varios componentes.

Estos apuntes te servirán como guía de referencia para empezar a trabajar y profundizar en el ecosistema Docker, cubriendo desde los conceptos básicos hasta ejemplos avanzados de orquestación.