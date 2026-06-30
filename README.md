# 🍲 Panela Amiga

> Sistema de gestão para pequenos negócios de cozinha — **estoque**, **receitas** e **financeiro** integrados num só fluxo.

Aplicação full-stack que ajuda quem vende marmitas, salgados, bolos e afins a controlar o negócio: saber o custo real de cada prato, acompanhar o estoque e enxergar o lucro do período.

---

## 🔗 Demo ao vivo

**👉 [panela-amiga-two.vercel.app](https://panela-amiga-two.vercel.app)**

Conta de teste (ou crie a sua):

| Email | Senha |
|-------|-------|
| `teste.deploy@panela.com` | `senha123` |

> ⏳ O backend roda no plano gratuito do Render e "hiberna" após inatividade. A **primeira** ação pode levar ~50s enquanto ele acorda — depois fica rápido.

---

## 📋 Sobre o projeto

Pequenos empreendedores da cozinha costumam administrar o negócio "no caderno". O Panela Amiga resolve isso integrando três áreas que conversam entre si:

- **Estoque** — ingredientes com quantidade, custo, validade e fornecedor.
- **Receitas** — montadas a partir dos ingredientes; o **custo de produção é calculado no servidor**.
- **Financeiro** — vendas (entradas) e despesas (saídas), com relatório consolidado.

**O ciclo que conecta tudo:**

```
custo do ingrediente → custo da receita → venda consome estoque → compra repõe estoque
```

---

## ✨ Funcionalidades

- 🔐 **Autenticação JWT** com cadastro/login e **isolamento de dados por usuário** (cada conta vê só os próprios dados)
- 📦 **Estoque:** CRUD de ingredientes, alerta de itens próximos do vencimento
- 📖 **Receitas:** CRUD com **cálculo automático de custo** a partir dos ingredientes
- 💰 **Vendas:** ao registrar uma venda, o **estoque é consumido automaticamente** (e barra se faltar ingrediente)
- 🛒 **Compra/reposição:** repõe o estoque e **lança a despesa no financeiro** numa única operação transacional
- 📊 **Dashboard:** total de entradas, saídas, lucro líquido, ticket médio e nº de vendas
- 🌗 **Tema claro/escuro**

---

## 🏗️ Arquitetura

```
 ┌─────────────┐      ┌──────────────┐      ┌──────────────┐
 │   Vercel    │ ───► │    Render    │ ───► │  PostgreSQL  │
 │  (React)    │ CORS │ (Spring Boot)│      │   (Render)   │
 │   frontend  │ ◄─── │    API/JWT   │ ◄─── │    banco     │
 └─────────────┘      └──────────────┘      └──────────────┘
```

- **Frontend** hospedado no **Vercel**
- **Backend** containerizado com **Docker** e hospedado no **Render**
- **Banco PostgreSQL** gerenciado no Render, com schema versionado por **Flyway**

---

## 🛠️ Tecnologias

**Backend**
- Java 21 · Spring Boot 3.4
- Spring Security + JWT (jjwt)
- Spring Data JPA · PostgreSQL · Flyway
- Maven · Docker

**Frontend**
- React 18 · Vite
- React Router · Axios
- Tailwind CSS

**Infra / Deploy**
- Render (backend + banco) · Vercel (frontend)

---

## 📸 Demonstração

<!--
  Dica: adicione prints em uma pasta /docs e referencie aqui, por exemplo:
  ![Dashboard](docs/dashboard.png)
  ![Receitas](docs/receitas.png)
-->

_Acesse a [demo ao vivo](https://panela-amiga-two.vercel.app) para explorar._

---

## 💻 Rodando localmente

**Pré-requisitos:** Java 21, Node 18+, Docker.

**1. Suba o banco (PostgreSQL via Docker):**
```bash
docker compose up -d
```

**2. Backend** — defina as variáveis de ambiente e rode:
```bash
# variáveis (ex.: na run config da IDE ou no shell)
DATABASE_URL=jdbc:postgresql://localhost:5433/panela_amiga
DATABASE_USERNAME=panela_amiga
DATABASE_PASSWORD=sa
JWT_SECRET=uma-chave-secreta-longa-com-mais-de-32-caracteres

cd backend
./mvnw spring-boot:run
```
A API sobe em `http://localhost:8080`.

**3. Frontend:**
```bash
cd frontend
npm install
npm run dev
```
O app sobe em `http://localhost:5173` (o Vite faz proxy de `/api` para o backend).

---

## 📚 Principais endpoints

| Método | Rota | Descrição |
|--------|------|-----------|
| `POST` | `/auth/register` · `/auth/login` | Cadastro e login (retorna JWT) |
| `GET/POST/PUT/DELETE` | `/ingredientes` | CRUD de ingredientes |
| `GET/POST/PUT/DELETE` | `/receitas` | CRUD de receitas |
| `GET/POST` | `/transacao` | Vendas e despesas |
| `POST` | `/movimentacoes-estoque` | Compra/reposição de ingrediente |
| `GET` | `/relatorio-financeiro` | Relatório consolidado |

> Todas as rotas (exceto `/auth/*`) exigem o header `Authorization: Bearer <token>`.

---

## 👤 Autor

**Leonardo Hideki**
[GitHub](https://github.com/HidekiLeonardo)
