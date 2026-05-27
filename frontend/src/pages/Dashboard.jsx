import { useEffect, useState } from "react"
import { relatorioService, alertasService } from "@/services/api"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card"
import { formatCurrency, formatDate } from "@/lib/utils"
import { TrendingUp, TrendingDown, DollarSign, ShoppingCart, BarChart3, AlertTriangle } from "lucide-react"

function StatCard({ title, value, icon: Icon, color }) {
  return (
    <Card>
      <CardHeader className="flex flex-row items-center justify-between pb-2">
        <CardTitle className="text-sm font-medium text-muted-foreground">{title}</CardTitle>
        <Icon size={18} className={color} />
      </CardHeader>
      <CardContent>
        <p className="text-2xl font-bold">{value}</p>
      </CardContent>
    </Card>
  )
}

export function Dashboard() {
  const [relatorio, setRelatorio] = useState(null)
  const [proximosVencimento, setProximosVencimento] = useState([])
  const [loading, setLoading] = useState(true)
  const [erro, setErro] = useState(null)

  useEffect(() => {
    Promise.all([
      relatorioService.buscar(),
      alertasService.proximosVencimento(7),
    ])
      .then(([relatorioRes, alertasRes]) => {
        setRelatorio(relatorioRes.data)
        setProximosVencimento(alertasRes.data)
      })
      .catch(() => setErro("Não foi possível carregar o relatório. O backend está rodando?"))
      .finally(() => setLoading(false))
  }, [])

  if (loading) {
    return (
      <div className="p-8 text-muted-foreground">Carregando relatório...</div>
    )
  }

  if (erro) {
    return (
      <div className="p-8">
        <div className="rounded-lg border border-destructive/30 bg-destructive/10 p-4 text-destructive text-sm">
          {erro}
        </div>
      </div>
    )
  }

  const lucroPositivo = (relatorio?.lucroLiquido ?? 0) >= 0

  return (
    <div className="p-8 space-y-6">
      <div>
        <h2 className="text-2xl font-bold">Dashboard</h2>
        <p className="text-muted-foreground text-sm mt-1">Visão geral financeira do negócio</p>
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-3">
        <StatCard
          title="Total de Entradas"
          value={formatCurrency(relatorio?.totalEntradas)}
          icon={TrendingUp}
          color="text-green-500"
        />
        <StatCard
          title="Total de Saídas"
          value={formatCurrency(relatorio?.totalSaida)}
          icon={TrendingDown}
          color="text-red-500"
        />
        <StatCard
          title="Lucro Líquido"
          value={formatCurrency(relatorio?.lucroLiquido)}
          icon={DollarSign}
          color={lucroPositivo ? "text-green-500" : "text-red-500"}
        />
        <StatCard
          title="Ticket Médio"
          value={formatCurrency(relatorio?.ticketMedio)}
          icon={BarChart3}
          color="text-blue-500"
        />
        <StatCard
          title="Quantidade de Vendas"
          value={relatorio?.quantidadeVendas ?? 0}
          icon={ShoppingCart}
          color="text-orange-500"
        />
      </div>

      {proximosVencimento.length > 0 && (
        <div className="rounded-lg border border-amber-300 bg-amber-50 p-4">
          <div className="flex items-center gap-2 mb-3">
            <AlertTriangle size={18} className="text-amber-600" />
            <h3 className="font-semibold text-amber-800">
              Ingredientes próximos do vencimento (próximos 7 dias)
            </h3>
          </div>
          <ul className="space-y-1">
            {proximosVencimento.map((ing) => (
              <li key={ing.id} className="flex justify-between text-sm text-amber-800">
                <span>{ing.nome} — {ing.marca}</span>
                <span className="font-medium">Vence em {formatDate(ing.dataValidade)}</span>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  )
}
