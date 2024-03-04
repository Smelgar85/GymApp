# GymApp

## Aplicación para la gestión de usuarios de gimnasio.

### ¿Qué es GymApp y cuál es su propósito?

Este software está destinado a la gestión de usuarios y abonos de un gimnasio. El propósito de este software es que sirva de apoyo al personal de recepción a la hora de inscribir, modificar y dar de baja a clientes en la base de datos del gimnasio. Además, podremos dar de alta abonos de clientes, renovarlos, modificarlos o borrarlos. Como añadido, ayuda al personal a identificar qué abonos están próximos a caducar. Se ha procurado que el software sea fácil y cómodo de usar.


### 1. Interfaz de Usuario
Para la interfac de usuario se ha escogido el color rojo. La interfaz de usuario se compone de elementos comunes como son el Header y el menú lateral, junto a los botones de éste y el logo, que sirve a su vez de botón para regresar a la pantalla principal.
Estos botones nos permitirán navegar por la aplicación e ir a todas las secciones sin importar en qué sección nos encontremos.
![imagen](https://github.com/iesfuengirola1es/proyectofinal-Smelgar85/assets/145584218/2046b548-f639-4669-94f8-cd275e015f04)

### 2. Pantalla Principal
La pantalla principal se compone de los elementos comunes, y una tabla. En esta tabla se muestran los abonos a vencer en los próximos 15 días, ordenados por la fecha más próxima.
Se nos muestra el nombre, apellidos, teléfono e email del usuario, así como si es usuario premium.
![imagen](https://github.com/iesfuengirola1es/proyectofinal-Smelgar85/assets/145584218/e182d434-3845-48b9-88c4-b4c37c140d44)
El objetivo de esto es que cada día, cuando el personal se siente frente a la pantalla, pueda ver claramente qué usuarios tienen sus abonos a punto de caducar.
Esto les permite realizazr una atención al cliente más personalizada, pudiendo contactar con el cliente, enviar recordatorios, etc.

### 3. Creación de Nuevo Usuario
La pantalla de creación de nuevo usuario nos muestra un formulario muy simple donde introducir sus datos de manera sencilla. Se realiza la verificación de los campos impidiendo introducir números en el nombre y apellidos, DNI o NIE mal con formato incorrecto, fechas erróneas, teléfonos con caracteres, emails mal formados, etc.
Tiene dos botones, uno para guardar el usuario, y el otro para cancelar, que nos devuelve a la pantalla principal.
![imagen](https://github.com/iesfuengirola1es/proyectofinal-Smelgar85/assets/145584218/fe9e575f-dec8-4e19-8ba8-590f99a237f8)


### 4. Creación de Nuevo Abono
La pantalla de creación de abonos nos permite crear abonos asociados a usuarios. Un usuario solo puede disponer de un abono. Para rellenar el formulario de abonos primero debemos escoger un usuario existente. Ello se hace desde el botón "Buscar..." junto al campo de Usuario (el menú que aparece se explica en el siguiente punto).
El resto de campos es simple, número de meses es el número de meses que se va a contratar. El precio mensual es un combobox en el que se pueden escoger varios precios, tenemos un combobox con el descuento, un campo de fecha de inicio, un campo de fecha de fin (que se auto-completa calculando la fecha de fin dependiendo del número de meses), un checkbox premium, que suma 10€ de recargo al importe mensual (este abono premium permite el uso de la sauna, piscina u otras instalaciones especiales)
Se nos muestra el importe total, del que se informará al usuario, que resulta del cálculo de (Precio mensual + Premium (en caso de estar seleccionado) * Número de meses) * Descuento. En este formulario existen también reglas de verificación: solo se podrán introducir de 1 a 12 meses en el número de meses, y la fecha debe ser correcta. Solo se pueden crear abonos en fechas actuales o futuras, no pasadas.
Por último tenemos un botón de confirmar abono y otro de cancelar.
![imagen](https://github.com/iesfuengirola1es/proyectofinal-Smelgar85/assets/145584218/8e82d343-4e2a-4cc7-9090-b495ba6c0f85)

### 5. Búsqueda de Usuarios
Esta pantalla nos permite seleccionar usuarios que rescatar en las pantallas de Creación de nuevo abono, y gestión de usuarios.
En ella se nos permite buscar clientes por varios criterios: DNI, Teléfono o Email. Simplemente seleccionamos el criterio en el combobox, introducimos los datos, pulsamos buscar y se nos muestra el usuario. Pinchamos en él, pulsamos en seleccionar, se cerrará la ventana y nos llevará a la ventana en la que hemos querido rescatar al usuario.
Por defecto se muestran los usuarios más recientes.
![imagen](https://github.com/iesfuengirola1es/proyectofinal-Smelgar85/assets/145584218/81e15c46-b4be-40fb-8c58-610d094b9f79)


### 6. Gestión de Usuarios y Abonos
Esta es quizá la pantalla más compleja. Se divide en dos secciones, una para modificar o borrar los datos del usuario, y otra para modificar o borrar los datos del abono asociado a dicho usuario.
Para usarla primero debemos escoger un usuario pulsando en "Buscar usuario..." (esto nos lleva a la ventana de Búsqueda de usuarios).
Una vez escogido el usuario, se nos rellenarán todos los campos con sus datos y, en caso de tener un abono asociado al usuario, se rellenarán también los datos de éste, en caso de no tener un abono asociado, se nos informará con un cuadro de aviso.
Una vez rellenos todos los datos del usuario, podremos modificarlos desde los mismos campos y pulsando sobre el botón "GUARDAR CAMBIOS". El DNI, por razones obvias, no puede ser modificado.
Además se podrá usar el botón "ELIMINAR USUARIO" para eliminarlo de nuestra base de datos. Al usar este botón se nos avisa de que el usuario, y su abono asociado se borrarán.
La sección del abono, que es la inferior de la pantalla, nos mostrará los datos del abono del usuario, pudiendo renovarlo introduciendo una nueva fecha y meses de abono, precio, etc.
Tenemos 3 botones, RENOVAR ABONO que confirmará los cambios, ELIMINAR ABONO, que eliminará únicamente el abono (no el usuario) y el botón CANCELAR, que nos devuelve a la página principal.
![imagen](https://github.com/iesfuengirola1es/proyectofinal-Smelgar85/assets/145584218/95176be2-5563-4787-8020-81f339b27a0e)

### IMPORTANCIA DE LA TABLA DE VENCIMIENTOS PRÓXIMOS

Esta tabla es importante porque:

1. **Organización:** Ayuda a llevar un control de qué abonos están a punto de expirar. Si el gestor tiene esta info a mano, puede planificar mejor qué hacer.
2. **Comunicación:** Con esta tabla el gestor puede saber a quién tiene que avisar para que no se les pase renovar. 
3. **Retención de clientes:** Si se avisa a tiempo a los usuarios, es más probable que renueven su abono. Es decir, esta tabla ayuda a que la gente no deje de ir al gimnasio por un despiste con las fechas.
4. **Servicio al cliente:** Muestra que la empresa está pendiente de las necesidades de sus usuarios y les ofrece un servicio proactivo.
5. **Ingresos del gimnasio:** Si se evita que los abonos caduquen sin que el usuario se dé cuenta, también se asegura un flujo constante de ingresos para el gimnasio.
