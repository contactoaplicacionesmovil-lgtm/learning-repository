# EasyTask

Aplicación web de gestión de tareas desarrollada con Angular. El proyecto utiliza componentes standalone y actualmente incluye la estructura inicial de la aplicación y su cabecera principal.

## Tecnologías

- Angular 18
- TypeScript 5.4
- RxJS 7.8
- Angular CLI 18
- Karma y Jasmine para pruebas unitarias

## Requisitos

Antes de comenzar, comprueba que tienes Node.js y npm instalados:

```powershell
node --version
npm.cmd --version
```

## Instalación

Abre PowerShell, entra en la carpeta del proyecto e instala las dependencias:

```powershell
cd "C:\Users\gianj\Desktop\Proyectos\01-starting-project\01-starting-project"
npm.cmd install
```

## Ejecutar en desarrollo

Inicia el servidor local con:

```powershell
npm.cmd start
```

Después visita [http://localhost:4200](http://localhost:4200). La página se actualizará automáticamente cuando modifiques el código fuente.

Para detener el servidor, presiona `Ctrl + C` en el terminal.

## Comandos disponibles

| Comando | Descripción |
| --- | --- |
| `npm.cmd start` | Inicia el servidor de desarrollo. |
| `npm.cmd run build` | Genera la versión de producción en `dist/essentials`. |
| `npm.cmd run watch` | Compila en modo desarrollo y observa cambios. |
| `npm.cmd test` | Ejecuta las pruebas unitarias con Karma y Jasmine. |

## Estructura principal

```text
src/
├── app/
│   ├── header/
│   │   ├── header.component.ts
│   │   ├── header.component.html
│   │   └── header.component.css
│   ├── app.component.ts
│   ├── app.component.html
│   └── app.component.css
├── assets/
├── index.html
├── main.ts
└── styles.css
```

## Generar componentes

Para crear un nuevo componente con la versión de Angular CLI instalada en el proyecto:

```powershell
npm.cmd run ng -- generate component nombre-del-componente
```

## Estado del proyecto

La aplicación se encuentra en una etapa inicial. El componente raíz carga `HeaderComponent`, que presenta el nombre y el logotipo de EasyTask.
