import { useEffect, useState } from "react"
import { ingredientesService, movimentacoesService } from "@/services/api"
import { Button } from "@/components/ui/Button"
import { Input } from "@/components/ui/Input"
import { Select } from "@/components/ui/Select"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card"
import { Badge } from "@/components/ui/Badge"
import { formatCurrency, formatDate, extractErrorMessage } from "@/lib/utils"
import { Plus, Pencil, Trash2, X, ShoppingCart } from "lucide-react"

const UNIDADES = ["KG", "ML", "UNIDADE"]
const FORMAS_PAGAMENTO = ["PIX", "DINHEIRO", "CARTAO"]

const ingredienteVazio = {
  nome: "",
  unidadeDeMedida: "KG",
  marca: "",
  fornecedor: "",
  custoUnitario: "",
  quantidadeEstoque: "",
  dataValidade: "",
}

export function Ingredientes() {
  const [ingredientes, setIngredientes] = useState([])
  const [loading, setLoading] = useState(true)
  const [formulario, setFormulario] = useState(null)
  const [salvando, setSalvando] = useState(false)
  const [erro, setErro] = useState(null)
  const [sucesso, setSucesso] = useState(null)
  const [compra, setCompra] = useState(null)
  const [salvandoCompra, setSalvandoCompra] = useState(false)

  function carregarIngredientes() {
    setLoading(true)
    ingredientesService.listar()
      .then((res) => setIngredientes(res.data))
      .catch(() => setErro("Não foi possível carregar os ingredientes."))
      .finally(() => setLoading(false))
  }

  useEffect(() => {
    carregarIngredientes()
  }, [])

  function abrirCriar() {
    setCompra(null)
    setFormulario({ ...ingredienteVazio })
  }

  function abrirEditar(ingrediente) {
    setCompra(null)
    setFormulario({
      ...ingrediente,
      dataValidade: ingrediente.dataValidade ?? "",
      custoUnitario: ingrediente.custoUnitario ?? "",
      quantidadeEstoque: ingrediente.quantidadeEstoque ?? "",
    })
  }

  function fecharFormulario() {
    setFormulario(null)
  }

  function abrirCompra(ingrediente) {
    setFormulario(null)
    setSucesso(null)
    setCompra({
      ingredienteId: ingrediente.id,
      ingredienteNome: ingrediente.nome,
      unidade: ingrediente.unidadeDeMedida,
      quantidade: "",
      custoUnitario: ingrediente.custoUnitario ?? "",
      pagamento: "PIX",
    })
  }

  function handleCompraChange(e) {
    const { name, value } = e.target
    setCompra((prev) => ({ ...prev, [name]: value }))
  }

  async function handleSalvarCompra(e) {
    e.preventDefault()
    setSalvandoCompra(true)
    try {
      const payload = {
        ingredienteId: Number(compra.ingredienteId),
        quantidade: Number(compra.quantidade),
        custoUnitario: Number(compra.custoUnitario),
        pagamento: compra.pagamento,
      }
      await movimentacoesService.registrarCompra(payload)
      const total = Number(compra.quantidade) * Number(compra.custoUnitario)
      setSucesso(
        `Compra registrada! Estoque de ${compra.ingredienteNome} reposto e despesa de ${formatCurrency(total)} lançada no financeiro.`
      )
      setCompra(null)
      carregarIngredientes()
    } catch (err) {
      setErro(extractErrorMessage(err, "Erro ao registrar compra."))
    } finally {
      setSalvandoCompra(false)
    }
  }

  function handleChange(e) {
    const { name, value } = e.target
    setFormulario((prev) => ({ ...prev, [name]: value }))
  }

  async function handleSalvar(e) {
    e.preventDefault()
    setSalvando(true)
    try {
      if (formulario.id) {
        await ingredientesService.atualizar(formulario.id, formulario)
      } else {
        await ingredientesService.criar(formulario)
      }
      fecharFormulario()
      carregarIngredientes()
    } catch (err) {
      setErro(extractErrorMessage(err, "Erro ao salvar ingrediente."))
    } finally {
      setSalvando(false)
    }
  }

  async function handleDeletar(id) {
    if (!confirm("Deseja remover este ingrediente?")) return
    try {
      await ingredientesService.deletar(id)
      carregarIngredientes()
    } catch (err) {
      setErro(extractErrorMessage(err, "Erro ao remover ingrediente."))
    }
  }

  return (
    <div className="p-8 space-y-6">
      <div className="flex items-start justify-between">
        <div>
          <h2 className="text-2xl font-bold tracking-tight">Ingredientes</h2>
          <p className="text-muted-foreground text-sm mt-1">Controle de estoque e custos</p>
        </div>
        <Button onClick={abrirCriar}>
          <Plus size={16} /> Novo Ingrediente
        </Button>
      </div>

      {erro && (
        <div className="rounded-xl border border-destructive/30 bg-destructive/10 p-4 text-destructive text-sm flex justify-between items-center">
          {erro}
          <button onClick={() => setErro(null)}><X size={14} /></button>
        </div>
      )}

      {sucesso && (
        <div className="rounded-xl border border-green-200 dark:border-green-800/50 bg-green-50 dark:bg-green-900/20 p-4 text-green-700 dark:text-green-400 text-sm flex justify-between items-center">
          {sucesso}
          <button onClick={() => setSucesso(null)}><X size={14} /></button>
        </div>
      )}

      {formulario && (
        <Card className="rounded-2xl shadow-sm">
          <CardHeader className="pb-4">
            <CardTitle className="text-base">{formulario.id ? "Editar Ingrediente" : "Novo Ingrediente"}</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSalvar} className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <div className="space-y-1.5">
                <label className="text-sm font-medium">Nome</label>
                <Input name="nome" value={formulario.nome} onChange={handleChange} required />
              </div>

              <div className="space-y-1.5">
                <label className="text-sm font-medium">Unidade de Medida</label>
                <Select name="unidadeDeMedida" value={formulario.unidadeDeMedida} onChange={handleChange}>
                  {UNIDADES.map((u) => <option key={u} value={u}>{u}</option>)}
                </Select>
              </div>

              <div className="space-y-1.5">
                <label className="text-sm font-medium">Marca</label>
                <Input name="marca" value={formulario.marca} onChange={handleChange} />
              </div>

              <div className="space-y-1.5">
                <label className="text-sm font-medium">Fornecedor</label>
                <Input name="fornecedor" value={formulario.fornecedor} onChange={handleChange} />
              </div>

              <div className="space-y-1.5">
                <label className="text-sm font-medium">Custo Unitário (R$)</label>
                <Input name="custoUnitario" type="number" step="0.01" min="0" value={formulario.custoUnitario} onChange={handleChange} required />
              </div>

              <div className="space-y-1.5">
                <label className="text-sm font-medium">Quantidade em Estoque</label>
                <Input name="quantidadeEstoque" type="number" min="0" value={formulario.quantidadeEstoque} onChange={handleChange} required />
              </div>

              <div className="space-y-1.5">
                <label className="text-sm font-medium">Data de Validade</label>
                <Input name="dataValidade" type="date" value={formulario.dataValidade} onChange={handleChange} />
              </div>

              <div className="sm:col-span-2 flex gap-3 pt-2">
                <Button type="submit" disabled={salvando}>
                  {salvando ? "Salvando..." : "Salvar"}
                </Button>
                <Button type="button" variant="outline" onClick={fecharFormulario}>
                  Cancelar
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}

      {compra && (
        <Card className="rounded-2xl shadow-sm border-primary/30">
          <CardHeader className="pb-4">
            <CardTitle className="text-base flex items-center gap-2">
              <ShoppingCart size={16} /> Repor estoque — {compra.ingredienteNome}
            </CardTitle>
            <p className="text-xs text-muted-foreground mt-1">
              Registra a compra deste ingrediente: aumenta o estoque e lança a despesa no financeiro.
            </p>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSalvarCompra} className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              <div className="space-y-1.5">
                <label className="text-sm font-medium">Quantidade comprada {compra.unidade ? `(${compra.unidade})` : ""}</label>
                <Input name="quantidade" type="number" step="0.01" min="0" value={compra.quantidade} onChange={handleCompraChange} required autoFocus />
              </div>

              <div className="space-y-1.5">
                <label className="text-sm font-medium">Custo Unitário (R$)</label>
                <Input name="custoUnitario" type="number" step="0.01" min="0" value={compra.custoUnitario} onChange={handleCompraChange} required />
              </div>

              <div className="space-y-1.5">
                <label className="text-sm font-medium">Forma de Pagamento</label>
                <Select name="pagamento" value={compra.pagamento} onChange={handleCompraChange}>
                  {FORMAS_PAGAMENTO.map((f) => <option key={f} value={f}>{f}</option>)}
                </Select>
              </div>

              <div className="space-y-1.5">
                <label className="text-sm font-medium">Total da despesa</label>
                <div className="flex h-10 items-center rounded-md border border-input bg-muted/40 px-3 text-sm font-semibold tabular-nums">
                  {formatCurrency((Number(compra.quantidade) || 0) * (Number(compra.custoUnitario) || 0))}
                </div>
              </div>

              <div className="sm:col-span-2 flex gap-3 pt-2">
                <Button type="submit" disabled={salvandoCompra}>
                  {salvandoCompra ? "Registrando..." : "Registrar compra"}
                </Button>
                <Button type="button" variant="outline" onClick={() => setCompra(null)}>
                  Cancelar
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}

      {loading ? (
        <p className="text-muted-foreground text-sm">Carregando...</p>
      ) : (
        <div className="rounded-2xl border border-border overflow-hidden shadow-sm">
          <table className="w-full text-sm">
            <thead>
              <tr className="bg-muted/60 border-b border-border">
                <th className="text-left px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Nome</th>
                <th className="text-left px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Unidade</th>
                <th className="text-left px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Estoque</th>
                <th className="text-left px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Custo Unit.</th>
                <th className="text-left px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Validade</th>
                <th className="text-left px-4 py-3 font-semibold text-xs uppercase tracking-wide text-muted-foreground">Fornecedor</th>
                <th className="px-4 py-3" />
              </tr>
            </thead>
            <tbody className="divide-y divide-border">
              {ingredientes.length === 0 ? (
                <tr>
                  <td colSpan={7} className="px-4 py-10 text-center text-muted-foreground text-sm">
                    Nenhum ingrediente cadastrado ainda.
                  </td>
                </tr>
              ) : (
                ingredientes.map((ing) => (
                  <tr key={ing.id} className="hover:bg-muted/30 transition-colors">
                    <td className="px-4 py-3 font-medium">{ing.nome}</td>
                    <td className="px-4 py-3">
                      <Badge variant="secondary">{ing.unidadeDeMedida}</Badge>
                    </td>
                    <td className="px-4 py-3 tabular-nums">{ing.quantidadeEstoque}</td>
                    <td className="px-4 py-3 tabular-nums">{formatCurrency(ing.custoUnitario)}</td>
                    <td className="px-4 py-3 tabular-nums">{formatDate(ing.dataValidade)}</td>
                    <td className="px-4 py-3 text-muted-foreground">{ing.fornecedor || "—"}</td>
                    <td className="px-4 py-3">
                      <div className="flex gap-1 justify-end">
                        <Button variant="ghost" className="h-8 w-8 p-0 text-primary hover:text-primary" title="Repor estoque" onClick={() => abrirCompra(ing)}>
                          <ShoppingCart size={14} />
                        </Button>
                        <Button variant="ghost" className="h-8 w-8 p-0" title="Editar" onClick={() => abrirEditar(ing)}>
                          <Pencil size={14} />
                        </Button>
                        <Button variant="ghost" className="h-8 w-8 p-0 text-destructive hover:text-destructive" title="Deletar" onClick={() => handleDeletar(ing.id)}>
                          <Trash2 size={14} />
                        </Button>
                      </div>
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
