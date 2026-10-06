# Distribución de Responsabilidades y Roles - Arquitectura MVC

Este documento especifica la división del trabajo entre los tres desarrolladores del equipo para garantizar un desarrollo desacoplado, modular y sin conflictos de fusión en Git.

---

## 1. Asignación de Módulos por Integrante

| Integrante | Capa / Módulo | Paquetes asignados | Responsabilidades principales |
| :--- | :--- | :--- | :--- |
| **Integrante 1** | **Modelo y Persistencia** | `com.unab.dunab.model`<br>`com.unab.dunab.persistence` | - Definición de entidades (`Usuario`, `Transaccion`, `Encuentro`, etc.).<br>- Reglas de integridad de datos y validaciones estructurales.<br>- Mecanismos de persistencia en archivos (JSON/CSV/Serialización) o base de datos.<br>- Implementación del patrón DAO/Repository. |
| **Integrante 2** | **Vista y UI/UX** | `com.unab.dunab.view`<br>`com.unab.dunab.view.theme` | - Diseño e implementación de la interfaz gráfica (Swing o JavaFX).<br>- Formularios, tablas, ventanas de diálogo y navegación visual.<br>- Gestión de estilos y soporte para Modo Oscuro (`view/theme`).<br>- Exposición de eventos visuales y listeners para el controlador. |
| **Integrante 3** | **Controlador y Lógica** | `com.unab.dunab.controller` | - Lógica de negocio, cálculos y orquestación del flujo de la aplicación.<br>- Intermediación entre las vistas y los modelos/persistencia.<br>- Manejo de eventos de usuario y actualización de estados en la UI.<br>- Coordinación de transacciones y operaciones complejas. |

---

## 2. Estructura de Directorios

```text
src/
└── com/
    └── unab/
        └── dunab/
            ├── model/           # Integrante 1: Entidades y objetos de datos
            ├── persistence/     # Integrante 1: Gestión de I/O, almacenamiento y repositorios
            ├── controller/      # Integrante 3: Orquestación, controladores y reglas de negocio
            └── view/            # Integrante 2: Vistas, pantallas y componentes UI
                └── theme/       # Integrante 2: Configuración de temas (Claro / Oscuro)
```

---

## 3. Recomendaciones de Flujo de Trabajo en Git

1. **Trabajo en ramas independientes:**
   - Cada integrante debe trabajar en ramas de características basadas en `main`, por ejemplo:
     - `feature/modelo-entidades` (Integrante 1)
     - `feature/interfaz-principal` (Integrante 2)
     - `feature/controlador-transacciones` (Integrante 3)
2. **Contratos e Interfaces:**
   - Antes de implementar la lógica acoplada, los integrantes 1, 2 y 3 deben consensuar las interfaces públicas de los modelos y controladores para trabajar en paralelo sin bloqueos.
3. **Commits atómicos:**
   - Realizar commits pequeños y con mensajes bajo la convención *Conventional Commits* (`feat:`, `fix:`, `chore:`, `refactor:`).
