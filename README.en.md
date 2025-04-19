# Ticho-Rainbow

## Project Overview

Ticho-Rainbow is a modern frontend-backend separated admin system based
on [Ticho-Boot2.0](https://github.com/zhajianjun/ticho-rainbow), Spring Boot 3, and Vue3. It integrates cutting-edge
technology stacks, offering features including RBAC permissions management, dynamic menus, scheduled tasks, data
dictionary, email service, and intranet penetration.

**Frontend Tech Stack**:

- Framework: Vue 3.2 + TypeScript
- UI Library: Ant Design Vue 3.x
- Engineering: Vite 4.x + Vue-Vben-Admin Scaffolding
- State Management: Pinia 2.x
- Build Tools: pnpm + ESLint

**Backend Tech Stack**:

- Core Framework: Spring Boot 3.1.4 + Ticho-Boot 2.0
- Security Framework: Spring Security 6.1.4
- ORM Framework: Mybatis-Plus 3.5.3
- Database: MySQL 8.0
- Development Environment: JDK17 + Maven 3.8+

---

## 🚀 Quick Start

### ☑️ Prerequisites

Before getting started with ticho-rainbow, ensure your runtime environment meets the following requirements:

- **Programming Language:** TypeScript
- **Package Manager:** Npm
- **Container Runtime:** Docker

### ⚙️ Installation

Install ticho-rainbow using one of the following methods:

**Build from source:**

1. Clone the ticho-rainbow repository:

```sh
❯ git clone https://github.com/zhajianjun/ticho-rainbow
```

2. Navigate to the project directory:

```sh
❯ cd ticho-rainbow
```

3. Install the project dependencies:

**Using `npm`**
&nbsp; [<img align="center" src="https://img.shields.io/badge/npm-CB3837.svg?style={badge_style}&logo=npm&logoColor=white" />](https://www.npmjs.com/)

```sh
❯ npm install
```

**Using `docker`**
&nbsp; [<img align="center" src="https://img.shields.io/badge/Docker-2CA5E0.svg?style={badge_style}&logo=docker&logoColor=white" />](https://www.docker.com/)

```sh
❯ docker build -t zhajianjun/ticho-rainbow .
```

### 🤖 Usage

Run ticho-rainbow using the following command:
**Using `npm`**
&nbsp; [<img align="center" src="https://img.shields.io/badge/npm-CB3837.svg?style={badge_style}&logo=npm&logoColor=white" />](https://www.npmjs.com/)

```sh
❯ npm start
```

**Using `docker`**
&nbsp; [<img align="center" src="https://img.shields.io/badge/Docker-2CA5E0.svg?style={badge_style}&logo=docker&logoColor=white" />](https://www.docker.com/)

```sh
❯ docker run -it {image_name}
```

## 🎗 License

This project is protected under the [MIT License](https://choosealicense.com/licenses/mit/) License. For more details,
refer to the [LICENSE](https://github.com/zhajianjun/ticho-rainbow/blob/main/LICENSE) file.
