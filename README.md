# Swing Framework – Lightweight UI + DB Layer for Desktop Apps
A lightweight XML-driven UI framework for Java Swing.  
Declarative UI, clean architecture, and fast screen development.

---

## 🚀 Overview
This framework provides:

- **XML-based UI definition (DSL)**
- **Automatic component creation via reflection**
- **ID-based component mapping (DI-like)**
- **Screen flow management**
- **MenuBar DSL**
- **Undo / KeyBinding / Popup utilities**
- **Creator pattern for XML binding**
- **Lightweight, dependency-free architecture**

A framework that enables Swing to be used declaratively through XML.

---

## 🧩 Features

### ✔ XML UI DSL
\<flow-panel align="center" hgap="10"\>  
  \<label\>Name\</label\>  
  \<text id="username"/\>  
  \<password id="password"/\>  
  \<action-button action="LoginController#login"\>Login\</action-button\>  
\</flow-panel\>

### ✔ DI-like Component Mapping
XmlComponent username = componentMap.get("username");

### ✔ Screen Flow
FlowScreenFrame.flow(actionEvent, "login-screen", params);

### ✔ MenuBar DSL  
The framework allows Swing menus to be defined declaratively in XML as well.

### ✔ Undo / KeyUtils / Popup  
A collection of utilities that enhance UX across Swing-based applications.

---

## 📦 Installation
The framework runs on Java 25 and leverages its updated language and runtime features.

---

## 🛠 XML Components

### Panels
- flow-panel
- grid-panel
- border-panel
- vbox-panel
- hbox-panel
- scroll
- tabs

### Components
- label
- text
- password
- action-button
- combobox
- list
- table

---

## 🧱 Example: Flow Panel
\<flow-panel align="center" hgap="8" vgap="8"\>  
  \<label h-align="center"\>Name\</label\>  
  \<text id="name"/\>  
  \<action-button action="MainWindow#submit"\>Send\</action-button\>  
\</flow-panel\>

---

## 🧱 Example: Tabs
\<tabs\>  
  \<tab title="User"\>  
    \<vbox-panel\>  
      \<label\>User List\</label\>  
      \<list id="userList"\>  
        \<item\>Nakamura\</item\>  
        \<item\>sato\</item\>  
      \</list\>  
    \</vbox-panel\>  
  \</tab\>  

  \<tab title="Settings"\>  
    \<grid-panel rows="2" cols="2"\>  
      \<label\>Theme\</label\>  
      \<combobox\>  
        \<item\>Light\</item\>  
        \<item\>Dark\</item\>  
      \</combobox\>  
    \</grid-panel\>  
  \</tab\>  
\</tabs\>

---

## 🧩 Action Binding

### Static method
\<action-button action="LoginController#login"\>Login\</action-button\>

### Window method
\<action-button action="login"\>Login\</action-button\>

---

## 🔧 Screen Parameter
SimpleScreenParameter param = new SimpleScreenParameter();  
param.addParam("userId", 10);  
flowController.move("user-detail", param);

On the screen side:
parameter.getParam("userId", Integer.class).ifPresent(id -> ...);

---

## 🧬 Architecture
XML  
↓  
DomElementWrapper  
↓  
XmlObjectCreator (annotation-based injection)  
↓  
XmlPanel / XmlComponent  
↓  
Swing Component  
↓  
FlowScreenFrame (screen management)

---

## 📁 Project Structure
sn.tools.swing.xml  
 ├─ component  
 ├─ panel  
 ├─ menu  
 ├─ create  
 ├─ flow  
 ├─ util  
sn.tools.xml.bind  
 ├─ annotation  
 ├─ creator  
 ├─ dom  

---

## 🧪 Sample Application
### demo-app
The demo can be launched from the sn.tools.demo.system.SystemMain class.

### demo-game
The demo can be launched from the sn.tools.demo.system.SystemMain class.

---

## 📝 License
Apache2.0 License

