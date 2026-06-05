import { useEffect, useState } from "react"
import { ingredientesService } from "@/services/api"
import { Button } from "@/components/ui/Button"
import { Input } from "@/components/ui/Input"
import { Select } from "@/components/ui/Select"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card"
import { Badge } from "@/components/ui/Badge"
import { formatCurrency, formatDate } from "@/lib/utils"
import { Plus, Pencil, Trash2, X } from "lucide-react"

const UNIDADES = ["KG", "ML", "UNIDADE"]

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
    setFormulario({ ...ingredienteVazio })
  }

  function abrirEditar(ingrediente) {
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
    } catch {
      setErro("Erro ao salvar ingrediente.")
    } finally {
      setSalvando(false)
    }
  }

  async function handleDeletar(id) {
    if (!confirm("Deseja remover este ingrediente?")) return
    try {
      await ingredientesService.deletar(id)
      carregarIngredientes()
    } catch {
      setErro("Erro ao remover ingrediente.")
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
                        <Button variant="ghost" className="h-8 w-8 p-0" onClick={() => abrirEditar(ing)}>
                          <Pencil size={14} />
                        </Button>
                        <Button variant="ghost" className="h-8 w-8 p-0 text-destructive hover:text-destructive" onClick={() => handleDeletar(ing.id)}>
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
