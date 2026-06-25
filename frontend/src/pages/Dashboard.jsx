import { useEffect, useState } from "react"
import { relatorioService, alertasService } from "@/services/api"
import { formatCurrency, formatDate, extractErrorMessage } from "@/lib/utils"
import { TrendingUp, TrendingDown, DollarSign, ShoppingCart, BarChart3, AlertTriangle, RefreshCw } from "lucide-react"

function StatCard({ title, value, icon: Icon, accent, sub }) {
  return (
    <div className="bg-card rounded-2xl border border-border p-6 flex flex-col gap-4 shadow-sm hover:shadow-md transition-shadow">
      <div className="flex items-center justify-between">
        <p className="text-sm font-medium text-muted-foreground">{title}</p>
        <div className={`w-10 h-10 rounded-xl flex items-center justify-center ${accent.bg}`}>
          <Icon size={18} className={accent.icon} />
        </div>
      </div>
      <div>
        <p className="text-2xl font-bold tracking-tight">{value}</p>
        {sub && <p className="text-xs text-muted-foreground mt-1">{sub}</p>}
      </div>
    </div>
  )
}

const STATS_CONFIG = [
  {
    key: "totalEntradas",
    title: "Total de Entradas",
    icon: TrendingUp,
    accent: { bg: "bg-green-100 dark:bg-green-900/30", icon: "text-green-600 dark:text-green-400" },
    format: (v) => formatCurrency(v),
    sub: "Receita bruta do período",
  },
  {
    key: "totalSaida",
    title: "Total de Saídas",
    icon: TrendingDown,
    accent: { bg: "bg-red-100 dark:bg-red-900/30", icon: "text-red-600 dark:text-red-400" },
    format: (v) => formatCurrency(v),
    sub: "Despesas do período",
  },
  {
    key: "lucroLiquido",
    title: "Lucro Líquido",
    icon: DollarSign,
    accent: { bg: "bg-orange-100 dark:bg-orange-900/30", icon: "text-orange-600 dark:text-orange-400" },
    format: (v) => formatCurrency(v),
    sub: "Entradas menos saídas",
  },
  {
    key: "ticketMedio",
    title: "Ticket Médio",
    icon: BarChart3,
    accent: { bg: "bg-blue-100 dark:bg-blue-900/30", icon: "text-blue-600 dark:text-blue-400" },
    format: (v) => formatCurrency(v),
    sub: "Valor médio por venda",
  },
  {
    key: "quantidadeVendas",
    title: "Vendas",
    icon: ShoppingCart,
    accent: { bg: "bg-violet-100 dark:bg-violet-900/30", icon: "text-violet-600 dark:text-violet-400" },
    format: (v) => v ?? 0,
    sub: "Pedidos registrados",
  },
]

export function Dashboard() {
  const [relatorio, setRelatorio] = useState(null)
  const [proximosVencimento, setProximosVencimento] = useState([])
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState(null)

  function carregar() {
    setLoading(true)
    setErro(null)
    Promise.all([
      relatorioService.buscar(),
      alertasService.proximosVencimento(7),
    ])
      .then(([relRes, alertRes]) => {
        setRelatorio(relRes.data)
        setProximosVencimento(alertRes.data)
      })
      .catch((err) => setErro(extractErrorMessage(err, "Não foi possível carregar o relatório. O backend está rodando?")))
      .finally(() => setLoading(false))
  }

  useEffect(() => { carregar() }, [])

  if (loading) {
    return (
      <div className="p-8 flex items-center gap-2 text-muted-foreground text-sm">
        <RefreshCw size={16} className="animate-spin" />
        Carregando relatório...
      </div>
    )
  }

  return (
    <div className="p-8 space-y-8">
      <div className="flex items-start justify-between">
        <div>
          <h2 className="text-2xl font-bold tracking-tight">Dashboard</h2>
          <p className="text-muted-foreground text-sm mt-1">Visão geral financeira do negócio</p>
        </div>
        <button
          onClick={carregar}
          className="flex items-center gap-2 text-sm text-muted-foreground hover:text-foreground transition-colors px-3 py-1.5 rounded-lg hover:bg-muted"
        >
          <RefreshCw size={14} />
          Atualizar
        </button>
      </div>

      {erro && (
        <div className="rounded-xl border border-destructive/30 bg-destructive/10 p-4 text-destructive text-sm">
          {erro}
        </div>
      )}

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-3">
        {STATS_CONFIG.map(({ key, title, icon, accent, format, sub }) => (
          <StatCard
            key={key}
            title={title}
            value={format(relatorio?.[key])}
            icon={icon}
            accent={accent}
            sub={sub}
          />
        ))}
      </div>

      {proximosVencimento.length > 0 && (
        <div className="rounded-2xl border border-amber-200 dark:border-amber-800/50 bg-amber-50 dark:bg-amber-900/20 p-5">
          <div className="flex items-center gap-2 mb-4">
            <AlertTriangle size={18} className="text-amber-600 dark:text-amber-400" />
            <h3 className="font-semibold text-amber-800 dark:text-amber-300 text-sm">
              Ingredientes próximos do vencimento — próximos 7 dias
            </h3>
          </div>
          <div className="space-y-2">
            {proximosVencimento.map((ing) => (
              <div key={ing.id} className="flex items-center justify-between text-sm">
                <span className="text-amber-900 dark:text-amber-200 font-medium">
                  {ing.nome}
                  {ing.marca && <span className="font-normal text-amber-700 dark:text-amber-400"> — {ing.marca}</span>}
                </span>
                <span className="text-amber-700 dark:text-amber-400 tabular-nums">
                  {formatDate(ing.dataValidade)}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}

      {!erro && proximosVencimento.length === 0 && relatorio && (
        <div className="rounded-2xl border border-green-200 dark:border-green-800/50 bg-green-50 dark:bg-green-900/20 p-4 text-sm text-green-700 dark:text-green-400">
          Nenhum ingrediente próximo do vencimento nos próximos 7 dias.
        </div>
      )}
    </div>
  )
}
