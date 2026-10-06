# Especificación Técnica de Arquitectura - Gestión de Dinero UNAB (DUNAB) v1.0

Este documento define la especificación arquitectónica y el contrato de diseño para el desarrollo de la aplicación **Gestión de Dinero UNAB (DUNAB) v1.0** (Prototipo funcional en Java con arquitectura MVC y soporte para Modo Oscuro). Está diseñado como guía estricta para la ejecución de agentes de IA (Antigravity) y el control de versiones en equipo mediante Git.

---

## 1. Mapeo de Paquetes y Ramas Git

El proyecto sigue estrictamente el patrón **Modelo-Vista-Controlador (MVC)**, desacoplando los módulos en tres ramas de desarrollo independientes basadas en `main`:

| Rama Git | Capa / Módulos | Paquetes Java | Responsables |
| :--- | :--- | :--- | :--- |
| `feature/modelo-persistencia` | Backend, Entidades y Persistencia | `com.unab.dunab.model`<br>`com.unab.dunab.persistence` | Integrante 1 |
| `feature/vista-ui` | Frontend, Interfaz Gráfica y Temas | `com.unab.dunab.view`<br>`com.unab.dunab.view.theme` | Integrante 2 |
| `feature/controlador` | Lógica de Negocio y Orquestación | `com.unab.dunab.controller` | Integrante 3 |

---

## 2. Capa Modelo (`com.unab.dunab.model`)
**Rama Git:** `feature/modelo-persistencia`

Clases POJO encapsuladas con atributos privados, constructores vacíos y parametrizados, y métodos getters/setters estándar.

### 2.1. `Usuario.java`
* **Atributos:**
  * `private String id;`
  * `private String nombre;`
  * `private String correo;`
  * `private String contrasena;`
  * `private String carrera;`
  * `private double saldoDUNAB;`
  * `private double metaDUNAB;` *(Valor por defecto inicializado en `10000.0`)*
* **Métodos requeridos:** Constructores, getters, setters y `toString()`.

### 2.2. `TransaccionDUNAB.java`
* **Atributos:**
  * `private String id;`
  * `private String usuarioId;`
  * `private double monto;`
  * `private String tipo;` *(Valores estrictos: `"INGRESO"` o `"GASTO"`)*
  * `private String concepto;`
  * `private LocalDateTime fecha;`
* **Métodos requeridos:** Constructores, getters, setters.

### 2.3. `Encuentro.java`
* **Atributos:**
  * `private String id;`
  * `private String titulo;`
  * `private String lugar;` *(Valores permitidos: `"Cafetería L"`, `"Cafetería el Bosque"`, `"Banú"`, `"Biblioteca"`, `"Cafetería CSU"`)*
  * `private LocalDateTime fechaHora;`
  * `private int cuposDisponibles;`
  * `private List<String> estudiantesInscritosIds;`
* **Métodos requeridos:** Constructores, getters, setters y métodos auxiliares para inscripción.

---

## 3. Capa Persistencia (`com.unab.dunab.persistence`)
**Rama Git:** `feature/modelo-persistencia`

Sistema de almacenamiento local basado en archivos JSON ubicados en la carpeta raíz `data/` (`data/usuarios.json`, `data/transacciones.json`, `data/encuentros.json`).

### 3.1. `IPersistencia.java` (Interfaz Genérica)
```java
public interface IPersistencia<T> {
    void guardar(T entidad) throws IOException;
    List<T> obtenerTodos() throws IOException;
    void actualizar(T entidad) throws IOException;
    void eliminar(String id) throws IOException;
}
```

### 3.2. Clases DAO
* **`UsuarioDAO.java`**: Implementa operaciones para persistir y buscar usuarios por correo y ID.
* **`TransaccionDAO.java`**: Gestiona el almacenamiento de transacciones filtrables por `usuarioId`.
* **`EncuentroDAO.java`**: Administra los encuentros universitarios y listas de asistentes.

---

## 4. Capa Controlador (`com.unab.dunab.controller`)
**Rama Git:** `feature/controlador`

Orquestadores de la lógica de negocio. Se comunican exclusivamente con `Model` y `Persistence`.

### 4.1. `AuthController.java`
* `boolean registrarUsuario(String nombre, String correo, String contrasena, String carrera)`
* `Usuario autenticar(String correo, String contrasena)`
* `Usuario getUsuarioSesionActual()`
* `void cerrarSesion()`

### 4.2. `DUNABController.java`
* `boolean registrarIngresoGasto(String usuarioId, double monto, String concepto, String tipo)`
* `double calcularPromedioSemanal(String usuarioId)`
* `double calcularPromedioMensual(String usuarioId)`
* `double calcularPromedioAnual(String usuarioId)`
* `double calcularPromedioSemestral(String usuarioId)`
* `double obtenerDUNABFaltantesGraduacion(String usuarioId)` *(Calcula `metaDUNAB - saldoDUNAB`)*

### 4.3. `EncuentroController.java`
* `List<Encuentro> listarEncuentros()`
* `boolean inscribirUsuario(String encuentroId, String usuarioId)`
* `boolean darseDeBaja(String encuentroId, String usuarioId)`
* CRUD completo de encuentros.

---

## 5. Capa Vista e Interfaz Gráfica (`com.unab.dunab.view`)
**Rama Git:** `feature/vista-ui`

Desarrollada en Java Swing con componentes modernos y soporte para Modo Oscuro (`com.unab.dunab.view.theme`).

### 5.1. Componentes Visuales
* **`LoginFrame.java`**: Ventana de autenticación (Correo, Contraseña, Botón Ingresar, Enlace Registro).
* **`RegisterFrame.java`**: Formulario de registro de nuevos estudiantes.
* **`MainDashboardFrame.java`**: Ventana principal con navegación por pestañas (`JTabbedPane`):
  * **`PerfilPanel.java`**: Muestra datos del usuario, saldo DUNAB actual y DUNABs faltantes para graduación.
  * **`TransaccionesPanel.java`**: Tabla de ingresos/gastos, formularios de registro y panel de promedios (semanal, mensual, semestral, anual).
  * **`EncuentrosPanel.java`**: Listado de encuentros por cafeterías/biblioteca y botones de inscripción/baja.

### 5.2. `ThemeManager.java` (`com.unab.dunab.view.theme`)
* `void toggleDarkMode(JFrame frame)`: Aplica estilos de colores oscuros (FlatDarkLaf o paleta personalizada de grises oscuros `#2b2b2b` y acentos naranjas/azules) y modo claro estándar.

---

## 6. Reglas de Integración Estrictas para Agentes e Integrantes

1. **Flujo Unidireccional:** La Vista (`View`) invoca únicamente al Controlador (`Controller`). Los Controladores invocan a los Modelos (`Model`) y la Persistencia (`Persistence`). **Nunca** una Vista debe invocar métodos de DAO o Persistencia directamente.
2. **Manejo de Errores:** Todos los formularios y eventos de UI deben envolver las llamadas a controladores en bloques `try-catch` y mostrar mensajes informativos mediante `JOptionPane.showMessageDialog`.
3. **Control de Versiones Atómico:** Cada integrante debe realizar commits frecuentes y descriptivos bajo la convención *Conventional Commits* (`feat:`, `fix:`, `refactor:`, `chore:`).

---

## 7. Orientación para Versión 1.0 (Prototipo Funcional)

Para estructurar la **Versión 1.0 (Prototipo)** de manera exitosa:
1. **Fase 1 (Contratos base):** Consensuar firmas de métodos en los Controladores antes de escribir implementaciones pesadas en UI o Backend.
2. **Fase 2 (Desarrollo en Paralelo por Ramas):**
   * Integrante 1 trabaja en `feature/modelo-persistencia`.
   * Integrante 2 trabaja en `feature/vista-ui`.
   * Integrante 3 trabaja en `feature/controlador`.
3. **Fase 3 (Integración y Release v1.0):**
   * Hacer `git push` de cada rama a `origin`.
   * Crear Pull Requests hacia `main`.
   * Realizar el tag de la versión: `git tag -a v1.0 -m "Versión 1.0 - Prototipo Funcional DUNAB"` y `git push origin v1.0`.
