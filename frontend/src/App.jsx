import { BrowserRouter, Routes, Route } from "react-router-dom"
import { Layout } from "@/components/Layout"
import { Dashboard } from "@/pages/Dashboard"
import { Ingredientes } from "@/pages/Ingredientes"
import { Receitas } from "@/pages/Receitas"
import { Transacoes } from "@/pages/Transacoes"

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<Layout />}>
          <Route path="/" element={<Dashboard />} />
          <Route path="/ingredientes" element={<Ingredientes />} />
          <Route path="/receitas" element={<Receitas />} />
          <Route path="/transacoes" element={<Transacoes />} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App
