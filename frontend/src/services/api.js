import axios from "axios"

const api = axios.create({
  baseURL: "/api",
  headers: { "Content-Type": "application/json" },
})

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token")
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

// Se o token expirou ou é inválido, o backend responde 401.
// Limpamos a sessão e mandamos o usuário para o login.
// Ignoramos as rotas /auth para não interferir no fluxo de login.
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const isAuthRoute = error.config?.url?.includes("/auth/")
    if (error.response?.status === 401 && !isAuthRoute) {
      localStorage.removeItem("token")
      if (window.location.pathname !== "/login") {
        window.location.href = "/login"
      }
    }
    return Promise.reject(error)
  }
)

export const authService = {
  login: (data) => api.post("/auth/login", data),
  register: (data) => api.post("/auth/register", data),
}

export const ingredientesService = {
  listar: () => api.get("/ingredientes"),
  buscar: (id) => api.get(`/ingredientes/${id}`),
  criar: (data) => api.post("/ingredientes", data),
  atualizar: (id, data) => api.put(`/ingredientes/${id}`, data),
  deletar: (id) => api.delete(`/ingredientes/${id}`),
}

export const receitasService = {
  listar: () => api.get("/receitas"),
  buscar: (id) => api.get(`/receitas/${id}`),
  criar: (data) => api.post("/receitas", data),
  atualizar: (id, data) => api.put(`/receitas/${id}`, data),
  deletar: (id) => api.delete(`/receitas/${id}`),
}

export const transacoesService = {
  listar: () => api.get("/transacao"),
  buscar: (id) => api.get(`/transacao/${id}`),
  criar: (data) => api.post("/transacao", data),
}

export const movimentacoesService = {
  // Comprar/repor um ingrediente existente: aumenta o estoque e lança
  // a despesa correspondente no financeiro (tudo no servidor).
  registrarCompra: (data) => api.post("/movimentacoes-estoque", data),
}

export const relatorioService = {
  buscar: (inicio, fim) => {
    const params = new URLSearchParams()
    if (inicio) params.append("inicio", inicio)
    if (fim) params.append("fim", fim)
    const query = params.toString()
    return api.get(`/relatorio-financeiro${query ? `?${query}` : ""}`)
  },
}

export const alertasService = {
  proximosVencimento: (dias = 7) =>
    api.get(`/ingredientes/proximos-vencimento?dia=${dias}`),
}

export default api
