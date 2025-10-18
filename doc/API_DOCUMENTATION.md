# 📚 Documentação da API - Blog Project Security

## 📋 Índice

- [Visão Geral](#visão-geral)
- [Configuração Inicial](#configuração-inicial)
- [Autenticação](#autenticação)
- [Endpoints](#endpoints)
  - [Authentication](#authentication)
  - [Users](#users)
  - [Posts](#posts)
- [Códigos de Status HTTP](#códigos-de-status-http)
- [Tratamento de Erros](#tratamento-de-erros)
- [Exemplos de Integração](#exemplos-de-integração)

---

## 🎯 Visão Geral

API RESTful para gerenciamento de blog desenvolvida com Spring Boot 3.5.6 e Java 17.

**Base URL:** `http://localhost:8090`

**Arquitetura:** Clean Architecture com DDD (Domain-Driven Design)

**Autenticação:** JWT (JSON Web Tokens) com Bearer Token

**Formato de Dados:** JSON

---

## ⚙️ Configuração Inicial

### Pré-requisitos

- **Servidor:** Rodando na porta `8090`
- **Database:** MySQL (localhost:3306/blogdb)
- **Autenticação:** Token JWT com validade de 1 hora

### Headers Padrão

Para requisições **não autenticadas**:
```http
Content-Type: application/json
```

Para requisições **autenticadas**:
```http
Content-Type: application/json
Authorization: Bearer {seu-token-jwt}
```

---

## 🔐 Autenticação

### Como Funciona

1. **Criar Conta:** Faça um POST para `/api/users` com seus dados
2. **Fazer Login:** Faça um POST para `/api/auth/login` com email e senha
3. **Receber Token:** A API retorna um token JWT válido por 1 hora
4. **Usar Token:** Inclua o token no header `Authorization: Bearer {token}` em todas as requisições protegidas

### Fluxo de Autenticação

```
┌─────────────┐      ┌─────────────┐      ┌──────────────┐
│   Frontend  │─────▶│  POST /auth │─────▶│ Recebe Token │
│             │      │    /login   │      │   JWT (1h)   │
└─────────────┘      └─────────────┘      └──────────────┘
                                                  │
                                                  ▼
                                    ┌──────────────────────────┐
                                    │ Usar em todas requisições│
                                    │ Authorization: Bearer... │
                                    └──────────────────────────┘
```

---

## 📍 Endpoints

### Authentication

#### 1. Login (Obter Token JWT)

Autentica o usuário e retorna um token JWT para uso nas demais requisições.

**Endpoint:** `POST /api/auth/login`

**Autenticação:** Não requerida

**Request Body:**
```json
{
  "email": "usuario@example.com",
  "password": "senha123"
}
```

**Validações:**
- `email`: obrigatório, não pode estar em branco
- `password`: obrigatório, não pode estar em branco

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZSI6IlVTRVIiLCJ1c2VyX2lkIjoxLCJpYXQiOjE2OTcwMDAwMDAsImV4cCI6MTY5NzAwMzYwMH0.signature",
  "type": "Bearer",
  "username": "usuario_nome",
  "role": "USER"
}
```

**Response (400 Bad Request) - Credenciais Inválidas:**
```json
{
  "error": "Credenciais inválidas"
}
```

**Response (400 Bad Request) - Validação:**
```json
{
  "email": "Email é obrigatório",
  "password": "Password é obrigatório"
}
```

**Exemplo com cURL:**
```bash
curl -X POST http://localhost:8090/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@example.com",
    "password": "senha123"
  }'
```

**Exemplo com JavaScript (fetch):**
```javascript
const login = async (email, password) => {
  const response = await fetch('http://localhost:8090/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password })
  });

  if (!response.ok) {
    throw new Error('Login falhou');
  }

  const data = await response.json();
  // Salvar token para uso futuro
  localStorage.setItem('token', data.token);
  localStorage.setItem('username', data.username);
  localStorage.setItem('role', data.role);

  return data;
};
```

---

### Users

#### 1. Criar Novo Usuário (Registro)

Cria uma nova conta de usuário no sistema.

**Endpoint:** `POST /api/users`

**Autenticação:** Não requerida

**Request Body:**
```json
{
  "username": "nome_usuario",
  "password": "senha123",
  "email": "usuario@example.com",
  "role": "USER"
}
```

**Validações:**
- `username`: obrigatório, 3-50 caracteres
- `password`: obrigatório, mínimo 6 caracteres
- `email`: obrigatório, formato de email válido
- `role`: obrigatório, valores aceitos: `"USER"` ou `"ADMIN"`

**Response (201 Created):**
```json
{
  "id": 1,
  "username": "nome_usuario",
  "password": "$2a$10$hashedpassword...",
  "email": "usuario@example.com",
  "role": "USER"
}
```

**Observação:** A senha retornada está hasheada com BCrypt por segurança.

**Response (400 Bad Request) - Validação:**
```json
{
  "username": "Username must be between 3 and 50 characters",
  "password": "Password must be at least 6 characters",
  "email": "Email should be valid",
  "role": "Role is required"
}
```

**Exemplo com JavaScript (fetch):**
```javascript
const createUser = async (userData) => {
  const response = await fetch('http://localhost:8090/api/users', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      username: userData.username,
      password: userData.password,
      email: userData.email,
      role: userData.role || 'USER'
    })
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(JSON.stringify(error));
  }

  return await response.json();
};
```

---

#### 2. Buscar Usuário por ID

Retorna os dados de um usuário específico pelo ID.

**Endpoint:** `GET /api/users/{id}`

**Autenticação:** Requerida (Bearer Token)

**Parâmetros de URL:**
- `id` (path parameter): ID do usuário (número)

**Response (200 OK):**
```json
{
  "id": 1,
  "username": "nome_usuario",
  "email": "usuario@example.com",
  "role": "USER"
}
```

**Response (404 Not Found):**
```json
{
  "message": "User not found"
}
```

**Exemplo com JavaScript (fetch):**
```javascript
const getUserById = async (userId) => {
  const token = localStorage.getItem('token');

  const response = await fetch(`http://localhost:8090/api/users/${userId}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    }
  });

  if (!response.ok) {
    throw new Error('Usuário não encontrado');
  }

  return await response.json();
};
```

---

#### 3. Buscar Usuário por Email

Retorna os dados de um usuário específico pelo email.

**Endpoint:** `GET /api/users/email/{email}`

**Autenticação:** Requerida (Bearer Token)

**Parâmetros de URL:**
- `email` (path parameter): Email do usuário

**Response (200 OK):**
```json
{
  "id": 1,
  "username": "nome_usuario",
  "email": "usuario@example.com",
  "role": "USER"
}
```

**Response (404 Not Found):**
```json
{
  "message": "User not found"
}
```

**Exemplo com JavaScript (fetch):**
```javascript
const getUserByEmail = async (email) => {
  const token = localStorage.getItem('token');

  const response = await fetch(`http://localhost:8090/api/users/email/${encodeURIComponent(email)}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    }
  });

  if (!response.ok) {
    throw new Error('Usuário não encontrado');
  }

  return await response.json();
};
```

---

### Posts

#### 1. Criar Novo Post

Cria um novo post de blog.

**Endpoint:** `POST /api/posts`

**Autenticação:** Requerida (Bearer Token)

**Request Body:**
```json
{
  "title": "Título do Meu Post com Mínimo 10 Caracteres",
  "content": "Conteúdo do post com no mínimo 20 caracteres de texto.",
  "tag": "tecnologia"
}
```

**Validações:**
- `title`: obrigatório, mínimo 10 caracteres
- `content`: obrigatório, mínimo 20 caracteres
- `tag`: opcional

**Response (201 Created):**
```json
{
  "id": 1,
  "title": "Título do Meu Post com Mínimo 10 Caracteres",
  "content": "Conteúdo do post com no mínimo 20 caracteres de texto.",
  "tag": "tecnologia",
  "created": "2025-10-18T14:30:00",
  "authorId": 1,
  "authorName": "nome_usuario",
  "slug": "titulo-do-meu-post-com-minimo-10-caracteres"
}
```

**Observações:**
- O campo `authorId` é automaticamente preenchido com o ID do usuário autenticado
- O campo `slug` é gerado automaticamente a partir do título
- O campo `created` é preenchido automaticamente com a data/hora atual

**Response (400 Bad Request) - Título Duplicado:**
```json
{
  "message": "Title already exists"
}
```

**Response (400 Bad Request) - Validação:**
```json
{
  "title": "Title most be more 10 characters",
  "content": "content most be more 10 characters"
}
```

**Exemplo com JavaScript (fetch):**
```javascript
const createPost = async (postData) => {
  const token = localStorage.getItem('token');

  const response = await fetch('http://localhost:8090/api/posts', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      title: postData.title,
      content: postData.content,
      tag: postData.tag || ''
    })
  });

  if (!response.ok) {
    const error = await response.json();
    throw new Error(error.message || 'Erro ao criar post');
  }

  return await response.json();
};
```

---

#### 2. Buscar Post por ID

Retorna um post específico pelo ID.

**Endpoint:** `GET /api/posts?id={id}`

**Autenticação:** Requerida (Bearer Token)

**Query Parameters:**
- `id`: ID do post (número)

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Título do Post",
  "content": "Conteúdo completo do post...",
  "tag": "tecnologia",
  "created": "2025-10-18T14:30:00",
  "authorId": 1,
  "slug": "titulo-do-post"
}
```

**Response (404 Not Found):**
```
Status: 404
Body: vazio
```

**Exemplo com JavaScript (fetch):**
```javascript
const getPostById = async (postId) => {
  const token = localStorage.getItem('token');

  const response = await fetch(`http://localhost:8090/api/posts?id=${postId}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    }
  });

  if (!response.ok) {
    throw new Error('Post não encontrado');
  }

  return await response.json();
};
```

---

#### 3. Listar Todos os Posts (com Filtros)

Lista todos os posts com opções de filtragem.

**Endpoint:** `GET /api/posts/all`

**Autenticação:** Requerida (Bearer Token)

**Query Parameters (todos opcionais):**
- `title`: string - Filtra por título
- `content`: string - Filtra por conteúdo
- `tag`: string - Filtra por tag
- `slug`: string - Filtra por slug
- `authorId`: number - Filtra por ID do autor
- `createdAfter`: string (ISO DateTime) - Posts criados após esta data
- `createdBefore`: string (ISO DateTime) - Posts criados antes desta data
- `updatedAfter`: string (ISO DateTime) - Posts atualizados após esta data
- `updatedBefore`: string (ISO DateTime) - Posts atualizados antes desta data

**Exemplos de URLs:**

```
# Todos os posts
GET /api/posts/all

# Posts de um autor específico
GET /api/posts/all?authorId=1

# Posts com tag específica
GET /api/posts/all?tag=tecnologia

# Posts criados após uma data
GET /api/posts/all?createdAfter=2025-10-01T00:00:00

# Combinando filtros
GET /api/posts/all?authorId=1&tag=tecnologia&createdAfter=2025-10-01T00:00:00
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Primeiro Post",
    "content": "Conteúdo do primeiro post...",
    "tag": "tecnologia",
    "created": "2025-10-18T14:30:00",
    "authorId": 1,
    "slug": "primeiro-post"
  },
  {
    "id": 2,
    "title": "Segundo Post",
    "content": "Conteúdo do segundo post...",
    "tag": "blog",
    "created": "2025-10-17T10:20:30",
    "authorId": 1,
    "slug": "segundo-post"
  }
]
```

**Observação:** Retorna um array vazio `[]` se nenhum post for encontrado.

**Exemplo com JavaScript (fetch):**
```javascript
const getAllPosts = async (filters = {}) => {
  const token = localStorage.getItem('token');

  // Construir query string a partir dos filtros
  const queryParams = new URLSearchParams();

  if (filters.title) queryParams.append('title', filters.title);
  if (filters.content) queryParams.append('content', filters.content);
  if (filters.tag) queryParams.append('tag', filters.tag);
  if (filters.slug) queryParams.append('slug', filters.slug);
  if (filters.authorId) queryParams.append('authorId', filters.authorId);
  if (filters.createdAfter) queryParams.append('createdAfter', filters.createdAfter);
  if (filters.createdBefore) queryParams.append('createdBefore', filters.createdBefore);
  if (filters.updatedAfter) queryParams.append('updatedAfter', filters.updatedAfter);
  if (filters.updatedBefore) queryParams.append('updatedBefore', filters.updatedBefore);

  const queryString = queryParams.toString();
  const url = `http://localhost:8090/api/posts/all${queryString ? '?' + queryString : ''}`;

  const response = await fetch(url, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    }
  });

  if (!response.ok) {
    throw new Error('Erro ao buscar posts');
  }

  return await response.json();
};

// Exemplo de uso:
// getAllPosts({ tag: 'tecnologia', authorId: 1 })
```

---

#### 4. Atualizar Post

Atualiza um post existente.

**Endpoint:** `PUT /api/posts/{id}`

**Autenticação:** Requerida (Bearer Token)

**Autorização:** Somente o autor do post ou usuários com role `ADMIN` podem atualizar

**Parâmetros de URL:**
- `id` (path parameter): ID do post a ser atualizado

**Request Body:**
```json
{
  "title": "Título Atualizado com Mínimo 10 Caracteres",
  "content": "Conteúdo atualizado do post...",
  "tag": "tag-atualizada"
}
```

**Validações:**
- `title`: obrigatório, mínimo 10 caracteres
- `content`: opcional
- `tag`: opcional

**Response (200 OK):**
```json
{
  "id": 1,
  "title": "Título Atualizado com Mínimo 10 Caracteres",
  "content": "Conteúdo atualizado do post...",
  "tag": "tag-atualizada",
  "created": "2025-10-18T14:30:00",
  "authorId": 1,
  "authorName": "nome_usuario",
  "slug": "titulo-atualizado-com-minimo-10-caracteres"
}
```

**Response (403 Forbidden) - Sem Permissão:**
```json
{
  "message": "Você não tem permissão para editar este post"
}
```

**Response (404 Not Found):**
```json
{
  "message": "Post não encontrado"
}
```

**Exemplo com JavaScript (fetch):**
```javascript
const updatePost = async (postId, updateData) => {
  const token = localStorage.getItem('token');

  const response = await fetch(`http://localhost:8090/api/posts/${postId}`, {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      title: updateData.title,
      content: updateData.content,
      tag: updateData.tag
    })
  });

  if (!response.ok) {
    if (response.status === 403) {
      throw new Error('Você não tem permissão para editar este post');
    }
    const error = await response.json();
    throw new Error(error.message || 'Erro ao atualizar post');
  }

  return await response.json();
};
```

---

## 📊 Códigos de Status HTTP

| Código | Significado | Quando Ocorre |
|--------|-------------|---------------|
| **200** | OK | Requisição bem-sucedida (GET, PUT) |
| **201** | Created | Recurso criado com sucesso (POST) |
| **400** | Bad Request | Erro de validação ou dados inválidos |
| **401** | Unauthorized | Token inválido, expirado ou ausente |
| **403** | Forbidden | Usuário não tem permissão para a ação |
| **404** | Not Found | Recurso não encontrado |
| **500** | Internal Server Error | Erro no servidor |

---

## ⚠️ Tratamento de Erros

### Tipos de Erros

#### 1. Erros de Validação (400)
```json
{
  "campo1": "Mensagem de erro do campo 1",
  "campo2": "Mensagem de erro do campo 2"
}
```

**Exemplo:**
```json
{
  "email": "Email should be valid",
  "password": "Password must be at least 6 characters"
}
```

#### 2. Erros de Autenticação (401)
```json
{
  "message": "Token expired"
}
```

ou

```json
{
  "error": "Credenciais inválidas"
}
```

#### 3. Erros de Permissão (403)
```json
{
  "message": "Você não tem permissão para editar este post"
}
```

#### 4. Erros de Recurso Não Encontrado (404)
```json
{
  "message": "User not found"
}
```

ou

```json
{
  "message": "Post não encontrado"
}
```

### Tratamento Recomendado no Frontend

```javascript
const handleApiError = (response, error) => {
  if (response.status === 401) {
    // Token expirado ou inválido - redirecionar para login
    localStorage.removeItem('token');
    window.location.href = '/login';
    return;
  }

  if (response.status === 403) {
    // Sem permissão
    alert('Você não tem permissão para realizar esta ação');
    return;
  }

  if (response.status === 404) {
    // Recurso não encontrado
    console.error('Recurso não encontrado');
    return;
  }

  if (response.status === 400) {
    // Erros de validação - exibir para o usuário
    console.error('Erros de validação:', error);
    return error;
  }

  // Erro genérico
  console.error('Erro na API:', error);
};
```

---

## 💡 Exemplos de Integração

### Exemplo Completo: Fluxo de Autenticação e Criação de Post

```javascript
// ========================================
// 1. REGISTRO DE NOVO USUÁRIO
// ========================================
const registerUser = async () => {
  try {
    const newUser = await fetch('http://localhost:8090/api/users', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: 'joao_silva',
        password: 'senha123',
        email: 'joao@example.com',
        role: 'USER'
      })
    });

    const userData = await newUser.json();
    console.log('Usuário criado:', userData);
    return userData;
  } catch (error) {
    console.error('Erro ao criar usuário:', error);
    throw error;
  }
};

// ========================================
// 2. LOGIN E OBTENÇÃO DO TOKEN
// ========================================
const login = async (email, password) => {
  try {
    const response = await fetch('http://localhost:8090/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });

    if (!response.ok) {
      throw new Error('Login falhou');
    }

    const data = await response.json();

    // Salvar token e informações do usuário
    localStorage.setItem('token', data.token);
    localStorage.setItem('username', data.username);
    localStorage.setItem('role', data.role);

    console.log('Login bem-sucedido:', data);
    return data;
  } catch (error) {
    console.error('Erro no login:', error);
    throw error;
  }
};

// ========================================
// 3. CRIAR UM POST
// ========================================
const createPost = async (title, content, tag) => {
  try {
    const token = localStorage.getItem('token');

    if (!token) {
      throw new Error('Usuário não autenticado');
    }

    const response = await fetch('http://localhost:8090/api/posts', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ title, content, tag })
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Erro ao criar post');
    }

    const post = await response.json();
    console.log('Post criado:', post);
    return post;
  } catch (error) {
    console.error('Erro ao criar post:', error);
    throw error;
  }
};

// ========================================
// 4. LISTAR POSTS DE UM AUTOR
// ========================================
const getPostsByAuthor = async (authorId) => {
  try {
    const token = localStorage.getItem('token');

    const response = await fetch(`http://localhost:8090/api/posts/all?authorId=${authorId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Erro ao buscar posts');
    }

    const posts = await response.json();
    console.log('Posts encontrados:', posts);
    return posts;
  } catch (error) {
    console.error('Erro ao buscar posts:', error);
    throw error;
  }
};

// ========================================
// 5. ATUALIZAR UM POST
// ========================================
const updatePost = async (postId, updatedData) => {
  try {
    const token = localStorage.getItem('token');

    const response = await fetch(`http://localhost:8090/api/posts/${postId}`, {
      method: 'PUT',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(updatedData)
    });

    if (!response.ok) {
      if (response.status === 403) {
        throw new Error('Você não tem permissão para editar este post');
      }
      const error = await response.json();
      throw new Error(error.message || 'Erro ao atualizar post');
    }

    const updatedPost = await response.json();
    console.log('Post atualizado:', updatedPost);
    return updatedPost;
  } catch (error) {
    console.error('Erro ao atualizar post:', error);
    throw error;
  }
};

// ========================================
// 6. VERIFICAR SE TOKEN AINDA É VÁLIDO
// ========================================
const checkAuth = () => {
  const token = localStorage.getItem('token');

  if (!token) {
    return false;
  }

  // Decodificar token JWT para verificar expiração
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const expirationTime = payload.exp * 1000; // Converter para milissegundos

    if (Date.now() >= expirationTime) {
      // Token expirado
      localStorage.removeItem('token');
      localStorage.removeItem('username');
      localStorage.removeItem('role');
      return false;
    }

    return true;
  } catch (error) {
    return false;
  }
};

// ========================================
// 7. LOGOUT
// ========================================
const logout = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('username');
  localStorage.removeItem('role');
  console.log('Logout realizado');
};

// ========================================
// EXEMPLO DE USO COMPLETO
// ========================================
async function exemploCompleto() {
  try {
    // 1. Registrar usuário
    await registerUser();

    // 2. Fazer login
    const loginData = await login('joao@example.com', 'senha123');

    // 3. Criar um post
    const post = await createPost(
      'Meu Primeiro Post de Tecnologia',
      'Este é o conteúdo do meu primeiro post sobre tecnologia e desenvolvimento.',
      'tecnologia'
    );

    // 4. Listar posts do autor
    const posts = await getPostsByAuthor(loginData.userId);

    // 5. Atualizar o post
    await updatePost(post.id, {
      title: 'Meu Primeiro Post Atualizado',
      content: 'Conteúdo atualizado do post...',
      tag: 'desenvolvimento'
    });

    // 6. Logout
    logout();
  } catch (error) {
    console.error('Erro no fluxo:', error);
  }
}
```

---

### Exemplo com React Hooks

```jsx
import { useState, useEffect } from 'react';

// Hook customizado para autenticação
const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Verificar se há token no localStorage
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');
    const role = localStorage.getItem('role');

    if (token) {
      // Verificar se token ainda é válido
      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const expirationTime = payload.exp * 1000;

        if (Date.now() < expirationTime) {
          setIsAuthenticated(true);
          setUser({ username, role });
        } else {
          // Token expirado
          localStorage.clear();
        }
      } catch (error) {
        console.error('Token inválido');
        localStorage.clear();
      }
    }

    setLoading(false);
  }, []);

  const login = async (email, password) => {
    try {
      const response = await fetch('http://localhost:8090/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });

      if (!response.ok) {
        throw new Error('Login falhou');
      }

      const data = await response.json();

      localStorage.setItem('token', data.token);
      localStorage.setItem('username', data.username);
      localStorage.setItem('role', data.role);

      setIsAuthenticated(true);
      setUser({ username: data.username, role: data.role });

      return data;
    } catch (error) {
      console.error('Erro no login:', error);
      throw error;
    }
  };

  const logout = () => {
    localStorage.clear();
    setIsAuthenticated(false);
    setUser(null);
  };

  return { isAuthenticated, user, loading, login, logout };
};

// Hook customizado para posts
const usePosts = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchPosts = async (filters = {}) => {
    setLoading(true);
    setError(null);

    try {
      const token = localStorage.getItem('token');
      const queryParams = new URLSearchParams(filters);
      const url = `http://localhost:8090/api/posts/all?${queryParams}`;

      const response = await fetch(url, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (!response.ok) {
        throw new Error('Erro ao buscar posts');
      }

      const data = await response.json();
      setPosts(data);
      return data;
    } catch (err) {
      setError(err.message);
      throw err;
    } finally {
      setLoading(false);
    }
  };

  const createPost = async (postData) => {
    try {
      const token = localStorage.getItem('token');

      const response = await fetch('http://localhost:8090/api/posts', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(postData)
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Erro ao criar post');
      }

      const newPost = await response.json();
      setPosts([newPost, ...posts]);
      return newPost;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const updatePost = async (postId, updatedData) => {
    try {
      const token = localStorage.getItem('token');

      const response = await fetch(`http://localhost:8090/api/posts/${postId}`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(updatedData)
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Erro ao atualizar post');
      }

      const updatedPost = await response.json();
      setPosts(posts.map(p => p.id === postId ? updatedPost : p));
      return updatedPost;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  return { posts, loading, error, fetchPosts, createPost, updatePost };
};

// Componente de exemplo
const BlogApp = () => {
  const { isAuthenticated, user, login, logout } = useAuth();
  const { posts, loading, fetchPosts, createPost } = usePosts();

  useEffect(() => {
    if (isAuthenticated) {
      fetchPosts();
    }
  }, [isAuthenticated]);

  if (!isAuthenticated) {
    return <LoginForm onLogin={login} />;
  }

  return (
    <div>
      <header>
        <h1>Blog - Bem-vindo, {user.username}!</h1>
        <button onClick={logout}>Sair</button>
      </header>

      <CreatePostForm onSubmit={createPost} />

      <PostList posts={posts} loading={loading} />
    </div>
  );
};
```

---

## 🔑 Informações Importantes

### Segurança

1. **Nunca** exponha o token JWT em URLs ou logs
2. **Sempre** use HTTPS em produção
3. **Armazene** o token de forma segura (localStorage ou sessionStorage)
4. **Implemente** refresh token para renovação automática (futura feature)
5. **Valide** sempre o token antes de fazer requisições

### Boas Práticas

1. **Interceptadores HTTP**: Configure interceptadores para adicionar o token automaticamente em todas as requisições
2. **Tratamento de Erros**: Implemente um sistema centralizado de tratamento de erros
3. **Renovação de Token**: Redirecione para login quando o token expirar (401)
4. **Loading States**: Sempre mostre feedback visual durante requisições
5. **Validação no Frontend**: Valide dados antes de enviar para evitar erros desnecessários

### Performance

1. **Cache**: Considere cachear respostas de GET quando apropriado
2. **Paginação**: Para grandes listas, implemente paginação (feature futura da API)
3. **Debouncing**: Use debounce em filtros de busca
4. **Lazy Loading**: Carregue posts sob demanda

---

## 📞 Contato e Suporte

Para dúvidas ou problemas com a API, entre em contato com o time de backend.

**Versão da API:** 1.0.0
**Última atualização:** 18/10/2025
