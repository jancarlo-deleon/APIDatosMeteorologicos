# API Datos Meteorológicos

#### API realizada en Java con el framework Spring Boot, la cual obtiene y presenta datos meteorológicos desde OpenWeatherMap.

 Esta API cuenta  con las siguientes características:


- Endpoints que permiten el registro y autenticación de usuarios.
- Endpoints que permiten consultar el clima actual, generar pronosticos por 5 dias y la contaminación de una ciudad, utilizando el nombre de la misma como parametro.
- Auditoria de las consultas a endpoints realizadas por el usuario.
- Seguridad con Spring Security 6.
- Autenticaciones con JWT y Bearer token.
- Cache manejado con Caffeine para reducir llamadas a las APIs externas.
- Limitaciones de tasa con Bucket4j para limitar el numero de peticiones que puede realizar un usuario en un marco de tiempo especificado.
- Dockerización para facilidad de ejecución.

## Requerimientos

Para poder utilizar esta API, se necesita contar con los siguientes requerimientos:

IDE
- [NetBeans](https://netbeans.apache.org/front/main/download/index.html)
> [!NOTE]
> El proyecto fué desarrollado con el IDE NetBeans, pero puedes usar el de tu preferencia.

Compilación
- [Java 17](https://www.oracle.com/java/technologies/downloads/#java17)

Ejecución del proyecto
- [Docker](https://www.docker.com/products/docker-desktop/)

Obtencion de API KEY para realizar consultas
- [OpenWeatherAPI](https://home.openweathermap.org/users/sign_up)
> [!NOTE]
> En **application.properties**, se debe de cambiar el valor de la variable **"datosmeteorologicos.app.apikey"** para colocar la KEY que permita las consultas a OpenWeatherMap

## Armar el contenedor para ejecutar el proyecto

Para poder ejecutar el proyecto del presente repositorio, se necesita seguir una serie de pasos:

                
1. **Clonar el repositorio**

    Puede lograrse utilizando 
   ```
    https://github.com/jancarlo-deleon/APIDatosMeteorologicos.git
   ```
2. **Compilar el proyecto**

    Clean and Build al proyecto para generar el .jar 
    
3.  **Crear la imagen del proyecto**

    Ejecutar el siguiente comando en el CMD:
     ```
      docker build -t datosmeteorologicosspring .
     ```
5. **Crear el contenedor**

   Ejecutar el siguiente comando en el CMD:
     ```
      docker compose up
     ```
7. **Importar dump de la base datos**

   Importar el siguiente dump de base de datos una vez que se haya levantado el contenedor, para así poder realizar la correcta ejecución del proyecto
    
      - [Dump para la base de datos](https://drive.google.com/file/d/1zaxvDj7QSNYeqjV27ajbr7V4jSQHa1EZ/view?usp=drive_link)
    
> [!NOTE]
> Generar los CMD estando en el directorio raiz del proyecto.

## Documentación

  Teniendo el contenedor levantado, puedes visitar el siguiente URL para poder acceder a la documentación y realización de pruebas de cada uno de los endpoints:
  
   ```
      http://localhost:8188/swagger-ui/index.html
   ```
> [!NOTE]
> El archivo **docker-compose.yml** se ha configurado para levantar el proyecto en el puerto 8188. Puedes cambiar esto en caso lo necesites.

## Ejemplos de solicitudes y respuestas

### Endpoint: /auth/signup
##### Utilizado para el registro de nuevos usuarios.

- Solicitud: **POST** `http://localhost:8188/auth/signup`

    Cuerpo de la solicitud:
   ```JSON
  {
    "username":"pruebagit",
    "email":"pruebagit@mail.com",
    "password":"123456"
  }
  ```
   Respuesta:
    ```JSON
  {
  "message": "Usuario registrado con exito."
  }
  ```
### Endpoint: /auth/signin
##### Utilizado autenticar a un usuario y generar el token a utilizar para consultas.
    
- Solicitud: **POST** `http://localhost:8188/auth/signin`

    Cuerpo de la solicitud:
   ```JSON
  {
  "username": "pruebagit",
  "password": "123456"
  }
  ```
   Respuesta:
    ```JSON
  {
  "id": 1,
  "username": "pruebagit",
  "email": "pruebagit@mail.com",
  "roles": [
    "ROLE_USER"
  ],
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcnVlYmFnaXQiLCJpYXQiOjE3MDc5NTE0NDQsImV4cCI6MTcwNzk1MjA0XX0.k41GVcdZIc303WxciNLq0OCYkOM0r58kzCynxRW7qYU",
  "tokenType": "Bearer"
  }
  ```
### Endpoint: /datosmeteorologicos/clima/{ciudad}
##### Utilizado para consultar el clima actual de una ciudad, pasando como parámetro el nombre de la misma.

- Solicitud: **GET** `http://localhost:8188/datosmeteorologicos/clima/{ciudad}`

    Cuerpo de la solicitud:
   ```
  http://localhost:8188/datosmeteorologicos/clima/madrid
  ```
   Respuesta:
    ```JSON
  {
    "name": "Madrid",
    "weather": [
      {
        "description": "cielo claro"
      }
    ],
    "wind": {
      "speed": 1.54
    },
    "coord": {
      "lon": -3.7026,
      "lat": 40.4165
    },
    "main": {
      "temp": 286.26,
      "pressure": 1021,
      "humidity": 86
    }
  }
  ```
### Endpoint: /datosmeteorologicos/pronostico/{ciudad}
##### Utilizado para consultar el pronostico de los próximos 5 días de una ciudad, pasando como parámetro el nombre de la misma.

- Solicitud: **GET** `http://localhost:8188/datosmeteorologicos/pronostico/{ciudad}`

    Cuerpo de la solicitud:
   ```
  http://localhost:8188/datosmeteorologicos/pronostico/valencia
  ```
   Respuesta:
    ```JSON
  {
  "list": [
    {
      "dt": 1707955200,
      "main": {
        "temp": 283.72,
        "temp_min": 283.72,
        "temp_max": 285.25,
        "pressure": 1022,
        "sea_level": 1022,
        "grnd_level": 979,
        "humidity": 81
      },
      "weather": [
        {
          "main": "Clouds",
          "description": "nubes"
        }
      ],
      "clouds": {
        "all": 100
      },
      "wind": {
        "speed": 0.64,
        "deg": 338
      },
      "visibility": 10000,
      "pop": 0,
      "dt_txt": "2024-02-15 00:00:00"
    },
    {
      "dt": 1707966000,
      "main": {
        "temp": 284.13,
        "temp_min": 284.13,
        "temp_max": 284.94,
        "pressure": 1022,
        "sea_level": 1022,
        "grnd_level": 977,
        "humidity": 81
      },
      "weather": [
        {
          "main": "Clouds",
          "description": "nubes"
        }
      ],
      "clouds": {
        "all": 100
      },
      "wind": {
        "speed": 0.37,
        "deg": 237
      },
      "visibility": 10000,
      "pop": 0,
      "dt_txt": "2024-02-15 03:00:00"
    },
    {
      "dt": 1707976800,
      "main": {
        "temp": 284.53,
        "temp_min": 284.53,
        "temp_max": 284.94,
        "pressure": 1021,
        "sea_level": 1021,
        "grnd_level": 976,
        "humidity": 80
      },
      "weather": [
        {
          "main": "Clouds",
          "description": "nubes"
        }
      ],
      "clouds": {
        "all": 100
      },
      "wind": {
        "speed": 0.21,
        "deg": 342
      },
      "visibility": 10000,
      "pop": 0,
      "dt_txt": "2024-02-15 06:00:00"
    },
    {
      "dt": 1707987600,
      "main": {
        "temp": 287.81,
        "temp_min": 287.81,
        "temp_max": 287.81,
        "pressure": 1020,
        "sea_level": 1020,
        "grnd_level": 977,
        "humidity": 67
      },
      "weather": [
        {
          "main": "Clouds",
          "description": "nubes"
        }
      ],
      "clouds": {
        "all": 100
      },
      "wind": {
        "speed": 1.28,
        "deg": 85
      },
      "visibility": 10000,
      "pop": 0,
      "dt_txt": "2024-02-15 09:00:00"
    },
    {
      "dt": 1707998400,
      "main": {
        "temp": 290.99,
        "temp_min": 290.99,
        "temp_max": 290.99,
        "pressure": 1018,
        "sea_level": 1018,
        "grnd_level": 975,
        "humidity": 54
      },
      "weather": [
        {
          "main": "Clouds",
          "description": "nubes"
        }
      ],
      "clouds": {
        "all": 100
      },
      "wind": {
        "speed": 2.63,
        "deg": 88
      },
      "visibility": 10000,
      "pop": 0,
      "dt_txt": "2024-02-15 12:00:00"
    },
   ]
  }
  ```
### Endpoint: /datosmeteorologicos/contaminacion/{ciudad}
##### Utilizado para consultar la contaminacion de una ciudad, pasando como parámetro el nombre de la misma.

- Solicitud: **GET** `http://localhost:8188/datosmeteorologicos/contaminacion/{ciudad}`

    Cuerpo de la solicitud:
   ```
  http://localhost:8188/datosmeteorologicos/contaminacion/tokyo
  ```
   Respuesta:
    ```JSON
  {
  "coord": {
    "lon": 139.7595,
    "lat": 35.6828
  },
  "list": [
    {
      "main": {
        "aqi": 3
      },
      "components": {
        "co": 647.54,
        "no": 146.63,
        "no2": 73.34,
        "o3": 1.48,
        "so2": 87.74,
        "pm2_5": 45.43,
        "pm10": 56.41,
        "nh3": 10.89
      }
    }
  ]
  }
  ```
> [!IMPORTANT]
> Se debe de estar autenticado para que se pueda generar el token y poder realizar las consultas

