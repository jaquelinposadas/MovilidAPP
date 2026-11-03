# MovilidAPP

Aplicación móvil para Android que ayuda a quienes usan combis y camiones en el Estado de México a consultar rutas, tarifas y tiempos estimados antes de salir.

> **Versión 1.0 (demostración).** Las tarifas, tiempos, tráfico y retrasos son estimaciones de demostración, no datos en vivo.

## Integrantes

| Nombre | Rol |
|---|---|
| Diego Ugarte Fragoso | Desarrollo |
| Jaquelin Posadas Domínguez | Desarrollo |

**Universidad Politécnica de Tecámac** · Ingeniería en Tecnologías de la Información · Grupo 4426ITI
**Asignatura:** Desarrollo de Aplicaciones Móviles · **Profesor:** López González Néstor Apolo
**Ciclo:** septiembre-diciembre 2026

## Funciones

- **Rutas guardadas:** consulta de trayectos habituales (Zumpango - Tecámac, Ecatepec - Coacalco) con tarifa y tiempo estimados.
- **Rutas cercanas:** exploración de trayectos por zona de referencia (Tecámac - Ojo de Agua). Usa la ubicación del dispositivo si la persona da el permiso.
- **Ayuda:** soporte técnico, versión de la aplicación, comentarios (máximo 500 caracteres) y donativos voluntarios de demostración.

## Tecnologías

- Android Studio y Kotlin
- Diseños en XML, Fragments y View Binding
- Navigation Component y Material 3
- Google Play Services Location

Versión mínima de Android: 24.

## Cómo ejecutar el proyecto

1. Instalar [Android Studio](https://developer.android.com/studio).
2. Clonar el repositorio:
   ```
   git clone https://github.com/jaquelinposadas/MovilidAPP.git
   ```
3. Abrir la carpeta del proyecto en Android Studio y esperar a que termine la sincronización de Gradle.
4. Conectar un teléfono Android con la depuración por USB activada, o usar un emulador.
5. Presionar **Run** para instalar y abrir la aplicación.

## Estructura del proyecto

```
app/src/main/
├── java/com/example/movilidapp/   Código en Kotlin (MainActivity y fragmentos)
├── res/layout/                    Diseños de las pantallas (XML)
├── res/navigation/                Grafo de navegación
├── res/drawable/                  Íconos y formas
└── AndroidManifest.xml            Permisos y configuración
```

## Flujo de trabajo

- La rama `main` contiene la versión estable.
- Los cambios nuevos se hacen en ramas (por ejemplo, `feature/rutas-cercanas`) y se integran a `main` con un *merge*.
- Cada commit lleva un mensaje que explica qué cambió.
- Revisado por Diego Ugarte Fragoso
-Pantalla Rutas cercanas: muestra trayectos por zona de referencia, con ubicacion opcional.
