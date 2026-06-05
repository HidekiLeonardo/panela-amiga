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
