# Swing XML UI Framework
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

Swing を JavaFX の FXML のように宣言的に扱えるようにするためのフレームワーク。

---

## 🧩 Features

### ✔ XML UI DSL
\<flow-panel align="center" hgap="10"\>  
  \<label\>ユーザー名\</label\>  
  \<text id="username"/\>  
  \<password id="password"/\>  
  \<action-button action="LoginController#login"\>ログイン\</action-button\>  
\</flow-panel\>

### ✔ DI-like Component Mapping
XmlComponent username = componentMap.get("username");

### ✔ Screen Flow
FlowScreenFrame.flow(actionEvent, "login-screen", params);

### ✔ MenuBar DSL  
メニューも XML で定義可能。

### ✔ Undo / KeyUtils / Popup  
UX を改善するユーティリティ群。

---

## 📦 Installation
Java 25 以上で動作。

（Maven 化していない場合は jar を直接追加）

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
  \<label h-align="center"\>名前\</label\>  
  \<text id="name"/\>  
  \<action-button action="MainWindow#submit"\>送信\</action-button\>  
\</flow-panel\>

---

## 🧱 Example: Tabs
\<tabs\>  
  \<tab title="ユーザー"\>  
    \<vbox-panel\>  
      \<label\>ユーザー一覧\</label\>  
      \<list id="userList"\>  
        \<item\>中村\</item\>  
        \<item\>佐藤\</item\>  
      \</list\>  
    \</vbox-panel\>  
  \</tab\>  

  \<tab title="設定"\>  
    \<grid-panel rows="2" cols="2"\>  
      \<label\>テーマ\</label\>  
      \<combobox\>  
        \<item\>ライト\</item\>  
        \<item\>ダーク\</item\>  
      \</combobox\>  
    \</grid-panel\>  
  \</tab\>  
\</tabs\>

---

## 🧩 Action Binding

### Static method
\<action-button action="LoginController#login"\>ログイン\</action-button\>

### Window method
\<action-button action="login"\>ログイン\</action-button\>

---

## 🔧 Screen Parameter
SimpleScreenParameter param = new SimpleScreenParameter();  
param.addParam("userId", 10);  
flowController.move("user-detail", param);

画面側：  
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
### demo-game

---

## 📝 License
Apache2.0 License

