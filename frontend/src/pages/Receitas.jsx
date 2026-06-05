import { useEffect, useState } from "react"
import { receitasService, ingredientesService } from "@/services/api"
import { Button } from "@/components/ui/Button"
import { Input } from "@/components/ui/Input"
import { Select } from "@/components/ui/Select"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/Card"
import { Badge } from "@/components/ui/Badge"
import { formatCurrency } from "@/lib/utils"
import { Plus, Pencil, Trash2, X, PlusCircle, MinusCircle } from "lucide-react"

const CATEGORIAS = ["SOBREMESA", "PRATO_PRINCIPAL", "ENTRADA", "BEBIDA", "LANCHE", "SALADA"]

const receitaVazia = {
  nome: "",
  modoPreparo: "",
  categoria: "PRATO_PRINCIPAL",
  tempoPreparo: "",
  porcoes: "",
  rendimento: "",
  precoVenda: "",
  ativo: true,
  ingredientes: [],
}

export function Receitas() {
  const [receitas, setReceitas] = useState([])
  const [ingredientes, setIngredientes] = useState([])
  const [loading, setLoading] = useState(true)
  const [formulario, setFormulario] = useState(null)
  const [salvando, setSalvando] = useState(false)
  const [erro, setErro] = useState(null)

  function carregar() {
    setLoading(true)
    Promise.all([receitasService.listar(), ingredientesService.listar()])
      .then(([recs, ings]) => {
        setReceitas(recs.data)
        setIngredientes(ings.data)
      })
      .catch(() => setErro("Não foi possível carregar os dados."))
      .finally(() => setLoading(false))
  }

  useEffect(() => { carregar() }, [])

  function abrirCriar() {
    setFormulario({ ...receitaVazia, ingredientes: [] })
  }

  function abrirEditar(receita) {
    setFormulario({
      ...receita,
      ingredientes: receita.ingredientes?.map((i) => ({
        ingredienteId: i.ingredienteId,
        quantidade: i.quantidade,
      })) ?? [],
    })
  }

  function fechar() { setFormulario(null) }

  function handleChange(e) {
    const { name, value } = e.target
    setFormulario((prev) => ({ ...prev, [name]: value }))
  }

  function adicionarIngrediente() {
    setFormulario((prev) => ({
      ...prev,
      ingredientes: [...prev.ingredientes, { ingredienteId: ingredientes[0]?.id ?? "", quantidade: "" }],
    }))
  }

  function removerIngrediente(index) {
    setFormulario((prev) => ({
      ...prev,
      ingredientes: prev.ingredientes.filter((_, i) => i !== index),
    }))
  }

  function handleIngredienteChange(index, field, value) {
    setFormulario((prev) => {
      const lista = [...prev.ingredientes]
      lista[index] = { ...lista[index], [field]: value }
      return { ...prev, ingredientes: lista }
    })
  }

  async function handleSalvar(e) {
    e.preventDefault()
    setSalvando(true)
    try {
      const payload = {
        ...formulario,
        ativo: formulario.ativo ?? true,
        tempoPreparo: Number(formulario.tempoPreparo),
        porcoes: Number(formulario.porcoes),
        rendimento: formulario.rendimento !== "" ? Number(formulario.rendimento) : null,
        precoVenda: formulario.precoVenda !== "" ? Number(formulario.precoVenda) : null,
        ingredientes: formulario.ingredientes.map((i) => ({
          ingredienteId: Number(i.ingredienteId),
          quantidade: Number(i.quantidade),
        })),
      }
      if (formulario.id) {
        await receitasService.atualizar(formulario.id, payload)
      } else {
        await receitasService.criar(payload)
      }
      fechar()
      carregar()
    } catch {
      setErro("Erro ao salvar receita.")
    } finally {
      setSalvando(false)
    }
  }

  async function handleDeletar(id) {
    if (!confirm("Deseja remover esta receita?")) return
    try {
      await receitasService.deletar(id)
      carregar()
    } catch {
      setErro("Erro ao remover receita.")
    }
  }

  return (
    <div className="p-8 space-y-6">
      <div className="flex items-start justify-between">
        <div>
          <h2 className="text-2xl font-bold tracking-tight">Receitas</h2>
          <p className="text-muted-foreground text-sm mt-1">Cardápio com custo de produção</p>
        </div>
        <Button onClick={abrirCriar}><Plus size={16} /> Nova Receita</Button>
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
            <CardTitle>{formulario.id ? "Editar Receita" : "Nova Receita"}</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleSalvar} className="space-y-4">
              <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                <div className="space-y-1">
                  <label className="text-sm font-medium">Nome</label>
                  <Input name="nome" value={formulario.nome} onChange={handleChange} required />
                </div>
                <div className="space-y-1">
                  <label className="text-sm font-medium">Categoria</label>
                  <Select name="categoria" value={formulario.categoria} onChange={handleChange}>
                    {CATEGORIAS.map((c) => <option key={c} value={c}>{c.replace("_", " ")}</option>)}
                  </Select>
                </div>
                <div className="space-y-1">
                  <label className="text-sm font-medium">Tempo de Preparo (min)</label>
                  <Input name="tempoPreparo" type="number" min="1" value={formulario.tempoPreparo} onChange={handleChange} required />
                </div>
                <div className="space-y-1">
                  <label className="text-sm font-medium">Porções</label>
                  <Input name="porcoes" type="number" min="1" value={formulario.porcoes} onChange={handleChange} required />
                </div>
                <div className="space-y-1">
                  <label className="text-sm font-medium">Rendimento</label>
                  <Input name="rendimento" type="number" step="0.01" min="0" value={formulario.rendimento} onChange={handleChange} />
                </div>
                <div className="space-y-1">
                  <label className="text-sm font-medium">Preço de Venda (R$)</label>
                  <Input name="precoVenda" type="number" step="0.01" min="0" value={formulario.precoVenda} onChange={handleChange} />
                </div>
              </div>

              <div className="space-y-1">
                <label className="text-sm font-medium">Modo de Preparo</label>
                <textarea
                  name="modoPreparo"
                  value={formulario.modoPreparo}
                  onChange={handleChange}
                  rows={3}
                  className="flex w-full rounded-md border border-input bg-background px-3 py-2 text-sm focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                />
              </div>

              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <label className="text-sm font-medium">Ingredientes</label>
                  <Button type="button" variant="outline" className="h-8 text-xs" onClick={adicionarIngrediente}>
                    <PlusCircle size={14} /> Adicionar
                  </Button>
                </div>
                {formulario.ingredientes.map((item, index) => (
                  <div key={index} className="flex gap-2 items-center">
                    <Select
                      value={item.ingredienteId}
                      onChange={(e) => handleIngredienteChange(index, "ingredienteId", e.target.value)}
                      className="flex-1"
                    >
                      {ingredientes.map((ing) => (
                        <option key={ing.id} value={ing.id}>{ing.nome} ({ing.unidadeDeMedida})</option>
                      ))}
                    </Select>
                    <Input
                      type="number"
                      step="0.01"
                      min="0"
                      placeholder="Quantidade"
                      value={item.quantidade}
                      onChange={(e) => handleIngredienteChange(index, "quantidade", e.target.value)}
                      className="w-32"
                    />
                    <button type="button" onClick={() => removerIngrediente(index)} className="text-destructive hover:text-red-700">
                      <MinusCircle size={18} />
                    </button>
                  </div>
                ))}
              </div>

              <div className="flex gap-3 pt-2">
                <Button type="submit" disabled={salvando}>{salvando ? "Salvando..." : "Salvar"}</Button>
                <Button type="button" variant="outline" onClick={fechar}>Cancelar</Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}

      {loading ? (
        <p className="text-muted-foreground">Carregando...</p>
      ) : (
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-3">
          {receitas.length === 0 ? (
            <p className="text-muted-foreground col-span-full">Nenhuma receita cadastrada ainda.</p>
          ) : (
            receitas.map((rec) => (
              <Card key={rec.id}>
                <CardHeader className="pb-2">
                  <div className="flex items-start justify-between gap-2">
                    <CardTitle className="text-base">{rec.nome}</CardTitle>
                    <Badge variant="secondary">{rec.categoria?.replace("_", " ")}</Badge>
                  </div>
                </CardHeader>
                <CardContent className="space-y-1 text-sm text-muted-foreground">
                  <p>{rec.tempoPreparo} min · {rec.porcoes} porções</p>
                  <p>Custo: <span className="font-medium text-foreground">{formatCurrency(rec.custoTotal)}</span></p>
                  <p>Venda: <span className="font-medium text-primary">{formatCurrency(rec.precoVenda)}</span></p>
                  <div className="flex gap-2 pt-3">
                    <Button variant="outline" className="h-8 text-xs flex-1" onClick={() => abrirEditar(rec)}>
                      <Pencil size={12} /> Editar
                    </Button>
                    <Button variant="ghost" className="h-8 w-8 p-0 text-destructive hover:text-destructive" onClick={() => handleDeletar(rec.id)}>
                      <Trash2 size={14} />
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))
          )}
        </div>
      )}
    </div>
  )
}
