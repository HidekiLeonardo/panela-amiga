import { clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs) {
  return twMerge(clsx(inputs))
}

export function formatCurrency(value) {
  return new Intl.NumberFormat("pt-BR", {
    style: "currency",
    currency: "BRL",
  }).format(value ?? 0)
}

export function formatDate(dateStr) {
  if (!dateStr) return "-"
  const [year, month, day] = String(dateStr).split("T")[0].split("-")
  return `${day}/${month}/${year}`
}

// Extrai a mensagem de erro vinda do backend (o GlobalExceptionHandler
// devolve o texto no corpo da resposta). Cai num texto padrão se não houver.
export function extractErrorMessage(error, fallback = "Algo deu errado. Tente novamente.") {
  const data = error?.response?.data
  if (typeof data === "string" && data.trim()) return data
  if (data?.message) return data.message
  return fallback
}
