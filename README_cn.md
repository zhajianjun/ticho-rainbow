# Ticho-Rainbow

## 项目说明

Ticho-Rainbow 基于[Ticho-Boot2.0](https://github.com/zhajianjun/ticho-rainbow)、Spring Boot 3 +
Vue3的现代化前后端分离后台管理系统，整合前沿技术栈，提供RBAC权限管理、动态菜单、定时任务、数据字典、邮件发送、内网穿透等功能。

**前端技术栈**：

- 框架：Vue 3.2 + TypeScript
- UI组件库：Ant Design Vue 3.x
- 工程化：Vite 4.x + Vue-Vben-Admin脚手架
- 状态管理：Pinia 2.x
- 构建工具：pnpm + ESLint

**后端技术栈**：

- 核心框架：Spring Boot 3.1.4 + Ticho-Boot 2.0
- 安全框架：Spring Security 6.1.4
- ORM框架：Mybatis-Plus 3.5.3
- 数据库：MySQL 8.0
- 开发环境：JDK17 + Maven 3.8+

---

## 🚀 快速开始

### ☑️ 环境要求

在开始使用 ticho-rainbow 前，请确保满足以下运行环境要求：

- **编程语言**: TypeScript
- **包管理器**: Npm
- **容器运行时**: Docker

### ⚙️ 安装

通过以下任一方式安装 ticho-rainbow：

**从源码构建**:

1. 克隆仓库代码：

```sh
❯ git clone https://github.com/zhajianjun/ticho-rainbow
```

2. 进入项目目录：

```sh
❯ cd ticho-rainbow
```

3. 安装项目依赖：

**Using `npm`**
&nbsp; [<img align="center" src="https://img.shields.io/badge/npm-CB3837.svg?style={badge_style}&logo=npm&logoColor=white" />](https://www.npmjs.com/)

```sh
❯ npm install
```

**使用 `docker`**
&nbsp; [<img align="center" src="https://img.shields.io/badge/Docker-2CA5E0.svg?style={badge_style}&logo=docker&logoColor=white" />](https://www.docker.com/)

```sh
❯ docker build -t zhajianjun/ticho-rainbow .
```

### 🤖 使用说明

运行以下命令启动项目：
**Using `npm`**
&nbsp; [<img align="center" src="https://img.shields.io/badge/npm-CB3837.svg?style={badge_style}&logo=npm&logoColor=white" />](https://www.npmjs.com/)

```sh
❯ npm start
```

**使用 `docker`**
&nbsp; [<img align="center" src="https://img.shields.io/badge/Docker-2CA5E0.svg?style={badge_style}&logo=docker&logoColor=white" />](https://www.docker.com/)

```sh
❯ docker run -it {image_name}
```

## 🎗 许可证

本项目采用 [MIT 许可证](https://choosealicense.com/licenses/mit/)
，更多细节请参阅 [LICENSE](https://github.com/zhajianjun/ticho-boot/blob/main/LICENSE) 文件。
