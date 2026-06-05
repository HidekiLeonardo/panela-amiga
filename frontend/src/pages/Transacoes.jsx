import { useEffect, useState } from "react"
import { transacoesService, receitasService } from "@/services/api"
import { Button } from "@/components/ui/Button"
import { Input } from "@/components/ui/Input"
import { Select } from "@/components/ui/Select"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card"
import { Badge } from "@/components/ui/Badge"
import { formatCurrency, formatDate } from "@/lib/utils"
import { Plus, X, TrendingUp, TrendingDown } from "lucide-react"

const TIPOS = ["ENTRADA", "SAIDA"]
const FORMAS_PAGAMENTO = ["PIX", "DINHEIRO", "CARTAO"]
const ORIGENS = ["IFOOD", "WHATSAPP", "INSTAGRAM", "LOCAL", "LOJA"]

const transacaoVazia = {
  tipoTransacao: "ENTRADA",
  descricao: "",
  valor: "",
  data: new Date().toISOString().slice(0, 10),
  pagamento: "PIX",
  origemTransacao: "LOCAL",
  receitaId: "",
}

export function Transacoes() {
  const [transacoes, setTransacoes] = useState([])
  const [receitas, setReceitas] = useState([])
  const [loading, setLoading] = useState(true)
  const [formulario, setFormulario] = useState(null)
  const [salvando, setSalvando] = useState(false)
  const [erro, setErro] = useState(null)

  function carregar() {
    setLoading(true)
    Promise.all([transacoesService.listar(), receitasService.listar()])
      .then(([trans, recs]) => {
        setTransacoes(trans.data)
        setReceitas(recs.data)
      })
      .catch(() => setErro("Não foi possível carregar as transações."))
      .finally(() => setLoading(false))
  }

  useEffect(() => { carregar() }, [])

  function handleChange(e) {
    const { name, value } = e.target
    setFormulario((prev) => {
      const next = { ...prev, [name]: value }
      if (name === "tipoTransacao" && value === "SAIDA") next.receitaId = ""
      return next
    })
  }

  async function handleSalvar(e) {
    e.preventDefault()
    setSalvando(true)
    try {
      const payload = {
        ...formulario,
        valor: Number(formulario.valor),
        receitaId: formulario.receitaId ? Number(formulario.receitaId) : null,
      }
      await transacoesService.criar(payload)
      setFormulario(null)
      carregar()
    } catch {
      setErro("Erro ao registrar transação.")
    } finally {
      setSalvando(false)
    }
  }

  const corTipo = (tipo) => tipo === "ENTRADA" ? "text-green-600" : "text-red-600"

  return (
    <div className="p-8 space-y-6">
      <div className="flex items-start justify-between">
        <div>
          <h2 className="text-2xl font-bold tracking-tight">Transações</h2>
          <p className="text-muted-foreground text-sm mt-1">Entradas e saídas financeiras</p>
        </div>
        <Button onClick={() => setFormulario({ ...transacaoVazia })}>
          <Plus size={16} /> Nova Transação
        </Button>
      </div>

      {erro && (
        <div className="rounded-lg border border-destructive/30 bg-destructive/10 p-4 text-destructive text-sm flex justify-between">
          {erro}
          <button onClick={() => setErro(null)}><X size={14} /></button>
        </div>
      )}

      {formulario && (
        <Card>
          <CardHeader>
            <CardTitle>Nova Transação</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSalvar} className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <div className="space-y-1">
                <label className="text-sm font-medium">Tipo</label>
                <Select name="tipoTransacao" value={formulario.tipoTransacao} onChange={handleChange}>
                  {TIPOS.map((t) => <option key={t} value={t}>{t}</option>)}
                </Select>
              </div>

              <div className="space-y-1">
                <label className="text-sm font-medium">Valor (R$)</label>
                <Input name="valor" type="number" step="0.01" min="0" value={formulario.valor} onChange={handleChange} required />
              </div>

              <div className="space-y-1 sm:col-span-2">
                <label className="text-sm font-medium">Descrição</label>
                <Input name="descricao" value={formulario.descricao} onChange={handleChange} required />
              </div>

              <div className="space-y-1">
                <label className="text-sm font-medium">Data</label>
                <Input name="data" type="date" value={formulario.data} onChange={handleChange} required />
              </div>

              <div className="space-y-1">
                <label className="text-sm font-medium">Forma de Pagamento</label>
                <Select name="pagamento" value={formulario.pagamento} onChange={handleChange}>
                  {FORMAS_PAGAMENTO.map((f) => <option key={f} value={f}>{f}</option>)}
                </Select>
              </div>

              <div className="space-y-1">
                <label className="text-sm font-medium">Origem</label>
                <Select name="origemTransacao" value={formulario.origemTransacao} onChange={handleChange}>
                  {ORIGENS.map((o) => <option key={o} value={o}>{o}</option>)}
                </Select>
              </div>

              {formulario.tipoTransacao === "ENTRADA" && (
                <div className="space-y-1">
                  <label className="text-sm font-medium">Receita vinculada <span className="text-destructive">*</span></label>
                  <Select name="receitaId" value={formulario.receitaId} onChange={handleChange} required>
                    <option value="">Selecione a receita vendida</option>
                    {receitas.map((r) => <option key={r.id} value={r.id}>{r.nome}</option>)}
                  </Select>
                </div>
              )}

              <div className="sm:col-span-2 flex gap-3 pt-2">
                <Button type="submit" disabled={salvando}>{salvando ? "Salvando..." : "Salvar"}</Button>
                <Button type="button" variant="outline" onClick={() => setFormulario(null)}>Cancelar</Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}

      {loading ? (
        <p className="text-muted-foreground">Carregando...</p>
      ) : (
        <div className="rounded-2xl border border-border overflow-hidden shadow-sm">
          <table className="w-full text-sm">
            <thead>
              <tr className="bg-muted/60 border-b border-border">
                <th className="text-left px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Data</th>
                <th className="text-left px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Descrição</th>
                <th className="text-left px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Tipo</th>
                <th className="text-left px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Origem</th>
                <th className="text-left px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Pagamento</th>
                <th className="text-right px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Valor</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border">
              {transacoes.length === 0 ? (
                <tr>
                  <td colSpan={6} className="px-4 py-8 text-center text-muted-foreground">
                    Nenhuma transação registrada ainda.
                  </td>
                </tr>
              ) : (
                transacoes.map((t) => (
                  <tr key={t.id} className="hover:bg-muted/30 transition-colors">
                    <td className="px-4 py-3">{formatDate(t.data)}</td>
                    <td className="px-4 py-3">{t.descricao}</td>
                    <td className="px-4 py-3">
                      <span className={`inline-flex items-center gap-1 font-medium ${corTipo(t.tipoTransacao)}`}>
                        {t.tipoTransacao === "ENTRADA" ? <TrendingUp size={14} /> : <TrendingDown size={14} />}
                        {t.tipoTransacao}
                      </span>
                    </td>
                    <td className="px-4 py-3">
                      <Badge variant="outline">{t.origemTransacao}</Badge>
                    </td>
                    <td className="px-4 py-3 text-muted-foreground">{t.pagamento}</td>
                    <td className={`px-4 py-3 text-right font-semibold ${corTipo(t.tipoTransacao)}`}>
                      {t.tipoTransacao === "SAIDA" ? "-" : ""}{formatCurrency(t.valor)}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  )
}
